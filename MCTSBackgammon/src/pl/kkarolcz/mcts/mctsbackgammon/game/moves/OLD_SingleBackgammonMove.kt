package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.OLD_BackgammonBoardIndex

/**
 * Created by kkarolcz on 24.08.2017.
 */
@Deprecated("REMOVE")
data class OLD_SingleBackgammonMove internal constructor(val oldCheckerIndex: OLD_BackgammonBoardIndex, val newCheckerIndex: OLD_BackgammonBoardIndex)