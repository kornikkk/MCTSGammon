package pl.kkarolcz.mcts.mctsbackgammon.game

/**
 * Created by kkarolcz on 24.08.2017.
 */
enum class BackgammonPlayer {
    PLAYER_ONE, PLAYER_TWO;

    companion object {
        fun fromInt(playerId: Int) = when (playerId) {
            0 -> PLAYER_ONE
            1 -> PLAYER_TWO
            else -> throw IllegalArgumentException("Wrong Player ID. Only '0' and '1' are legal parameters")
        }
    }

    fun opponent(): BackgammonPlayer = when (this) {
        PLAYER_ONE -> PLAYER_TWO
        PLAYER_TWO -> PLAYER_ONE
    }

    fun toInt(): Int = when (this) {
        PLAYER_ONE -> 0
        PLAYER_TWO -> 1
    }
}