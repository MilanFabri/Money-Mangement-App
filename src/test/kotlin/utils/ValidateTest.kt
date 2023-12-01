package utils

import models.Budget
import models.Entry
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFalse
import utils.Validate.isValidDate
import utils.Utilities
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ValidateTest {

    @Test
    fun `return false if dateSpent doesn't contain 'Slash'`(){
        val entryOne = Entry(1, "Entry One", "Online", "11223", 53, "Card")
        assertFalse(Utilities.validDate(entryOne.dateSpent))
    }

    @Test
    fun `return true if dateSpent contains 'Slash'`(){
        val entryTwo = Entry(1, "Entry One", "Online", "1/12/23", 53, "Card")
        assertTrue(Utilities.validDate(entryTwo.dateSpent))
    }
}