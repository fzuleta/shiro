package shiro

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.credential.DefaultPasswordService
import org.apache.shiro.crypto.hash.DefaultHashService
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.session.ExpiredSessionException
import org.apache.shiro.subject.Subject
import org.apache.shiro.util.SimpleByteSource

object Shiro {
    var exists                  = false
    var domain                  = "localhost"
    var rememberMeCypher        = "Tzhfrprwksieaxxw"
    var hashService             = "LnnjousalfizuleiPsiyzzwpelvbnfpo"
    var passwordSalt            = "Hagqudyjehyyzhqr"
    var sessionDuration:Long    = 3600000

    val hashIterations          = 2048
    var _passwordService: DefaultPasswordService? = null

    fun getPasswordService(): DefaultPasswordService {
        if (_passwordService == null) {
            val hashService = DefaultHashService()
            hashService.hashIterations = hashIterations // 500000
            hashService.hashAlgorithmName = Sha256Hash.ALGORITHM_NAME
            hashService.privateSalt = SimpleByteSource(passwordSalt) // NOT base64-encoded.
            hashService.isGeneratePublicSalt = true

            _passwordService = DefaultPasswordService()
            _passwordService!!.hashService = hashService
        }

        return _passwordService!!
    }
    fun validatePassword(plain:String, encrypted:String)= getPasswordService().passwordsMatch(plain, encrypted)

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