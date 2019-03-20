package io.github.infinityz25.uhckotlin

import io.github.infinityz25.uhckotlin.Border.barrier.ProtocolLibHook
import io.github.infinityz25.uhckotlin.Border.barrier.VisualiseHandler
import io.github.infinityz25.uhckotlin.Border.barrier.WallBorderListener
import io.github.infinityz25.uhckotlin.commands.UniversalCommand
import io.github.infinityz25.uhckotlin.database.PlayerDataInterface
import io.github.infinityz25.uhckotlin.database.types.MongoDB
import io.github.infinityz25.uhckotlin.events.listeners.CoreEvents
import io.github.infinityz25.uhckotlin.player.PlayerManager
import io.github.infinityz25.uhckotlin.scoreboard.ScoreboardManager
import io.github.infinityz25.uhckotlin.teams.TeamManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class UHC : JavaPlugin(){

    companion object {
        @JvmStatic lateinit var instance: UHC
    }

    lateinit var scoreboardManager : ScoreboardManager
    lateinit var playerDataInterface: PlayerDataInterface
    lateinit var playerManager: PlayerManager
    lateinit var teamManager: TeamManager
    lateinit var visualiseHandler: VisualiseHandler

    override fun onEnable(){
        /*Instantiate the UHC Plugin for external access*/
        instance = this

        loadListeners()
        teamManager = TeamManager()
        scoreboardManager = ScoreboardManager(this)
        playerDataInterface = MongoDB(this, "mongodb://root:p1p2p3p4p5p6@155.94.211.222/admin?connectTimeoutMS=5000", "myStats")
        playerManager = PlayerManager(this)
        visualiseHandler = VisualiseHandler()
        ProtocolLibHook.hook(this)

        loadCommands()
    }

    override fun onDisable(){

    }

    fun loadListeners(){
        Bukkit.getPluginManager().registerEvents(CoreEvents(this), this)
        Bukkit.getPluginManager().registerEvents(WallBorderListener(this), this)
    }

    fun loadCommands(){
        getCommand("uhc").executor = UniversalCommand(this)
    }
}