package entities.members

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import entities.MemberProperties

data class Member(var reference:String) {
    constructor() : this("")

    fun checkSize(v:String, s:Int=100):Boolean { return v.length <= s }

    var firstName:String? = null;       set(v){ if (v!=null && checkSize(v)) field=v }
    var lastName:String? = null;        set(v){ if (v!=null && checkSize(v)) field=v }

    var username:String? = null;        set(v){ if (v!=null && checkSize(v)) field=v }
    var password:String = ""
    var email:String? = null;           set(v){ if (v!=null && checkSize(v, 150)) field=v }
    var confirmedEmail= false

    var country:String? = null;         set(v){ if (v!=null && checkSize(v)) field=v }
    var city:String? = null;            set(v){ if (v!=null && checkSize(v)) field=v }
    var state:String? = null;           set(v){ if (v!=null && checkSize(v)) field=v }
    var dob:Long = 0

    var dateRegistration                = 0L
    var dateLastLogin                   = 0L

    val socialMedia                     = MemberProperties.SocialMedia()
















    override fun toString(): String = GsonBuilder().disableHtmlEscaping().create().toJson(this, this.javaClass)
    fun toJsonObject(): JsonObject = GsonBuilder().disableHtmlEscaping().create().fromJson(toString(), JsonObject::class.java)

    fun toUser(): JsonObject {
        val o = toJsonObject()
        // remove data not to be sent to the user
        o.remove("password")
        return o
    }

    companion object {
        fun fromString(s: String): Member {
            val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
            val t = gson.fromJson(s, Member::class.java)
            return t
        }
    }
}
