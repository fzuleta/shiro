package jetty.ep.login

import com.google.gson.Gson
import com.google.gson.JsonObject
import common.Google
import entities.members.Member
import jetty.EndPoint
import objects.EndPointReply
import objects.EPError
import rabbit.Rabbit
import java.time.LocalDateTime
import java.time.ZoneOffset
import common.functions.s
import common.functions.bool
import shiro.Shiro

class Register: EndPoint() {
    override fun doAction(gson: Gson, obj: JsonObject, endPointReply: EndPointReply, utcTime: LocalDateTime) {

        val testing = !Shiro.exists

        //Step 1
        val username    = obj.s("username") ?: return
        val password    = obj.s("password") ?: "x"
        val password2   = obj.s("password2") ?:"y"

        //Step 3
        val approveTOS  = obj.bool("approve")
        val captcha     = obj.s("captcha") ?: "-"
        val rememberMe  = obj.bool("rememberMe")

        val isCaptchaValid = Google.checkCaptcha(captcha)

        //Do validation
        if (!approveTOS) {
            endPointReply.error = EPError.Error.MEMBER_VAL_TOS
            return

        } else if (isCaptchaValid == false) {
            endPointReply.error = EPError.Error.BAD_CAPTCHA
            return

        } else if (!username.contains("@") || !username.contains(".")) {
            endPointReply.error = EPError.Error.BAD_USERNAME
            return

        } else if (password != password2 || password.length < 6) {
            endPointReply.error = EPError.Error.BAD_PASSWORD
            return
        }


        val member = Member()
        member.username = username
        member.password = Shiro.encryptPassword(password)
        member.dateRegistration = utcTime.toEpochSecond(ZoneOffset.UTC)

        val epRegister = Rabbit.ep("members.register", member.toJsonObject())
        endPointReply.data      = epRegister.data
        endPointReply.error     = epRegister.error
        endPointReply.success   = epRegister.success

        if(!endPointReply.success) return

        if (testing) {
            endPointReply.success=true
            return
        }

        // Login automatically
        val login = Login()
        endPointReply.success = false
        val oLogin = JsonObject()
        oLogin.addProperty("username",   username)
        oLogin.addProperty("password",   password)
        oLogin.addProperty("rememberMe", rememberMe)
        login.doAction(gson, oLogin, endPointReply, utcTime)

        if (endPointReply.success) {
            endPointReply.data.addProperty("firstTime", true)
        }

    }
}
