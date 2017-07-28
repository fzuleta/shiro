package shiro

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.credential.DefaultPasswordService
import org.apache.shiro.subject.Subject

object Shiro {
    var exists                  = false
    var domain                  = "localhost"
    var rememberMeCypher        = "Tzhfrprwksieaxxw"
    var hashService             = "LnnjousalfizuleiPsiyzzwpelvbnfpo"
    var sessionDuration:Long    = 3600000

    val hashIterations          = 2048
    private val passwordService: DefaultPasswordService by lazy {
        MyShiroConstructor.getPasswordService()
    }

    fun encryptPassword(plain:String) = passwordService.encryptPassword(plain)
    fun validatePassword(plain:String, encrypted:String)= passwordService.passwordsMatch(plain, encrypted)

    fun validateSession(s: Subject): Boolean {
        var b = false
        if (s.isAuthenticated || s.isRemembered) {
            s.session.touch()
            b = true
        }
        return b
    }

    fun touchSession(subject: Subject) {
        try {
            val session = subject.getSession(false)
            session.touch()
        } catch (e: Exception) {
        }
    }

    fun validateMe():String? {
        if(exists) {
            val subject = SecurityUtils.getSubject()
            return Shiro.get_reference(subject) ?: return null
        }
        return null
    }
    fun get_reference(subject: Subject): String? {
        if (subject.principals != null)
            return subject.principals.primaryPrincipal.toString()
        return null
    }
}