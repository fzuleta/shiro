package entities

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class MemberProperties {
    class SocialMedia {
        fun checkSize(v:String, s:Int=100):Boolean { return v.length <= s }

        var facebook:String? = null;    set(v){ if (v!=null && checkSize(v)) field=v }
        var twitter:String? = null;     set(v){ if (v!=null && checkSize(v)) field=v }
    }
}