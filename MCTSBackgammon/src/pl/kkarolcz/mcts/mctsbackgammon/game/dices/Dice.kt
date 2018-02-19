package pl.kkarolcz.mcts.mctsbackgammon.game.dices

import pl.kkarolcz.utils.combinations

/**
 * Created by kkarolcz on 24.08.2017.
 */
class Dice constructor(val first: Byte, val second: Byte) {

    val doubling = first == second

    val values = when (doubling) {
        true -> arrayOf(first, first, second, second)
        false -> arrayOf(first, second)
    }

    val valuesOLD: List<Die> = when (doubling) {
        true -> arrayListOf(Die(first), Die(first), Die(second), Die(second))
        false -> arrayListOf(Die(first), Die(second))
    }

    companion object {
        private val POSSIBLE_VALUES_COMBINATIONS = combinations(Die.POSSIBLE_VALUES.toList().toTypedArray(), 2)
        val POSSIBLE_COMBINATIONS = this.POSSIBLE_VALUES_COMBINATIONS.map { (dice1, dice2) -> Dice(dice1, dice2) }
    }
}
