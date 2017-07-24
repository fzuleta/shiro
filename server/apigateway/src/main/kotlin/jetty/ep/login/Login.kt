package jetty.ep.login

import com.google.gson.Gson
import com.google.gson.JsonObject
import jetty.EndPoint
import objects.EndPointReply
import objects.EPError
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.*
import java.time.LocalDateTime
import common.functions.s
import common.functions.bool

class Login: EndPoint() {
    override fun doAction(gson: Gson, obj: JsonObject, endPointReply: EndPointReply, utcTime: LocalDateTime) {

        val username            = obj.s("username") ?: return
        val password            = obj.s("password") ?: return
        val expected_token      = obj.s("expected_token") ?: return
        val rememberMe          = obj.bool("rememberMe")

        val token = UsernamePasswordToken(username, password)
        token.isRememberMe = rememberMe

        val subject = SecurityUtils.getSubject()
        try {
            val server_expected_token = subject.session.getAttribute("expected_token")
            if (expected_token != server_expected_token) { throw Exception() }

            subject.login(token)
            endPointReply.success = true
            endPointReply.data.addProperty("isAuthenticated", subject.isAuthenticated)
            endPointReply.data.addProperty("isRemembered", subject.isRemembered)

        } catch ( uae:UnknownAccountException) {
            endPointReply.errorCode = 5
        } catch ( ice: IncorrectCredentialsException) {
            endPointReply.errorCode = 6
        } catch ( lae:LockedAccountException) {
            endPointReply.errorCode = 7
        } catch ( ea:ExcessiveAttemptsException) {
            endPointReply.errorCode = 8
        } catch ( ae:AuthenticationException) {
            endPointReply.errorCode = 9
        } catch (e: Exception) {
            endPointReply.error = EPError.Error.SERVER_COMMUNICATION_ERROR
        }

    }
}