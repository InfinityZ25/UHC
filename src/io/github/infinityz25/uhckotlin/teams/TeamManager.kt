package io.github.infinityz25.uhckotlin.teams

import io.github.infinityz25.uhckotlin.teams.objects.TeamRequest
import io.github.infinityz25.uhckotlin.teams.objects.UHCTeam
import java.util.*

class TeamManager{

    var teamMap = mutableMapOf<UUID, UHCTeam>()
    var teamRequestMap = mutableMapOf<UUID, TeamRequest>()

}