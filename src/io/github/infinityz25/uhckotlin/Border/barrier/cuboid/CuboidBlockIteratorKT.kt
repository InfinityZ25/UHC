package io.github.infinityz25.uhckotlin.Border.barrier.cuboid

import org.bukkit.World
import org.bukkit.block.Block

class CuboidBlockIteratorKT(val world: World, x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) : Iterator<Block> {

    var baseX = x1
    var baseY = y1
    var baseZ = z1
    var sizeX = Math.abs(x2 - x1)+1
    var sizeY = Math.abs(y2 - y1)+1
    var sizeZ = Math.abs(z2 - z1)+1
    var x = 0
    var y = 0
    var z = 0

    override fun hasNext(): Boolean {
        return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ
    }

    override fun next(): Block {
        val block = this.world.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z)
        if (++this.x >= this.sizeX) {
            this.x = 0
            if (++this.y >= this.sizeY) {
                this.y = 0
                ++this.z
            }
        }
        return block
    }


}