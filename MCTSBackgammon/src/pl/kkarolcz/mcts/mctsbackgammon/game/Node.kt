package pl.kkarolcz.mcts.mctsbackgammon.game

import pl.kkarolcz.mcts.MCTSNode
import pl.kkarolcz.mcts.MCTSState
import pl.kkarolcz.mcts.mctsbackgammon.game.dices.Dice
import pl.kkarolcz.mcts.mctsbackgammon.game.moves.FullMove
import pl.kkarolcz.mcts.node.selectionpolicies.NodeSelectionPolicy

/**
 * Created by kkarolcz on 25.02.2018.
 */
class Node(nodeSelectionPolicy: NodeSelectionPolicy, state: MCTSState<FullMove, Dice>, originMove: FullMove?)
    : MCTSNode<Node, FullMove, Dice>(nodeSelectionPolicy, state, originMove) {

    companion object {

        fun createRootNode(nodeSelectionPolicy: NodeSelectionPolicy, initialState: State): Node
                = Node(nodeSelectionPolicy, initialState, null)

    }

    override fun newNode(nodeSelectionPolicy: NodeSelectionPolicy, state: MCTSState<FullMove, Dice>, originMove: FullMove?): Node
            = Node(nodeSelectionPolicy, state, originMove)

}