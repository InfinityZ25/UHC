package io.github.infinityz25.uhckotlin

import io.github.infinityz25.uhckotlin.commands.UniversalCommand
import io.github.infinityz25.uhckotlin.database.PlayerDataInterface
import io.github.infinityz25.uhckotlin.database.types.MongoDB
import io.github.infinityz25.uhckotlin.events.listeners.CoreEvents
import io.github.infinityz25.uhckotlin.player.PlayerManager
import io.github.infinityz25.uhckotlin.scoreboard.ScoreboardManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class UHC : JavaPlugin(){

    lateinit var scoreboardManager : ScoreboardManager
    lateinit var playerDataInterface: PlayerDataInterface
    lateinit var playerManager: PlayerManager


    override fun onEnable(){
        loadListeners()
        scoreboardManager = ScoreboardManager(this)
        playerDataInterface = MongoDB(this, "mongodb://root:p1p2p3p4p5p6@155.94.211.222/admin?connectTimeoutMS=5000", "myStats")
        playerManager = PlayerManager(this)

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