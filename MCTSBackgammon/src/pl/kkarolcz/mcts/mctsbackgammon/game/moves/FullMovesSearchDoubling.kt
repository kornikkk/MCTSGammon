package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.settings.Statistics
import java.util.*

/**
 * Created by kkarolcz on 27.12.2017.
 */
class FullMovesSearchDoubling(board: Board, currentPlayer: Player, dice: Dice)
    : AbstractFullMovesSearch(board, currentPlayer, dice) {

    private val die = dice.first

    private var barMove: SingleMove? = null
    private var barSequentialMoves = SequencesForPartialMoves()

    private val partialMoves = mutableListOf<SingleMove>()
    private val standardSequentialMoves = SequencesForPartialMoves()

    private var diceLeft = 4

    private val initialFullMoveBuilder = FullMovesBuilder()

    private var longestFullMove = 0


    override fun findAllImpl() {
        Statistics.currentGame.currentRound.incDoublingSearches()

        if (playerCheckers.barCheckers > 0) {
            barMove = findPartialBarMove(die)
            if (barMove == null) {
                return // No moves possible
            }
            diceLeft -= playerCheckers.barCheckers

            for (i in 1..minOf(playerCheckers.barCheckers, 4)) {
                initialFullMoveBuilder.append(barMove!!)
            }

            if (diceLeft == 0) {
                appendAndAddFullMove(initialFullMoveBuilder)
                return // No need to do anything when there's >= 4 checkers on the bar
            }

            findBarSequentialMoves()
        }

        findStandardPartialMoves()
        findStandardSequentialMoves()

        findFullMoves()
    }

    private fun findStandardPartialMoves() {
        for (tower in playerCheckers.towerIterator()) {
            val move = findStandardPartialMoveForTower(tower.index, die)
            if (move != null) {
                for (i in 1..minOf(tower.checkers.toInt(), diceLeft)) {
                    partialMoves.add(move.clone())
                }
            }
        }
    }

    private fun findBarSequentialMoves() {
        // No need to decrement dice like in findStandardSequentialMoves(). It was already done because bar moves exist
        findSequentialMoves(Collections.singleton(barMove!!), barSequentialMoves, diceLeft)
    }

    private fun findStandardSequentialMoves() {
        // We assume that one of the partial moves is used so dice are decremented
        findSequentialMoves(partialMoves, standardSequentialMoves, diceLeft - 1)
    }

    private fun findSequentialMoves(partialMoves: Iterable<SingleMove>, sequences: SequencesForPartialMoves, initialDiceLeft: Int) {
        if (initialDiceLeft == 0)
            return

        for (partialMove in partialMoves) {
            var diceLeft = initialDiceLeft
            var oldIndex: Byte
            var newIndex = partialMove.newIndex
            val sequenceForPartialMove = sequences.getOrCreateSequence(partialMove)

            while (diceLeft > 0) {
                oldIndex = newIndex
                newIndex = findMove(oldIndex, die)
                diceLeft -= 1

                if (newIndex == BoardIndex.NO_INDEX) {
                    break
                }

                sequenceForPartialMove.add(SingleMove(oldIndex, newIndex))
            }
        }
    }

    private fun findFullMoves() {
        if (barMove != null) {
            findSequencesWithBarPartialMovesOnly()
        }

        if (partialMoves.isEmpty() && fullMoves.isEmpty()) {
            appendAndAddFullMove(getBuilder())
            return
        }

        if (partialMoves.isNotEmpty()) {
            findFullMovesIteration(partialMoves.toMutableList(), PartialMovesIteration())
        }
    }

    private fun findFullMovesIteration(partialMoves: MutableList<SingleMove>, previousPartialMoves: PartialMovesIteration) {

        val iterator = partialMoves.listIterator()
        while (iterator.hasNext()) {
            val currentPartialMoves = previousPartialMoves.newIteration(iterator.next())
            iterator.remove()

            appendAndAddFullMove(getBuilder())

            when (currentPartialMoves.count) {
                1 -> {
                    appendAndAddFullMove(getBuilder(), currentPartialMoves[0])
                    findSequencesWith1PartialMove(currentPartialMoves[0])
                }
                2 -> {
                    appendAndAddFullMove(getBuilder(), currentPartialMoves[0], currentPartialMoves[1])
                    findSequencesWith2PartialMoves(currentPartialMoves[0], currentPartialMoves[1])
                }
                3 -> {
                    appendAndAddFullMove(getBuilder(), currentPartialMoves[0], currentPartialMoves[1], currentPartialMoves[2])
                    findSequencesWith3PartialMoves(currentPartialMoves[0], currentPartialMoves[1], currentPartialMoves[2])
                }
                4 -> {
                    appendAndAddFullMove(getBuilder(), currentPartialMoves[0], currentPartialMoves[1], currentPartialMoves[2], currentPartialMoves[3])
                }
            }

            if (diceLeft - currentPartialMoves.count > 0) {
                findFullMovesIteration(partialMoves.toMutableList(), currentPartialMoves)
            }
        }

    }

    private fun findSequencesWithBarPartialMovesOnly() {
        val builder = getBuilder()
        val barSequence = barMove?.let { barSequentialMoves.getSequence(it) }
        when (diceLeft) {
            3 -> { // 1 partial move from the bar
                val barSequence1 = barSequence?.get(0)
                val barSequence2 = barSequence?.get(1)
                val barSequence3 = barSequence?.get(2)

                if (barSequence1 != null && barSequence2 != null && barSequence3 != null) {
                    appendAndAddFullMove(builder, barSequence1, barSequence2, barSequence3)
                }

                if (barSequence1 != null && barSequence2 != null) {
                    appendAndAddFullMove(builder, barSequence1, barSequence2)
                }

                if (barSequence1 != null) {
                    appendAndAddFullMove(builder, barSequence1)
                }
            }
            2 -> { // 2 partial moves from the bar
                val barSequence1 = barSequence?.get(0)
                val barSequence2 = barSequence?.get(1)

                if (barSequence1 != null && barSequence2 != null)
                    appendAndAddFullMove(builder, barSequence1, barSequence2)

                if (barSequence1 != null)
                    appendAndAddFullMove(builder, barSequence1, barSequence1)
            }
            1 -> { // 3 partial moves from the bar
                val barSequence1 = barSequence?.get(0)
                if (barSequence1 != null)
                    appendAndAddFullMove(builder, barSequence1)
            }
        }
    }

    private fun findSequencesWith1PartialMove(partialMove: SingleMove) {
        val builder = getBuilder()
        builder.append(partialMove)

        val barSequence = barMove?.let { barSequentialMoves.getSequence(it) }
        val barSequence1 = barSequence?.get(0)
        val barSequence2 = barSequence?.get(1)

        val standardSequence = standardSequentialMoves.getSequence(partialMove)
        val sequence1 = standardSequence?.get(0)
        val sequence2 = standardSequence?.get(1)
        val sequence3 = standardSequence?.get(2)

        when (diceLeft) {
            4 -> { // Bar is empty
                // Full moves (4)
                if (sequence1 != null && sequence2 != null && sequence3 != null)
                    appendAndAddFullMove(builder, sequence1, sequence2, sequence3)

                // Non full moves (3)
                if (sequence1 != null && sequence2 != null)
                    appendAndAddFullMove(builder, sequence1, sequence2)

                // Non full moves (2)
                if (sequence1 != null)
                    appendAndAddFullMove(builder, sequence1)
            }
            3 -> { // 1 partialMove from the bar
                // Full moves (4)
                if (barSequence1 != null && barSequence2 != null)
                    appendAndAddFullMove(builder, barSequence1, barSequence2)
                if (barSequence1 != null && sequence1 != null)
                    appendAndAddFullMove(builder, barSequence1, sequence1)
                if (sequence1 != null && sequence2 != null)
                    appendAndAddFullMove(builder, sequence1, sequence2)

                // Non full moves (3)
                if (barSequence1 != null && barSequence2 == null && sequence1 == null) {
                    appendAndAddFullMove(builder, barSequence1)
                }
            }
            2 -> { // 2 moves from the bar
                // Full moves (4)
                if (barSequence1 != null)
                    appendAndAddFullMove(builder, barSequence1)
                if (sequence1 != null)
                    appendAndAddFullMove(builder, sequence1)
            }
        }
    }

    private fun findSequencesWith2PartialMoves(partialMove1: SingleMove, partialMove2: SingleMove) {
        val builder = getBuilder()
        builder.append(partialMove1, partialMove2)

        val standardSequence1 = standardSequentialMoves.getSequence(partialMove1)
        val sequence1x1 = standardSequence1?.get(0)
        val sequence1x2 = standardSequence1?.get(1)

        val standardSequence2 = standardSequentialMoves.getSequence(partialMove2)
        val sequence2x1 = standardSequence2?.get(0)
        val sequence2x2 = standardSequence2?.get(1)

        when (diceLeft) {
            4 -> { // Bar is empty
                // Full moves (4)
                if (sequence1x1 != null && sequence2x1 != null)
                    appendAndAddFullMove(builder, sequence1x1, sequence2x1)
                if (sequence1x1 != null && sequence1x2 != null)
                    appendAndAddFullMove(builder, sequence1x1, sequence1x2)
                if (sequence2x1 != null && sequence2x2 != null)
                    appendAndAddFullMove(builder, sequence2x1, sequence2x2)

                // Non full moves (3)
                if (sequence1x1 != null)
                    appendAndAddFullMove(builder, sequence1x1)
                if (sequence2x1 != null)
                    appendAndAddFullMove(builder, sequence2x1)
            }
        }
    }


    private fun findSequencesWith3PartialMoves(partialMove1: SingleMove, partialMove2: SingleMove, partialMove3: SingleMove) {
        val builder = getBuilder()
        builder.append(partialMove1, partialMove2, partialMove3)

        val sequence1x1 = standardSequentialMoves.getSequence(partialMove1)?.get(0)
        val sequence2x1 = standardSequentialMoves.getSequence(partialMove2)?.get(0)
        val sequence3x1 = standardSequentialMoves.getSequence(partialMove3)?.get(0)

        when (diceLeft) {
            4 -> { // Bar is empty
                // Full moves (4)
                if (sequence1x1 != null)
                    appendAndAddFullMove(builder, sequence1x1)
                if (sequence2x1 != null)
                    appendAndAddFullMove(builder, sequence2x1)
                if (sequence3x1 != null)
                    appendAndAddFullMove(builder, sequence3x1)
            }
        }
    }

    private fun getBuilder() = initialFullMoveBuilder.clone()

    private fun appendAndAddFullMove(initialBuilder: FullMovesBuilder) {
        if (ensureLongestMove(initialBuilder, 0)) {
            fullMoves.add(initialBuilder.clone().build())
        }
    }

    private fun appendAndAddFullMove(initialBuilder: FullMovesBuilder, move: SingleMove) {
        if (ensureLongestMove(initialBuilder, 1)) {
            fullMoves.add(initialBuilder.clone().append(move).build())
        }
    }

    private fun appendAndAddFullMove(initialBuilder: FullMovesBuilder, move1: SingleMove, move2: SingleMove) {
        if (ensureLongestMove(initialBuilder, 2)) {
            fullMoves.add(initialBuilder.clone().append(move1, move2).build())
        }
    }

    private fun appendAndAddFullMove(initialBuilder: FullMovesBuilder, move1: SingleMove, move2: SingleMove, move3: SingleMove) {
        if (ensureLongestMove(initialBuilder, 3)) {
            fullMoves.add(initialBuilder.clone().append(move1, move2, move3).build())
        }
    }

    private fun appendAndAddFullMove(initialBuilder: FullMovesBuilder, move1: SingleMove, move2: SingleMove, move3: SingleMove, move4: SingleMove) {
        if (ensureLongestMove(initialBuilder, 4)) {
            fullMoves.add(initialBuilder.clone().append(move1, move2, move3, move4).build())
        }
    }

    /**
     * @return true if the new move will be the longest or equally long move compared to the current state
     */
    private fun ensureLongestMove(initialBuilder: FullMovesBuilder, newMovesCount: Int): Boolean {
        val length = initialBuilder.length + newMovesCount
        if (length > longestFullMove) {
            fullMoves.clear()
            longestFullMove = length
        }

        return length == longestFullMove
    }

    private class SequencesForPartialMoves {
        private val map = IdentityHashMap<SingleMove, SequenceForPartialMove>(15)

        fun getOrCreateSequence(partialMove: SingleMove): SequenceForPartialMove =
                map.computeIfAbsent(partialMove) { SequenceForPartialMove() }

        fun getSequence(partialMove: SingleMove): SequenceForPartialMove? = map.getOrDefault(partialMove, null)

    }

    private class SequenceForPartialMove : Iterable<SingleMove> {

        private val sequence = ArrayList<SingleMove>(3)

        override fun iterator(): Iterator<SingleMove> = sequence.iterator()

        operator fun get(index: Int): SingleMove? = sequence.getOrNull(index)

        fun add(move: SingleMove) {
            sequence.add(move)
        }

    }

    private inner class PartialMovesIteration {
        private val moves: Array<SingleMove?> = arrayOfNulls(diceLeft)

        private var realSize = 0
        val count: Int get() = realSize

        constructor()

        private constructor(previous: PartialMovesIteration, newMove: SingleMove) {
            System.arraycopy(previous.moves, 0, this.moves, 0, previous.realSize)

            this.realSize = previous.realSize
            this.moves[realSize] = newMove
            this.realSize += 1
        }

        fun newIteration(newMove: SingleMove) = PartialMovesIteration(this, newMove)

        operator fun get(iteration: Int): SingleMove = moves[iteration] as SingleMove

    }

}