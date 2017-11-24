package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.BEAR_OFF_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.BackgammonMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.BackgammonMovesSequence

/**
 * Created by kkarolcz on 30.08.2017.
 */
fun convertToGNUBackgammonMove(movesSequence: BackgammonMovesSequence?): String {
    if (movesSequence == null) {
        return ""
    }
    return movesSequence.moves.joinToString(" ") { singleMove -> singleMove.convertToGNUBackgammonSingleMove() }
}

private fun BackgammonMove.convertToGNUBackgammonSingleMove(): String =
        convertToGNUBackgammonCheckerIndex(oldIndex) + "/" + convertToGNUBackgammonCheckerIndex(newIndex)


private fun convertToGNUBackgammonCheckerIndex(checkerIndex: Byte): String = when (checkerIndex) {
    BEAR_OFF_INDEX -> "off"
    BAR_INDEX -> "bar"
    else -> checkerIndex.toString()
}