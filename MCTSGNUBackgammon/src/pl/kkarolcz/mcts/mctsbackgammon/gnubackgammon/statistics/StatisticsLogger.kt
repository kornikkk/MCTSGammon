package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.statistics

import org.apache.log4j.Logger
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts.toGNUBackgammonFormat
import pl.kkarolcz.mcts.mctsbackgammon.statistics.Statistics
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter

/**
 * Created by kkarolcz on 24.03.2018.
 */
object StatisticsLogger {
    private val log = Logger.getLogger(StatisticsLogger.javaClass)

    private var logWriter: PrintWriter? = null

    fun setLogFile(filename: String) {
        logWriter = PrintWriter(BufferedWriter(FileWriter(filename)), true)
        Runtime.getRuntime().addShutdownHook(Thread {
            logWriter?.close()
        })
    }

    fun logGameStarted() {
        log("Game: ${Statistics.gamesCount}")
    }

    fun logRoundStarted() {
        log("  Round: ${Statistics.currentGame.roundsCount}")
    }

    fun logRound() {
        val round = Statistics.currentGame.currentRound
        log("    Non doubling searches: ${round.nonDoublingSearches}")
        log("    Doubling searches: ${round.doublingSearches}")
        log("    Moving checkers: ${round.move.toGNUBackgammonFormat()}")
    }

    fun logGameFinished() {
        log("Game finished. Winner: ${Statistics.currentGame.winner}")
    }

    private fun log(message: String) {
        log.info(message)
    }

    private fun printToFile(message: String) {
        logWriter?.print(message)
    }
}