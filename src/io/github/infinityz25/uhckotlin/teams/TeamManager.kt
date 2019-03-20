package io.github.infinityz25.uhckotlin.teams

import io.github.infinityz25.uhckotlin.player.UHCPlayer
import io.github.infinityz25.uhckotlin.teams.objects.TeamRequest
import io.github.infinityz25.uhckotlin.teams.objects.UHCTeam
import java.util.*

class TeamManager{

    var teamManagement = true
    var maxTeamSize = 2
    var teamMap = mutableMapOf<UUID, UHCTeam>()
    var teamRequestMap = mutableMapOf<UUID, TeamRequest>()

    fun createTeam(teamOwner: UHCPlayer){
        if(!teamManagement)return
        if(teamOwner.team != null){
            teamOwner.player.sendMessage("You already are in a team")
            return
        }
        val team = UHCTeam(teamOwner.uuid)
        teamMap[teamOwner.uuid] = team
        teamOwner.team = team
        teamOwner.player.sendMessage("You create a team!\n ID: ${team.teamID}")
    }

}