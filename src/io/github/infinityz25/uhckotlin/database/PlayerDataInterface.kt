package io.github.infinityz25.uhckotlin.database

import java.util.*

interface PlayerDataInterface {
    var cachedUsers: MutableMap<UUID, CachedUser>
    fun loadPlayer(uuid: UUID)

    fun savePlayer(uuid: UUID)

    fun getPlayer(uuid: UUID)

    fun cachePlayer(uuid: UUID)

    fun isCached(uuid: UUID): Boolean


}