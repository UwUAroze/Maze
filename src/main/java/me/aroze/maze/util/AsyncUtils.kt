package me.aroze.maze.util

import me.aroze.maze.Maze
import org.bukkit.scheduler.BukkitTask
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer


fun async(code: Consumer<BukkitTask>) {
    Maze.getInstance().server.scheduler.runTaskAsynchronously(Maze.getInstance(), code)
}

fun sync(code: Consumer<BukkitTask>) {
    Maze.getInstance().server.scheduler.runTask(Maze.getInstance(), code)
}

fun delay(code: Consumer<BukkitTask>, ticks: Int) {
    Maze.getInstance().server.scheduler.runTaskLater(Maze.getInstance(), code, ticks.toLong())
}

fun timer(code: Consumer<BukkitTask>, times: Int, delay: Int) {
    val ran = AtomicInteger()
    Maze.getInstance().server.scheduler.runTaskTimer(Maze.getInstance(), { it: BukkitTask ->
        code.accept(it)
        ran.getAndIncrement()
        if (ran.get() == times) it.cancel()
    }, 0, delay.toLong())
}