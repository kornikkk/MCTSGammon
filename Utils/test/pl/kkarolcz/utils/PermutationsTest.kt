package pl.kkarolcz.utils

import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by kkarolcz on 28.08.2017.
 */
class PermutationsTest {
    @Test
    fun `Test permutations`() {
        val expected = mutableListOf<Array<Int>>()
        expected.add(createExpectedArray(1, 2, 3))
        expected.add(createExpectedArray(1, 3, 2))
        expected.add(createExpectedArray(2, 1, 3))
        expected.add(createExpectedArray(2, 3, 1))
        expected.add(createExpectedArray(3, 1, 2))
        expected.add(createExpectedArray(3, 2, 1))

        val range = IntRange(1, 3)
        val permutations = permutations(range)
        var permutationsCount = 0
        permutations.forEach { permutation ->
            ++permutationsCount
            assertTrue(containsPermutation(expected, permutation))
        }

        assertEquals(expected.size, permutationsCount)
    }

    private fun containsPermutation(expected: Iterable<Array<Int>>, actual: Array<Int>): Boolean {
        return expected.any { permutation -> Arrays.equals(permutation, actual) }
    }

    private fun createExpectedArray(vararg elements: Int): Array<Int> {
        val array: Array<Int> = Array(elements.size) { 0 }
        (0 until elements.size).forEach { i -> array[i] = elements[i] }
        return array
    }
}