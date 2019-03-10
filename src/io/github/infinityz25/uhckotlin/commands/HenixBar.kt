package io.github.infinityz25.uhckotlin.commands

import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class SharedBossBar(var title: String) : BukkitRunnable() {
    var withers = mutableMapOf<UUID, EntityWither>()
    override fun run() {
        if (withers.isEmpty()) {
            cancel()
            return
        }
        withers.forEach { playerUUID, entityWither ->
            val player = Bukkit.getPlayer(playerUUID)
            if (player == null) {
                withers.remove(playerUUID)
            } else {
                val location = getWitherLocation(Bukkit.getPlayer(playerUUID).eyeLocation)
                entityWither.setLocation(location.x, location.y, location.z, location.yaw, location.pitch)
                val packetPlayOutEntityTeleport = PacketPlayOutEntityTeleport(entityWither)
                getConnection(player).sendPacket(packetPlayOutEntityTeleport)
            }
        }
        if (withers.isEmpty()) {
            cancel()
            return
        }
    }

    fun addPlayer(player: Player) {
        val craftworld = player.world as CraftWorld
        val worldserver = craftworld.handle
        val wither = EntityWither(worldserver)

        val location = getWitherLocation(player.location)

        wither.customName = title
        wither.isInvisible = true
        wither.setLocation(location.x, location.y, location.z, 0f, 0f)
        val packet = PacketPlayOutSpawnEntityLiving(wither)

        getConnection(player).sendPacket(packet)

        withers[player.uniqueId] = wither

    }

    fun removePlayer(player: Player) {
        if (!withers.containsKey(player.uniqueId)) return

        val packet = PacketPlayOutEntityDestroy(withers[player.uniqueId]!!.id)
        withers.remove(player.uniqueId)
        getConnection(player).sendPacket(packet)
    }

    fun hasPlayer(player: Player): Boolean {
        return withers.containsKey(player.uniqueId)
    }

    fun setProgress(progress: Float) {
        withers.forEach { playerUUID, entityWither ->
            val player = Bukkit.getPlayer(playerUUID)
            if (player == null) {
                withers.remove(playerUUID)
            } else {
                entityWither.health = progress * entityWither.maxHealth
                val packetPlayOutEntityMetadata =
                    PacketPlayOutEntityMetadata(entityWither.id, entityWither.dataWatcher, true)
                getConnection(player).sendPacket(packetPlayOutEntityMetadata)
            }
        }
    }

    fun updateTitle(title: String) {
        this.title = title
        withers.forEach { playerUUID, entityWither ->
            val player = Bukkit.getPlayer(playerUUID)
            if (player == null) {
                withers.remove(playerUUID)
            } else {
                entityWither.customName = title
                val packetPlayOutEntityMetadata =
                    PacketPlayOutEntityMetadata(entityWither.id, entityWither.dataWatcher, true)

                getConnection(player).sendPacket(packetPlayOutEntityMetadata)
            }
        }
    }
}

class HenixBar(var title: String, var player: Player) : BukkitRunnable() {
    var witherEntity: EntityWither

    init {
        val craftworld = player.world as CraftWorld
        val worldserver = craftworld.handle
        val wither = EntityWither(worldserver)

        val location = getWitherLocation(player.location)

        wither.customName = title
        wither.isInvisible = true
        wither.setLocation(location.x, location.y, location.z, 0f, 0f)
        val packet = PacketPlayOutSpawnEntityLiving(wither)

        getConnection(player).sendPacket(packet)

        witherEntity = wither
    }

    override fun run() {
        if (!player.isOnline) {
            this.cancel()
            return
        }
        val location = getWitherLocation(player.eyeLocation)
        witherEntity.setLocation(location.x, location.y, location.z, location.yaw, location.pitch)
        val packetPlayOutEntityTeleport = PacketPlayOutEntityTeleport(witherEntity)
        getConnection(player).sendPacket(packetPlayOutEntityTeleport)

    }

    //TODO: CHECK SET PROGRESS, CAUSES BUG WITH VISIBILITY OF ENTITY ONCE PERCENTAGE <0.5 (VANILLA BEHAVIOR, MAYBE CREATE CUSTOM MOB?)
    fun setProgress(progress: Float) {
        witherEntity.health = progress * witherEntity.maxHealth

        val packetPlayOutEntityMetadata =
            PacketPlayOutEntityMetadata(witherEntity.id, witherEntity.dataWatcher, true)

        getConnection(player).sendPacket(packetPlayOutEntityMetadata)
    }
    fun destroy(){
        val packet = PacketPlayOutEntityDestroy(witherEntity.id)
        getConnection(player).sendPacket(packet)
        cancel()

    }

    fun updateTitle(title: String) {
        this.title = title
        witherEntity.customName = title

        val packetPlayOutEntityMetadata =
            PacketPlayOutEntityMetadata(witherEntity.id, witherEntity.dataWatcher, true)

        getConnection(player).sendPacket(packetPlayOutEntityMetadata)
    }


}

private fun getWitherLocation(location: Location): Location {
    return location.add(location.direction.normalize().multiply(50).add(Vector(0, 15, 0)))
}

private fun getConnection(player: Player): PlayerConnection {
    return (player as CraftPlayer).handle.playerConnection
}