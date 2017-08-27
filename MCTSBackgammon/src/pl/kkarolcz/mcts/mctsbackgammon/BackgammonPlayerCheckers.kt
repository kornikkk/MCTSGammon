package pl.kkarolcz.mcts.mctsbackgammon

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonPlayerCheckers : Cloneable {
    private val checkers: IntArray

    companion object {
        private val SIZE: Int = BackgammonBoard.SIZE
    }

    constructor() {
        checkers = IntArray(SIZE) { 0 }
    }

    private constructor(other: BackgammonPlayerCheckers) {
        this.checkers = other.checkers.copyOf()
    }

    /**
     * @throws IllegalArgumentException if the board is of the wrong size
     */
    constructor(checkers: IntArray) {
        when (checkers.size) {
            SIZE -> this.checkers = checkers
            else -> throw IllegalArgumentException("Wrong size of the board")
        }

    }

    public override fun clone(): BackgammonPlayerCheckers {
        return BackgammonPlayerCheckers(this)
    }

    fun barEmpty(): Boolean = checkers[BackgammonBoardIndex.BAR_INDEX] == 0

    fun anyLeft(): Boolean = checkers.any { checkersOnPoint -> checkersOnPoint > 0 }

    fun allInHomeBoard(): Boolean = checkers.withIndex()
            .none { (index, value) -> index > BackgammonBoardIndex.HOME_BOARD_START_INDEX && value > 0 }

    fun firstOnHomeBoard(): BackgammonBoardIndex? =
            checkers.slice(BackgammonBoardIndex.HOME_BOARD_INDICES
                    .map(BackgammonBoardIndex::toInt))
                    .firstOrNull { numberOfCheckers -> numberOfCheckers > 0 }
                    ?.let(BackgammonBoardIndex.Companion::of)

    /**
     * Notice that board is indexed like in Backgammon!!! from 1 to 24
     */
    operator fun get(index: BackgammonBoardIndex) = checkers[index.toInt()]
}