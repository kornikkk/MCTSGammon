package pl.kkarolcz.mcts.mctsbackgammon.settings

import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.formatForGNUBackgammon
import java.io.File

/**
 * Created by kkarolcz on 14.03.2018.
 */
object Statistics {
    private var logFile: File? = null
    private var logToFile: Boolean = false
    private var logToConsole: Boolean = true

    private var currentGame: Int = 0

    fun setUp(logToFile: Boolean, logToConsole: Boolean) {
        this.logToFile = logToFile
        this.logToConsole = logToConsole
    }

    fun setLogFile(logFile: File) {
        if (logFile.exists()) {
            logFile.createNewFile()
        }
        Statistics.logFile = logFile
    }

    fun newGame() {
        Game.reset()
        currentGame += 1
        log("Game: $currentGame")
    }

    object Game {
        private var currentRound: Int = 0

        fun newRound() {
            if (currentGame == 0) throw IllegalStateException("Game not started. Cannot start a new round")

            Round.reset()
            currentRound += 1
            log("  Round: $currentRound")
        }

        internal fun reset() {
            currentRound = 0
        }

        object Round {
            internal var nonDoublingSearches: Long = 0
                private set

            internal var doublingSearches: Long = 0
                private set

            fun incNonDoublingSearches() {
                if (currentRound == 0) throw IllegalStateException("Round not started")
                nonDoublingSearches += 1
            }

            fun incDoublingSearches() {
                if (currentRound == 0) throw IllegalStateException("Round not started")
                doublingSearches += 1
            }

            fun finishRound(move: FullMove) {
                if (currentRound == 0) throw IllegalStateException("Round not started")
                log("    Non doubling searches: ${Round.nonDoublingSearches}")
                log("    Doubling searches: ${Round.doublingSearches}")
                log("    Moving checkers: ${formatForGNUBackgammon(move)}")
            }

            internal fun reset() {
                nonDoublingSearches = 0
                doublingSearches = 0
            }

        }
    }

    fun log(message: String) {
        if (logToConsole)
            println(message)

        if (logToFile)
            printToFile(message)
    }

    private fun printToFile(message: String) {
        logFile?.appendText(message, Charsets.UTF_8) ?: IllegalStateException("Log file not set")
    }
}