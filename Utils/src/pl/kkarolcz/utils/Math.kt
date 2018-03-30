package pl.kkarolcz.utils

import java.util.*

/**
 * Created by kkarolcz on 28.03.2018.
 */
private val random = Random()

fun binomialCoefficient(n: Int, k: Int): Int {
    if (n < k)
        throw ArithmeticException("n has to be greater or equal to k")
    var k = k
    var res = 1

    // Since C(n, k) = C(n, n-k)
    if (k > n - k)
        k = n - k

    // Calculate value of [n * (n-1) *---* (n-k+1)] / [k * (k-1) *----* 1]
    for (i in 0 until k) {
        res *= n - i
        res /= i + 1
    }

    return res
}

fun <T> weightedRandom(weights: Array<Int>, items: Array<T>): T? {
    val weightsSum = weights.sum()
    if (weightsSum == 0) {
        return null
    }

    var randomNumber = random.nextInt(weightsSum) + 1
    for (i in 0..weights.size) {
        randomNumber -= weights[i]
        if (randomNumber <= 0) {
            return items[i]
        }
    }
    throw IllegalStateException()
}