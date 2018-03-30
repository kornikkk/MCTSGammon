package pl.kkarolcz.utils

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by kkarolcz on 28.03.2018.
 */
class MathTest {

    @Test
    fun binomialCoefficientTest() {
        assertEquals(64684950, binomialCoefficient(200, 4))
        assertEquals(129024480, binomialCoefficient(32, 21))
    }

    @Test(expected = ArithmeticException::class)
    fun binomialCoefficientWrongDataTest() {
        binomialCoefficient(1, 4)
    }

    @Test
    fun weightedRandomTest() {
        val weights = arrayOf(27, 60, 13)
        val items = arrayOf("A", "B", "C")
        var aCount = 0
        var bCount = 0
        var cCount = 0

        val error = 0.1
        val tries = 1_000_000

        for (i in 0 until tries) {
            val randomItem = weightedRandom(weights, items)
            when (randomItem) {
                "A" -> aCount++
                "B" -> bCount++
                "C" -> cCount++
            }
        }

        assertTrue(aCount in rangeWithError((0.27 * tries).toInt(), error))
        assertTrue(bCount in rangeWithError((0.60 * tries).toInt(), error))
        assertTrue(cCount in rangeWithError((0.13 * tries).toInt(), error))
    }

    private fun rangeWithError(number: Int, error: Double): IntRange {
        val errorValue: Int = (number * error).toInt()
        return number - errorValue..number + errorValue
    }
}