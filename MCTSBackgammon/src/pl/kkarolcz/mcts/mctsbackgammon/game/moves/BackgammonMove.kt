package pl.kkarolcz.mcts.mctsbackgammon.game.moves

/**
 * Created by kkarolcz on 24.08.2017.
 */
data class BackgammonMove internal constructor(val oldIndex: Byte, val newIndex: Byte) {
    fun reversed() = BackgammonMove(newIndex, oldIndex)
}