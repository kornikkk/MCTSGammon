package pl.kkarolcz.utils

/**
 * Created by kkarolcz on 10.08.2017.
 */
inline fun <reified INNER> array2d(rows: Int, columns: Int, noinline innerInit: (Int) -> INNER): Array<Array<INNER>>
        = Array(rows) { Array(columns, innerInit) }

inline fun <reified T> Array<Array<T>>.copyOf(): Array<Array<T>> {
    val copy = arrayOfNulls<Array<T>>(size)
    this.forEachIndexed { rowIndex, row ->
        val clonedRow = row.copyOf()
        copy[rowIndex] = clonedRow
    }
    @Suppress("UNCHECKED_CAST") // Copied table is already filled and we can safely cast that to not nullable
    return copy as Array<Array<T>>
}