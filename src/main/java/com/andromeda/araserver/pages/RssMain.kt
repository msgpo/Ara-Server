package com.andromeda.araserver.pages

import com.andromeda.araserver.util.Sort
import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.feed.synd.SyndFeedImpl
import com.rometools.rome.io.FeedException
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.SyndFeedOutput
import com.rometools.rome.io.XmlReader


import java.io.IOException
import java.net.URL
import kotlin.collections.ArrayList

/**
 * Object used to handle RSS Feeds.
 */
@Deprecated(message = "this will be replaced with the new news system")
object RssMain {
    /**
     * Main Function. Returns feeds based on the passed in mode.
     * @param mode the specify the type of feeds to return.
     * @return The SyndFeed containing the feeds.
     * @throws IOException
     * @throws FeedException
     */
    @Throws(IOException::class, FeedException::class)
    fun rssMain1(mode: Int): SyndFeed {
        //Feed link text
        val feeds = arrayOfNulls<String>(4)
        when (mode) {
            1 -> {
                //World news in english
                feeds[0] = "https://www.cbsnews.com/latest/rss/world"
                feeds[1] = "http://feeds.foxnews.com/foxnews/world"
                feeds[2] = "http://feeds.bbci.co.uk/news/world/rss.xml"
                feeds[3] = "http://feeds.reuters.com/Reuters/worldNews"
            }
            2 -> {
                // Us news
                feeds[0] = "https://www.cbsnews.com/latest/rss/us"
                feeds[1] = "http://feeds.foxnews.com/foxnews/national"
                feeds[2] = "http://feeds.reuters.com/Reuters/domesticNews"
                feeds[3] = "http://news.yahoo.com/rss/"
            }
            4 -> {
                //Business news in english
                feeds[0] = "http://feeds.reuters.com/reuters/businessNews"
                feeds[1] = "https://www.espn.com/espn/rss/news/rss.xml"
                feeds[2] = "http://feeds.bbci.co.uk/news/business/rss.xml"
                feeds[3] = "http://feeds.reuters.com/reuters/businessNews"

            }
            else -> {
                // general news in english
                feeds[0] = "https://www.cbsnews.com/latest/rss/main"
                feeds[1] = "http://feeds.foxnews.com/foxnews/latest"
                feeds[2] = "https://www.espn.com/espn/rss/news"
                feeds[3] = "http://feeds.reuters.com/Reuters/worldNews"

            }
        }
        //Declare Feed
        val feed = SyndFeedImpl()
        //Set feed type
        feed.feedType = "rss_2.0"
        //Entries of the feeds
        val entries = ArrayList<SyndEntry>()
        //Entries after sorting by date
        val sortedEntries = ArrayList<SyndEntry>()


        // set general feed info
        feed.title = "Ara feed"
        feed.description = "a hole lot of feeds in one"
        feed.author = "Andromeda Software"
        feed.link = ""


        //get all the feed results
        for (i in feeds.indices) {
            val inputUrl = URL(feeds[i])

            val input = SyndFeedInput()
            val inFeed = input.build(XmlReader(inputUrl))

            entries.addAll(inFeed.entries)


        }
        //sort and reverse
        sortedEntries.addAll(Sort().sortDateSyndEntry(entries))
        feed.entries = sortedEntries.reversed()
        //return the value
        return feed
    }
    fun inString(url: String): String {
        val tag = when (url) {
            "/world" -> 1
            "/us" -> 2
            "/tech" -> 3
            "/money" -> 4
            else -> 0
        }
        var main2 = ""
        var syndFeed:SyndFeed? = null
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
        return main2
    }
}
