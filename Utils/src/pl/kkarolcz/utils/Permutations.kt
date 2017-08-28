package pl.kkarolcz.utils

import java.util.*


/**
 * Created by kkarolcz on 28.08.2017.
 */

inline fun <reified T> permutations(elements: Iterable<T>): Sequence<Sequence<T>> =
        PermutationIterator(elements.toList().toTypedArray().copyOf()).asSequence()

/**
 * Permutations as Iterator implementation of the QuickPerm algorithm described by Phillip Paul Fuchs at http://www.quickperm.org
 */
@PublishedApi internal
class PermutationIterator<out T>(private val elements: Array<T>) : Iterator<Sequence<T>> {
    private val p = IntArray(elements.size)
    private var i = 1

    private var permutationsCount = elements.size.factorial()
    private var currentPermutation = 0L

    override fun hasNext(): Boolean {
        return currentPermutation < permutationsCount
    }

    override fun next(): Sequence<T> {
        if (currentPermutation == 0L) {
            ++currentPermutation
            return elements.asSequence()
        }
        while (i < elements.size) {
            if (p[i] < i) {
                val j = if (i % 2 == 0) 0 else p[i]
                swap(elements, i, j)

                p[i]++
                i = 1

                ++currentPermutation
                return elements.asSequence()
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
        var number = this.toLong()
        return when (number) {
            0L -> 1L
            else -> {
                var result = 1L
                while (number > 0) {
                    result *= number
                    number--
                }
                result
            }
        }
    }
}