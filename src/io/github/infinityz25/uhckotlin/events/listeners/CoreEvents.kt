package io.github.infinityz25.uhckotlin.events.listeners

import io.github.infinityz25.uhckotlin.UHC
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import kotlin.random.Random

class CoreEvents(val instance: UHC) : Listener{

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent){
        e.player.sendMessage("You've joined the server: " + Random.nextInt(150) +1)

    }

}

