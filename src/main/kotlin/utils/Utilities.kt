package utils

import models.Budget
import models.Entry

object Utilities {

    @JvmStatic
    fun formatListString(budgetsToFormat: ArrayList<Budget>): String =
        budgetsToFormat
            .joinToString(separator = "\n ") { budget -> "$budget" }

    @JvmStatic
    fun formatSetString(entriesToFormat: Set<Entry>): String =
        entriesToFormat
            .joinToString(separator = "\n") { entry -> "\t$entry" }
}
