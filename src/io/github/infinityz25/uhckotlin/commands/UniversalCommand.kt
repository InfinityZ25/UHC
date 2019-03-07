package io.github.infinityz25.uhckotlin.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UniversalCommand : CommandExecutor{

    init{
        print("init universal command")
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if(cmd.name.toLowerCase() == "uhc"){

            if(sender is Player){
                val p : Player = sender
                p.sendMessage("Player is op")

                return true
            }
            sender.sendMessage("Only a player can execute this")

            return true
        }
        return false
    }

}