package jetty.ep.login

import com.google.gson.Gson
import com.google.gson.JsonObject
import jetty.EndPoint
import objects.EndPointReply
import rabbit.Rabbit
import java.time.LocalDateTime


class ConfirmPin: EndPoint() {
    override fun doAction(gson: Gson, obj: JsonObject, endPointReply: EndPointReply, utcTime: LocalDateTime) {
        val code = if (obj.has("code") && !obj.get("code").isJsonNull) obj.get("code").asString else ""

        if (code == "") return

        val u_client = JsonObject()
        u_client.addProperty("code", code)

        val epRegister = Rabbit.ep("members.confirmpin", u_client)
        endPointReply.data      = epRegister.data
        endPointReply.error     = epRegister.error
        endPointReply.success   = epRegister.success
    }
}