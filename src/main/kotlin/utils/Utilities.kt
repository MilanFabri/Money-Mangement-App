package utils

import com.sun.org.apache.xpath.internal.operations.Bool
import models.Budget
import models.Entry

object Utilities {

    @JvmStatic
    fun formatListString(budgetsToFormat: List<Budget>): String =
        budgetsToFormat
            .joinToString(separator = "\n") { budget -> "$budget" }

    @JvmStatic
    fun formatSetString(entriesToFormat: Set<Entry>): String =
        entriesToFormat
            .joinToString(separator = "\n") { entry -> "\t$entry" }

    @JvmStatic
    fun validDate(dateToCheck: String): Boolean {
        if (dateToCheck.contains("/")) {
            return true
        }
        return false
    }
}


