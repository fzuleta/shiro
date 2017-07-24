package shiro

import com.google.gson.JsonObject
import objects.EndPointReply
import org.apache.shiro.authc.*
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.subject.SimplePrincipalCollection

open class Base_ODB_Realm : AuthorizingRealm() {
    override fun doGetAuthenticationInfo(authenticationToken: AuthenticationToken): AuthenticationInfo {
        val o_user      = (authenticationToken as UsernamePasswordToken).username
        val o_pass      = String(authenticationToken.password)

        val ep = database_connect(o_user, o_pass)
        if (!ep.success) throw AuthenticationException("Error during login")

        val principals  = createPrincipals(ep.data)
        println("principals created: $principals | $o_pass")

        super.doClearCache(principals)
        return SimpleAuthenticationInfo(principals, o_pass)
    }
    open fun createPrincipals(data: JsonObject): PrincipalCollection {
        val principals = mutableListOf<String>()
        principals.add(data.get("reference").asString)
        return SimplePrincipalCollection(principals, name)
    }
    protected open fun database_connect(username:String, password:String): EndPointReply {
        return EndPointReply()
    }
    public override fun clearCachedAuthorizationInfo(principals: PrincipalCollection) {
        super.clearCachedAuthorizationInfo(principals)
    }
    override fun doGetAuthorizationInfo(principalCollection: PrincipalCollection): AuthorizationInfo {
        return SimpleAuthorizationInfo()
    }

}
