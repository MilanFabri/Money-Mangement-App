package models

import utils.Utilities

data class Budget(var budgetID: Int,
                  var budgetTitle: String,
                  var allocatedAmount: Int,
                  var isBudgetExpired: Boolean = false,
                  var entries: MutableSet<Entry> = mutableSetOf()){

    private var lastEntryId = 1
    private fun getEntryId() = lastEntryId++

    fun numberOfEntries() = entries.size

    fun findOne(id: Int): Entry?{
        return entries.find{ item -> item.entryID == id }
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
}
