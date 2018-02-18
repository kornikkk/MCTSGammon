package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.NO_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.toOpponentsIndex
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.BackgammonDice

/**
 * Created by kkarolcz on 19.11.2017.
 */
abstract class AbstractFullMovesSearch(board: BackgammonBoard, currentPlayer: BackgammonPlayer, protected val dice: BackgammonDice) {

    protected val playerCheckers = board.getPlayerCheckers(currentPlayer)
    protected val opponentCheckers = board.getPlayerCheckers(currentPlayer.opponent())
    protected val fullMoves: MutableCollection<BackgammonMovesSequence> = mutableSetOf()

    fun findAll(): Collection<BackgammonMovesSequence> {
        findAllImpl()
        return fullMoves
    }

    protected abstract fun findAllImpl()

    protected fun findPartialBarMove(diceValue: Byte): BackgammonMove? {
        val newIndex = BackgammonBoardIndex.shift(BAR_INDEX, diceValue) // No need to check NO_INDEX

        if (opponentCheckers.isNotOccupiedOrCanBeHit(toOpponentsIndex(newIndex)))
            return BackgammonMove.create(BAR_INDEX, newIndex)
        return null
    }

    protected fun findStandardPartialMoveForTower(index: Byte, diceValue: Byte): BackgammonMove? {
        val newIndex = findMove(index, diceValue)
        if (newIndex != NO_INDEX)
            return BackgammonMove.create(index, newIndex)
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
    protected fun findMove(index: Byte, dice: Byte): Byte {
        val newIndex = BackgammonBoardIndex.shift(index, dice)
        if (newIndex != NO_INDEX && opponentCheckers.isNotOccupiedOrCanBeHit(toOpponentsIndex(newIndex))) {
            return newIndex
        }
        return NO_INDEX
    }


    protected fun firstForBearingOff(homeTowersIndices: Collection<Byte>, dice: Byte): BackgammonMove? {
        var greaterThanDiceFound = false
        for (index in homeTowersIndices) {
            if (index == dice)
                return BackgammonMove.create(index, BackgammonBoardIndex.BEAR_OFF_INDEX)

            if (index > dice) {
                greaterThanDiceFound = true
                continue
            }
            if (!greaterThanDiceFound && index < dice)
                return BackgammonMove.create(index, BackgammonBoardIndex.BEAR_OFF_INDEX)

            break
        }
        return null
    }
}