package rabbit.members


import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import memory.db_users
import objects.EndPointReply
import common.functions.primitive
import common.functions.s
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import kotlin.coroutines.experimental.suspendCoroutine
import kotlin.system.exitProcess

class update {
    fun a(s:String): EndPointReply {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val ep = EndPointReply()
        val o = gson.fromJson(s, JsonObject::class.java)

        fun has(o:JsonObject, p:String, fcn: (o:JsonElement)->Unit) {
            val ref = o.primitive(p) ?: return
            fcn(ref)
        }

        val member_reference = o.s("member_reference") ?: return ep
        val m = db_users.get(member_reference) ?: return ep


        m.apply {
            has(o, "firstName") { firstName = it.asString }
            has(o, "lastName") { lastName = it.asString }
            has(o, "country") { country = it.asString }
            has(o, "city") { city = it.asString }
            has(o, "state") { state = it.asString }
            has(o, "dob") { dob = it.asLong }

            has(o, "socialMedia", fun(e) {
                val g = e.asJsonObject
                has(g, "facebook") { socialMedia.facebook = it.asString }
                has(g, "twitter") { socialMedia.twitter = it.asString }
            })

        }

        //TODO fire up event store to update history

        ep.data = m.toUser()
        ep.success = true
        return ep
    }

}
