import common.Google
import jetty.Jetty
import org.apache.shiro.crypto.AesCipherService
import rabbit.Rabbit
import shiro.Shiro
import java.io.File


fun main(args: Array<String>) {
    class folderLoc{}
    val LOCAL_FOLDER_LOCATION         = File( folderLoc().javaClass.protectionDomain.codeSource.location.toURI().path)
            .toString()
            .split("server/apigateway/out/production/classes")[0]

    val recaptchaKey                  = "google-key"
    val captchaEnabled                = false

    val rabbitHost:String             = System.getenv("RABBIT_HOST") ?: "localhost"
    val rabbitUser:String             = System.getenv("RABBIT_USER") ?: "root"
    val rabbitPass:String             = System.getenv("RABBIT_PASS") ?: "root"
    val rabbitPort:String             = System.getenv("RABBIT_PORT") ?: "10002"

    val domain:String                 = System.getenv("DOMAIN") ?: "localhost"
    val rememberMeCypher:String       = System.getenv("REMEMBERME") ?: "Tzhfrprwksieaxxw"
    val hashService:String            = System.getenv("HASHSERVICE") ?: "LnnjousalfizuleiPsiyzzwpelvbnfpo"
    val passwordSalt:String           = System.getenv("PASSWORDSALT") ?: "Hagqudyjehyyzhqr"
    val sessionDuration:String        = System.getenv("SESSIONDURATION") ?: "3600000"

    val ip:String                     = "0.0.0.0"
    val httpPort:Int                  = 8089
    val httpsPort:Int                 = 8090
    val cors_allowedOrigins           = System.getenv("CORS") ?: "*"

    val sslEnabled:Boolean            = true
    val sslKeyStorePath:String        = System.getenv("SSLPATH") ?: LOCAL_FOLDER_LOCATION + "resources/self.keystore"
    val sslKeyStorePassword:String    = System.getenv("SSLPASS") ?: "some-password"
    val sslKeyManagerPassword:String  = System.getenv("SSLPASS") ?: "some-password"
    val sslTrustStorePath:String      = System.getenv("SSLPATH") ?: LOCAL_FOLDER_LOCATION + "resources/self.keystore"
    val sslTrustStorePassword:String  = System.getenv("SSLPASS") ?: "some-password"


    // setup data
    Google.recaptchaKey = recaptchaKey
    Google.captchaEnabled = captchaEnabled

    // start rabbit
    Rabbit.start(rabbitHost, rabbitPort.toInt(), rabbitUser, rabbitPass)

    // Shiro
    Shiro.domain = domain
    Shiro.rememberMeCypher = rememberMeCypher
    Shiro.hashService = hashService
    Shiro.passwordSalt = passwordSalt
    Shiro.sessionDuration = sessionDuration.toLong()

    // Jetty
    Jetty.start(
        ip,
        httpPort,
        httpsPort,
        cors_allowedOrigins,
        sslEnabled,
        sslKeyStorePath,
        sslKeyStorePassword,
        sslKeyManagerPassword,
        sslTrustStorePath,
        sslTrustStorePassword
    )

    Shiro.exists = true
    println("App started")
}