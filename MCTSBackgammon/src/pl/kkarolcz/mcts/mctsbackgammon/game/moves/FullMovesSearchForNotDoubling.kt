package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoard
import pl.kkarolcz.mcts.mctsbackgammon.board.BackgammonBoardIndex.Companion.NO_INDEX
import pl.kkarolcz.mcts.mctsbackgammon.game.BackgammonPlayer
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.BackgammonDices

/**
 * Created by kkarolcz on 21.11.2017.
 */
class FullMovesSearchForNotDoubling(board: BackgammonBoard, currentPlayer: BackgammonPlayer, dices: BackgammonDices)
    : AbstractFullMovesSearch(board, currentPlayer, dices) {

    private var moveFromBarFirstDice: BackgammonMove? = null
    private var moveFromBarSecondDice: BackgammonMove? = null
    private val partialMovesFirstDice = mutableListOf<BackgammonMove>()
    private val partialMovesSecondDice = mutableListOf<BackgammonMove>()
    private var anyFullSequenceFound = false

    override fun findAllNormalMoves() {
        // Find bar moves only when there are any checkers which must be moved
        if (playerCheckers.barCheckers > 0) {
            findMovesFromBar()

            // Skip finding normal moves when number of bar checkers is greater or equal to 2
            if (playerCheckers.barCheckers >= 2) {
                if (moveFromBarFirstDice != null || moveFromBarSecondDice != null)
                    fullMoves.add(BackgammonMovesSequence(moveFromBarFirstDice, moveFromBarSecondDice))
                return
            }
        }

        findPartialMovesForTowers()

        // Find only sequence of bar checker and then normal move
        if (playerCheckers.barCheckers == 1.toByte()) {
            val moveFromBarFirstDiceList = barMoveToList(moveFromBarFirstDice)
            val moveFromBarSecondDiceList = barMoveToList(moveFromBarFirstDice)

            findFullMoves(moveFromBarFirstDiceList, dices.first, partialMovesSecondDice, dices.second)
            findFullMoves(moveFromBarSecondDiceList, dices.first, partialMovesFirstDice, dices.second)

            if (!anyFullSequenceFound) {
                fullMoves.addAll(moveFromBarFirstDiceList.map { move -> BackgammonMovesSequence(move) })
                fullMoves.addAll(moveFromBarSecondDiceList.map { move -> BackgammonMovesSequence(move) })
            }
        }
        // Find normal moves
        else {
            findFullMoves(partialMovesFirstDice, dices.first, partialMovesSecondDice, dices.second)
            findFullMoves(partialMovesFirstDice, dices.first, partialMovesSecondDice, dices.second)

            if (!anyFullSequenceFound) {
                fullMoves.addAll(partialMovesFirstDice.map { move -> BackgammonMovesSequence(move) })
                fullMoves.addAll(partialMovesSecondDice.map { move -> BackgammonMovesSequence(move) })
            }
        }
    }

    private fun findMovesFromBar() {
        moveFromBarFirstDice = findSingleMoveFromBar(dices.first)
        moveFromBarSecondDice = findSingleMoveFromBar(dices.second)
    }

    private fun findPartialMovesForTowers() {
        for (tower in playerCheckers.towerIterator()) {
            findPartialMovesForTower(partialMovesFirstDice, tower.index, dices.first)
            findPartialMovesForTower(partialMovesSecondDice, tower.index, dices.second)
        }
    }

    private fun findPartialMovesForTower(partialMoves: MutableList<BackgammonMove>, index: Byte, diceValue: Byte) {
        val newIndex = findPartialMove(index, diceValue)
        if (newIndex != NO_INDEX)
            partialMoves.add(BackgammonMove(index, newIndex))
    }

    private fun findFullMoves(firstPartialMoves: Collection<BackgammonMove>, firstDiceValue: Byte,
                              secondPartialMoves: Collection<BackgammonMove>, secondDiceValue: Byte) {

        for (move1 in firstPartialMoves) {
            findSequenceAfterMovingChecker(move1, secondDiceValue)
            for (move2 in secondPartialMoves) {
                findSequenceAfterMovingChecker(move2, firstDiceValue)

                //TODO Check if that's really necessary
                if (isMovePossibleAfterPreviousMove(move1, move2)) {
                    fullMoves.add(BackgammonMovesSequence(move1, move2))
                    anyFullSequenceFound = true
                }
            }
        }
    }

    private fun isMovePossibleAfterPreviousMove(firstMove: BackgammonMove, secondMove: BackgammonMove): Boolean {
        if (firstMove.oldIndex == secondMove.oldIndex) {
            return playerCheckers.get(firstMove.oldIndex) - 1 > 0
        }
        return true
    }

    private fun findSequenceAfterMovingChecker(partialMove: BackgammonMove, otherDiceValue: Byte): Boolean {
        val nextIndex = findPartialMove(partialMove.newIndex, otherDiceValue)
        if (nextIndex != NO_INDEX) {
            fullMoves.add(BackgammonMovesSequence(partialMove, BackgammonMove(partialMove.newIndex, nextIndex)))
            anyFullSequenceFound = true
            return true
        }
        return false
    }

    private fun barMoveToList(barMove: BackgammonMove?): Collection<BackgammonMove> {
        if (barMove == null)
            return emptyList()
        return listOf(barMove)
    }

    private fun wouldBearOffAfterMove(move: BackgammonMove): Boolean {
        /**
         * Normal bear off moves are pretty easy. When everything is in the home board then we just look for them.
         * We have to find the first bearing move for each dice.
         * We have to make sure we check both dices because we cannot smaller index than dice when there is any other bear off available.
         * That should probably be checked as a sequence of moves.
         * Bear off moves can be also combined with normal partial moves so probably they should be combined here.
         *
         * The problem is when there is a tower not in the home board...
         * Bear off sequences can be checked by somehow checking indices of towers which are non home board and comparing them to dices.
         * Bear off will be possible ONLY IF THERE IS ONE TOWER outside the home board.
         * If there are 2 or more tower we won't be able to make it anyway.
         * If there is only one tower and by using any dice it can be moved to the home board then we may check the sequence.
         *
         * That should cover all the cases...
         * Probably...
         */
//        if (playerCheckers.)
//            return true
//        if (_barCheckers == ByteMath.ZERO_BYTE && nonHomeTowers.size() == 1 && nonHomeTowers.contains(move.oldIndex)) {
//            return towers[move.oldIndex] == 1.toByte()
//        }
        return false
    }
}