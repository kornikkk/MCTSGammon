package pl.kkarolcz.mcts.mctsbackgammon.game.dices

import pl.kkarolcz.utils.combinations

/**
 * Created by kkarolcz on 24.08.2017.
 */
class BackgammonDices constructor(val first: Byte, val second: Byte) {

    val doubling = first == second

    val values = when (doubling) {
        true -> arrayOf(first, first, second, second)
        false -> arrayOf(first, second)
    }

    val valuesOLD: List<Dice> = when (doubling) {
        true -> arrayListOf(Dice(first), Dice(first), Dice(second), Dice(second))
        false -> arrayListOf(Dice(first), Dice(second))
    }

    companion object {
        private val POSSIBLE_VALUES_COMBINATIONS = combinations(Dice.POSSIBLE_VALUES.toList().toTypedArray(), 2)
        val POSSIBLE_COMBINATIONS = this.POSSIBLE_VALUES_COMBINATIONS.map { (dice1, dice2) -> BackgammonDices(dice1, dice2) }
    }
}
