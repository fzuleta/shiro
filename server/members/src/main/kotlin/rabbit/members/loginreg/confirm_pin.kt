package rabbit.members.loginreg


import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import common.StringUtils
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import memory.db_users
import objects.EndPointReply
import common.functions.s

class confirm_pin {
    fun a(s: String): EndPointReply {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val ep = EndPointReply()
        val obj = gson.fromJson(s, JsonObject::class.java)

        val c = obj.s("code") ?: return ep
        val code = StringUtils.decrypt(c)

        val m = db_users.get(code!!) ?: return ep
        m.confirmedEmail = true
        // TODO store in event storage

        ep.data.addProperty("reference", m.reference)
        ep.success = true


        return ep
    }

}
