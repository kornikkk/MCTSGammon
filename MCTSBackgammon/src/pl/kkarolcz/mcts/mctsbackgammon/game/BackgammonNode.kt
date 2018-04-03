package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.MCTSNode
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.node.selectionpolicies.NodeSelectionPolicy

/**
 * Created by kkarolcz on 25.02.2018.
 */
class BackgammonNode(nodeSelectionPolicy: NodeSelectionPolicy, state: BackgammonState, parent: BackgammonNode?, originMove: FullMove?)
    : MCTSNode<BackgammonNode, BackgammonState, FullMove>(nodeSelectionPolicy, state, parent, originMove) {

    companion object {

        fun createRootNode(nodeSelectionPolicy: NodeSelectionPolicy, initialState: BackgammonState): BackgammonNode =
                BackgammonNode(nodeSelectionPolicy, initialState, null, null)

    }

    override fun newNode(nodeSelectionPolicy: NodeSelectionPolicy, state: BackgammonState, originMove: FullMove?): BackgammonNode =
            BackgammonNode(nodeSelectionPolicy, state, this, originMove)

    fun discardOtherDice(dice: Dice) {
        children.removeIf { child -> child.originMove!!.dice != dice } //Can't be null here. If it is that's probably an error in the code
        state.movesProvider.discardOtherDice(dice)
    }

}