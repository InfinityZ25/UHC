package io.github.infinityz25.uhckotlin.teams.objects

import io.github.infinityz25.uhckotlin.UHC
import io.github.infinityz25.uhckotlin.player.UHCPlayer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.util.*

class UHCTeam(var teamOwner: UUID) {

    var teamID = UUID.randomUUID()
    var teamMembers = mutableSetOf<UUID>()

    init{
        teamMembers.add(teamOwner)
    }

    fun isTeamMember(uuid: UUID): Boolean{
        return teamMembers.contains(uuid)
    }
    public fun isOwner(uuid: UUID) : Boolean{
        return uuid == teamOwner
    }
    fun changeTeamOwner(sender: UUID, newOwner: UUID){
        if(!isOwner(sender))return
        if(!isTeamMember(newOwner))return
        //TODO:Handle the chat messages, team color on tab, etc
        teamOwner = newOwner
    }
    fun kickMember(sender: UUID, toKick: UUID){
        if(!UHC.instance.teamManager.teamManagement)return
        if(!isOwner(sender))return
        if(!isTeamMember(toKick))return
        //TODO:Handle the chat messages, team color on tab, etc
        teamMembers.remove(toKick)
    }
    fun sendTeamChat(message: String){
        teamMembers.forEach {
            val player = Bukkit.getPlayer(it)
            if(player.isOnline && player != null)player.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
        }

    }
    fun inviteMember(sender: UHCPlayer, toInvite: UHCPlayer){
        if(!UHC.instance.teamManager.teamManagement)return
        if(!isOwner(sender.uuid))return
        if(isTeamMember(toInvite.uuid))return
        if(sender == toInvite)return

        UHC.instance.teamManager.teamRequestMap[sender.uuid] = TeamRequest(this, toInvite, sender)

        sender.player.sendMessage("You invited ${toInvite.player.name} to your team")
        toInvite.player.sendMessage("You've been invited to ${sender.player.name}'s team, use '/uhc team accept ${sender.player.name}' to accept")
        //TODO: Send the team request on chat with clickable message
    }

    fun joinTeam(toJoin: UHCPlayer){
        if(!UHC.instance.teamManager.teamManagement)return
        if(isTeamMember(toJoin.uuid))return
        teamMembers.add(toJoin.uuid)
        toJoin.team = this
        sendTeamChat("[UHC Team]${toJoin.player.name} has joined the team!")
    }

}