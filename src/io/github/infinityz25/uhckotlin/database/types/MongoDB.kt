package io.github.infinityz25.uhckotlin.database.types

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.github.infinityz25.uhckotlin.UHC
import io.github.infinityz25.uhckotlin.database.PlayerDataInterface
import org.bson.Document
import java.util.*
import java.util.logging.Level.WARNING
import java.util.logging.Logger

class MongoDB(val instance: UHC, connetionURL: String, databaseName: String) : PlayerDataInterface{
    var mongoDatabase: MongoDatabase

    init {
        /*Making mongo logger shut up*/
        Logger.getLogger("org.mongodb.driver").level= WARNING
        val mongoClient = MongoClients.create(connetionURL)
        mongoDatabase = mongoClient.getDatabase(databaseName)


        try{
            /*Attempt to reach the database with a ping command*/
            mongoDatabase.runCommand(Document("ping", 1))
            println("Succesfully connceted to mongo")
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
    }

}