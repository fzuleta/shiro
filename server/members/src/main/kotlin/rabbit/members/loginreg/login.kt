package rabbit.members.loginreg

import kotlinx.coroutines.experimental.runBlocking
import memory.db_users


class login {
    fun a(s: String): objects.EndPointReply {
        val gson = com.google.gson.GsonBuilder().disableHtmlEscaping().create()
        val ep = objects.EndPointReply()
        val obj = gson.fromJson(s, com.google.gson.JsonObject::class.java)

        val m = db_users.login(obj.get("username").asString, obj.get("password").asString)
        if (m!=null) {
            ep.data.addProperty("reference", m.reference)
            ep.success = true
        }

        return ep
    }

}
