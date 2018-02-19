package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.BAR_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.NO_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.toOpponentsIndex
import pl.kkarolcz.mcts.mctsbackgammon.game.Player
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice

/**
 * Created by kkarolcz on 19.11.2017.
 */
abstract class AbstractFullMovesSearch(board: Board, currentPlayer: Player, protected val dice: Dice) {

    protected val playerCheckers = board.getPlayerCheckers(currentPlayer)
    protected val opponentCheckers = board.getPlayerCheckers(currentPlayer.opponent())
    protected val fullMoves: MutableCollection<FullMove> = mutableSetOf()

    fun findAll(): Collection<FullMove> {
        findAllImpl()
        return fullMoves
    }

    protected abstract fun findAllImpl()

    protected fun findPartialBarMove(diceValue: Byte): SingleMove? {
        val newIndex = BoardIndex.shift(BAR_INDEX, diceValue) // No need to check NO_INDEX

        if (opponentCheckers.isNotOccupiedOrCanBeHit(toOpponentsIndex(newIndex)))
            return SingleMove.create(BAR_INDEX, newIndex)
        return null
    }

    protected fun findStandardPartialMoveForTower(index: Byte, diceValue: Byte): SingleMove? {
        val newIndex = findMove(index, diceValue)
        if (newIndex != NO_INDEX)
            return SingleMove.create(index, newIndex)
        return null
    }

    //TODO Do something with that shit
//    /** BEAR OFF MOVES */
//    if (continueSearchForBearOff) {
//        val bearOffNewIndex = BoardIndex.shiftForBearOff(tower.index, dice)
//        if (bearOffNewIndex != BoardIndex.NO_INDEX) {
//            moves.add(SingleMove(tower.index, bearOffNewIndex))
//            continueSearchForBearOff = false
//        }
//    }


    //TODO BEAR OFF MOVES
    protected fun findMove(index: Byte, dice: Byte): Byte {
        val newIndex = BoardIndex.shift(index, dice)
        if (newIndex != NO_INDEX && opponentCheckers.isNotOccupiedOrCanBeHit(toOpponentsIndex(newIndex))) {
            return newIndex
        }
        return NO_INDEX
    }


    protected fun firstForBearingOff(homeTowersIndices: Collection<Byte>, dice: Byte): SingleMove? {
        var greaterThanDiceFound = false
        for (index in homeTowersIndices) {
            if (index == dice)
                return SingleMove.create(index, BoardIndex.BEAR_OFF_INDEX)

            if (index > dice) {
                greaterThanDiceFound = true
                continue
            }
            if (!greaterThanDiceFound && index < dice)
                return SingleMove.create(index, BoardIndex.BEAR_OFF_INDEX)

            break
        }
        return null
    }
}