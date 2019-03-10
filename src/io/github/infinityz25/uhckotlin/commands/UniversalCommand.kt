package io.github.infinityz25.uhckotlin.commands

import io.github.infinityz25.uhckotlin.UHC
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UniversalCommand(val instance: UHC) : CommandExecutor{

    init{
        print("init universal command")
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if(cmd.name.equals("uhc", true)){

            if(sender is Player){
                val p : Player = sender

                when(args[0]){
                    "stats"->{
                        val uhcPlayer = instance.playerManager.getUHCPlayer(p.uniqueId)!!
                        p.sendMessage("${p.name}' stats:\nKills: ${uhcPlayer.kills}\nDeaths: ${uhcPlayer.deaths}\nDiamonds: ${uhcPlayer.diamonds}\n")
                    }
                }
                //test



                return true
            }
            sender.sendMessage("Only a player can execute this")

            return true
        }
        return false
    }

}