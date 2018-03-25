package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.Player

/**
 * Created by kkarolcz on 24.03.2018.
 */
class BackgammonMCTSProgress {
    private val listeners = mutableListOf<BackgammonMCTSProgressListener>()

    private var gamesCount: Int = 0
    private var simulationsLimit: Int = 0
    private var game: Int = 0
    private var round: Int = 0
    private var simulation: Int = 0

    fun addProgressListener(listener: BackgammonMCTSProgressListener) {
        listeners.add(listener)
    }

    fun reset(gamesCount: Int) {
        this.gamesCount = gamesCount
        this.simulationsLimit = 0
        this.game = 0
        this.round = 0
        this.simulation = 0
    }

    fun setSimulationsLimit(simulationsLimit: Int) {
        this.simulationsLimit = 0
        this.simulation = 0
    }

    fun newGame() {
        game += 1
        round = 0
        listeners.forEach { it.onNewGame(game, gamesCount) }
    }

    fun newGameRound() {
        round += 1
        simulation = 0
        listeners.forEach { it.onNewGameRound(round) }
    }

    fun newMonteCarloRound() {
        simulation += 1
        listeners.forEach { it.onNewMonteCarloSimulation(simulation, simulationsLimit) }
    }

    fun endGame(winner: Player) {
        listeners.forEach { it.onGameEnd(game, gamesCount, winner) }
    }

    interface BackgammonMCTSProgressListener {
        fun onNewGame(game: Int, gamesCount: Int)
        fun onGameEnd(game: Int, gamesCount: Int, winner: Player)
        fun onNewGameRound(round: Int)
        fun onNewMonteCarloSimulation(simulation: Int, simulationsLimit: Int)
    }
}