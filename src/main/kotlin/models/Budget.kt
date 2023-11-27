package models

import controllers.BudgetAPI
import utils.Utilities
import models.Entry

data class Budget(var budgetID: Int,
                  var budgetTitle: String,
                  var allocatedAmount: Int,
                  var isBudgetClosed: Boolean = false,
                  var entries: MutableSet<Entry> = mutableSetOf()){

    private var lastEntryId = 1
    private fun getEntryId() = lastEntryId++

    fun numberOfEntries() = entries.size

    fun findOne(id: Int): Entry?{
        return entries.find{ entry -> entry.entryID == id }
    }

    fun addEntry(entry: Entry): Boolean {
        entry.entryID = getEntryId()
        return entries.add(entry)
    }

    fun deleteEntry(id: Int): Boolean {
        return entries.removeIf { entry -> entry.entryID == id}
    }

    fun listEntries() =
        if (entries.isEmpty())  "\tThere is currently no entries added!"
        else  Utilities.formatSetString(entries)

    fun updateEntry(id: Int, newEntry: Entry): Boolean {
        val foundEntry = findOne(id)

        if (foundEntry != null){
            foundEntry.entryDesc = newEntry.entryDesc
            foundEntry.location = newEntry.location
            foundEntry.dateSpent = newEntry.dateSpent
            foundEntry.amountSpent = newEntry.amountSpent
            foundEntry.transactionType = newEntry.transactionType
            return true
        }

        return false
    }
}
