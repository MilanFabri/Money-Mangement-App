package controllers

import models.Budget
import models.Entry
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
class BudgetAPITests {

    private var holidayBudget: Budget? = null
    private var groceries: Budget? = null
    private var weeklyBudget: Budget? = null
    private var firstEntry: Entry? = null
    private var secondEntry: Entry? = null
    private var thirdEntry: Entry? = null
    private var populatedBudgets: BudgetAPI? = BudgetAPI()
    private var populatedEntry: Budget? = Budget(1, "testing", 100)
    private var Entries: Budget? = null
    private var emptyBudgets: BudgetAPI? = BudgetAPI()

    @BeforeEach
    fun setup() {
        holidayBudget = Budget(1, "Holiday Budget", 1000, false)
        groceries = Budget(2, "Groceries", 100, false)
        weeklyBudget = Budget(3, "Weekly Budget", 200, false)

        firstEntry = Entry(1, "Entry One", "Online", 23, 53, "Card")
        secondEntry = Entry(2, "Entry Two", "Tramore", 20, 100, "Cash")
        thirdEntry = Entry(3, "Entry Three", "Dublin", 10, 225, "Card")

        populatedBudgets!!.addBudget(holidayBudget!!)
        populatedBudgets!!.addBudget(groceries!!)
        populatedBudgets!!.addBudget(weeklyBudget!!)

        populatedEntry!!.addEntry(firstEntry!!)
        populatedEntry!!.addEntry(secondEntry!!)
        populatedEntry!!.addEntry(thirdEntry!!)
    }

    @AfterEach
    fun tearDown(){
        holidayBudget = null
        groceries = null
        weeklyBudget = null
        populatedBudgets = null
        emptyBudgets = null
        populatedEntry = null
    }

    @Nested
    inner class AddingBudgets {
        @Test
        fun `adding a Budget to a populated list adds to ArrayList`() {
            val newBudget = Budget(1, "Holiday Budget", 1000, false)
            assertEquals(3, populatedBudgets!!.numberOfBudgets())
            assertTrue(populatedBudgets!!.addBudget(newBudget))
            assertEquals(4, populatedBudgets!!.numberOfBudgets())
            assertEquals(newBudget, populatedBudgets!!.findBudget(populatedBudgets!!.numberOfBudgets() - 0))
        }

        @Test
        fun `adding a Budget to an empty list adds to ArrayList`() {
            val newBudget = Budget(1, "Holiday Budget", 1000, false)
            assertEquals(0, emptyBudgets!!.numberOfBudgets())
            assertTrue(emptyBudgets!!.addBudget(newBudget))
            assertEquals(1, emptyBudgets!!.numberOfBudgets())
            assertEquals(newBudget, emptyBudgets!!.findBudget(emptyBudgets!!.numberOfBudgets() - 0))
        }
    }

    @Nested
    inner class ListingBudgets {
        @Test
        fun `listAllBudgets returns No Budgets Stored message when ArrayList is empty`() {
            assertEquals(0, emptyBudgets!!.numberOfBudgets())
            assertTrue(emptyBudgets!!.listAllBudgets().lowercase().contains("no budgets"))
        }

        @Test
        fun `listAllBudgets returns Budgets when ArrayList has Budgets stored`() {
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
        fun `deleting a Budget that exists delete and returns deleted object`() {
            assertEquals(3, populatedBudgets!!.numberOfBudgets())
            assertTrue(populatedBudgets!!.deleteBudget(1))
            assertEquals(2, populatedBudgets!!.numberOfBudgets())
            assertTrue(populatedBudgets!!.deleteBudget(2))
            assertEquals(1, populatedBudgets!!.numberOfBudgets())
        }
    }

    @Nested
    inner class UpdatingBudgets{
        @Test
        fun `updating a budget that does not exist returns false`(){
            assertFalse(populatedBudgets!!.updateBudget(6, Budget(6, "Updating Budget", 2000, false)))
            assertFalse(populatedBudgets!!.updateBudget(-1, Budget(-1, "Updating Budget", 500, false)))
            assertFalse(emptyBudgets!!.updateBudget(0, Budget(0, "Updating Budget", 100, false)))
        }

        @Test
        fun `updating a budget that exists returns true and updates`() {
            assertEquals(holidayBudget, populatedBudgets!!.findBudget(1))
            assertEquals("Holiday Budget", populatedBudgets!!.findBudget(1)!!.budgetTitle)
            assertEquals(1000, populatedBudgets!!.findBudget(1)!!.allocatedAmount)

            assertTrue(populatedBudgets!!.updateBudget(1, Budget(1, "Updating Budget", 2000, false)))
            assertEquals("Updating Budget", populatedBudgets!!.findBudget(1)!!.budgetTitle)
            assertEquals(2000, populatedBudgets!!.findBudget(1)!!.allocatedAmount)
        }
    }

    @Nested
    inner class AddingEntries {
        @Test
        fun `adding an Entry`() {
            val newEntry = Entry(1, "Entry One", "Online", 23, 53, "Card")
            assertEquals(3, populatedEntry!!.numberOfEntries())
            assertTrue(populatedEntry!!.addEntry(newEntry))
            assertEquals(4, populatedEntry!!.numberOfEntries())
            assertEquals(newEntry, populatedEntry!!.findOne(populatedEntry!!.numberOfEntries() - 0))
        }
    }

    @Nested
    inner class DeletingEntries {
        @Test
        fun `deleting a Entry that does not exist, returns null`() {
            assertFalse(populatedEntry!!.deleteEntry(-5))
            assertFalse(populatedEntry!!.deleteEntry(-3))
            assertFalse(populatedEntry!!.deleteEntry(-1))
        }

        @Test
        fun `deleting a Entry that exists delete and returns deleted object`() {
            assertEquals(3, populatedEntry!!.numberOfEntries())
            assertTrue(populatedEntry!!.deleteEntry(1))
            assertEquals(2, populatedEntry!!.numberOfEntries())
            assertTrue(populatedEntry!!.deleteEntry(2))
            assertEquals(1, populatedEntry!!.numberOfEntries())
        }
    }
}
