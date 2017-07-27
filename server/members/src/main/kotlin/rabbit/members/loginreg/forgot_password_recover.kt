package rabbit.members.loginreg

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import memory.db_users
import shiro.Shiro
import common.functions.s
import common.StringUtils

class forgot_password_recover {
    fun a(s: String): objects.EndPointReply {
        val gson = com.google.gson.GsonBuilder().disableHtmlEscaping().create()
        val ep = objects.EndPointReply()
        val obj = gson.fromJson(s, com.google.gson.JsonObject::class.java)

        val sentCode = obj.s("code") ?: return ep
        val codeDec   = StringUtils.decrypt(sentCode)!!.split("@@@")

        if (codeDec.size != 2) return ep

        val username  = codeDec[0]
        val reference = codeDec[1]

        val passwordEncrypted =
                if (Shiro.exists)
                    Shiro.encryptPassword(obj.get("password").asString)
                 else
                    obj.s("passwordEncrypted")



        runBlocking {
            val m = db_users.get(reference)
            if (m!=null) {
                if (username.contains(m.username!!)) {
                    //TODO set password on event storage
                }
                ep.success = true
            }
        }

        return ep
    }

}
