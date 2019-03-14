package io.github.infinityz25.uhckotlin.player

import io.github.infinityz25.uhckotlin.UHC
import io.github.infinityz25.uhckotlin.teams.objects.UHCTeam
import org.bukkit.entity.Player
import java.util.*

class UHCPlayer(val uuid: UUID, val player: Player) {

    var kills = 0
    var deaths = 0
    var diamonds = 0
    var team: UHCTeam? = null

    init {
        if(UHC.instance.teamManager.teamMap.containsKey(uuid)){
             team  = UHC.instance.teamManager.teamMap[uuid]
        }
    }



}