package com.sheryv.tubegripper.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.View
import com.sheryv.tubegripper.NavigationActivity
import com.sheryv.tubegripper.R
import com.sheryv.tubegripper.ui.main.MainFragment
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuItem
import com.sheryv.tubegripper.ui.add.AddActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
        Log.d("Main", "onCreate")

    }


    public fun click(view: View){
        val i = Intent(this, NavigationActivity::class.java)
        startActivity(i)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                startActivity(Intent(this, AddActivity::class.java))
                true
            }
            R.id.action_settings -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=5qlIPTDE274")
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "Share link"))
                true
            }
            R.id.action_test -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)

        Log.d("Main", "onSaveInstanceState(outState: $outState,\n outPersistentState: $outPersistentState)")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        Log.d("Main", "onRestoreInstanceState(savedInstanceState: $savedInstanceState,\n persistentState: $persistentState)")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d("Main", "onSaveInstanceState(outState: $outState)")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("Main", "onRestoreInstanceState(savedInstanceState: $savedInstanceState)")
    }

}
