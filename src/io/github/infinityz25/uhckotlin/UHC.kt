package io.github.infinityz25.uhckotlin

import org.bukkit.plugin.java.JavaPlugin

class UHC : JavaPlugin(){

    override fun onEnable(){
        System.out.println("starts")
    }

    override fun onDisable(){
        System.out.println("disables")

    }
}