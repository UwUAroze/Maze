package me.aroze.maze

import me.aroze.maze.command.TestGenerateCommand
import org.bukkit.plugin.java.JavaPlugin

class Maze : JavaPlugin() {
    override fun onEnable() {
        getCommand("testgenerate")!!.setExecutor(TestGenerateCommand)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}