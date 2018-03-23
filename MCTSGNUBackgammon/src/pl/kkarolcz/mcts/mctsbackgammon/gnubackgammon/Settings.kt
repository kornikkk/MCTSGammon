package pl.kkarolcz.mcts.mctsbackgammon.gnubackgammon

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


/**
 * Created by kkarolcz on 22.03.2018.
 */
object Settings {
    private const val FILE_NAME = "gnubg.properties"

    private const val GNU_BACKGAMMON_BINARY = "gnubgbinpath"

    private val properties = Properties()

    var gnuBackgammonBinary: String?
        get() = properties.getProperty(GNU_BACKGAMMON_BINARY)
        set(value) {
            properties.setProperty(GNU_BACKGAMMON_BINARY, value)
            save()
        }

    init {
        load()
    }

    private fun load() {
        val propertiesFile = File(FILE_NAME)
        if (!propertiesFile.exists()) {
            propertiesFile.createNewFile()
        }
        FileInputStream(propertiesFile).use {
            properties.load(it)
        }
    }

    private fun save() {
        FileOutputStream(FILE_NAME).use {
            properties.store(it, "MCTS for GNU Backgammon properties")
        }
    }
}