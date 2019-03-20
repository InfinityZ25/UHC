package io.github.infinityz25.uhckotlin.teams.objects

import io.github.infinityz25.uhckotlin.UHC
import io.github.infinityz25.uhckotlin.player.UHCPlayer
import java.util.*

class TeamRequest(val uhcTeam: UHCTeam, invitee: UHCPlayer, inviter : UHCPlayer) {
    val inviteeUUID = invitee.uuid
    val inviterUUID = inviter.uuid
    val inviteeName = invitee.player.name
    val inviterName = inviter.player.name

    fun acceptInvite(){
        UHC.instance.teamManager.teamRequestMap.remove(inviterUUID)
        uhcTeam.joinTeam(UHC.instance.playerManager.getUHCPlayer(inviteeUUID)!!)
    }

    fun rejectInvite(){
        UHC.instance.teamManager.teamRequestMap.remove(inviterUUID)
        getPlayer(inviterUUID)!!.player.sendMessage("${getPlayer(inviteeUUID)!!.player.name} has rejected the team invite!")

    }

    private fun getPlayer(uuid: UUID) : UHCPlayer? {
        return UHC.instance.playerManager.getUHCPlayer(uuid)
    }
}