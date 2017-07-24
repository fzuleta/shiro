package members

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import jetty.ep.login.Register
import objects.EndPointReply
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import rabbit.Rabbit
import java.time.LocalDateTime
import java.time.ZoneOffset


class LoginRegTest {
    companion object {
        val gson: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
        val utcTime = LocalDateTime.now(ZoneOffset.UTC)!!
        @BeforeClass @JvmStatic
        fun setup() {
            println("Setup")
            Rabbit.start()
        }
    }

    @Test fun `User can register`() {
        if (!Rabbit.started) return
        val ep = EndPointReply()
        val obj = JsonObject()
        obj.addProperty("username", "u${Math.random()}@somedomain.com")
        obj.addProperty("password", "p${Math.random()}")
        obj.add("password2", obj["password"])
        obj.addProperty("approve", true)
        obj.addProperty("rememberMe", true)
        obj.addProperty("captcha", "-")
        Register().doAction(gson, obj, ep, utcTime)
        Assert.assertTrue(ep.success)
    }
    @Test fun `User cant register with an invalid email`() {
        val ep = EndPointReply()
        val obj = JsonObject()
        obj.addProperty("username", "u${Math.random()}")
        obj.addProperty("password", "p${Math.random()}")
        obj.add("password2", obj["password"])
        obj.addProperty("approve", true)
        obj.addProperty("rememberMe", true)
        obj.addProperty("captcha", "-")
        Register().doAction(gson, obj, ep, utcTime)
        Assert.assertFalse(ep.success)
    }
    @Test fun `User cant register with different passwords`() {
        val ep = EndPointReply()
        val obj = JsonObject()
        obj.addProperty("username", "u${Math.random()}")
        obj.addProperty("password", "xxxxxxxxxxxx")
        obj.addProperty("password2", "y")
        obj.addProperty("approve", true)
        obj.addProperty("rememberMe", true)
        obj.addProperty("captcha", "-")
        Register().doAction(gson, obj, ep, utcTime)
        Assert.assertFalse(ep.success)
    }
    @Test fun `User cant register if he didnt agree to terms`() {
        val ep = EndPointReply()
        val obj = JsonObject()
        obj.addProperty("username", "u${Math.random()}")
        obj.addProperty("password", "xxxxxxxxxxxx")
        obj.addProperty("password2", "xxxxxxxxxxxx")
        obj.addProperty("approve", false)
        obj.addProperty("rememberMe", true)
        obj.addProperty("captcha", "-")
        Register().doAction(gson, obj, ep, utcTime)
        Assert.assertFalse(ep.success)
    }
    @Test fun `User cant register if password is less than 6 chars`() {
        val ep = EndPointReply()
        val obj = JsonObject()
        obj.addProperty("username", "u${Math.random()}")
        obj.addProperty("password", "xxx")
        obj.addProperty("password2", "xxx")
        obj.addProperty("approve", true)
        obj.addProperty("rememberMe", true)
        obj.addProperty("captcha", "-")
        Register().doAction(gson, obj, ep, utcTime)
        Assert.assertFalse(ep.success)
    }
}