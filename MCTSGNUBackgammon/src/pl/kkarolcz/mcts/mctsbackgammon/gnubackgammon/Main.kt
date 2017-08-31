package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon

import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts.GNUBackgammonMCTS
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.server.GNUBackgammonServerSocket

/**
 * Created by kkarolcz on 29.08.2017.
 */
var debug = false

fun main(args: Array<String>) {
    if (args.contains("-d"))
        debug = true
    println("MCTSGammon started")

    val gnuBackgammonMCTS = GNUBackgammonMCTS()

    println("Starting server socket...")
    try {
        println("Server socket started")
        GNUBackgammonServerSocket(gnuBackgammonMCTS).startServer()
    } finally {
        println("Server socket stopped")
    }
}