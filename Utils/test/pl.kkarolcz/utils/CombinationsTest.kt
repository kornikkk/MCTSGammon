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
    fun `Test combinations with repetitions`() {
        val expected = mutableListOf<Array<Int>>()
        expected.add(arrayOf(1, 1))
        expected.add(arrayOf(1, 2))
        expected.add(arrayOf(1, 3))
        expected.add(arrayOf(2, 1))
        expected.add(arrayOf(2, 2))
        expected.add(arrayOf(2, 3))
        expected.add(arrayOf(3, 1))
        expected.add(arrayOf(3, 2))
        expected.add(arrayOf(3, 3))


        val range = IntRange(1, 3)
        val combinations = combinationsWithRepetitions(range.toList().toTypedArray(), 2)
        var combinationsCount = 0
        combinations.forEach { combination ->
            ++combinationsCount
            assertTrue(containsCombination(expected, combination.toList().toTypedArray()))
        }

        assertEquals(expected.size, combinationsCount)
    }

    @Test
    fun `Test combinations without repetitions`() {
//        val values = mutableListOf("A", "B", "C", "D", "E", "F")
//
//        val expected = mutableListOf<Array<String>>()
//        expected.add(arrayOf("A", "B"))
//        expected.add(arrayOf("A", "C"))
//        expected.add(arrayOf("B", "A"))
//        expected.add(arrayOf("B", "C"))
//        expected.add(arrayOf("C", "A"))
//        expected.add(arrayOf("C", "B"))

        //val actual = permutations(IntRange(0, 60), 4)
        // println("Count: " + counter)
        r_nCr(0x00111, 0x00010, 0x10000)
    }

    private fun <T> containsCombination(expected: Iterable<Array<T>>, actual: Array<T>): Boolean {
        return expected.any { permutation -> Arrays.equals(permutation, actual) }
    }

    private fun createExpectedArray(vararg elements: Int): Array<Int> {
        val array: Array<Int> = Array(elements.size) { 0 }
        (0 until elements.size).forEach { i -> array[i] = elements[i] }
        return array
    }

}