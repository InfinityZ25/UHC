package io.github.infinityz25.uhckotlin.events.listeners

import io.github.infinityz25.uhckotlin.UHC
import io.github.infinityz25.uhckotlin.scoreboard.UHCScoreboard
import net.md_5.bungee.api.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.random.Random

class CoreEvents(val instance: UHC) : Listener{

    init {
        print("testing init method")
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent){
        val uhcScoreboard = UHCScoreboard(e.player, ChatColor.translateAlternateColorCodes('&', "&b  Badlion UHC  "))

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


        instance.scoreboardManager!!.map[e.player.uniqueId] = uhcScoreboard

        e.player.sendMessage("You've joined the server: " + Random.nextInt(150) +1)

    }

    @EventHandler
    fun onLeave(e: PlayerQuitEvent) {
    }

}

