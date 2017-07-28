package pl.kkarolcz.mctsgammon.mcts


/**
 * Created by kkarolcz on 23.07.2017.
 */
interface Board : Cloneable {

    /**
     * Create one copy of the board. It is important that the copies do
     * not store references to objects shared by other boards unless
     * those objects are immutable.

     * @return
     */
    public override fun clone(): Board

    /**
     * The location parameter indicates from where in the algorithm
     * the method was called. Can be either treePolicy or playout.

     * @param location
     * @return Collection of all available moves for the current state. MCTS calls this to know what actions are possible at that point.
     */
    fun getMoves(location: CallLocation): Collection<Move>

    /**
     * Apply the move m to the current state of the board.
     * @param m
     */
    fun makeMove(m: Move)

    /**
     * Returns true if the game is over.
     * @return
     */
    fun gameOver(): Boolean

    /**
     * @return active player's ID
     */
    val currentPlayer: Int

    val numberOfPlayers: Int

    /**
     * Returns a score vector.
     * [1.0, 0.0] indicates a win for player 0.
     * [0.0, 1.0] indicates a win for player 1
     * [0.5, 0.5] indicates a draw
     * @return score array
     */
    val score: DoubleArray

    /**
     * @return an array of probability weights for each move possible on this board. This is only relevant in board states where the choice to make is a random choice.
     */
    val moveWeights: DoubleArray

}