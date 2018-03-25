package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts

import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BEAR_OFF_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleMove

/**
 * Created by kkarolcz on 30.08.2017.
 */
fun FullMove.toGNUBackgammonFormat(): String {
    return this.joinToString(" ") { singleMove -> singleMove.toGNUBackgammonFormat() }
}

private fun SingleMove.toGNUBackgammonFormat(): String =
        toGNUBackgammonFormat(oldIndex) + "/" + toGNUBackgammonFormat(newIndex)


private fun toGNUBackgammonFormat(checkerIndex: Byte): String = when (checkerIndex) {
    BEAR_OFF_INDEX -> "off"
    BAR_INDEX -> "bar"
    else -> checkerIndex.toString()
}