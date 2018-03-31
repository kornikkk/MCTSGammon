package pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.allmoves

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMovesBuilder
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.SingleMove
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.search.doubling.AbstractMovesSearchDoubling

/**
 * Created by kkarolcz on 27.12.2017.
 */
class FullMovesSearchDoubling(board: Board, currentPlayer: Player, dice: Dice) : AbstractMovesSearchDoubling(board, currentPlayer, dice) {

    private var longestFullMove: Int = 0

    override fun findAllImpl() {
        initialize()
        when {
            diceLeft > 0 -> findFullMoves()
            possibleMoves.barMove != null -> addFullMoveIfValid(initialFullMoveBuilder)
        }
    }

    private fun findFullMoves() {
        if (possibleMoves.barMove != null) {
            findSequencesWithBarPartialMovesOnly()
        }

        findBearOffMovesOnly()

        if (possibleMoves.partialMoves.isEmpty() && fullMoves.isEmpty()) {
            addFullMoveIfValid(getBuilder())
            return
        }

        if (possibleMoves.partialMoves.isNotEmpty()) {
            findFullMovesIteration(possibleMoves.partialMoves.toMutableList(), PartialMovesIteration())
        }
    }

    private fun findFullMovesIteration(partialMoves: MutableList<SingleMove>, previousPartialMoves: PartialMovesIteration) {
        //TODO some more research if order matters in any case. Maybe even more moves could be skipped
        val iterator = partialMoves.listIterator()
        while (iterator.hasNext()) {
            val currentPartialMoves = previousPartialMoves.newIteration(iterator.next())
            iterator.remove()

            addFullMoveIfValid(getBuilder())

            when (currentPartialMoves.count) {
                1 -> {
                    addFullMoveIfValid(getBuilder(), currentPartialMoves[0])
                    findSequencesWith1PartialMove(currentPartialMoves[0])

                    findBearOffFullMoveWith1PartialMove(currentPartialMoves[0])
                }
                2 -> {
                    addFullMoveIfValid(getBuilder(), currentPartialMoves[0], currentPartialMoves[1])
                    findSequencesWith2PartialMoves(currentPartialMoves[0], currentPartialMoves[1])

                    findBearOffFullMoveWith2PartialMoves(currentPartialMoves[0], currentPartialMoves[1])
                }
                3 -> {
                    addFullMoveIfValid(getBuilder(), currentPartialMoves[0], currentPartialMoves[1], currentPartialMoves[2])
                    findSequencesWith3PartialMoves(currentPartialMoves[0], currentPartialMoves[1], currentPartialMoves[2])

                    findBearOffFullMoveWith3PartialMoves(currentPartialMoves[0], currentPartialMoves[1], currentPartialMoves[2])
                }
                4 -> {
                    addFullMoveIfValid(getBuilder(), currentPartialMoves[0], currentPartialMoves[1], currentPartialMoves[2], currentPartialMoves[3])
                }
            }

            if (diceLeft - currentPartialMoves.count > 0) {
                findFullMovesIteration(partialMoves.toMutableList(), currentPartialMoves)
            }
        }

    }

    private fun findSequencesWithBarPartialMovesOnly() {
        val builder = getBuilder()
        val barSequence = possibleMoves.barMove?.let { possibleMoves.barSequentialMoves.getSequence(it) }
        when (diceLeft) {
            3 -> { // 1 partial move from the bar
                val barSequence0x1 = barSequence?.get(0)
                val barSequence0x2 = barSequence?.get(1)
                val barSequence0x3 = barSequence?.get(2)

                if (barSequence0x1 != null && barSequence0x2 != null && barSequence0x3 != null) {
                    addFullMoveIfValid(builder, barSequence0x1, barSequence0x2, barSequence0x3)
                }

                if (barSequence0x1 != null && barSequence0x2 != null) {
                    addFullMoveIfValid(builder, barSequence0x1, barSequence0x2)
                }

                if (barSequence0x1 != null) {
                    addFullMoveIfValid(builder, barSequence0x1)
                }
            }
            2 -> { // 2 partial moves from the bar
                val barSequence0x1 = barSequence?.get(0)
                val barSequence0x2 = barSequence?.get(1)

                if (barSequence0x1 != null && barSequence0x2 != null)
                    addFullMoveIfValid(builder, barSequence0x1, barSequence0x2)

                if (barSequence0x1 != null)
                    addFullMoveIfValid(builder, barSequence0x1, barSequence0x1)
            }
            1 -> { // 3 partial moves from the bar
                val barSequence0x1 = barSequence?.get(0)
                if (barSequence0x1 != null)
                    addFullMoveIfValid(builder, barSequence0x1)
            }
        }
    }

