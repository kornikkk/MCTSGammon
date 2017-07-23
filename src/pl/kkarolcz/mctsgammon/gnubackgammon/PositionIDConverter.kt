package pl.kkarolcz.mctsgammon.gnubackgammon

import java.util.*

/**
 * Created by kkarolcz on 22.07.2017.
 */
object PositionIDConverter {

    fun convert(positionId: String): Array<Array<Int>> {
        val anpBoard = Base64.getDecoder().decode(positionId)
        return positionFromKey(anpBoard)
    }

    private fun Byte.unsigned(): Int = this + 128 xor 0b10000000


    fun positionFromKey(decoded: ByteArray): Array<Array<Int>> {
        val anBoard: Array<Array<Int>> = Array(2) { Array(25) { 0 } }

        var i: Int = 0
        var j: Int = 0

        for (a in 0..decoded.size - 1) {
            var cur: Int = decoded[a].unsigned()
            var k = 0
            while (k < 8) {
                if (cur and 0x1 != 0) {
                    anBoard[i][j] = anBoard[i][j] + 1
                } else {
                    j += 1
                    if (j == 25) {
                        i += 1
                        j = 0
                    }
                }
                cur = cur shr 1
                k += 1
            }
        }

        return anBoard
    }

}