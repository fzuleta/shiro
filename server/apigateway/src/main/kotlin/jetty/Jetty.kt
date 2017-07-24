package jetty

import jetty.socket.Socket
import org.eclipse.jetty.servlet.ServletContextHandler


object Jetty {
    class Service: JettyService(){
        override fun addServlets(s: ServletContextHandler) {
            val list = hashMapOf(
                "/socket"                               to Socket::class.java,
                "/api/member/register/"                 to jetty.ep.login.Register::class.java,
                "/api/member/login/"                    to jetty.ep.login.Login::class.java,
                "/api/member/logout/"                   to jetty.ep.login.Logout::class.java,
                "/api/member/forgot-password/"          to jetty.ep.login.ForgotPassword::class.java,
                "/api/member/forgot-password-recover/"  to jetty.ep.login.ForgotPasswordRecover::class.java,
                "/api/member/confirm-pin/"              to jetty.ep.login.ConfirmPin::class.java,

                "/api/refresh/"                         to jetty.ep.refresh::class.java

            )
            for ((a, b) in list) s.addServlet(b, a)
        }
    }

    val j = Service()

    fun start(
              ip:String,
              httpPort:Int,
              httpsPort:Int,
              cors_allowedOrigins:String,
              sslEnabled:Boolean,
              sslKeyStorePath:String,
              sslKeyStorePassword:String,
              sslKeyManagerPassword:String,
              sslTrustStorePath:String,
              sslTrustStorePassword:String

    ) {
        j.ip = ip
        j.httpPort = httpPort
        j.httpsPort = httpsPort
        j.sslEnabled = sslEnabled
        j.sslKeyStorePath = sslKeyStorePath
        j.sslKeyStorePassword = sslKeyStorePassword
        j.sslKeyManagerPassword = sslKeyManagerPassword
        j.sslTrustStorePath = sslTrustStorePath
        j.sslTrustStorePassword = sslTrustStorePassword
        j.cors_allowedOrigins = cors_allowedOrigins

        j.startAsync()
    }

}