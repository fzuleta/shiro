package rabbit.members.loginreg

import common.StringUtils
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import memory.db_users


class forgot_password {
    fun a(s: String): objects.EndPointReply {
        val gson = com.google.gson.GsonBuilder().disableHtmlEscaping().create()
        val ep = objects.EndPointReply()
        val obj = gson.fromJson(s, com.google.gson.JsonObject::class.java)

        val username = obj.get("username").asString
        val redirect_uri = obj.get("redirect_uri").asString

        val m = db_users.getByUsername(username) ?: return ep

        val emailEncrypted = StringUtils.encrypt(username + "@@@" + m.reference)
        val url = redirect_uri + emailEncrypted

        ep.data.addProperty("email", username)
        ep.data.addProperty("name", m.firstName)
        ep.data.addProperty("link", url)
        ep.success = true


        return ep
    }

}
