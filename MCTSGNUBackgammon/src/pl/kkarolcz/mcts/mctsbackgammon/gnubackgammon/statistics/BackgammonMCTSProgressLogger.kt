package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.statistics

import org.apache.log4j.Logger
import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonMCTSProgress.BackgammonMCTSProgressListener
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts.toGNUBackgammonFormat
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter

/**
 * Created by kkarolcz on 24.03.2018.
 */
class BackgammonMCTSProgressLogger : BackgammonMCTSProgressListener {
    private val log = Logger.getLogger(this.javaClass)

    private var logWriter: PrintWriter? = null

    fun setLogFile(filename: String) {
        logWriter = PrintWriter(BufferedWriter(FileWriter(filename)), true)
        Runtime.getRuntime().addShutdownHook(Thread {
            logWriter?.close()
        })
    }

    override fun onGamesSequenceStart(gamesCount: Int, simulationsLimit: Int) {
        log("Playing $gamesCount games...")
    }

    override fun onGameStart(game: Int) {
        log("Game: $game")
    }

    override fun onGameRoundStart(round: Int, dice: Dice) {
        log("  Round: $round, Dice: $dice")
    }

    override fun onMonteCarloSimulationStart(simulation: Int) {}

    override fun onGameRoundEnd(round: Int, simulationsLimit: Int, move: FullMove) {
        log("    Move: ${move.toGNUBackgammonFormat()}")
    }

    override fun onGameEnd(game: Int, gameRounds: Int, winner: Player) {
        log("Game $game finished. Rounds: $gameRounds, Winner: $winner")
    }

    override fun onGamesSequenceEnd(games: Int) {
        log("Games sequence finished. $games games played.")
    }

    private fun log(message: String) {
        log.info(message + "\n")
    }

    private fun printToFile(message: String) {
        logWriter?.print(message)
    }
}