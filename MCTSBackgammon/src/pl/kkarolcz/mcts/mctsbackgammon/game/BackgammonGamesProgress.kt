package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove

/**
 * Created by kkarolcz on 24.03.2018.
 */
class BackgammonGamesProgress {
    private val listeners = mutableListOf<BackgammonMCTSProgressListener>()

    private var gamesCount: Int = 0
    private var simulationsLimit: Int = 0
    private var game: Int = 0
    private var round: Int = 0
    private var simulation: Int = 0

    fun addProgressListener(listener: BackgammonMCTSProgressListener) {
        listeners.add(listener)
    }

    fun newGamesSequence(gamesCount: Int, simulationsLimit: Int) {
        this.gamesCount = gamesCount
        this.simulationsLimit = simulationsLimit
        this.game = 0
        this.round = 0
        this.simulation = 0

        listeners.forEach { it.onGamesSequenceStart(gamesCount, simulationsLimit) }
    }

    fun newGame() {
        game += 1
        round = 0
        listeners.forEach { it.onGameStart(game) }
    }

    fun newGameRound(dice: Dice) {
        round += 1
        simulation = 0
        listeners.forEach { it.onGameRoundStart(round, dice) }
    }

    fun newMonteCarloRound() {
        simulation += 1
        listeners.forEach { it.onMonteCarloSimulationStart(simulation) }
    }

    fun endGameRound(move: FullMove) {
        listeners.forEach { it.onGameRoundEnd(round, simulationsLimit, move) }
    }

    fun endGame(winner: Player) {
        listeners.forEach { it.onGameEnd(game, round, winner) }
    }

    fun endGamesSequence(games: Int) {
        listeners.forEach { it.onGamesSequenceEnd(games) }
    }

    abstract class BackgammonMCTSProgressListenerAdapter : BackgammonMCTSProgressListener {
        override fun onGamesSequenceStart(gamesCount: Int, simulationsLimit: Int) {}
        override fun onGameStart(game: Int) {}
        override fun onGameRoundStart(round: Int, dice: Dice) {}
        override fun onMonteCarloSimulationStart(simulation: Int) {}
        override fun onGameRoundEnd(round: Int, simulationsLimit: Int, move: FullMove) {}
        override fun onGameEnd(game: Int, gameRounds: Int, winner: Player) {}
        override fun onGamesSequenceEnd(games: Int) {}

    }

    interface BackgammonMCTSProgressListener {
        fun onGamesSequenceStart(gamesCount: Int, simulationsLimit: Int)
        fun onGameStart(game: Int)
        fun onGameRoundStart(round: Int, dice: Dice)
        fun onMonteCarloSimulationStart(simulation: Int)
        fun onGameRoundEnd(round: Int, simulationsLimit: Int, move: FullMove)
        fun onGameEnd(game: Int, gameRounds: Int, winner: Player)
        fun onGamesSequenceEnd(games: Int)
    }
}