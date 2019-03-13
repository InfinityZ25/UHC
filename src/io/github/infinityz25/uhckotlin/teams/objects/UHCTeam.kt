package io.github.infinityz25.uhckotlin.teams.objects

import io.github.infinityz25.uhckotlin.UHC
import java.util.*

class UHCTeam(var teamOwner: UUID) {

    var teamMembers = mutableSetOf<UUID>()

    fun isTeamMember(uuid: UUID): Boolean{
        return teamMembers.contains(uuid)
    }
    fun isOwner(uuid: UUID) : Boolean{
        return uuid == teamOwner
    }
    fun changeTeamOwner(newOwner: UUID){
        teamOwner = newOwner
    }
    fun kickMember(sender: UUID, toKick: UUID){
        if(!UHC.instance.teamManager.teamManagement)return
        if(!isOwner(sender))return
        if(!isTeamMember(toKick))return
        //TODO:Handle the chat messages, team color on tab, etc
        teamMembers.remove(toKick)
    }
    fun inviteMember(sender: UUID, toInvite: UUID){
        if(!UHC.instance.teamManager.teamManagement)return
        if(!isOwner(sender))return
        if(isTeamMember(toInvite))return

        //TODO: Send the team request on chat with clickable message

    }

}