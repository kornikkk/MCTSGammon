package pl.kkarolcz.utils

import java.util.*


/**
 * Created by kkarolcz on 15.11.2017.
 */

inline fun <reified T> combinationsWithRepetitions(elements: Iterable<T>, n: Int): Iterable<Array<T>>
        = combinationsWithRepetitions(elements.toList().toTypedArray(), n)

inline fun <reified T> combinationsWithRepetitions(elements: Array<T>, n: Int): Iterable<Array<T>> {
    // Calculate the number of arrays we should create
    val arraysCount = Math.pow(elements.size.toDouble(), n.toDouble()).toInt()
    val combinations = ArrayList<Array<T?>>(arraysCount)
    // Create each array
    for (i in 0 until arraysCount) {
        combinations.add(Array(n) { null })
    }
    // Fill up the arrays
    for (j in 0 until n) {
        // Period with which this position changes, i.e.
        // a period of 5 means the value changes every 5th array
        val period = Math.pow(elements.size.toDouble(), (n - j - 1).toDouble()).toInt()
        for (i in 0 until arraysCount) {
            val current = combinations[i]
            // Get the correct item and set it
            val index = i / period % elements.size
            current[j] = elements[index]
        }
    }

    @Suppress("UNCHECKED_CAST")
    return combinations as Iterable<Array<T>>
}

var counter = 0

fun <T> permutations(elements: Iterable<T>, k: Int): MutableList<Array<T>> {
    val elementsList = elements.toMutableList()
    val permutations = mutableListOf<Array<T>>()

    permutationsEnumerate(elementsList, elementsList.size, k, permutations)

    return permutations
}

// n is the array size
private fun <T> permutationsEnumerate(elements: MutableList<T>, n: Int, k: Int, permutations: MutableList<Array<T>>) {
    if (k == 0) {
        val permutation = Array<Any?>(elements.size - n, { null })
        for (i in n until elements.size) {
            permutation[i - n] = elements[i]
        }

        @Suppress("UNCHECKED_CAST") // Not possible to know the type of an array when it's created
        permutations.add(permutation as Array<T>)
        return
    }

    for (i in 0 until n) {
        counter += 1
        swap(elements, i, n - 1)
        permutationsEnumerate(elements, n - 1, k - 1, permutations)
        swap(elements, i, n - 1)
    }
}

fun <T> swap(a: MutableList<T>, i: Int, j: Int) {
    val temp = a[i]
    a[i] = a[j]
    a[j] = temp
}


fun r_nCr(startNum: Int, bitVal: Int, testNum: Int) {
    var n: Int = (startNum xor bitVal) shl 1
    val RENAME_ME = when (bitVal) {
        0 -> 0
        else -> 1
    }
    n = n or RENAME_ME

    for (i in (log(testNum, 2) + 1) downTo 1) // Prints combination as a series of 1s and 0s
        print(Integer.toBinaryString(n + (i - 1) and 1))
    println()

    if ((n and testNum) == 0 && n != startNum)
        r_nCr(n, bitVal, testNum)

    if (bitVal != 0 && bitVal < testNum)
        r_nCr(startNum, bitVal ushr 1, testNum)
}

fun log(x: Int, base: Int): Int {
    return (Math.log(x.toDouble()) / Math.log(base.toDouble())).toInt()
}