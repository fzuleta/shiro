package rabbit.members


import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import memory.db_users
import objects.EndPointReply
import common.functions.s
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking

class exists {
    fun a(s:String): EndPointReply {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val ep = EndPointReply()
        val obj = gson.fromJson(s, JsonObject::class.java)

        val member_reference = obj.s("reference") ?: return ep

        val m = db_users.get(member_reference)
        ep.data.add("member", m?.toUser())
        ep.success = m != null

        return ep
    }
}
