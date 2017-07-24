package rabbit.members


import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import memory.db_users
import objects.EndPointReply

class get {
    fun a(s:String): EndPointReply {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val ep = EndPointReply()
        val obj = gson.fromJson(s, JsonObject::class.java)
        val data = JsonArray()

        // find an individual one
        if (obj.has("reference") && !obj.get("reference").isJsonNull) {
            val m = db_users.get(obj.get("reference").asString)
            if (m != null) data.add(m.toUser())
        }

        ep.data.add("list", data)
        ep.success = true
        return ep
    }

}
