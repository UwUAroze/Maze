package me.aroze.maze.command

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

        val endLoc = startLoc.clone().add(sizeX - 2.0, 0.0, sizeZ - 2.0)
        val deltaX = endLoc.blockX - startLoc.blockX
        val deltaZ = endLoc.blockZ - startLoc.blockZ

        var currentLoc = startLoc.clone()
        var lastLoc = startLoc.clone()
        var lastDirection = 0

        while (currentLoc != endLoc) {
            direction = getNewDirection(currentLoc, direction)
            lastLoc = currentLoc


        }

        endLoc.block.type = Material.LIME_CONCRETE

    }

    private fun Int.flip(): Int {
        return if (this == 0) 1 else 0
    }

    private fun getNewDirection(currentLoc: Location, currentDirection: Int): Int {

        val world = currentLoc.getWorld()!!;

        val availableDirections = mutableListOf<Int>()

        if (world.getBlockAt(currentLoc.blockX + 1, currentLoc.blockY, currentLoc.blockZ).type.isAir) availableDirections.add(0)
        if (world.getBlockAt(currentLoc.blockX, currentLoc.blockY, currentLoc.blockZ + 1).type.isAir) availableDirections.add(1)
        if (world.getBlockAt(currentLoc.blockX - 1, currentLoc.blockY, currentLoc.blockZ).type.isAir) availableDirections.add(2)
        if (world.getBlockAt(currentLoc.blockX, currentLoc.blockY, currentLoc.blockZ + 1).type.isAir) availableDirections.add(3)

        return if (Math.random() > 0.3) currentDirection else availableDirections.random()
    }

}