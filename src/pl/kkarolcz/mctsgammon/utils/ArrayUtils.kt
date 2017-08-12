package pl.kkarolcz.mctsgammon.utils

/**
 * Created by kkarolcz on 10.08.2017.
 */
inline fun <reified INNER> array2d(rows: Int, columns: Int, noinline innerInit: (Int) -> INNER): Array<Array<INNER>>
        = Array(rows) { Array(columns, innerInit) }

inline fun <reified T> Array<Array<T>>.deepCopy(): Array<Array<T>> {
    val copy = arrayOfNulls<Array<T?>>(size)
    this.forEachIndexed { rowIndex, row ->
        val clonedRow = arrayOfNulls<T>(row.size)
        copy[rowIndex] = clonedRow

        row.forEachIndexed { columnIndex, value ->
            clonedRow[columnIndex] = value
        }
    }
    @Suppress("UNCHECKED_CAST") // Copied table is already filled and we can safely cast that to not nullable
    return copy as Array<Array<T>>
}