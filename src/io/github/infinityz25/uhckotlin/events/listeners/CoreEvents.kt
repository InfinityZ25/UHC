package io.github.infinityz25.uhckotlin.events.listeners

import io.github.infinityz25.uhckotlin.UHC
import io.github.infinityz25.uhckotlin.database.types.MongoDB
import io.github.infinityz25.uhckotlin.player.UHCPlayer
import io.github.infinityz25.uhckotlin.scoreboard.board
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import kotlin.random.Random

class CoreEvents(val instance: UHC) : Listener{

    init {
        print("testing init method")
    }

    @EventHandler
    fun onPreLogin(e: AsyncPlayerPreLoginEvent) {
        //TODO: Check if player is allowed to connect and then cache the player to avoid possible ram abuses
        instance.playerDataInterface.cachePlayer(e.uniqueId)
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent){
        val uhcScoreboard = board(e.player, ChatColor.translateAlternateColorCodes('&', "&b  Badlion UHC  "))

        uhcScoreboard.create()
        uhcScoreboard.setLine(9, ChatColor.translateAlternateColorCodes('&', "&aGame Time: &f00:00:00"))
        uhcScoreboard.setLine(8, "   ")
        uhcScoreboard.setLine(7, ChatColor.translateAlternateColorCodes('&', "&aYour Kills: &f2"))
        uhcScoreboard.setLine(6, "  ")
        uhcScoreboard.setLine(5, ChatColor.translateAlternateColorCodes('&', "&aPlayers Left: &f28"))
        uhcScoreboard.setLine(4, ChatColor.translateAlternateColorCodes('&', "&aSpectators: &f11"))
        uhcScoreboard.setLine(3, " ")
        uhcScoreboard.setLine(2, ChatColor.translateAlternateColorCodes('&', "&6Current Border:&f 1000"))
        uhcScoreboard.setLine(1, ChatColor.translateAlternateColorCodes('&', "&bwww.badlion.net"))


        instance.scoreboardManager.map[e.player.uniqueId] = uhcScoreboard
/*
        val bar = HenixBar(ChatColor.translateAlternateColorCodes('&', "&6Welcome to BadlionUHC 600"), e.player)
        bar.runTaskTimerAsynchronously(instance, 0L, 1L)

        object : BukkitRunnable() {
            var time = 60
            var percetange = 0.6F
            override fun run() {
                if(time <= 0){
                    bar.destroy()
                    cancel()
                    return
                }
                time--
                percetange-=0.01F
                bar.updateTitle(ChatColor.translateAlternateColorCodes('&', "&6Welcome to BadlionUHC $time"))
                bar.setProgress(percetange)
            }
        }.runTaskTimerAsynchronously(instance, 20L, 20)

*/
        e.player.sendMessage("You've joined the server: " + Random.nextInt(150) +1)

        /*Check if the player is cached*/
        if(instance.playerDataInterface.isCached(e.player.uniqueId)){
            Bukkit.broadcastMessage("Player is cached \n")

            val cachedPlayer = instance.playerDataInterface.cachedUsers[e.player.uniqueId]!!
            val uhcPlayer = UHCPlayer(e.player.uniqueId, e.player)
            /*Get all the data from cached user to an instance of an actual UHC player*/
            uhcPlayer.deaths = cachedPlayer.deaths
            uhcPlayer.diamonds = cachedPlayer.diamonds
            uhcPlayer.kills = cachedPlayer.kills

            /*Add the player with set data to the UHCPlayer List*/
            instance.playerManager.playerList[e.player.uniqueId] = uhcPlayer

            /*Remove the player from the cache*/
            instance.playerDataInterface.cachedUsers.remove(e.player.uniqueId)
        }
        else{
            Bukkit.broadcastMessage("Player is not cached, creating a Document for them")

            /*Tell mongodb to create a document for this player, may be worth doing it async*/
            val mongo = instance.playerDataInterface as MongoDB
            mongo.createDocument(e.player)

            /*Add the player with default data to the UHCPlayer List*/
            val uhcPlayer = UHCPlayer(e.player.uniqueId, e.player)
            instance.playerManager.playerList[e.player.uniqueId] = uhcPlayer
        }

    }


}

