package shiro

import com.google.gson.JsonObject
import objects.EndPointReply
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.subject.SimplePrincipalCollection
import rabbit.Rabbit

import java.util.ArrayList

class ODBRealm : Base_ODB_Realm() {
    override fun database_connect(username:String, password:String): EndPointReply {
        val user = JsonObject()
        user.addProperty("username", username)
        user.addProperty("password", password)
        return Rabbit.ep("members.login", user)
    }
    override fun createPrincipals(data: JsonObject): PrincipalCollection {
        val principals = ArrayList<Any>(1)
        if (!data.has("reference")) throw AuthenticationException("Error during login")
        principals.add(data.get("reference").asString)

        return SimplePrincipalCollection(principals, name)
    }
}
