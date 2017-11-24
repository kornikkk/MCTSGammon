package pl.kkarolcz.utils

import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by kkarolcz on 15.11.2017.
 */
class CombinationsTest {

    @Test
    fun `Test combinations`() {
        val expected = mutableListOf<Array<Int>>()
        expected.add(createExpectedArray(1, 1))
        expected.add(createExpectedArray(1, 2))
        expected.add(createExpectedArray(1, 3))
        expected.add(createExpectedArray(2, 1))
        expected.add(createExpectedArray(2, 2))
        expected.add(createExpectedArray(2, 3))
        expected.add(createExpectedArray(3, 1))
        expected.add(createExpectedArray(3, 2))
        expected.add(createExpectedArray(3, 3))


        val range = IntRange(1, 3)
        val combinations = combinations(range.toList().toTypedArray(), 2)
        var combinationsCount = 0
        combinations.forEach { combination ->
            ++combinationsCount
            assertTrue(containsPermutation(expected, combination.toList().toTypedArray()))
        }

        assertEquals(expected.size, combinationsCount)
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