package com.andromeda.araserver.localSearchData

import com.andromeda.araserver.iot.DevicesDB
import com.andromeda.araserver.util.CosmosClients.client
import com.google.gson.Gson
import com.microsoft.azure.documentdb.ConnectionPolicy
import com.microsoft.azure.documentdb.ConsistencyLevel
import com.microsoft.azure.documentdb.DocumentClient

class GetUserSkills {
    fun list(url:String): String? {
        val db = DevicesDB()
        val mainVal = url.replace("/user/", "")
        val key:String? = mainVal
        return Gson().toJson(ReadDB().userSkill(client, key!!))
    }
    fun one(url:String): String? {
        val mainVal = url.replace("/1user/", "")
        val actions = mainVal.split("&")
        val dbLink = System.getenv("IOTDB")
        var id:String? = null
        var key:String? = null
        println("test")
        for (i in actions) when {
            i.startsWith("id=") -> id = i.replace("id=", "")
            i.startsWith("user=") -> key = i.replace("user=", "")
        }
        val client = client
        return Gson().toJson(ReadDB().userSkill(client, key!!, id!!))
    }
}