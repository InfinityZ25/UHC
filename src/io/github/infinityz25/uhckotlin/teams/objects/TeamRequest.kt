package io.github.infinityz25.uhckotlin.teams.objects

import io.github.infinityz25.uhckotlin.UHC
import io.github.infinityz25.uhckotlin.player.UHCPlayer

class TeamRequest(val uhcTeam: UHCTeam, val invitee: UHCPlayer, val inviter : UHCPlayer) {

    fun acceptInvite(){
        UHC.instance.teamManager.teamRequestMap.remove(inviter.uuid)
        uhcTeam.joinTeam(invitee)
    }

    fun rejectInvite(){
        UHC.instance.teamManager.teamRequestMap.remove(inviter.uuid)
        inviter.player.sendMessage("${invitee.player.name} has rejected the team invite!")

    }
}