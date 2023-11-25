package controllers

import models.Budget
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
class BudgetAPITests {

    private var holidayBudget: Budget? = null
    private var groceries: Budget? = null
    private var weeklyBudget: Budget? = null
    private var populatedBudgets: BudgetAPI? = BudgetAPI()
    private var emptyBudgets: BudgetAPI? = BudgetAPI()

    @BeforeEach
    fun setup() {
        holidayBudget = Budget(1, "Holiday Budget", 1000, false)
        groceries = Budget(2, "Groceries", 100, false)
        weeklyBudget = Budget(3, "Weekly Budget", 200, false)

        //adding 5 Note to the notes api
        populatedBudgets!!.addBudget(holidayBudget!!)
        populatedBudgets!!.addBudget(groceries!!)
        populatedBudgets!!.addBudget(weeklyBudget!!)

    }

    @AfterEach
    fun tearDown(){
        holidayBudget = null
        groceries = null
        weeklyBudget = null
        populatedBudgets = null
        emptyBudgets = null
    }

    @Nested
    inner class AddingBudgets {
        @Test
        fun `adding a Budget to a populated list adds to ArrayList`() {
            val newBudget = Budget(1, "Holiday Budget", 1000, false)
            assertEquals(3, populatedBudgets!!.numberOfBudgets())
            assertTrue(populatedBudgets!!.addBudget(newBudget))
            assertEquals(4, populatedBudgets!!.numberOfBudgets())
            assertEquals(newBudget, populatedBudgets!!.findBudget(populatedBudgets!!.numberOfBudgets() - 1))
        }

        @Test
        fun `adding a Budget to an empty list adds to ArrayList`() {
            val newBudget = Budget(1, "Holiday Budget", 1000, false)
            assertEquals(0, emptyBudgets!!.numberOfBudgets())
            assertTrue(emptyBudgets!!.addBudget(newBudget))
            assertEquals(1, emptyBudgets!!.numberOfBudgets())
            assertEquals(newBudget, emptyBudgets!!.findBudget(emptyBudgets!!.numberOfBudgets() - 1))
        }
    }

    @Nested
    inner class ListingBudgets {
        @Test
        fun `listAllNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyBudgets!!.numberOfBudgets())
            assertTrue(emptyBudgets!!.listAllBudgets().lowercase().contains("no budgets"))
        }

        @Test
        fun `listAllNotes returns Notes when ArrayList has notes stored`() {
            assertEquals(3, populatedBudgets!!.numberOfBudgets())
            val budgetsString = populatedBudgets!!.listAllBudgets().lowercase()
            assertTrue(budgetsString.contains("holiday budget"))
            assertTrue(budgetsString.contains("groceries"))
            assertTrue(budgetsString.contains("weekly budget"))
        }
    }

    @Nested
    inner class DeletingBudgets {
        @Test
        fun `deleting a Budget that does not exist, returns null`() {
            assertFalse(populatedBudgets!!.deleteBudget(5))
            assertFalse(populatedBudgets!!.deleteBudget(-1))
            assertFalse(populatedBudgets!!.deleteBudget(5))
        }

        @Test
        fun `deleting a note that exists delete and returns deleted object`() {
            assertEquals(3, populatedBudgets!!.numberOfBudgets())
            assertTrue(populatedBudgets!!.deleteBudget(1))
            assertEquals(2, populatedBudgets!!.numberOfBudgets())
            assertTrue(populatedBudgets!!.deleteBudget(2))
            assertEquals(1, populatedBudgets!!.numberOfBudgets())
        }
    }
}
