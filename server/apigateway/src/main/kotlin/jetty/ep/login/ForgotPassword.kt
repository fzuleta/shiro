package jetty.ep.login

import com.google.gson.Gson
import com.google.gson.JsonObject
import common.Constants
import email.Email
import jetty.EndPoint
import objects.EndPointReply
import rabbit.Rabbit
import java.time.LocalDateTime
import java.util.HashMap


class ForgotPassword: EndPoint() {
    override fun doAction(gson: Gson, obj: JsonObject, endPointReply: EndPointReply, utcTime: LocalDateTime) {

        //Step 1
        val username = if (obj.has("username") && !obj.get("username").isJsonNull) obj.get("username").asString else ""

        if (username == "" || !username.contains("@") || !username.contains("."))
            return

        val u_client = JsonObject()
        u_client.addProperty("username", username)
        u_client.addProperty("redirect_uri", Constants.websitehost + "/user/forgotpasswordrecover/")


        val epRegister = Rabbit.ep("members.forgotpassword", u_client)
        endPointReply.data      = epRegister.data
        endPointReply.error     = epRegister.error
        endPointReply.success   = epRegister.success

        //Send the email
        if (endPointReply.success) {
            val data = endPointReply.data
            val map = HashMap<String, Any>()
            val newMessage = HashMap<String, String>()
            newMessage.put("name", data.get("name").asString)
            newMessage.put("link", data.get("link").asString)

            map.put("newMessage", newMessage)

            Email.sendHTMLEmail(
                    "Restore Password",
                    username,
                    "forgotPassword",
                    map)

            endPointReply.success = true
        }
    }
}