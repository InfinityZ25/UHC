package io.github.infinityz25.uhckotlin.database.types

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import io.github.infinityz25.uhckotlin.UHC
import io.github.infinityz25.uhckotlin.database.CachedUser
import io.github.infinityz25.uhckotlin.database.PlayerDataInterface
import org.bson.Document
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.logging.Level.WARNING
import java.util.logging.Logger


class MongoDB(val instance: UHC, connectionURI: String, databaseName: String) : PlayerDataInterface{
    private var mongoDatabase: MongoDatabase
    private var mongoCollection: MongoCollection<Document>
    override var cachedUsers = mutableMapOf<UUID, CachedUser>()

    init {
        /*Making mongo logger shut up*/
        Logger.getLogger("org.mongodb.driver").level= WARNING
        val mongoClient = MongoClients.create(connectionURI)
        mongoDatabase = mongoClient.getDatabase(databaseName)


        try{
            /*Attempt to reach the database with a ping command*/
            mongoDatabase.runCommand(Document("ping", 1))
            println("Successfully connected to mongo")
            mongoCollection = mongoDatabase.getCollection("HenixUHC")
        }catch (io: Exception){
            throw Exception("Couldn't Reach MongoDB server!")
        }
    }

    override fun loadPlayer(uuid: UUID) {
    }

    override fun savePlayer(uuid: UUID) {
    }

    override fun getPlayer(uuid: UUID) {
    }

    override fun cachePlayer(uuid: UUID) {
        val cachedUser = CachedUser()

        //TODO: Don't create a document when player is not on collection, it does not matter and opens room for hacking and abuses.
        if(isInCollection(uuid)){
            print("in collection")
            val found = findDocument(uuid)!!
            cachedUser.deaths = found.getInteger("deaths")
            cachedUser.diamonds = found.getInteger("diamonds")
            cachedUser.kills = found.getInteger("kills")
            cachedUsers[uuid] = cachedUser
        }
    }


    override fun isCached(uuid: UUID): Boolean {
        return cachedUsers.containsKey(uuid)
    }

    fun createDocument(p: Player) {
        /*Make the task in Async to avoid blocking the flow of everything else in the plugin whilst waiting for the response of the server*/
        object : BukkitRunnable() {
            override fun run() {
                val doc = Document("_id", p.uniqueId)
                    .append("name", p.name)
                    .append("deaths", 0)
                    .append("diamonds", 0)
                    .append("kills", 0)
                mongoCollection.insertOne(doc)
            }
        }.runTaskAsynchronously(instance)
    }

    fun createDocumentOffline(uuid: UUID) {
        val doc = Document("_id", uuid)
            .append("name", Bukkit.getOfflinePlayer(uuid).name)
            .append("deaths", 0)
            .append("diamonds", 0)
            .append("kills", 0)
        mongoCollection.insertOne(doc)
    }

    private fun findDocument(uuid: UUID): Document? {
        return mongoCollection.find(
            eq("_id", uuid)
        ).limit(1).first()
    }
    private fun isInCollection(p: Player): Boolean {
        return isInCollection(p.uniqueId)
    }

    private fun isInCollection(uuid: UUID): Boolean {
        return mongoCollection.find(eq("_id", uuid)).limit(1).first() != null
    }

}