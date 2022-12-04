package me.aroze.maze.command

import me.aroze.maze.util.delay
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object TestGenerateCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        val player = sender as Player
        val startLoc = player.location

        val sizeX = args[0].toInt()
        val sizeZ = args[1].toInt()

        generateMaze(sizeX, sizeZ, startLoc)

        return true

    }

    fun generateMaze(sizeX: Int, sizeZ: Int, startLoc: Location) {

        val world = startLoc.world!!
        val matrix = Array(sizeX) { Array(sizeZ) { false } }
        val stack = mutableListOf<Pair<Int, Int>>()

        var currentX = 0
        var currentZ = 0

        matrix[currentX][currentZ] = true
        stack.add(Pair(currentX, currentZ))

            while (stack.isNotEmpty()) {

                val neighbors = mutableListOf<Pair<Int, Int>>()
                if (currentX > 0 && !matrix[currentX - 2][currentZ]) neighbors.add(Pair(currentX - 2, currentZ))
                if (currentX < sizeX - 2 && !matrix[currentX + 2][currentZ]) neighbors.add(Pair(currentX + 2, currentZ))
                if (currentZ > 0 && !matrix[currentX][currentZ - 2]) neighbors.add(Pair(currentX, currentZ - 2))
                if (currentZ < sizeZ - 2 && !matrix[currentX][currentZ + 2]) neighbors.add(Pair(currentX, currentZ + 2))

                if (neighbors.isNotEmpty()) {

                    for (neighbor in neighbors) {
                    }

                    val next = neighbors.random()
                    val middle = getMiddleBlock(currentX, currentZ, next.first, next.second)

                    currentX = next.first
                    currentZ = next.second

                    matrix[currentX][currentZ] = true
                    stack.add(Pair(currentX, currentZ))

                    world.getBlockAt(startLoc.clone().add(middle.first.toDouble(), 0.0, middle.second.toDouble())).type = Material.WHITE_CONCRETE
                    world.getBlockAt(startLoc.clone().add(currentX.toDouble(), 0.0, currentZ.toDouble())).type = Material.WHITE_CONCRETE
                } else {
                    val last = stack.removeAt(stack.size - 1)
                    currentX = last.first
                    currentZ = last.second
                }

            }

        }

    fun getMiddleBlock(x1: Int, z1: Int, x2: Int, z2: Int): Pair<Int, Int> {
        val x = (x1 + x2) / 2
        val z = (z1 + z2) / 2
        return Pair(x, z)
    }

}