package io.github.infinityz25.uhckotlin.scoreboard

import org.bukkit.entity.Player

class UHCScoreboard(player: Player?, objectiveName: String?) : ScoreboardNMS(player, objectiveName) {
    var timerLine: Int? = null
    var killsLine: Int? = null
}