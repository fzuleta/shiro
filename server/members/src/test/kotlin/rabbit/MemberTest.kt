package rabbit
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.experimental.*
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import common.functions.s

class MemberTest {
    @Test fun `member can be registered`() {
        val o = JsonObject()
        o.addProperty("username", "user-${Math.random()}")
        o.addProperty("password", "pass-${Math.random()}")

        val ep = rabbit.members.loginreg.register().a(o.toString())
        Assert.assertTrue(ep.success)
    }
    @Test fun `member can login`() {
        val o = JsonObject()
        o.addProperty("username", "user-${Math.random()}")
        o.addProperty("password", "pass-${Math.random()}")

        var ep = rabbit.members.loginreg.register().a(o.toString())
        Assert.assertTrue(ep.success)

        ep.success = false
        ep = rabbit.members.loginreg.login().a(o.toString())

        Assert.assertTrue(ep.success)
        println(ep)
    }





    @Ignore @Test fun `comparing coRoutines to see which one waits`() {
        launch(CommonPool){
            delay(300)
            println("launch")
        }
        async(CommonPool){
            delay(400)
            println("async")
        }
        runBlocking {
            delay(100)
            println("runBlocking")
        }

    }
}