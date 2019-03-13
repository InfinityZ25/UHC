package io.github.infinityz25.uhckotlin.teams

import io.github.infinityz25.uhckotlin.teams.objects.TeamRequest
import io.github.infinityz25.uhckotlin.teams.objects.UHCTeam
import org.bukkit.entity.Player
import java.util.*

class TeamManager{

    var teamManagement = false
    var teamMap = mutableMapOf<UUID, UHCTeam>()
    var teamRequestMap = mutableMapOf<UUID, TeamRequest>()

    /*Function to send team request*/
    fun sendRequest(sender: Player, target: Player){
        if(!teamManagement)return


    }

}