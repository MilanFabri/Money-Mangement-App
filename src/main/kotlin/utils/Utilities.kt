package utils

import models.Budget

object Utilities {

    @JvmStatic
    fun formatListString(notesToFormat: List<Budget>): String =
        notesToFormat
            .joinToString(separator = "\n ") { budget ->  "$budget" }
}