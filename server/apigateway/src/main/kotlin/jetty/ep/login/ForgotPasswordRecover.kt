package jetty.ep.login

import com.google.gson.Gson
import com.google.gson.JsonObject
import jetty.EndPoint
import objects.EndPointReply
import rabbit.Rabbit
import java.time.LocalDateTime


class ForgotPasswordRecover: EndPoint() {
    override fun doAction(gson: Gson, obj: JsonObject, endPointReply: EndPointReply, utcTime: LocalDateTime) {
//Step 1

        val code        = if (obj.has("code") && !obj.get("code").isJsonNull) obj.get("code").asString else ""
        val password    = if (obj.has("password") && !obj.get("password").isJsonNull) obj.get("password").asString else "x"
        val password2   = if (obj.has("password2") && !obj.get("password2").isJsonNull) obj.get("password2").asString else "y"

        if (code == null || code == "" || password == null || password != password2)
            return

        val u_client = JsonObject()
        u_client.addProperty("code", code)
        u_client.addProperty("password", password)

        val epRegister = Rabbit.ep("members.forgotpasswordrecover", u_client)
        endPointReply.data      = epRegister.data
        endPointReply.error     = epRegister.error
        endPointReply.success   = epRegister.success

        return
    }
}