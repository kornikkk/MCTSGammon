package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon.gui

import java.awt.Component
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JFileChooser.*
import javax.swing.filechooser.FileFilter


/**
 * Created by kkarolcz on 22.03.2018.
 */
object GNUBackgammonBinaryFileChooser {

    fun chooseFile(parent: Component): File {
        val fileChooser = JFileChooser()
        fileChooser.fileFilter = GNUBackgammonBinaryFilter
        when (fileChooser.showOpenDialog(parent)) {
            CANCEL_OPTION -> System.exit(0)
            ERROR_OPTION -> System.exit(0)
            APPROVE_OPTION -> {
                return fileChooser.selectedFile
            }
        }
        throw IllegalStateException()
    }

    object GNUBackgammonBinaryFilter : FileFilter() {
        override fun getDescription(): String = "GNU Backgammon CommandBuilder Line Interface (gnubg-cli.exe)"

        override fun accept(file: File): Boolean = when (file.isDirectory) {
            true -> true
            else -> file.name == "gnubg-cli.exe"
        }
    }
}