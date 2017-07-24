package memory

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import common.functions
import entities.members.Member
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import shiro.Shiro

import java.util.concurrent.ConcurrentHashMap

object db_users {

    var members = ConcurrentHashMap<String, Member>()

    fun remove(reference: String) {
        members.remove(reference)
    }



    /* =============================================================================
        CONVENIENCE FUNCTIONS
    ================================================================================ */
    fun restore(m:Member) {
        members.put(m.reference, m)
    }
    fun register(m:Member):Member? {
        var m1:Member? = null
        runBlocking { m1 = fetchRegister(m) }
        return m1
    }
    fun login(username:String, password:String):Member? {
        var m:Member? = null
        runBlocking { m = fetchLogin(username, password) }
        return m
    }
    fun get(reference: String?): Member? {
        var m:Member? = null
        runBlocking { m = fetchByReference(reference) }
        return m
    }
    fun getByUsername(username: String?):Member?{
        var m:Member? = null
        runBlocking { m = fetchByUsername(username) }
        return m
    }



    suspend fun fetchRegister(m:Member): Member? {
        if (getByUsername(m.username) != null ) return null

        // TODO register in the DB

        m.reference = uniqueId()
        members.put(m.reference, m)
        return m
    }
    suspend fun fetchLogin(username:String, password:String):Member? {
        // TODO the trip to the DB, for now return a new member
        // TODO I don't like the idea of storing the password in memory,
        // TODO this MUST be fixed when event store is implemented

        val m = getByUsername(username) ?: return null
        val match = if(Shiro.exists) Shiro.validatePassword(password, m.password) else password == m.password
        if (!match) return null

        return m
    }
    suspend private fun fetchByReference(reference: String?): Member? {
        if (reference == null) return null

        if (members.containsKey(reference))
            return members[reference]

        //TODO if doesn't exist in memory, try to fetch from DB

        return null
    }
    suspend fun fetchByUsername(username: String?):Member?{
        if (username == null) return null
        for ((_, member) in members) {
            if (member.username == username) {
                return member
            }
        }
        //TODO if doesn't exist in memory, try to fetch from DB
        return null
    }



    private suspend fun uniqueId(): String {
        //TODO get it from the server
        var reference = functions.aUniqueReference
        while (true) {
            var f = members.containsKey(reference)
            if (!f) f = members.containsKey(reference)

            if (f) reference = functions.aUniqueReference
            if (!f) break
        }
        return reference
    }
}
