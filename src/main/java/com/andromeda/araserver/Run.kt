package com.andromeda.araserver

import com.andromeda.araserver.iot.*
import com.andromeda.araserver.localSearchData.GetData
import com.andromeda.araserver.localSearchData.GetUserSkills
import com.andromeda.araserver.pages.*
import com.andromeda.araserver.pages.RssMain.rssMain1
import com.andromeda.araserver.skills.*
import com.andromeda.araserver.skills.Timer
import com.andromeda.araserver.util.*
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.FeedException
import com.rometools.rome.io.SyndFeedOutput
import fi.iki.elonen.NanoHTTPD
import java.io.IOException

object Run : NanoHTTPD(Port().main()!!) {

    //If connected to
    override fun serve(session: IHTTPSession): Response {
        val tag: Int
        //URI passed from client
        val sessionUri = session.uri
        //Feed if any
        var syndFeed: SyndFeed? = null
        //Text to be outputted
        var main2: String? = "err"

        //Functions related to the search api
        //Start API function

        when {
            sessionUri.startsWith("/news/us") -> main2 = NewsCache.usNews
            sessionUri.startsWith("/postupdate/") ->UpdateDB().arrayUpdate(sessionUri,
                session.headers["data"]!!
            )
            sessionUri.startsWith("/news/tech") -> main2 = NewsCache.tech
            sessionUri.startsWith("/news/money") -> main2 = NewsCache.money
            sessionUri.startsWith("/news/mex") -> main2 = NewsCache.mexNews
            sessionUri.startsWith("/news/de") -> main2 = NewsCache.deNews
            sessionUri.startsWith("/news/uk") -> main2 = NewsCache.ukNews
            sessionUri.startsWith("/api") -> main2 = ApiStart().apiMain(sessionUri)
            sessionUri.startsWith("/user/") -> main2 = GetUserSkills().list(sessionUri)
            sessionUri.startsWith("/1user/") -> main2 = GetUserSkills().one(sessionUri)
            sessionUri.startsWith("/hi/") -> main2 = Hello().hello(sessionUri)
            sessionUri.startsWith("/del/") -> main2 = DeleteDoc().main(sessionUri)
            sessionUri.startsWith("/yelpclient") -> main2 = Locdec().main(sessionUri)
            sessionUri.startsWith("/weath") -> main2 = Weather().mainPart(sessionUri)
            sessionUri.startsWith("/devices/") -> main2 = Main().main(sessionUri)
            sessionUri.startsWith("/updateuserdata/") -> main2 = UpdateDB().main(sessionUri)
            sessionUri.startsWith("/devicelist/") -> main2 = ListDevices().main(sessionUri)
            sessionUri.startsWith("/deviceinfo/") -> main2 = Status().main(sessionUri)
            sessionUri.startsWith("/searcht/") -> main2 = GetInfo().main(sessionUri)
            sessionUri.startsWith("/openapp/") -> main2 = OpenApp().main(sessionUri)
            sessionUri.startsWith("/searchn/") -> main2 = GetInfo().bingNews(sessionUri)
            sessionUri.startsWith("/searchi/") -> main2 = GetInfo().imageSearch(sessionUri)
            sessionUri.startsWith("/searchv/") -> main2 = GetInfo().bingVideos(sessionUri)
            sessionUri.startsWith("/getha/") -> main2 = HaGetData().main(sessionUri)
            sessionUri.startsWith("/newdevice/") -> {
                main2 = ""
                NewDevice().main(sessionUri)
            }
            sessionUri.startsWith("/newdoc/")->{
                NewDoc().main(sessionUri, session.headers["data"]!!)
            }
            sessionUri.startsWith("/getforcache")-> main2 = GetData().main()
            sessionUri.startsWith("/math") -> main2 =
                Equations().main(sessionUri)
            sessionUri.startsWith("/call") ->
                    main2 =  Call().main(sessionUri)
            sessionUri.startsWith("/remindern/") -> main2 = Reminders().new(sessionUri)
            sessionUri.startsWith("/likesinput/") -> main2 = com.andromeda.araserver.persona.Main().newLikes(sessionUri);
            sessionUri.startsWith("/remindernn/") -> main2 = Reminders().newJustData(sessionUri)
            sessionUri.startsWith("/reminderga/") -> main2 = Reminders().getAll(sessionUri)
            sessionUri.startsWith("/remindergaapi/") -> main2 = Reminders().getAllApi(sessionUri)
            sessionUri.startsWith("/remindergapi/") -> main2 = Reminders().getOneApi(sessionUri)
            sessionUri.startsWith("/reminderu/") -> main2 = Reminders().update(sessionUri)
            sessionUri.startsWith("/reminderg/") -> main2 = Reminders().getOne(sessionUri)
            sessionUri.startsWith("/reminderd/") -> main2 = Reminders().delete(sessionUri)
            sessionUri.startsWith("/text") ->
                main2 =  Text().main(sessionUri)
            sessionUri.startsWith("/skillsdata/") -> main2 =
                GetSkillData().main(sessionUri)
            sessionUri.startsWith("/class") -> main2 =
                GetDeviceClass().main(sessionUri)
            sessionUri.startsWith("/searchb") -> main2 =
                GetInfo().getBing(sessionUri)
            sessionUri.startsWith("/person") -> main2 =
                com.andromeda.araserver.persona.Main().main(sessionUri)
            sessionUri.startsWith("/time") -> main2 =
                 Timer().main(sessionUri)
            else -> { // if getting RSS info set tag value this will be used to get the correct feed
                tag = when (sessionUri) {
                    "/world" -> 1
                    "/us" -> 2
                    "/tech" -> 3
                    "/money" -> 4
                    else -> 0
                }
                try { // get Rss feed from RssMain.kt
                    syndFeed = rssMain1(tag)
                } catch (e: IOException) { // if any issues
                    e.printStackTrace()
                } catch (e: FeedException) {
                    e.printStackTrace()
                }
                // turn feed content in to XML text
                try {
                    assert(syndFeed != null)
                    main2 = SyndFeedOutput().outputString(syndFeed)
                } catch (e: FeedException) {
                    e.printStackTrace()
                }
            }
        }
        println(sessionUri)
        //Output response
        return newFixedLengthResponse(main2)
    }
    // Static function, to be run on start.
    @JvmStatic
    fun main(args: Array<String>) {
        start(SOCKET_READ_TIMEOUT, false)
        News()
        SecurityDBCheck()
        println(" Ara server is running and is available on your domain, IP, or http://localhost:8080/")
        println(
            "Copy write (c) 2020 Fulton Browne " +
                    "This program is free software: you can redistribute it and/or modify\n" +
                    "    it under the terms of the GNU General Public License as published by\n" +
                    "    the Free Software Foundation, either version 3 of the License, or\n" +
                    "    (at your option) any later version.\n" +
                    "\n" +
                    "    This program is distributed in the hope that it will be useful,\n" +
                    "    but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                    "    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                    "    GNU General Public License for more details.\n" +
                    "\n" +
                    "    You should have received a copy of the GNU General Public License\n" +
                    "    along with this program.  If not, see <https://www.gnu.org/licenses/>."
        )
        println("test")
        Thread{
            NLPManager()

        }.start()

        println("start")


    }

}