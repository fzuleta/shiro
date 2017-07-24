import rabbit.Rabbit
import shiro.Shiro

fun main(args: Array<String>){
    println("Hello")

    val passwordSalt:String           = System.getenv("PASSWORDSALT") ?: "Hagqudyjehyyzhqr"

    val rabbitHost:String             = System.getenv("RABBIT_HOST") ?: "localhost"
    val rabbitUser:String             = System.getenv("RABBIT_USER") ?: "root"
    val rabbitPass:String             = System.getenv("RABBIT_PASS") ?: "root"
    val rabbitPort:String             = System.getenv("RABBIT_PORT") ?: "10002"

    Shiro.passwordSalt = passwordSalt

    Rabbit.start(rabbitHost, rabbitPort.toInt(), rabbitUser, rabbitPass)
}