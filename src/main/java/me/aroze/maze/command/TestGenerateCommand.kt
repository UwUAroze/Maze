package me.aroze.maze.command

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object TestGenerateCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        val player = sender as Player

        generateFrame(player.location, args[0].toInt(), args[1].toInt())

        return true

    }

    private fun generateFrame(location: Location, sizeX: Int, sizeZ: Int) {

        val world = location.world ?: return

        location.block.type = Material.LIME_CONCRETE

        val borderStart = location.clone().subtract(1.0, 0.0,1.0)

        for (x in borderStart.blockX .. borderStart.blockX + sizeX) {
            world.getBlockAt(x, borderStart.blockY, borderStart.blockZ).type = Material.PINK_CONCRETE
            world.getBlockAt(x, borderStart.blockY, borderStart.blockZ + sizeZ).type = Material.PINK_CONCRETE
        }

        for (z in borderStart.blockZ .. borderStart.blockZ + sizeZ) {
            world.getBlockAt(borderStart.blockX, borderStart.blockY, z).type = Material.PINK_CONCRETE
            world.getBlockAt(borderStart.blockX + sizeX, borderStart.blockY, z).type = Material.PINK_CONCRETE
        }

    }

}