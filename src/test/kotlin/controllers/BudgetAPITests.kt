package controllers

import models.Budget
import models.Entry
import org.junit.jupiter.api.*
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
class BudgetAPITests {

    private var holidayBudget: Budget? = null
    private var groceries: Budget? = null
    private var weeklyBudget: Budget? = null
    private var firstEntry: Entry? = null
    private var secondEntry: Entry? = null
    private var thirdEntry: Entry? = null
    private var populatedBudgets: BudgetAPI? = BudgetAPI(XMLSerializer(File("budgets.xml")))
    private var populatedEntry: Budget? = Budget(1, "testing", 100)
    private var emptyBudgets: BudgetAPI? = BudgetAPI(XMLSerializer(File("budgets.xml")))

    @BeforeEach
    fun setup() {
        holidayBudget = Budget(1, "Holiday Budget", 1000, false)
        groceries = Budget(2, "Groceries", 100, false)
        weeklyBudget = Budget(3, "Weekly Budget", 200, false)

        firstEntry = Entry(1, "Entry One", "Online", "1/12/23", 1000, "Card")
        secondEntry = Entry(2, "Entry Two", "Tramore", "2/12/23", 100, "Cash")
        thirdEntry = Entry(3, "Entry Three", "Dublin", "3/12/23", 225, "Card")

        populatedBudgets!!.addBudget(holidayBudget!!)
        populatedBudgets!!.addBudget(groceries!!)
        populatedBudgets!!.addBudget(weeklyBudget!!)

        populatedEntry!!.addEntry(firstEntry!!)
        populatedEntry!!.addEntry(secondEntry!!)
        populatedEntry!!.addEntry(thirdEntry!!)
    }

