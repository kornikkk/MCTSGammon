package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.NO_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.BackgammonDices

/**
 * Created by kkarolcz on 19.11.2017.
 */
abstract class AbstractFullMovesSearch(board: BackgammonBoard, currentPlayer: BackgammonPlayer, protected val dices: BackgammonDices) {

    protected val playerCheckers = board.getPlayerCheckers(currentPlayer)
    protected val opponentCheckers = board.getPlayerCheckers(currentPlayer.opponent())
    protected val fullMoves = mutableListOf<BackgammonMovesSequence>()

    fun findAll(): List<BackgammonMovesSequence> {
        findAllNormalMoves()
        return fullMoves
    }

    abstract fun findAllNormalMoves()

    protected fun findSingleMoveFromBar(diceValue: Byte): BackgammonMove? {
        val newIndex = BackgammonBoardIndex.shift(BAR_INDEX, diceValue) // No need to check NO_INDEX

        if (opponentCheckers.isNotOccupiedOrCanBeHit(BackgammonBoardIndex.toOpponentsIndex(newIndex)))
            return BackgammonMove(BAR_INDEX, newIndex)
        return null
    }

    //TODO Do something with that shit
//    /** BEAR OFF MOVES */
//    if (continueSearchForBearOff) {
//        val bearOffNewIndex = BackgammonBoardIndex.shiftForBearOff(tower.index, dice)
//        if (bearOffNewIndex != BackgammonBoardIndex.NO_INDEX) {
//            moves.add(BackgammonMove(tower.index, bearOffNewIndex))
//            continueSearchForBearOff = false
//        }
//    }


    //TODO BEAR OFF MOVES
    protected fun findPartialMove(currentIndex: Byte, diceValue: Byte): Byte {
        val newIndex = BackgammonBoardIndex.shift(currentIndex, diceValue)
        if (opponentCheckers.isNotOccupiedOrCanBeHit(BackgammonBoardIndex.toOpponentsIndex(newIndex))) {
            return newIndex
        }
        return NO_INDEX
    }

}