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

        generateFrame(startLoc, sizeX, sizeZ)
        generateCorrectPath(startLoc, sizeX, sizeZ)

        return true

    }

    private fun generateFrame(startLoc: Location, sizeX: Int, sizeZ: Int) {

        val world = startLoc.world ?: return

        startLoc.block.type = Material.LIME_CONCRETE

        val borderStart = startLoc.clone().subtract(1.0, 0.0,1.0)

        for (x in borderStart.blockX .. borderStart.blockX + sizeX) {
            world.getBlockAt(x, borderStart.blockY, borderStart.blockZ).type = Material.PINK_CONCRETE
            world.getBlockAt(x, borderStart.blockY, borderStart.blockZ + sizeZ).type = Material.PINK_CONCRETE
        }

        for (z in borderStart.blockZ .. borderStart.blockZ + sizeZ) {
            world.getBlockAt(borderStart.blockX, borderStart.blockY, z).type = Material.PINK_CONCRETE
            world.getBlockAt(borderStart.blockX + sizeX, borderStart.blockY, z).type = Material.PINK_CONCRETE
        }

    }

    private fun generateCorrectPath(startLoc: Location, sizeX: Int, sizeZ: Int) {
        val world = startLoc.world ?: return

        var direction = 0 // 0:X | 1:Z, 2:-X, 3:-Z
        var lastDirection = 0

        val endLoc = startLoc.clone().add(sizeX - 2.0, 0.0, sizeZ - 2.0)
        val deltaX = endLoc.blockX - startLoc.blockX
        val deltaZ = endLoc.blockZ - startLoc.blockZ

        var location = startLoc.clone()
        var lastLocation = startLoc.clone()

        //while (location != endLoc) {
        for (n in 0 .. 10) {
                delay({
                lastLocation = location
                lastDirection = direction

                val new = getNewPosition(location, direction)
                location = new.first
                direction = new.second

                location.block.type = Material.WHITE_CONCRETE

                delay({
                    makeWalls(lastLocation)
                }, (20 * n) + 10)

                //makeWalls(lastLocation)
                //Bukkit.broadcastMessage("${location.blockX}, ${location.blockY}, ${location.blockZ}")
                Bukkit.broadcastMessage("$direction")
            }, 20 * n)
        }

        endLoc.block.type = Material.LIME_CONCRETE

    }

    private fun Int.flip(): Int {
        return if (this == 0) 1 else 0
    }

        private fun getAvailableDirections(location: Location) : MutableList<Int> {

        val world = location.getWorld()!!;
        val availableDirections = mutableListOf<Int>()

        val loc0 = location.clone().add(1.0, 0.0, 0.0)
        val loc1 = location.clone().add(0.0, 0.0, 1.0)
        val loc2 = location.clone().add(-1.0, 0.0, 0.0)
        val loc3 = location.clone().add(0.0, 0.0, -1.0)
        if (world.getBlockAt(loc0).type.isAir) availableDirections.add(0)
        if (world.getBlockAt(loc1).type.isAir) availableDirections.add(1)
        if (world.getBlockAt(loc2).type.isAir) availableDirections.add(2)
        if (world.getBlockAt(loc3).type.isAir) availableDirections.add(3)

        return availableDirections

    }

    private fun getLocationInDirection(location: Location, direction: Int) : Location {
        return when (direction) {
            0 -> location.clone().add(1.0, 0.0, 0.0)
            1 -> location.clone().add(0.0, 0.0, 1.0)
            2 -> location.clone().add(-1.0, 0.0, 0.0)
            3 -> location.clone().add(0.0, 0.0, -1.0)
            else -> location // this should never happen
        }
    }

    private fun getNewDirection(currentLoc: Location, currentDirection: Int): Int {
        val availableDirections = getAvailableDirections(currentLoc)
        if (availableDirections.size > 1) availableDirections.remove(currentDirection)
        return if (availableDirections.contains(currentDirection) && Math.random() > 0.3) currentDirection else availableDirections.random()
    }

    private fun getNewPosition(currentLoc: Location, currentDirection: Int) : Pair<Location, Int> {
        val newDirection = getNewDirection(currentLoc, currentDirection)
        val newLocation = getLocationInDirection(currentLoc, newDirection)
        return Pair(newLocation, newDirection)
    }

    private fun makeWalls(location: Location) {
        Bukkit.broadcastMessage("${location.blockX}, ${location.blockY}, ${location.blockZ}")
        val world = location.world ?: return
        val availableDirections = getAvailableDirections(location)
        for (direction in availableDirections) {
            val newLoc = getLocationInDirection(location, direction)
            world.getBlockAt(newLoc.clone().subtract(0.0,0.0,0.0)).type = Material.RED_CONCRETE
        }
    }

}