    @AfterEach
    fun tearDown() {
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
    inner class UpdatingBudgets {
        @Test
        fun `updating a budget that does not exist returns false`() {
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
            val newEntry = Entry(1, "Entry One", "Online", "1/12/23", 53, "Card")
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

    @Nested
    inner class UpdatingEntries {
        @Test
        fun `updating a Entry that does not exist returns false`() {
            assertFalse(populatedEntry!!.updateEntry(6, Entry(1, "Entry One Update", "Online", "1/12/23", 53, "Card")))
            assertFalse(populatedEntry!!.updateEntry(-1, Entry(1, "Entry Two Update", "Tramore", "2/12/23", 163, "Card")))
        }

        @Test
        fun `updating a Entry that exists returns true and updates`() {
            assertEquals(firstEntry, populatedEntry!!.findOne(1))
            assertEquals("Entry One", populatedEntry!!.findOne(1)!!.entryDesc)

            assertTrue(populatedEntry!!.updateEntry(1, Entry(1, "New Entry One", "Waterford", "1/12/23", 150, "Cash")))
            assertEquals("New Entry One", populatedEntry!!.findOne(1)!!.entryDesc)
        }
    }

    @Nested
    inner class ListActiveBudgets {
        @Test
        fun `listActiveBudgets returns no active budgets stored when ArrayList is empty`() {
            assertEquals(0, emptyBudgets!!.numberOfActiveBudgets())
            assertTrue(
                emptyBudgets!!.listActiveBudgets().contains("There is currently no active budgets stored!")
            )
        }

        @Test
        fun `listActiveBudgets returns active budgets when ArrayList has active budgets stored`() {
            assertEquals(3, populatedBudgets!!.numberOfActiveBudgets())
            holidayBudget = Budget(1, "Holiday Budget", 1000, false)
            groceries = Budget(2, "Groceries", 100, false)
            weeklyBudget = Budget(3, "Weekly Budget", 200, false)
        }
    }

    @Nested
    inner class ListClosedBudgets{

        @Test
        fun `listClosedBudgets returns no closed budgets when ArrayList is empty`() {
            assertEquals(0, emptyBudgets!!.numberOfClosedBudgets())
            assertTrue(
                emptyBudgets!!.listClosedBudgets().contains("There is currently no closed budgets stored!")
            )
        }

        @Test
        fun `listClosedBudgets returns close budgets when ArrayList has closed budgets stored`() {
            assertEquals(0, populatedBudgets!!.numberOfClosedBudgets())
            holidayBudget = Budget(1, "Holiday Budget", 1000, false)
            groceries = Budget(2, "Groceries", 100, false)
            weeklyBudget = Budget(3, "Weekly Budget", 200, false)
        }
    }

    @Nested
    inner class CloseBudgets {
        @Test
        fun `closing a Budget that does not exist, returns null`() {
            assertFalse(populatedBudgets!!.closeBudget(-5))
            assertFalse(populatedBudgets!!.closeBudget(-3))
            assertFalse(populatedBudgets!!.closeBudget(-1))
        }

        @Test
        fun `closing a Budget that exists closes and returns closed object`() {
            assertTrue(populatedBudgets!!.closeBudget(1))
            assertEquals(1, populatedBudgets!!.numberOfClosedBudgets())
        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            val storingBudgets = BudgetAPI(XMLSerializer(File("budgets.xml")))
            storingBudgets.store()

            val loadedBudgets = BudgetAPI(XMLSerializer(File("budgets.xml")))
            loadedBudgets.load()

            Assertions.assertEquals(0, storingBudgets.numberOfBudgets())
            Assertions.assertEquals(0, loadedBudgets.numberOfBudgets())
            assertEquals(storingBudgets.numberOfBudgets(), loadedBudgets.numberOfBudgets())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            val storingBudgets = BudgetAPI(XMLSerializer(File("budgets.xml")))
            storingBudgets.addBudget(holidayBudget!!)
            storingBudgets.addBudget(groceries!!)
            storingBudgets.addBudget(weeklyBudget!!)
            storingBudgets.store()

            val loadedBudgets = BudgetAPI(XMLSerializer(File("budgets.xml")))
            loadedBudgets.load()

            Assertions.assertEquals(3, storingBudgets.numberOfBudgets())
            Assertions.assertEquals(3, loadedBudgets.numberOfBudgets())
            assertEquals(storingBudgets.numberOfBudgets(), loadedBudgets.numberOfBudgets())
            assertEquals(storingBudgets.findBudget(1), loadedBudgets.findBudget(1))
            assertEquals(storingBudgets.findBudget(2), loadedBudgets.findBudget(2))
            assertEquals(storingBudgets.findBudget(3), loadedBudgets.findBudget(3))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            val storingBudgets = BudgetAPI(JSONSerializer(File("budgets.json")))
            storingBudgets.store()

            val loadedBudgets = BudgetAPI(JSONSerializer(File("budgets.json")))
            loadedBudgets.load()

            Assertions.assertEquals(0, storingBudgets.numberOfBudgets())
            Assertions.assertEquals(0, loadedBudgets.numberOfBudgets())
            assertEquals(storingBudgets.numberOfBudgets(), loadedBudgets.numberOfBudgets())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            val storingBudgets = BudgetAPI(JSONSerializer(File("budgets.json")))
            storingBudgets.addBudget(holidayBudget!!)
            storingBudgets.addBudget(groceries!!)
            storingBudgets.addBudget(weeklyBudget!!)
            storingBudgets.store()

            val loadedBudgets = BudgetAPI(JSONSerializer(File("budgets.json")))
            loadedBudgets.load()

            Assertions.assertEquals(3, storingBudgets.numberOfBudgets())
            Assertions.assertEquals(3, loadedBudgets.numberOfBudgets())
            assertEquals(storingBudgets.numberOfBudgets(), loadedBudgets.numberOfBudgets())
            assertEquals(storingBudgets.findBudget(1), loadedBudgets.findBudget(1))
            assertEquals(storingBudgets.findBudget(2), loadedBudgets.findBudget(2))
            assertEquals(storingBudgets.findBudget(3), loadedBudgets.findBudget(3))
        }
    }


}
