package pl.kkarolcz.utils

import java.util.*
import java.util.Collections.singletonList

/**
 * Created by kkarolcz on 09.08.2017.
 */
private val random: Random = Random()

fun <T> List<T>.randomOrNullElement(): T? {
    if (isEmpty())
        return null
    return randomElement()
}

/**
 * @throws IllegalArgumentException if list is empty
 */
fun <T> List<T>.randomElement(): T {
    return this[random.nextInt(size)]
}

fun <T> singletonOrEmptyList(element: T?): List<T> {
    if (element != null)
        return singletonList<T>(element)
    return emptyList()
}
