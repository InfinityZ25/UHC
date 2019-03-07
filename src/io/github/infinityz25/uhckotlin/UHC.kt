package io.github.infinityz25.uhckotlin

import io.github.infinityz25.uhckotlin.commands.UniversalCommand
import io.github.infinityz25.uhckotlin.events.listeners.CoreEvents
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class UHC : JavaPlugin(){

    override fun onEnable(){
        loadListeners()
        loadCommands()
    }

    override fun onDisable(){

    }

    fun loadListeners(){
        Bukkit.getPluginManager().registerEvents(CoreEvents(this), this)
    }

    fun loadCommands(){
        getCommand("uhc").executor = UniversalCommand()
    }
}