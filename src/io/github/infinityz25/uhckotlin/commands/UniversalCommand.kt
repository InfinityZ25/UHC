package io.github.infinityz25.uhckotlin.commands

import io.github.infinityz25.uhckotlin.UHC
import org.bukkit.Bukkit
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
                    "team"->{
                        val uhcPlayer = instance.playerManager.getUHCPlayer(p.uniqueId)!!
                        val teamManager = instance.teamManager
                        when(args[1]){
                            "create"->{
                                if(!teamManager.teamManagement){
                                    sender.sendMessage("Team Management is disabled")
                                    return true
                                }
                                teamManager.createTeam(uhcPlayer)
                            }
                            "man", "management"->{
                                teamManager.teamManagement = !teamManager.teamManagement
                                sender.sendMessage("Team Management has been set to ${teamManager.teamManagement}")
                            }
                            "cancel"->{
                                if(uhcPlayer.team == null){
                                    sender.sendMessage("You don't have a team, use /uhc team create to create one")
                                    return true
                                }
                                if(!uhcPlayer.team!!.isOwner(uhcPlayer.uuid)){
                                    sender.sendMessage("You are not the team owner!")
                                    return true
                                }
                                if(!instance.teamManager.teamRequestMap.contains(uhcPlayer.uuid)){
                                    sender.sendMessage("You don't have any pending team invites")
                                    return true
                                }
                                val invite = instance.teamManager.teamRequestMap[uhcPlayer.uuid]
                                sender.sendMessage("Cancelling your current team invite to ${invite!!.invitee.player.name}")
                                instance.teamManager.teamRequestMap.remove(uhcPlayer.uuid)

                            }
                            "invite"->{
                                if(uhcPlayer.team == null){
                                    sender.sendMessage("You don't have a team, use /uhc team create to create one")
                                    return true
                                }

                                val targetPlayer = instance.playerManager.getUHCPlayer(Bukkit.getPlayer(args[2]).uniqueId)
                                if(targetPlayer == null){
                                    uhcPlayer.player.sendMessage("Player target is null")
                                    return true
                                }
                                if(instance.teamManager.teamRequestMap.contains(uhcPlayer.uuid)){
                                    uhcPlayer.player.sendMessage("You already have a pending invite for ${instance.teamManager.teamRequestMap[uhcPlayer.uuid]!!.invitee.player.name}!\n" +
                                            "use '/uhc team invite cancel' to cancel it")
                                    return true
                                }
                                uhcPlayer.team!!.inviteMember(uhcPlayer, targetPlayer)
                            }
                            "accept", "reject", "deny"->{
                                val targetPlayer = instance.playerManager.getUHCPlayer(Bukkit.getPlayer(args[2]).uniqueId)
                                if(targetPlayer == null){
                                    uhcPlayer.player.sendMessage("Player target is null")
                                    return true
                                }
                                val invite = instance.teamManager.teamRequestMap[targetPlayer.uuid]
                                if(invite == null || invite.invitee.uuid != uhcPlayer.uuid){
                                    uhcPlayer.player.sendMessage("Player target hasn't sent you any team invites!")
                                    return true
                                }
                                if(args[1].toLowerCase() == "accept"){
                                    invite.acceptInvite()

                                }else{
                                    invite.rejectInvite()
                                }

                            }
                        }
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