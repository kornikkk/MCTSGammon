package pl.kkarolcz.utils

import java.util.*
import java.util.Collections.singletonList
import kotlin.collections.ArrayList

/**
 * Created by kkarolcz on 09.08.2017.
 */
private val random: Random = Random()

fun <T> List<T>.randomElement(): T {
    return this[random.nextInt(size)]
}

fun <T> MutableList<T>.copyAsArrayList(): MutableList<T> {
    return ArrayList(this)
}

fun <T> singletonOrEmptyList(element: T?): List<T> {
    if (element != null)
        return singletonList<T>(element)
    return emptyList()
}
