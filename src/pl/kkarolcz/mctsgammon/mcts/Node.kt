package pl.kkarolcz.mctsgammon.mcts

import java.util.*
import java.util.concurrent.ThreadLocalRandom


/**
 * Created by kkarolcz on 23.07.2017.
 */
class Node {
    val state: State
    var parent: Node? = null
    val childArray: MutableList<Node> = ArrayList()

    val randomChildNode: Node?
        get() = if (childArray.isNotEmpty()) childArray[ThreadLocalRandom.current().nextInt(childArray.size)] else null

    val childWithMaxScore: Node?
        get() = childArray.maxBy { n -> n.state.visitCount }

    constructor() {
        this.state = State()
    }

    constructor(state: State) {
        this.state = state
    }

    constructor(state: State, parent: Node, childArray: MutableList<Node>) {
        this.state = state
        this.parent = parent
        this.childArray = childArray
    }

    constructor(node: Node) {
        this.state = State(node.state)
        this.parent = node.parent
        node.childArray.forEach { child -> this.childArray.add(Node(child)) }
    }

}