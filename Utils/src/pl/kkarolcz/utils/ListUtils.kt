package pl.kkarolcz.utils

import java.util.*

/**
 * Created by kkarolcz on 09.08.2017.
 */
private val random: Random = Random()

fun <T> List<T>.randomElement(): T {
    return this[random.nextInt(size)]
}
