package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.PlayerBoard
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import java.util.*

/**
 * Created by kkarolcz on 27.12.2017.
 */


fun main(vararg args: String) {
    val player1Checkers = PlayerBoard()
    for (i in 1..15)
        player1Checkers.put((BoardIndex.BAR_INDEX - i.toByte()).toByte(), 1)

    val board = Board(player1Checkers, PlayerBoard())
    val dices = Dice(3, 3)

    val attempts = 10000

    val startTime = System.currentTimeMillis()
    for (i in 1..attempts) {
        FullMovesSearchDoubling_V2(board, Player.MCTS, dices).findAll()
    }
    val endTime = System.currentTimeMillis()

    println("Average time: ${(endTime - startTime) / attempts} ms")
}

class FullMovesSearchDoubling_V2(board: Board, currentPlayer: Player, dice: Dice)
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
                addFullMove(initialFullMoveBuilder)
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

            while (diceLeft > 0) {
                oldIndex = newIndex
                newIndex = findMove(oldIndex, die)
                diceLeft -= 1

                if (newIndex == BoardIndex.NO_INDEX) {
                    break
                }

                sequences.addToSequence(partialMove, SingleMove(oldIndex, newIndex))
            }
        }
    }


    private fun findFullMoves() {
        if (barMove != null) {
            findSequencesWithBarPartialMovesOnly()
        }

        if (partialMoves.isEmpty() && fullMoves.isEmpty()) {
            addFullMove(getBuilder())
            return
        }



        for (move1 in partialMoves) {
            addFullMove(getBuilder().append(move1))

            if (diceLeft < 2)
                continue

            findSequencesWith1PartialMove(move1)

            for (move2 in partialMoves) {
                if (shouldSkipPartialMove(move2, move1))
                    continue

                addFullMove(getBuilder().append(move1, move2))

                if (diceLeft < 3)
                    continue

                findSequencesWith2PartialMoves(move1, move2)

                for (move3 in partialMoves) {
                    if (shouldSkipPartialMove(move3, move1, move2))
                        continue

                    addFullMove(getBuilder().append(move1, move2, move3))

                    if (diceLeft < 4)
                        continue

                    findSequencesWith3PartialMoves(move1, move2, move3)

                    for (move4 in partialMoves) {
                        if (shouldSkipPartialMove(move4, move1, move2, move3))
                            continue

                        addFullMove(getBuilder().append(move1, move2, move3, move4))
                    }
                }
            }
        }
    }

    private fun findSequencesWithBarPartialMovesOnly() {
        val barSequence = barMove?.let { barSequentialMoves.getSequence(it) }
        when (diceLeft) {
            3 -> { // 1 partial move from the bar
                if (barSequence != null) {
                    val builder = getBuilder()
                    barSequence.forEach { builder.append(it) }
                    addFullMove(builder)
                }
            }
            2 -> { // 2 partial moves from the bar
                val barSequence1 = barSequence?.get(0)
                val barSequence2 = barSequence?.get(1)

                if (barSequence1 != null && barSequence2 != null)
                    addFullMove(getBuilder().append(barSequence1, barSequence2))

                if (barSequence1 != null)
                    addFullMove(getBuilder().append(barSequence1, barSequence1))
            }
            1 -> { // 3 partial moves from the bar
                val barSequence1 = barSequence?.get(0)
                if (barSequence1 != null)
                    addFullMove(getBuilder().append(barSequence1))
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
                    addFullMove(builder.clone().append(sequence1, sequence2, sequence3))

                // Non full moves (3)
                if (sequence1 != null && sequence2 != null)
                    addFullMove(builder.clone().append(sequence1, sequence2))

                // Non full moves (2)
                if (sequence1 != null)
                    addFullMove(builder.clone().append(sequence1))
            }
            3 -> { // 1 partialMove from the bar
                // Full moves (4)
                if (barSequence1 != null && barSequence2 != null)
                    addFullMove(builder.clone().append(barSequence1, barSequence2))
                if (barSequence1 != null && sequence1 != null)
                    addFullMove(builder.clone().append(barSequence1, sequence1))
                if (sequence1 != null && sequence2 != null)
                    addFullMove(builder.clone().append(sequence1, sequence2))

                // Non full moves (3)
                if (barSequence1 != null && barSequence2 == null && sequence1 == null) {
                    addFullMove(builder.clone().append(barSequence1))
                }
            }
            2 -> { // 2 moves from the bar
                // Full moves (4)
                if (barSequence1 != null)
                    addFullMove(builder.clone().append(barSequence1))
                if (sequence1 != null)
                    addFullMove(builder.clone().append(sequence1))
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
                    addFullMove(builder.clone().append(sequence1x1, sequence2x1))
                if (sequence1x1 != null && sequence1x2 != null)
                    addFullMove(builder.clone().append(sequence1x1, sequence1x2))
                if (sequence2x1 != null && sequence2x2 != null)
                    addFullMove(builder.clone().append(sequence2x1, sequence2x2))

                // Non full moves (3)
                if (sequence1x1 != null)
                    addFullMove(builder.clone().append(sequence1x1))
                if (sequence2x1 != null)
                    addFullMove(builder.clone().append(sequence2x1))
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
                    addFullMove(builder.clone().append(sequence1x1))
                if (sequence2x1 != null)
                    addFullMove(builder.clone().append(sequence2x1))
                if (sequence3x1 != null)
                    addFullMove(builder.clone().append(sequence3x1))
            }
        }
    }

    private fun shouldSkipPartialMove(currentMove: SingleMove, vararg previousMoves: SingleMove): Boolean {
        return previousMoves.any { currentMove === it }
    }

    private fun getBuilder() = initialFullMoveBuilder.clone()

    private fun addFullMove(builder: FullMovesBuilder) {
        val length = builder.length
        if (length > longestFullMove) {
            fullMoves.clear()
            longestFullMove = length
        }

        if (length == longestFullMove) {
            fullMoves.add(builder.build())
        }
    }

    private class SequencesForPartialMoves {
        private val map = IdentityHashMap<SingleMove, SequenceForPartialMove>(15)

        fun addToSequence(partialMove: SingleMove, sequentialMove: SingleMove) {
            map.computeIfAbsent(partialMove) { SequenceForPartialMove() }.add(sequentialMove)
        }

        fun getSequence(partialMove: SingleMove): SequenceForPartialMove? {
            return map.getOrDefault(partialMove, null)
        }

    }

    private class SequenceForPartialMove : Iterable<SingleMove> {

        private val sequence = ArrayList<SingleMove>(3)

        override fun iterator(): Iterator<SingleMove> = sequence.iterator()

        operator fun get(index: Int): SingleMove? = sequence.getOrNull(index)

        fun add(move: SingleMove) {
            sequence.add(move)
        }

    }

}