package io.github.infinityz25.uhckotlin.events.listeners

import io.github.infinityz25.uhckotlin.UHC
import io.github.infinityz25.uhckotlin.scoreboard.UHCScoreboard
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import kotlin.random.Random

class CoreEvents(val instance: UHC) : Listener{

    init {
        print("testing init method")
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent){
        val uhcScoreboard = UHCScoreboard(e.player, "Henix UHC")

        uhcScoreboard.create()
        uhcScoreboard.setLine(14, "Testing the limits of the scoreboard")
        uhcScoreboard.setLine(13, "It should be able to go all the way up to 48")


        instance.scoreboardManager!!.map[e.player.uniqueId] = uhcScoreboard

        e.player.sendMessage("You've joined the server: " + Random.nextInt(150) +1)

    }

}

