package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BEAR_OFF_INDEX

/**
 * Created by kkarolcz on 30.08.2017.
 */
fun formatForGNUBackgammon(fullMove: FullMove?): String {
    if (fullMove == null) {
        return ""
    }
    return fullMove.joinToString(" ") { singleMove -> singleMove.formatForGNUBackgammon() }
}

private fun SingleMove.formatForGNUBackgammon(): String =
        formatIndexForGNUBackgammon(oldIndex) + "/" + formatIndexForGNUBackgammon(newIndex)


private fun formatIndexForGNUBackgammon(checkerIndex: Byte): String = when (checkerIndex) {
    BEAR_OFF_INDEX -> "off"
    BAR_INDEX -> "bar"
    else -> checkerIndex.toString()
}