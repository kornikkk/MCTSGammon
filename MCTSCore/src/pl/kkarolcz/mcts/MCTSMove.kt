package pl.kkarolcz.mcts

/**
 * Created by kkarolcz on 07.08.2017.
 */
interface MCTSMove {
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int

}