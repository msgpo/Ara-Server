package com.andromeda.araserver.persona

import com.andromeda.araserver.skills.SkillsModel
import com.andromeda.araserver.util.CosmosClients
import com.andromeda.araserver.util.OutputModel
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.google.gson.Gson
import com.microsoft.azure.documentdb.FeedOptions
import com.microsoft.azure.documentdb.PartitionKey
import org.json.JSONObject
import java.util.*

class GetDbArray {
    fun likes(search: String, cc: Locale): String? {
        val options = FeedOptions()
        val mapper = YAMLMapper()

        options.partitionKey = PartitionKey("likes")
        CosmosClients.client.queryDocuments("/dbs/Ara-android-database/colls/Ara-android-collection", "SELECT * FROM c ", options).queryIterable.forEach{
            val json = it.get("document") as JSONObject
            val level = try{
                json.getString(cc.language)
            }
                catch (e:Exception){json.getString("level")}
            if (json.getString("name").equals(search)){
                val outputModel =  arrayListOf(OutputModel(level, "", "", "", level, "" ))
                return Gson().toJson(outputModel)
            }
        }
        val yml = SkillsModel("RESPOND", "I don't know yet, what do you think?", "ARASERVERlikesinput/term=TERM&word=$search")
        val outputModel =  arrayListOf(OutputModel(
            "I don't know, what do you think?",
            "",
            "",
            "",
            "I don't know, what do you think?",
            mapper.writeValueAsString(arrayListOf(yml))
        ))
        return Gson().toJson(outputModel)
    }
}