    private fun findSequencesWith1PartialMove(partialMove: SingleMove) {
        val builder = getBuilder()
        builder.append(partialMove)

        val barSequence = possibleMoves.barMove?.let { possibleMoves.barSequentialMoves.getSequence(it) }
        val barSequence0x1 = barSequence?.get(0)
        val barSequence0x2 = barSequence?.get(1)

        val standardSequence = possibleMoves.standardSequentialMoves.getSequence(partialMove)
        val sequence0x1 = standardSequence?.get(0)
        val sequence0x2 = standardSequence?.get(1)
        val sequence0x3 = standardSequence?.get(2)

        when (diceLeft) {
            4 -> { // Bar is empty
                // Full moves (4)
                if (sequence0x1 != null && sequence0x2 != null && sequence0x3 != null)
                    addFullMoveIfValid(builder, sequence0x1, sequence0x2, sequence0x3)

                // Non full moves (3)
                if (sequence0x1 != null && sequence0x2 != null)
                    addFullMoveIfValid(builder, sequence0x1, sequence0x2)

                // Non full moves (2)
                if (sequence0x1 != null)
                    addFullMoveIfValid(builder, sequence0x1)
            }
            3 -> { // 1 partialMove from the bar
                // Full moves (4)
                if (barSequence0x1 != null && barSequence0x2 != null)
                    addFullMoveIfValid(builder, barSequence0x1, barSequence0x2)
                if (barSequence0x1 != null && sequence0x1 != null)
                    addFullMoveIfValid(builder, barSequence0x1, sequence0x1)
                if (sequence0x1 != null && sequence0x2 != null)
                    addFullMoveIfValid(builder, sequence0x1, sequence0x2)

                // Non full moves (3)
                if (barSequence0x1 != null && barSequence0x2 == null && sequence0x1 == null) {
                    addFullMoveIfValid(builder, barSequence0x1)
                }
            }
            2 -> { // 2 moves from the bar
                // Full moves (4)
                if (barSequence0x1 != null)
                    addFullMoveIfValid(builder, barSequence0x1)
                if (sequence0x1 != null)
                    addFullMoveIfValid(builder, sequence0x1)
            }
        }
    }

    private fun findSequencesWith2PartialMoves(partialMove1: SingleMove, partialMove2: SingleMove) {
        val builder = getBuilder()
        builder.append(partialMove1, partialMove2)

        val standardSequence1 = possibleMoves.standardSequentialMoves.getSequence(partialMove1)
        val sequence1x1 = standardSequence1?.get(0)
        val sequence1x2 = standardSequence1?.get(1)

        val standardSequence2 = possibleMoves.standardSequentialMoves.getSequence(partialMove2)
        val sequence2x1 = standardSequence2?.get(0)
        val sequence2x2 = standardSequence2?.get(1)

        when (diceLeft) {
            4 -> { // Bar is empty
                // Full moves (4)
                if (sequence1x1 != null && sequence2x1 != null)
                    addFullMoveIfValid(builder, sequence1x1, sequence2x1)
                if (sequence1x1 != null && sequence1x2 != null)
                    addFullMoveIfValid(builder, sequence1x1, sequence1x2)
                if (sequence2x1 != null && sequence2x2 != null)
                    addFullMoveIfValid(builder, sequence2x1, sequence2x2)

                // Non full moves (3)
                if (sequence1x1 != null)
                    addFullMoveIfValid(builder, sequence1x1)
                if (sequence2x1 != null)
                    addFullMoveIfValid(builder, sequence2x1)
            }
        }
    }


