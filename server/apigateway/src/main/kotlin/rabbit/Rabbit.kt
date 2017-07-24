package rabbit

import com.google.gson.JsonObject
import rabbitmq.RabbitMQ

object Rabbit {
    val rabbit = RabbitMQ()
    val started:Boolean
        get() {
            return rabbit.started
        }

    fun start(host:String = "localhost", port:Int = 15672, user:String = "root", password:String = "root"){
        if (started) return

        rabbit.host = host
        rabbit.user = user
        rabbit.pass = password
        rabbit.port = port
        rabbit.initCallback = {
            println("Rabbit started")
        }

        rabbit.startAsync()
    }
    fun ep(queue:String, o:JsonObject)= rabbit.ep(queue, o)

    // convenience functions
    fun memberExists(reference:String):Boolean? {
        val o = JsonObject();
        o.addProperty("reference", reference)
        val e = rabbit.ep("members.exist", o)
        return e.ok
    }
}