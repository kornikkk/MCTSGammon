package pl.kkarolcz.utils

/**
 * Created by kkarolcz on 15.11.2017.
 */

inline fun <reified T> combinations(elements: Iterable<T>, n: Int): Iterable<Array<T>>
        = combinations(elements.toList().toTypedArray(), n)

inline fun <reified T> combinations(elements: Array<T>, n: Int): Iterable<Array<T>> {
    val combinations = mutableListOf<Array<T?>>()
    // Calculate the number of arrays we should create
    val numArrays = Math.pow(elements.size.toDouble(), n.toDouble()).toInt()
    // Create each array
    for (i in 0 until numArrays) {
        combinations.add(Array(n) { null })
    }
    // Fill up the arrays
    for (j in 0 until n) {
        // Period with which this position changes, i.e.
        // a period of 5 means the value changes every 5th array
        val period = Math.pow(elements.size.toDouble(), (n - j - 1).toDouble()).toInt()
        for (i in 0 until numArrays) {
            val current = combinations[i]
            // Get the correct item and set it
            val index = i / period % elements.size
            current[j] = elements[index]
        }
    }

    @Suppress("UNCHECKED_CAST")
    return combinations as Iterable<Array<T>>
}