package rabbit.members.loginreg

import com.google.gson.GsonBuilder
import memory.db_users
import common.functions.s
import entities.members.Member
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking


class register {
    fun a(s: String): objects.EndPointReply {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val ep = objects.EndPointReply()
        val obj = gson.fromJson(s, com.google.gson.JsonObject::class.java)

        obj.s("username") ?: return ep
        obj.s("password") ?: return ep

        val member = gson.fromJson(obj.toString(), Member::class.java)
        val m = db_users.register(member) ?: return ep

        ep.success = true
        ep.data = m.toUser()
        return ep
    }

}
