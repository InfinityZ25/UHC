package io.github.infinityz25.uhckotlin.Border.barrier.cuboid

import com.google.common.base.Preconditions
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.*

class Cuboid(first: Location, second: Location) : Iterable<Block>, Cloneable, ConfigurationSerializable {
    private var worldName: String? = null
    private var x1: Int = 0
    private var y1: Int = 0
    private var z1: Int = 0
    private var x2: Int = 0
    private var y2: Int = 0
    private var z2: Int = 0

    val world: World
        get() = Bukkit.getWorld(this.worldName)

    val sizeX: Int
        get() = this.x2 - this.x1 + 1

    val sizeY: Int
        get() = this.y2 - this.y1 + 1

    val sizeZ: Int
        get() = this.z2 - this.z1 + 1


    val minimumPoint: Location
        get() =
            Location(
                world,
                Math.min(this.x1, this.x2).toDouble(),
                Math.min(this.y1, this.y2).toDouble(),
                Math.min(this.z1, this.z2).toDouble()
            )

    val maximumPoint: Location
        get() =
            Location(
                world,
                Math.max(this.x1, this.x2).toDouble(),
                Math.max(this.y1, this.y2).toDouble(),
                Math.max(this.z1, this.z2).toDouble()
            )

    val length: Int
        get() = maximumPoint.blockZ - minimumPoint.blockZ

    val chunks: List<Chunk>
        get() {
            val world = world
            val x1 = this.x1 and -0x10
            val x2 = this.x2 and -0x10
            val z1 = this.z1 and -0x10
            val z2 = this.z2 and -0x10
            val result = ArrayList<Chunk>(x2 - x1 + 16 + (z2 - z1) * 16)
            var x3 = x1
            while (x3 <= x2) {
                var z3 = z1
                while (z3 <= z2) {
                    result.add(world.getChunkAt(x3 shr 4, z3 shr 4))
                    z3 += 16
                }
                x3 += 16
            }
            return result
        }

    init {
        Preconditions.checkNotNull(first, "Location 1 cannot be null")
        Preconditions.checkNotNull(second, "Location 2 cannot be null")
        Preconditions.checkArgument(first.world == second.world, "Locations must be on the same world")
        this.worldName = first.world.name
        this.x1 = Math.min(first.blockX, second.blockX)
        this.y1 = Math.min(first.blockY, second.blockY)
        this.z1 = Math.min(first.blockZ, second.blockZ)
        this.x2 = Math.max(first.blockX, second.blockX)
        this.y2 = Math.max(first.blockY, second.blockY)
        this.z2 = Math.max(first.blockZ, second.blockZ)
    }

    override fun serialize(): Map<String, Any> {
        val map = LinkedHashMap<String, Any>()
        map["uhcworld"] = this.worldName!!
        map["x1"] = this.x1
        map["y1"] = this.y1
        map["z1"] = this.z1
        map["x2"] = this.x2
        map["y2"] = this.y2
        map["z2"] = this.z2
        return map
    }

    @JvmOverloads
    fun edges(fixedMinX: Int = -1, fixedMaxX: Int = -1, fixedMinZ: Int = -1, fixedMaxZ: Int = -1): List<Vector> {
        val v1 = minimumPoint.toVector()
        val v2 = maximumPoint.toVector()
        val minX = v1.blockX
        val maxX = v2.blockX
        val minZ = v1.blockZ
        val maxZ = v2.blockZ
        var capacity = (maxX - minX) * 4 + (maxZ - minZ) * 4
        capacity += 4
        val result = ArrayList<Vector>(capacity)
        if (capacity <= 0) {
            return result
        }
        val minY = v1.blockY
        val maxY = v1.blockY
        for (x in minX..maxX) {
            result.add(Vector(x, minY, minZ))
            result.add(Vector(x, minY, maxZ))
            result.add(Vector(x, maxY, minZ))
            result.add(Vector(x, maxY, maxZ))
        }
        for (z in minZ..maxZ) {
            result.add(Vector(minX, minY, z))
            result.add(Vector(minX, maxY, z))
            result.add(Vector(maxX, minY, z))
            result.add(Vector(maxX, maxY, z))
        }
        return result
    }

    operator fun contains(cuboid: Cuboid): Boolean {
        return contains(cuboid.minimumPoint) || contains(cuboid.maximumPoint)
    }

    operator fun contains(player: Player): Boolean {
        return contains(player.location)
    }

    fun contains(world: World?, x: Int, z: Int): Boolean {
        return (world == null || world == world) && x >= this.x1 && x <= this.x2 && z >= this.z1 && z <= this.z2
    }

    fun contains(x: Int, y: Int, z: Int): Boolean {
        return x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2 && z >= this.z1 && z <= this.z2
    }

    operator fun contains(block: Block): Boolean {
        return contains(block.location)
    }

    operator fun contains(location: Location?): Boolean {
        if (location == null || this.worldName == null) {
            return false
        }
        val world = location.world
        return world != null && this.worldName == location.world.name && contains(
            location.blockX,
            location.blockY,
            location.blockZ
        )
    }

    override fun iterator(): Iterator<Block> {
        return CuboidBlockIteratorKT(world, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2)
    }

    fun locationIterator(): Iterator<Location> {
        return CuboidLocationIteratorKT(world, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2)
    }

    public override fun clone(): Cuboid {
        try {
            return super.clone() as Cuboid
        } catch (ex: CloneNotSupportedException) {
            throw RuntimeException("This could never happen", ex)
        }

    }

    override fun toString(): String {
        return "Cuboid: " + this.worldName + ','.toString() + this.x1 + ','.toString() + this.y1 + ','.toString() + this.z1 + "=>" + this.x2 + ','.toString() + this.y2 + ','.toString() + this.z2
    }
}
