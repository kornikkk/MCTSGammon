package pl.kkarolcz.mcts.mctsbackgammon.statistics

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove

/**
 * Created by kkarolcz on 14.03.2018.
 */
object Statistics {
    private val games = mutableListOf<Game>()

    val currentGame: Game get() = games.lastOrNull() ?: throw IllegalStateException("Game not started")
    val gamesCount: Int get() = games.size

    fun newGame() {
        games.add(Game())
    }

    fun reset() {
        games.clear()
    }


    class Game {
        private val rounds = mutableListOf<Round>()

        val currentRound: Round get() = rounds.lastOrNull() ?: throw IllegalStateException("Round not started")
        val roundsCount: Int get() = rounds.size

        private var _winner: Player? = null
        val winner: Player get() = _winner ?: throw IllegalStateException("Game not finished")

        fun newRound() {
            rounds.add(Round())
        }

        fun endGame(winner: Player) {
            _winner = winner
        }


        class Round {
            var nonDoublingSearches: Long = 0
                private set

            var doublingSearches: Long = 0
                private set

            private var _move: FullMove? = null
            val move: FullMove get() = _move ?: throw IllegalStateException("Round not finished")

            fun incNonDoublingSearches() {
                nonDoublingSearches += 1
            }

            fun incDoublingSearches() {
                doublingSearches += 1
            }

            fun finishRound(move: FullMove) {
                _move = move
            }

        }
    }


}