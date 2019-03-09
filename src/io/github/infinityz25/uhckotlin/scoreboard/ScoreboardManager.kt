package io.github.infinityz25.uhckotlin.scoreboard

import io.github.infinityz25.uhckotlin.UHC
import java.util.*

class ScoreboardManager(val instance: UHC){

    var map = mutableMapOf<UUID, board>()


    fun getScoreboard(uuid: UUID) : board{

        return map.getValue(uuid)
    }

}