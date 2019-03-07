package io.github.infinityz25.uhckotlin

import io.github.infinityz25.uhckotlin.commands.UniversalCommand
import io.github.infinityz25.uhckotlin.events.listeners.CoreEvents
import io.github.infinityz25.uhckotlin.scoreboard.ScoreboardManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class UHC : JavaPlugin(){

    var scoreboardManager : ScoreboardManager? = null

    override fun onEnable(){

        loadListeners()
        scoreboardManager = ScoreboardManager(this)
        loadCommands()
    }

    override fun onDisable(){

    }

    fun loadListeners(){
        Bukkit.getPluginManager().registerEvents(CoreEvents(this), this)
    }

    fun loadCommands(){
        getCommand("uhc").executor = UniversalCommand(this)
    }
}