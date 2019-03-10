package io.github.infinityz25.uhckotlin.player

import io.github.infinityz25.uhckotlin.UHC
import java.util.*

class PlayerManager(val instance: UHC){
    val playerList = mutableMapOf<UUID, UHCPlayer>()


    fun getUHCPlayer(uuid: UUID): UHCPlayer?{
        return playerList[uuid]
    }


}