package pl.kkarolcz.mcts

/**
 * Created by kkarolcz on 24.08.2017.
 */
enum class Player {
    MCTS, OPPONENT;

    fun opponent(): Player = when (this) {
        MCTS -> OPPONENT
        OPPONENT -> MCTS
    }

    override fun toString(): String = when (this) {
        MCTS -> "MCTS"
        OPPONENT -> "Opponent"
    }
}