package io.github.infinityz25.uhckotlin.commands

import io.github.infinityz25.uhckotlin.UHC
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UniversalCommand(val instance: UHC) : CommandExecutor{

    init{
        print("init universal command")
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if(cmd.name.toLowerCase() == "uhc"){

            if(sender is Player){
                val p : Player = sender
                when(args[0]){
                    "set", "add"->{
                        val score = instance.scoreboardManager?.getScoreboard(p.uniqueId)

                        val allArgs = StringBuilder()

                        for (i in  2 until args.size) allArgs.append(args[i]).append(" ")

                        val text = ChatColor.translateAlternateColorCodes('&', allArgs.toString().trim())

                        if(text.length > 48){
                            p.sendMessage("Scoreboard lines cannot be longer than 48 characters")
                            return true
                        }
                        score?.setLine(args[1].toInt(), text)
                    }
                    "reset"->{
                        val score = instance.scoreboardManager?.getScoreboard(p.uniqueId)
                        score?.destroy()
                    }
                    "create"->{
                        val score = instance.scoreboardManager?.getScoreboard(p.uniqueId)
                        score?.create()
                    }
                    "title"->{
                        val score = instance.scoreboardManager?.getScoreboard(p.uniqueId)

                        val allArgs = StringBuilder()

                        for (i in  1 until args.size) allArgs.append(args[i]).append(" ")

                        score?.setObjectiveName(ChatColor.translateAlternateColorCodes('&', allArgs.toString().trim()))
                    }
                }



                return true
            }
            sender.sendMessage("Only a player can execute this")

            return true
        }
        return false
    }

}