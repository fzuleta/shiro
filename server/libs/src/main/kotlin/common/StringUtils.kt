package common

import com.google.gson.JsonObject
import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder

import java.io.IOException
import java.util.Random
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

import org.apache.commons.codec.binary.Base64

object StringUtils {
    // keys for encryption/decryption
    var aes_key0 = "dmd3IGlzIHRoZSBiZXN0IGNvbXBhbnk"
    var aes_key1 = "bXkgbmFtZSBpIGZlbGlwZSB6dWxldGE"

    fun encryptWithKeys(obj: JsonObject): String? {
        return encrypt(aes_key0, aes_key1, obj.toString())
    }

    fun encrypt(key1: String, key2: String, value: String): String? {
        try {
            val iv = IvParameterSpec(key2.toByteArray(charset("UTF-8")))

            val skeySpec = SecretKeySpec(key1.toByteArray(charset("UTF-8")),
                    "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
            val encrypted = cipher.doFinal(value.toByteArray())
            //            System.out.println("encrypted string:"
            //                    + Base64.encodeBase64String(encrypted));
            return Base64.encodeBase64String(encrypted)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }

    fun decryptWithKeys(s: String): String? {
        return decrypt(aes_key0, aes_key1, s)
    }

    fun decrypt(key1: String, key2: String, encrypted: String): String? {
        try {
            val iv = IvParameterSpec(key2.toByteArray(charset("UTF-8")))

            val skeySpec = SecretKeySpec(key1.toByteArray(charset("UTF-8")),
                    "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            val original = cipher.doFinal(Base64.decodeBase64(encrypted))

            return String(original)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }


    //    private static Random random = new Random((new Date()).getTime());
    var randKey: Long? = 12345.toLong()
    private var random: Random? = null
    /**
     * Encrypts the string along with salt
     * @param userId
     * *
     * @return
     * *
     * @throws Exception
     */
    fun encrypt(userId: String): String {
        set()
        val encoder = BASE64Encoder()

        // let's create some dummy salt
        val salt = ByteArray(8)
        random!!.nextBytes(salt)
        return encoder.encode(salt) + encoder.encode(userId.toByteArray())
    }


    /**
     * Decrypts the string and removes the salt
     * @param encryptKey
     * *
     * @return
     * *
     * @throws Exception
     */
    fun decrypt(encryptKey: String): String? {
        set()
        // let's ignore the salt
        if (encryptKey.length > 12) {
            val cipher = encryptKey.substring(12)
            val decoder = BASE64Decoder()
            try {
                return String(decoder.decodeBuffer(cipher))
            } catch (e: IOException) {
                //  throw new InvalidImplementationException(
                //    "Failed to perform decryption for key ["+encryptKey+"]",e);
            }

        }
        return null
    }

    private fun set() {

        if (random == null) {
            random = Random(randKey!!)
        }
    }
}