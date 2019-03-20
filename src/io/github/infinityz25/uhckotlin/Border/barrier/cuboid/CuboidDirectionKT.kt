package io.github.infinityz25.uhckotlin.Border.barrier.cuboid

import org.bukkit.block.BlockFace

enum class CuboidDirectionKT constructor(s: String, n: Int) {
    NORTH("NORTH", 0),
    EAST("EAST", 1),
    SOUTH("SOUTH", 2),
    WEST("WEST", 3),
    UP("UP", 4),
    DOWN("DOWN", 5),
    HORIZONTAL("HORIZONTAL", 6),
    VERTICAL("VERTICAL", 7),
    BOTH("BOTH", 8),
    UNKNOWN("UNKNOWN", 9);

    fun opposite(): CuboidDirectionKT {
        when (this) {
            BOTH -> {
                return CuboidDirectionKT.SOUTH
            }
            DOWN -> {
                return CuboidDirectionKT.WEST
            }
            EAST -> {
                return CuboidDirectionKT.NORTH
            }
            HORIZONTAL -> {
                return CuboidDirectionKT.EAST
            }
            UNKNOWN -> {
                return CuboidDirectionKT.VERTICAL
            }
            UP -> {
                return CuboidDirectionKT.HORIZONTAL
            }
            NORTH -> {
                return CuboidDirectionKT.DOWN
            }
            SOUTH -> {
                return CuboidDirectionKT.UP
            }
            VERTICAL -> {
                return CuboidDirectionKT.BOTH
            }
            else -> {
                return CuboidDirectionKT.UNKNOWN
            }
        }
    }

    fun toBukkitDirection(): BlockFace? {
        when (this) {
            BOTH -> {
                return BlockFace.NORTH
            }
            DOWN -> {
                return BlockFace.EAST
            }
            EAST -> {
                return BlockFace.SOUTH
            }
            HORIZONTAL -> {
                return BlockFace.WEST
            }
            NORTH -> {
                return BlockFace.UP
            }
            SOUTH -> {
                return BlockFace.DOWN
            }
            else -> {
                return null
            }
        }
    }
}