package io.github.infinityz25.uhckotlin

import io.github.infinityz25.uhckotlin.events.listeners.CoreEvents
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class UHC : JavaPlugin(){

    override fun onEnable(){
        System.out.println("starssts")
    }

    override fun onDisable(){
        System.out.println("disables")

    }

    fun loadListeners(){
        Bukkit.getPluginManager().registerEvents(CoreEvents(this), this)
    }
}