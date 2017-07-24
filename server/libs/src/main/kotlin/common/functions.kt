package common

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.apache.commons.math3.random.RandomDataGenerator
import org.slf4j.Logger
import java.math.BigDecimal
import java.util.*


object functions {
    var logger: Logger? = null

    val Int.bd:         BigDecimal get() = BigDecimal(this)
    val Double.bd:      BigDecimal get() = BigDecimal(this)

    val String.bd:      BigDecimal get() = BigDecimal(this)
    val String.p:       JsonPrimitive get() = JsonPrimitive(this)
    val BigDecimal.p:   JsonPrimitive get() = JsonPrimitive(this)

    fun JsonObject.primitive(s:String): JsonElement? {
        if (has(s) && !get(s).isJsonNull) return get(s)
        return null
    }
    fun JsonObject.s(s:String): String? = primitive(s)?.asString
    fun JsonObject.l(s:String): Long?   = primitive(s)?.asLong
    fun JsonObject.a(s:String): JsonArray?   = primitive(s)?.asJsonArray
    fun JsonObject.o(s:String): JsonObject?   = primitive(s)?.asJsonObject
    fun JsonObject.bool(s:String): Boolean   = primitive(s)!=null && primitive(s)!!.asBoolean
    fun JsonObject.bd(str:String): BigDecimal? {
        val s = this.s(str) ?: return null
        var b:BigDecimal? = null
        try { b = s.bd } catch (e:Exception) {}
        return b
    }

    fun generateString(rng: Random, characters: String, length: Int): String {
        val text = CharArray(length)
        for (i in 0..length - 1) {
            text[i] = characters[rng.nextInt(characters.length)]
        }
        return String(text)
    }

    fun randInt(min: Int, max: Int): Int {

        // Usually this can be a field rather than a method variable
        val rand = Random()

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        val randomNum = rand.nextInt(max - min + 1) + min

        return randomNum
    }

    fun isNumeric(str: String): Boolean {
        return str.matches("-?\\d+(\\.\\d+)?".toRegex())  //match a number with optional '-' and decimal.
    }

    val aUniqueReference: String
        get() {
            val randomData = RandomDataGenerator()
            return randomData.nextLong(1000L, 999999999L).toString() + ""
        }
}
