package com.example.androidassignment.assignment

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException


fun getImages(url:String){
    var doc: Document? = null

    try {
        doc = Jsoup.connect(url).get()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    val imgs: Elements? = doc?.select("img")
    //println("Damn images$imgs")
    if (imgs != null) {
        var i =1
        for (img in imgs) {
            if(i<2){
                println(img.attr("data-src"))
                i++
            }
//            Log.d("image-src", img.attr("data-src")) //changed `src` to `data-src`
        }
    }


}
fun main() {
    var list = listOf(1, 2, 3)
    for (number in list) {
        println(number)
        list = listOf(4, 5, 6)
    }
    print(list)
//     val document = Jsoup.connect("https://www.google.co.in/search?biw=1366&bih=675&tbm=isch&sa=1&ei=qFSJWsuTNc-wzwKFrZHoCw&q=android development").get()
//    val imgs = document.select("img")
//    var i =0
//    for(img in imgs){
//        if(i<2){
//            val s = img.attr("src")
//            println(s)
//            i++
//        }
//
//    }

//    Log.i("someething", "something")
   // getImages()

}

