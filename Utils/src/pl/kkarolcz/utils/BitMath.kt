package pl.kkarolcz.utils


/**
 * Created by kkarolcz on 06.12.2017.
 */
fun setBit(bits: Int, position: Number): Int = bits or (1 shl position.toInt())

fun clearBit(bits: Int, position: Number) = bits and (1 shl position.toInt()).inv()

/**
 * @param maxPosition maximum position of the bit counting from the least significant
 * @return Iterator of set bits positions
 */
fun setBitsFromMostSignificant(bits: Int): List<Int> {
    val positions = mutableListOf<Int>()

    var mask = bits
    var currentPosition = 0
    while (mask != 0) {
        if (mask and 1 == 1) {
            positions.add(currentPosition)
        }
        ++currentPosition
        mask = mask ushr 1
    }

    return positions
}
//
//    val startPositionInt = startPosition.toInt()
//    var currentPosition = startPositionInt
//
//    var i = 0
//    while (currentPosition >= startPositionInt) {
//        if (bits shr currentPosition and 1 == 1) {
//            positions[i] = currentPosition
//            ++i
//        }
//        --currentPosition
//    }
//    return positions
//}