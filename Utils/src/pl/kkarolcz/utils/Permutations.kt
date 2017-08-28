package pl.kkarolcz.utils

import java.util.*


/**
 * Created by kkarolcz on 28.08.2017.
 */

inline fun <reified T> permutations(elements: Iterable<T>): Sequence<Array<T>> =
        PermutationIterator(elements.toList().toTypedArray().copyOf()).asSequence()

/**
 * Permutations as Iterator implementation of the QuickPerm algorithm described by Phillip Paul Fuchs at http://www.quickperm.org
 */
@PublishedApi internal
class PermutationIterator<T>(private val elements: Array<T>) : Iterator<Array<T>> {
    private val n = elements.size
    private val p = IntArray(n)
    private var i = 1

    private var permutationsCount = n.factorial()
    private var currentPermutation = 0L

    override fun hasNext(): Boolean {
        return currentPermutation < permutationsCount
    }

    override fun next(): Array<T> {
        if (currentPermutation == 0L) {
            ++currentPermutation
            return elements
        }
        while (i < n) {
            if (p[i] < i) {
                val j = if (i % 2 == 0) 0 else p[i]
                swap(elements, i, j)

                p[i]++
                i = 1

                ++currentPermutation
                return elements
            } else {
                p[i] = 0
                ++i
            }
        }
        throw NoSuchElementException()
    }

    private fun <T> swap(elements: Array<T>, i: Int, j: Int) {
        val temp = elements[i]
        elements[i] = elements[j]
        elements[j] = temp
    }

    private fun Int.factorial(): Long {
        var number = this
        if (number == 0)
            return 1

        var result = 1L
        while (number > 0) {
            result *= number
            number--
        }
        return result
    }
}