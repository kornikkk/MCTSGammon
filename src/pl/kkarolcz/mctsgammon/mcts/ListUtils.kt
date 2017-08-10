package pl.kkarolcz.mctsgammon.mcts

import java.util.*

/**
 * Created by kkarolcz on 09.08.2017.
 */
private val random: Random = Random()

fun <T> List<T>.randomElement() = this[random.nextInt(size)]
