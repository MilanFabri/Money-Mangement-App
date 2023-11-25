package controllers

import models.Budget
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

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
        populatedBudgets!!.add(holidayBudget!!)
        populatedBudgets!!.add(groceries!!)
        populatedBudgets!!.add(weeklyBudget!!)

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
            assertTrue(populatedBudgets!!.add(newBudget))
            assertEquals(4, populatedBudgets!!.numberOfBudgets())
            assertEquals(newBudget, populatedBudgets!!.findBudget(populatedBudgets!!.numberOfBudgets() - 1))
        }

        @Test
        fun `adding a Budget to an empty list adds to ArrayList`() {
            val newBudget = Budget(1, "Holiday Budget", 1000, false)
            assertEquals(0, emptyBudgets!!.numberOfBudgets())
            assertTrue(emptyBudgets!!.add(newBudget))
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
}