    private fun findSequencesWith3PartialMoves(partialMove1: SingleMove, partialMove2: SingleMove, partialMove3: SingleMove) {
        val builder = getBuilder()
        builder.append(partialMove1, partialMove2, partialMove3)

        val sequence1x1 = possibleMoves.standardSequentialMoves.getSequence(partialMove1)?.get(0)
        val sequence2x1 = possibleMoves.standardSequentialMoves.getSequence(partialMove2)?.get(0)
        val sequence3x1 = possibleMoves.standardSequentialMoves.getSequence(partialMove3)?.get(0)

        when (diceLeft) {
            4 -> { // Bar is empty
                // Full moves (4)
                if (sequence1x1 != null)
                    addFullMoveIfValid(builder, sequence1x1)
                if (sequence2x1 != null)
                    addFullMoveIfValid(builder, sequence2x1)
                if (sequence3x1 != null)
                    addFullMoveIfValid(builder, sequence3x1)
            }
        }
    }


    private fun findBearOffMovesOnly() {
        if (!possibleMoves.bearOffPossibleInitially) {
            return
        }

        val bearOffMove1 = firstForBearingOffAfterMoves()
        if (bearOffMove1 != null) {
            addFullMoveIfValid(getBuilder(), bearOffMove1)

            val bearOffMove2 = firstForBearingOffAfterMoves(bearOffMove1)
            if (bearOffMove2 != null) {
                addFullMoveIfValid(getBuilder(), bearOffMove1, bearOffMove2)

                val bearOffMove3 = firstForBearingOffAfterMoves(bearOffMove1, bearOffMove2)
                if (bearOffMove3 != null) {
                    addFullMoveIfValid(getBuilder(), bearOffMove1, bearOffMove2, bearOffMove3)

                    val bearOffMove4 = firstForBearingOffAfterMoves(bearOffMove1, bearOffMove2, bearOffMove3)
                    if (bearOffMove4 != null) {
                        addFullMoveIfValid(getBuilder(), bearOffMove1, bearOffMove2, bearOffMove3, bearOffMove4)
                    }
                }

            }
        }
    }

    private fun findBearOffFullMoveWith1PartialMove(partialMove: SingleMove) {
        if (!(possibleMoves.bearOffPossibleInitially || possibleMoves.bearOffPossibleConditionally)) {
            return
        }

        val standardSequence = possibleMoves.standardSequentialMoves.getSequence(partialMove)
        val sequence0x1 = standardSequence?.get(0)
        val sequence0x2 = standardSequence?.get(1)

        if (sequence0x1 != null && sequence0x2 != null) {
            val bearOffMove = firstForBearingOffAfterMoves(partialMove, sequence0x1, sequence0x2)
            if (bearOffMove != null) {
                addFullMoveIfValid(getBuilder(), partialMove, sequence0x1, sequence0x2, bearOffMove)
            }
        }

        if (sequence0x1 != null) {
            val bearOffMove1 = firstForBearingOffAfterMoves(partialMove, sequence0x1)
            if (bearOffMove1 != null) {
                addFullMoveIfValid(getBuilder(), partialMove, sequence0x1, bearOffMove1)

                val bearOffMove2 = firstForBearingOffAfterMoves(partialMove, sequence0x1, bearOffMove1)
                if (bearOffMove2 != null) {
                    addFullMoveIfValid(getBuilder(), partialMove, sequence0x1, bearOffMove1, bearOffMove2)
                }
            }
        }

        val bearOffMove1 = firstForBearingOffAfterMoves(partialMove)
        if (bearOffMove1 != null) {
            addFullMoveIfValid(getBuilder(), partialMove, bearOffMove1)

            val bearOffMove2 = firstForBearingOffAfterMoves(partialMove, bearOffMove1)
            if (bearOffMove2 != null) {
                addFullMoveIfValid(getBuilder(), partialMove, bearOffMove1, bearOffMove2)

                val bearOffMove3 = firstForBearingOffAfterMoves(partialMove, bearOffMove1, bearOffMove2)
                if (bearOffMove3 != null) {
                    addFullMoveIfValid(getBuilder(), partialMove, bearOffMove1, bearOffMove2, bearOffMove3)
                }
            }
        }
    }


