package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.mcts

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.BackgammonMovesSequence
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleBackgammonMove

/**
 * Created by kkarolcz on 30.08.2017.
 */
fun convertToGNUBackgammonMove(movesSequence: BackgammonMovesSequence?): String {
    if (movesSequence == null) {
        return ""
    }
    return movesSequence.singleMoves.joinToString(" ") { singleMove -> singleMove.convertToGNUBackgammonSingleMove() }
}

private fun SingleBackgammonMove.convertToGNUBackgammonSingleMove(): String =
        convertToGNUBackgammonCheckerIndex(oldCheckerIndex) + "/" + convertToGNUBackgammonCheckerIndex(newCheckerIndex)


private fun convertToGNUBackgammonCheckerIndex(checkerIndex: BackgammonBoardIndex): String = when {
    checkerIndex.isBearOff() -> "off"
    checkerIndex.isBar() -> "bar"
    else -> checkerIndex.toInt().toString()
}