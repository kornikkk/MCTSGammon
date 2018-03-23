package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui.utils

/**
 * Created by kkarolcz on 22.03.2018.
 */
import java.awt.EventQueue
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*
import javax.swing.JTextArea

class TextAreaOutputStream constructor(textArea: JTextArea, maxLines: Int = 1000) : OutputStream() {

    private val oneByte: ByteArray = ByteArray(1)
    private var appender: Appender? = Appender(textArea, maxLines)

    init {
        if (maxLines < 1) {
            throw IllegalArgumentException("TextAreaOutputStream maximum lines must be positive (value=$maxLines)")
        }
    }

    /** Clear the current console text area. */
    @Synchronized
    fun clear() {
        appender?.clear()
    }

    @Synchronized
    override fun close() {
        appender = null
    }

    @Synchronized
    override fun flush() {
    }

    @Synchronized
    override fun write(value: Int) {
        oneByte[0] = value.toByte()
        write(oneByte, 0, 1)
    }

    @Synchronized
    override fun write(ba: ByteArray) {
        write(ba, 0, ba.size)
    }

    @Synchronized
    override fun write(ba: ByteArray, str: Int, len: Int) {
        appender?.append(bytesToString(ba, str, len))
    }

    private fun bytesToString(ba: ByteArray, str: Int, len: Int) = String(ba, str, len, Charset.defaultCharset())


    internal class Appender(private val textArea: JTextArea, private val maxLines: Int) : Runnable {
        private val lengths = LinkedList<Int>()
        private val values = ArrayList<String>()

        private var currentLength = 0
        private var clear = false
        private var queue = true

        companion object {
            private const val EOL1 = "\n"
            private val EOL2 = System.getProperty("line.separator", EOL1)
        }

        @Synchronized
        fun append(value: String) {
            values.add(value)
            if (queue) {
                queue = false
                EventQueue.invokeLater(this)
            }
        }

        @Synchronized
        fun clear() {
            clear = true
            currentLength = 0
            lengths.clear()
            values.clear()
            if (queue) {
                queue = false
                EventQueue.invokeLater(this)
            }
        }

        @Synchronized
        override fun run() {
            if (clear) {
                textArea.text = ""
            }
            for (value in values) {
                currentLength += value.length
                if (value.endsWith(EOL1) || value.endsWith(EOL2)) {
                    if (lengths.size >= maxLines) {
                        textArea.replaceRange("", 0, lengths.removeFirst())
                    }
                    lengths.addLast(currentLength)
                    currentLength = 0
                }
                textArea.append(value)
            }
            values.clear()
            clear = false
            queue = true
        }

    }

}