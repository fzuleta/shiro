package rabbit

import com.google.gson.JsonObject
import entities.members.Member
import memory.db_users
import rabbitmq.RabbitMQ
import shiro.Shiro

object Rabbit {
    val r = RabbitMQ()
    var started = false

    fun start(host:String = "localhost", port:Int = 15672, user:String = "root", password:String = "root"){
        if (started) return

        started = true
        r.host = host
        r.user = user
        r.pass = password
        r.port = port
        r.initCallback = {
            println(" [x] Awaiting RPC requests")

            r.add(r.channel, "members.get",                     { rabbit.members.get().a(it) })
            r.add(r.channel, "members.exist",                   { rabbit.members.exists().a(it) })
            r.add(r.channel, "members.update",                  { rabbit.members.update().a(it) })
            r.add(r.channel, "members.login",                   { rabbit.members.loginreg.login().a(it) })
            r.add(r.channel, "members.register",                { rabbit.members.loginreg.register().a(it) })
            r.add(r.channel, "members.forgotpassword",          { rabbit.members.loginreg.forgot_password().a(it) })
            r.add(r.channel, "members.forgotpasswordrecover",   { rabbit.members.loginreg.forgot_password_recover().a(it) })
            r.add(r.channel, "members.confirmpin",              { rabbit.members.loginreg.confirm_pin().a(it) })

            restoreState()
        }

        r.startAsync()
    }
    fun ep(queue:String, o: JsonObject)= r.ep(queue, o)

    private fun restoreState() {
        // TODO

        (0..5).forEach({
            val m = Member()
            m.reference = "r${Math.random()}"
            m.username = "$it@a.com"
            m.password = Shiro.getPasswordService().encryptPassword("123123")
            db_users.restore(m)
            println("restored: ${m.username}")
        })
    }
}
