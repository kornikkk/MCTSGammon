package pl.kkarolcz.mcts.mctsbackgammon.game.moves

import pl.kkarolcz.mcts.Player
import pl.kkarolcz.mcts.mctsbackgammon.board.Board
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex
import pl.kkarolcz.mcts.mctsbackgammon.board.BoardIndex.Companion.NO_INDEX
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
        FullMovesSearchDoubling(board, Player.MCTS, dices).findAll()
    }
    val endTime = System.currentTimeMillis()

    println("Average time: ${(endTime - startTime) / attempts} ms")
}

class FullMovesSearchDoubling(board: Board, currentPlayer: Player, dice: Dice)
    : AbstractFullMovesSearch(board, currentPlayer, dice) {

    private val die = dice.first

    private val barMoves = mutableListOf<PartialMove>()
    private val barSequentialMoves = SequencesForPartialMoves()

    private val partialMoves = mutableListOf<PartialMove>()
    private val standardSequentialMoves = SequencesForPartialMoves()

    private var diceLeft = 4

    private var currentFullMoveMaxLength = 0


    override fun findAllImpl() {
        if (playerCheckers.barCheckers > 0) {
            findBarMoves()

            if (playerCheckers.barCheckers >= 4) {
                fullMoves.add(FullMove(barMoves.map(PartialMove::move)))
                return // No need to do anything when there's >= 4 checkers on the bar
            }
            diceLeft -= playerCheckers.barCheckers
        }

        findBarSequentialMoves()
        findStandardPartialMoves()
        findStandardSequentialMoves()

        findFullMovesRecursive()
        //  println("Count of partial doubling moves: ${partialMoves.length}")
    }

    private fun findBarMoves() {
        val moveFromBar = findPartialBarMove(die) ?: return // Stop if there are checkers on the bar and no possible moves
        for (i in 1..minOf(playerCheckers.barCheckers, 4)) {
            barMoves.add(PartialMove(moveFromBar))
        }
    }

    private fun findStandardPartialMoves() {
        for (tower in playerCheckers.towerIterator()) {
            val move = findStandardPartialMoveForTower(tower.index, die)
            if (move != null) {
                for (i in 1..minOf(tower.checkers.toInt(), diceLeft)) {
                    partialMoves.add(PartialMove(move))
                }
            }
        }
    }

    private fun findBarSequentialMoves() {
        // No need to decrement dice like in findStandardSequentialMoves(). It was already done because bar moves exist
        findSequentialMoves(barMoves, barSequentialMoves, diceLeft)
    }

    private fun findStandardSequentialMoves() {
        // We assume that one of the partial moves is used so dice are decremented
        findSequentialMoves(partialMoves, standardSequentialMoves, diceLeft - 1)
    }

    private fun findSequentialMoves(initialMoves: List<PartialMove>, sequences: SequencesForPartialMoves, initialDiceLeft: Int) {
        for (partialMove in initialMoves.distinct()) {
            var diceLeft = initialDiceLeft
            var oldIndex: Byte
            var newIndex = partialMove.move.newIndex

            while (diceLeft > 0) {
                oldIndex = newIndex
                newIndex = findMove(oldIndex, die)
                diceLeft -= 1

                if (newIndex == NO_INDEX) {
                    break
                }

                sequences.getOrCreate(partialMove).add(SingleMove(oldIndex, newIndex))
            }
        }
    }

    private fun findFullMovesRecursive() {
        findFullMovesRecursive(RecursionState(), diceLeft)
    }

    private fun findFullMovesRecursive(recursionState: RecursionState, diceLeft: Int) {
        val fullMoveBuilder = recursionState.fullMovesBuilder
        val barMovesSequences = recursionState.barMovesSequences
        val partialMoves = recursionState.partialMoves
        val sequences = recursionState.standardSequences

        //TODO handle situation when dice left
        if (diceLeft > 0) {
            //TODO bear off

            for (sequence in barMovesSequences) {
                findFullMovesWithBarSequence(recursionState.copy(true, false, false), diceLeft, sequence.key)
            }

            for (sequence in sequences) {
                findFullMovesWithSequence(recursionState.copy(false, false, true), diceLeft, sequence.key)
            }

            for (partialMove in partialMoves) {
                findFullMovesWithPartialMove(recursionState.copy(false, true, true), diceLeft, partialMove)
            }
        }
        addFullMoveIfCorrect(fullMoveBuilder)

        //TODO handle situation when dice left
    }

    private fun findFullMovesWithBarSequence(recursionState: RecursionState, diceLeft: Int, partialMove: PartialMove) {
        val sequentialMove = recursionState.barMovesSequences.getSequence(partialMove)?.poll()
        if (sequentialMove != null) {
            recursionState.fullMovesBuilder.append(sequentialMove)
        }

        findFullMovesRecursive(recursionState, diceLeft - 1)
    }

    private fun findFullMovesWithSequence(recursionState: RecursionState, diceLeft: Int, partialMove: PartialMove) {
        val sequentialMove = recursionState.standardSequences.getSequence(partialMove)?.poll()
        if (sequentialMove != null) {
            recursionState.fullMovesBuilder.append(sequentialMove)
        }

        findFullMovesRecursive(recursionState, diceLeft - 1)
    }

    private fun findFullMovesWithPartialMove(recursionState: RecursionState, diceLeft: Int, partialMove: PartialMove) {
        recursionState.fullMovesBuilder.append(partialMove.move)

        val sequenceForPartialMove = standardSequentialMoves.getSequence(partialMove)
        if (sequenceForPartialMove != null) {
            recursionState.standardSequences.add(sequenceForPartialMove)
        }

        recursionState.partialMoves.remove(partialMove)

        findFullMovesRecursive(recursionState, diceLeft - 1)
    }

    private fun addFullMoveIfCorrect(fullMoveBuilder: FullMovesBuilder) {
        val size = fullMoveBuilder.length
        if (size < currentFullMoveMaxLength) {
            return
        }
        if (size > currentFullMoveMaxLength) {
            currentFullMoveMaxLength = size
            fullMoves.clear()
        }
        fullMoves.add(fullMoveBuilder.build())

    }

    var testCounter = 0

    private inner class RecursionState : Cloneable {

        val fullMovesBuilder: FullMovesBuilder
        val barMovesSequences: SequencesForPartialMoves
        val partialMoves: MutableList<PartialMove>
        val standardSequences: SequencesForPartialMoves

        constructor() {
            this.fullMovesBuilder = FullMovesBuilder(this@FullMovesSearchDoubling.barMoves.map(PartialMove::move))
            this.barMovesSequences = SequencesForPartialMoves()
            this.partialMoves = this@FullMovesSearchDoubling.partialMoves
            this.standardSequences = SequencesForPartialMoves()

            addAllBarMovesSequences()
        }

        constructor(other: RecursionState, copyBarSequences: Boolean, copyPartialMoves: Boolean, copyStandardSequences: Boolean) {
            this.fullMovesBuilder = other.fullMovesBuilder.clone()
            this.barMovesSequences = when (copyBarSequences) {
                true -> other.barMovesSequences.clone()
                else -> other.barMovesSequences
            }
            this.partialMoves = when (copyPartialMoves) {
                true -> other.partialMoves.toMutableList()
                else -> other.partialMoves
            }
            this.standardSequences = when (copyStandardSequences) {
                true -> other.standardSequences.clone()
                else -> other.standardSequences
            }
        }

        private fun addAllBarMovesSequences() {
            for (barMove in barMoves) {
                val sequence = this@FullMovesSearchDoubling.barSequentialMoves.getSequence(barMove)
                if (sequence != null) {
                    barMovesSequences.add(sequence)
                }
            }
        }

        fun copy(copyBarSequences: Boolean, copyPartialMoves: Boolean, copyStandardSequences: Boolean): RecursionState {
            testCounter += 1
            //    println(testCounter)
            return RecursionState(this, copyBarSequences, copyPartialMoves, copyStandardSequences)
        }

    }

    private class PartialMove(val move: SingleMove)

    private class SequencesForPartialMoves : Cloneable, Iterable<SequenceForPartialMove> {
        private val map: IdentityHashMap<PartialMove, SequenceForPartialMove>

        constructor() {
            this.map = IdentityHashMap()
        }

        private constructor(other: SequencesForPartialMoves) {
            this.map = IdentityHashMap(other.map.size)
            for (entry in other.map) {
                if (!entry.value.isEmpty()) {
                    val cloned = entry.value.clone()
                    map.put(entry.key, cloned)
                }
            }
        }

        fun getOrCreate(key: PartialMove): SequenceForPartialMove {
            return map.computeIfAbsent(key) { SequenceForPartialMove(key) }
        }

        fun getSequence(partialMove: PartialMove): SequenceForPartialMove? {
            return map.getOrDefault(partialMove, null)
        }

        fun add(sequence: SequenceForPartialMove) {
            map.put(sequence.key, sequence.clone())
        }

        override fun iterator(): Iterator<SequenceForPartialMove> = map.values.iterator()

        public override fun clone(): SequencesForPartialMoves {
            return SequencesForPartialMoves(this)
        }
    }

    private class SequenceForPartialMove : Cloneable {
        val key: PartialMove
        private val sequence: MutableList<SingleMove>

        constructor(key: PartialMove) {
            this.key = key
            this.sequence = ArrayList(4)
        }

        private constructor(other: SequenceForPartialMove) {
            this.key = other.key
            this.sequence = ArrayList(other.sequence)
        }

        fun add(move: SingleMove) {
            sequence.add(move)
        }

        fun isEmpty() = sequence.isEmpty()

        fun poll(): SingleMove? {
            if (sequence.isNotEmpty()) {
                return sequence.removeAt(0)
            }
            return null
        }

        public override fun clone() = SequenceForPartialMove(this)

    }
}