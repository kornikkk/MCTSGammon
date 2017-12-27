package pl.kkarolcz.utils

import java.lang.Byte.MIN_VALUE

/**
 * Created by kkarolcz on 14.11.2017.
 */
object ByteMath {
    val ZERO_BYTE: Byte = 0
    val ONE_BYTE: Byte = 1

    fun abs(byte: Byte): Byte {
        if (byte == MIN_VALUE) throw ArithmeticException("abs called on Byte.MIN_VALUE")
        return if (byte < 0) (-byte).toByte() else byte
    }
}