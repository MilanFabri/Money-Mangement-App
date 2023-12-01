package utils

import models.Entry
import utils.ScannerInput.readNextLine
import java.util.Scanner

object Validate {

    @JvmStatic
    fun isValidDate(prompt: String?): String {
        var input = readNextLine(prompt)
        do {
            if (Utilities.validDate(input))
                return input
            else {
                print("┃ Invalid date entered: $input. Please enter a valid date e.g. 1/12/23 \n")
                input = readNextLine(prompt)
            }
        } while (true)
    }
}