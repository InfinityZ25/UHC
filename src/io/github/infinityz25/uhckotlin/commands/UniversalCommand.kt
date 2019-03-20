package io.github.infinityz25.uhckotlin.commands

import io.github.infinityz25.uhckotlin.UHC
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class UniversalCommand(val instance: UHC) : CommandExecutor{

    init{
        print("init universal command")
    }

    fun getName(uuid: UUID): String{
        val player = Bukkit.getPlayer(uuid)
        if(player != null){
            return player.name
        }
        return Bukkit.getOfflinePlayer(uuid).name
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
                                sender.sendMessage("Cancelling your current team invite to ${invite!!.inviteeName}")

                                Bukkit.getPlayer(invite.inviteeUUID)?.sendMessage("${uhcPlayer.player.name} has cancelled the team invite!")

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
                                    uhcPlayer.player.sendMessage("You already have a pending invite for ${instance.teamManager.teamRequestMap[uhcPlayer.uuid]!!.inviteeName}!\n" +
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
                                if(invite == null || invite.inviteeUUID != uhcPlayer.uuid){
                                    uhcPlayer.player.sendMessage("Player target hasn't sent you any team invites!")
                                    return true
                                }
                                if(args[1].toLowerCase() == "accept"){
                                    invite.acceptInvite()

                                }else{
                                    invite.rejectInvite()
                                }
                            }
                            "members", "crew", "group", "list"->{
                                val team = uhcPlayer.team
                                if(team ==null){
                                    sender.sendMessage("You are not in a team!")
                                    return true
                                }
                                val str = StringBuilder()
                                team.teamMembers.forEach {

                                    str.append("${getName(it)}, ")
                                }
                                str.replace(str.length-2, str.length, ".")
                                sender.sendMessage("Team Members: ${str.toString().replace(getName(team.teamOwner), ChatColor.translateAlternateColorCodes('&', "&b${getName(team.teamOwner)}&r"))}")
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