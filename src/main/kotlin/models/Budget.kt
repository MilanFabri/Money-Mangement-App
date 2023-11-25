package models

data class Budget(var budgetID: Int,
                  var budgetTitle: String,
                  var allocatedAmount: Int,
                  var isBudgetExpired: Boolean = false,
                  var entries: MutableSet<Entry> = mutableSetOf()){

    private var lastEntryId = 0
    private fun getEntryId() = lastEntryId++

    fun addEntry(entry: Entry): Boolean {
        entry.entryID = getEntryId()
        return entries.add(entry)
    }
}
