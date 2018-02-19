package pl.kkarolcz.mcts.mctsbackgammon.game

/**
 * Created by kkarolcz on 24.08.2017.
 */
enum class Player {
    PLAYER_ONE, PLAYER_TWO;

    companion object {
        fun fromInt(playerId: Int) = when (playerId) {
            0 -> PLAYER_ONE
            1 -> PLAYER_TWO
            else -> throw IllegalArgumentException("Wrong Player ID. Only '0' and '1' are legal parameters")
        }
    }

    fun opponent(): Player = when (this) {
        PLAYER_ONE -> PLAYER_TWO
        PLAYER_TWO -> PLAYER_ONE
    }

    fun toInt(): Int = when (this) {
        PLAYER_ONE -> 0
        PLAYER_TWO -> 1
    }

    override fun toString(): String = when (this) {
        PLAYER_ONE -> "MCTS"
        PLAYER_TWO -> "Opponent"
    }
}