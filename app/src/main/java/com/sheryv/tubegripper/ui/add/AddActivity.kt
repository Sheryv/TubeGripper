package com.sheryv.tubegripper.ui.add

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.sheryv.tubegripper.R
import com.sheryv.tubegripper.ui.main.MainViewModel

import kotlinx.android.synthetic.main.activity_add.*
import android.R.string.ok
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import com.sheryv.tubegripper.ui.MainActivity
import android.content.Intent
import android.opengl.Visibility
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sheryv.tubegripper.model.Item
import com.sheryv.tubegripper.model.Path
import com.sheryv.tubegripper.util.Lc
import com.sheryv.tubegripper.util.Util
import com.squareup.picasso.Picasso
import org.jetbrains.anko.sp
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import android.util.TypedValue


class AddActivity : AppCompatActivity() {

    private lateinit var viewModel: AddViewModel
    lateinit var adapter: SpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setSupportActionBar(toolbar)
        progress.visibility = View.VISIBLE
        viewModel = ViewModelProviders.of(this).get(AddViewModel::class.java)
        viewModel.viewCreated()
        viewModel.item.observe(this, Observer {
            if (it != null) {
                loadItem(it)
            }
        })
        val (valid, link) = isLinkReceived(intent)
        if (valid) {
            viewModel.loadVideoInfoAsync(link)
        } else {
            Lc.st("No link received", this)
        }
        adapter = SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, ArrayList())
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, id: Long) {
                if (viewModel.item.value != null) {
                    val i = adapter.getItem(pos)
                    length.text = i.durationString()
                    val e = viewModel.item.value?.expire ?: 0L
                    expire.text = Util.getDateString(if (e == 0L) System.currentTimeMillis() - 1000 else e * 1000,
                            true, "expired")
                }
            }
        }
    }

    private fun loadItem(item: Item) {
        titleTv.text = item.title
        adapter.clear()
        adapter.addAll(item.videos)
        adapter.notifyDataSetChanged()
        spinner.setSelection(0)
        Picasso.get().load(item.thumbnailUrl).into(thumbIv)
        progress.visibility = View.GONE
    }

    private fun isLinkReceived(intent: Intent): Pair<Boolean, String> {
        val ac = getIntent().action
        if (Intent.ACTION_SEND == intent.action
                && intent.type != null && "text/plain" == intent.type) {

            val ytLink = intent.getStringExtra(Intent.EXTRA_TEXT)

            if (ytLink != null) {
                val id = Util.parseLink(ytLink)
                if (id != null) {
                    // We have a valid link
                    Lc.d("valid yt link: $ytLink | id: $id")
                    //  startActivity(new Intent(this, MainActivity.class));
                    // Bundle b = new Bundle();
                    // b.putString(KEY_INTENT_URL, ytLink);
                    //goToFrag(MainActivity.Tabs.Add, b);
                    return Pair(true, ytLink)
                }
            }
        }
        Lc.d("No download link available from intent")
        return Pair(false, "")
    }

    public fun builtinManager(view: View) {
        if (viewModel.item.value != null) {
            val v = adapter.getItem(spinner.selectedItemPosition)
            downloadWithManager(viewModel.item.value!!, v)
        }
    }

    public fun downloadClick(view: View) {

    }
    public fun browser(view: View) {
        if (viewModel.item.value != null) {
            val v = adapter.getItem(spinner.selectedItemPosition)
            val sendIntent = Intent(Intent.ACTION_VIEW, Uri.parse(v.url))
            startActivity(Intent.createChooser(sendIntent, "Open in browser"))
        }
    }


    private fun downloadWithManager(item: Item, video: Path) {
        val request = DownloadManager.Request(Uri.parse(video.url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
        request.setAllowedOverRoaming(false)
        request.setTitle("TG: "+item.title)
        val file = item.title + ".mp4"
        request.setDescription("/Download/TubeGripper/$file [${video.sizeMB()} MB]")
        request.setVisibleInDownloadsUi(true)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/TubeGripper/$file")

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager;
        val refid = downloadManager.enqueue(request)
    }

    class SpinnerAdapter(context: Context, layout: Int, list: List<Path>) :
            ArrayAdapter<Path>(context, layout, list) {

        @SuppressLint("SetTextI18n")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = super.getView(position, convertView, parent)
            val t = view as TextView
            val item = getItem(position)
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            t.text = "${item.formatData?.fullName ?: item.itag.toString()} [${item.sizeMB()} MB]"
            return view
        }


        @SuppressLint("SetTextI18n")
        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = super.getDropDownView(position, convertView, parent)
            val t = view as TextView
            val item = getItem(position)
            t.text = "${item.formatData?.fullName ?: item.itag.toString()} [${item.sizeMB()} MB]"
            return view
        }
    }
}
