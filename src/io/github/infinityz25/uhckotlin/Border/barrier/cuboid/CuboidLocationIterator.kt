package io.github.infinityz25.uhckotlin.Border.barrier.cuboid

import org.bukkit.Location
import org.bukkit.World

class CuboidLocationIteratorKT(
    private val world: World,
    private val baseX: Int,
    private val baseY: Int,
    private val baseZ: Int,
    x2: Int,
    y2: Int,
    z2: Int
) : Iterator<Location> {
    private val sizeX= Math.abs(x2 - baseX) + 1
    private val sizeY= Math.abs(y2 - baseY) + 1
    private val sizeZ= Math.abs(z2 - baseZ) + 1
    private var x=0
    private var y= 0
    private var z= 0


    override fun hasNext(): Boolean {
        return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ
    }

    override fun next(): Location {
        val location = Location(
            this.world,
            (this.baseX + this.x).toDouble(),
            (this.baseY + this.y).toDouble(),
            (this.baseZ + this.z).toDouble()
        )
        if (++this.x >= this.sizeX) {
            this.x = 0
            if (++this.y >= this.sizeY) {
                this.y = 0
                ++this.z
            }
        }
        return location
    }


}