    private fun findBearOffFullMoveWith2PartialMoves(partialMove1: SingleMove, partialMove2: SingleMove) {
        if (!(possibleMoves.bearOffPossibleInitially || possibleMoves.bearOffPossibleConditionally)) {
            return
        }

        val sequence1x1 = possibleMoves.standardSequentialMoves.getSequence(partialMove1)?.get(0)
        val sequence2x1 = possibleMoves.standardSequentialMoves.getSequence(partialMove2)?.get(0)

        if (sequence1x1 != null) {
            val bearOffMove = firstForBearingOffAfterMoves(partialMove1, partialMove2, sequence1x1)
            if (bearOffMove != null) {
                addFullMoveIfValid(getBuilder(), partialMove1, partialMove2, sequence1x1, bearOffMove)
            }
        }

        if (sequence2x1 != null) {
            val bearOffMove = firstForBearingOffAfterMoves(partialMove1, partialMove2, sequence2x1)
            if (bearOffMove != null) {
                addFullMoveIfValid(getBuilder(), partialMove1, partialMove2, sequence2x1, bearOffMove)
            }
        }

        val bearOffMove1 = firstForBearingOffAfterMoves(partialMove1, partialMove2)
        if (bearOffMove1 != null) {
            addFullMoveIfValid(getBuilder(), partialMove1, partialMove2, bearOffMove1)

            val bearOffMove2 = firstForBearingOffAfterMoves(partialMove1, partialMove2, bearOffMove1)
            if (bearOffMove2 != null) {
                addFullMoveIfValid(getBuilder(), partialMove1, partialMove2, bearOffMove1, bearOffMove2)
            }
        }
    }


    private fun findBearOffFullMoveWith3PartialMoves(partialMove1: SingleMove, partialMove2: SingleMove, partialMove3: SingleMove) {
        if (!(possibleMoves.bearOffPossibleInitially || possibleMoves.bearOffPossibleConditionally)) {
            return
        }

        val bearOffMove = firstForBearingOffAfterMoves(partialMove1, partialMove2, partialMove3)
        if (bearOffMove != null) {
            addFullMoveIfValid(getBuilder(), partialMove1, partialMove2, partialMove3, bearOffMove)
        }
    }


    private fun getBuilder() = initialFullMoveBuilder.clone()


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** Stupid code for faster execution */
    private fun addFullMoveIfValid(initialBuilder: FullMovesBuilder) {
        if (ensureLongestMove(initialBuilder, 0)) {
            fullMoves.add(initialBuilder.clone().build())
        }
    }

    /** Stupid code for faster execution */
    private fun addFullMoveIfValid(initialBuilder: FullMovesBuilder, move: SingleMove) {
        if (ensureLongestMove(initialBuilder, 1)) {
            fullMoves.add(initialBuilder.clone().append(move).build())
        }
    }

    /** Stupid code for faster execution */
    private fun addFullMoveIfValid(initialBuilder: FullMovesBuilder, move1: SingleMove, move2: SingleMove) {
        if (ensureLongestMove(initialBuilder, 2)) {
            fullMoves.add(initialBuilder.clone().append(move1, move2).build())
        }
    }

    /** Stupid code for faster execution */
    private fun addFullMoveIfValid(initialBuilder: FullMovesBuilder, move1: SingleMove, move2: SingleMove, move3: SingleMove) {
        if (ensureLongestMove(initialBuilder, 3)) {
            fullMoves.add(initialBuilder.clone().append(move1, move2, move3).build())
        }
    }

    /** Stupid code for faster execution */
    private fun addFullMoveIfValid(initialBuilder: FullMovesBuilder, move1: SingleMove, move2: SingleMove, move3: SingleMove, move4: SingleMove) {
        if (ensureLongestMove(initialBuilder, 4)) {
            fullMoves.add(initialBuilder.clone().append(move1, move2, move3, move4).build())
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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