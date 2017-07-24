package jetty.ep

import com.google.gson.Gson
import com.google.gson.JsonObject
import common.Constants
import common.functions
import common.Google
import jetty.EndPoint
import objects.EndPointReply
import org.apache.shiro.SecurityUtils
import rabbit.Rabbit
import shiro.Shiro
import java.time.LocalDateTime

class refresh : EndPoint() {
    override fun doAction(gson: Gson, obj: JsonObject, endPointReply: EndPointReply, utcTime: LocalDateTime) {
        var isRemembered = false
        var isAuthenticated = false

        val subject = SecurityUtils.getSubject()
        var expected_token:Any? = ""

        try {
            // expected token
            expected_token = subject.session.getAttribute("expected_token")
            if (expected_token == null) {
                expected_token = functions.aUniqueReference
                subject.session.setAttribute("expected_token", expected_token)
            }

            isRemembered    = subject.isRemembered
            isAuthenticated = subject.isAuthenticated

        } catch (e:Exception) {
            e.printStackTrace()
        }

        endPointReply.data.addProperty("isRemembered", isRemembered)
        endPointReply.data.addProperty("isAuthenticated", isAuthenticated)

        val loginInfo = JsonObject()
        loginInfo.addProperty("clientURL", Constants.clientURL)
        loginInfo.addProperty("expected_token", expected_token as String)
        loginInfo.addProperty("captchaEnabled", Google.captchaEnabled)
        endPointReply.data.add("login", loginInfo)

        val reference = Shiro.get_reference(subject)
        if (reference!=null) {
            val u_client = JsonObject()
            u_client.addProperty("reference", reference)

            // get my balance and my info
            val epMyInfo = Rabbit.ep("members.get", u_client)
            endPointReply.data.add("me", epMyInfo.data.get("list").asJsonArray.get(0).asJsonObject)

        }

        endPointReply.success = true
    }
}