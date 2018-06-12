package com.sheryv.tubegripper

import com.sheryv.tubegripper.util.Util
import okhttp3.FormBody
import okhttp3.Request
import org.junit.Test

import org.junit.Assert.*
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun parse() {
        //  \"url\":\"([\?=:&#,.A-Za-z0-9\/\\-])+\"
//        val text = Util.readFile(File("src\\test\\res\\data.json"))
        val c = Converter()
        val v = c.convert("https://www.youtube.com/watch?v=j-hJiBzvlZc")
//        val v = c.parseItem(text, "id")
        println(if (v.isExpired()) "Expired!!" else "Correct yet")
        println(v);
    }

    @Test
    fun downloadFile() {
        val d = Downloader("http://pliki.totalcmd.pl/pobierz.php?typ=app&plik=tcmd912x32_64.exe") { bytesRead, contentLength, done ->
            println("$bytesRead / $contentLength | ${bytesRead.toFloat() / contentLength * 100}")
            if (done) println("Done")
        }
        d.updatePerPercent = true

        d.getFile("D:\\temp\\totalcmd.exe")

    }

    @Test
    fun downloadString() {
        val url2 = "https://stackoverflow.com/questions/26509107/how-to-specify-a-default-user-agent-for-okhttp-2-x-requests?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa"

        val url = "https://www.clipconverter.cc/check.php"
        val body = FormBody.Builder().add("mediaurl", "https://www.youtube.com/watch?v=j-hJiBzvlZc").build()
        val req = Request.Builder().url(url)
                .header("User-Agent", "Mozilla/5.0 (Android 4.4; Tablet; rv:41.0) Gecko/41.0 Firefox/41.0").post(body).build()
        val d = Downloader(req) { bytesRead, contentLength, done ->
            println("$bytesRead / $contentLength | ${bytesRead.toFloat() / contentLength * 100}")
            if (done) println("Done")
        }
        println(d.getString())
    }
}
