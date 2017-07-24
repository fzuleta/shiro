import jetty.Jetty
import java.io.File

fun main(args: Array<String>) {
    class folderLoc{}
    val LOCAL_FOLDER_LOCATION         = File( folderLoc().javaClass.protectionDomain.codeSource.location.toURI().path)
            .toString()
            .split("server/webserver/out/production/classes")[0]

    val ip:String                     = "0.0.0.0"
    val httpPort:Int                  = 8880
    val httpsPort:Int                 = 8443
    val sslEnabled:Boolean            = true
    val sslKeyStorePath:String        = System.getenv("SSLPATH") ?: LOCAL_FOLDER_LOCATION + "resources/self.keystore"
    val sslKeyStorePassword:String    = System.getenv("SSLPASS") ?: "some-password"
    val sslKeyManagerPassword:String  = System.getenv("SSLPASS") ?: "some-password"
    val sslTrustStorePath:String      = System.getenv("SSLPATH") ?: LOCAL_FOLDER_LOCATION + "resources/self.keystore"
    val sslTrustStorePassword:String  = System.getenv("SSLPASS") ?: "some-password"
    val staticFolderLocation:String   = System.getenv("AURELIALOCATION") ?: LOCAL_FOLDER_LOCATION + "client/" // local folder

    // Jetty
    Jetty.start(
            ip,
            httpPort,
            httpsPort,
            sslEnabled,
            sslKeyStorePath,
            sslKeyStorePassword,
            sslKeyManagerPassword,
            sslTrustStorePath,
            sslTrustStorePassword,
            staticFolderLocation
    )
}

