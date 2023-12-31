package controllers

import models.Budget
import models.Entry
import utils.Utilities.formatListString
import persistence.Serializer

class BudgetAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var budgets = ArrayList<Budget>()

    fun findBudget(budgetID: Int) = budgets.find { budget -> budget.budgetID == budgetID }
    fun numberOfBudgets() = budgets.size
    fun numberOfClosedBudgets(): Int = budgets.count { budget: Budget -> budget.isBudgetClosed }
    fun numberOfFullBudgets(): Int = budgets.count { budget: Budget -> budget.isBudgetFull }
    fun numberOfActiveBudgets(): Int = budgets.count { budget: Budget -> !budget.isBudgetClosed }

    private var lastId = 1
    private fun getId() = lastId++

    fun addBudget(budget: Budget): Boolean {
        budget.budgetID = getId()
        return budgets.add(budget)
    }

    fun deleteBudget(id: Int) = budgets.removeIf { budget -> budget.budgetID == id }

    fun closeBudget(id: Int): Boolean {
        val foundBudget = findBudget(id)
        if ((foundBudget != null) && (!foundBudget.isBudgetClosed)) {
            foundBudget.isBudgetClosed = true
            return true
        }
        return false
    }

    fun autoCloseBudget() {
        for (budget in budgets) {
            for (entry in budget.entries) {
                val totalSpent = budget.entries.sumOf { it.amountSpent }
                if (totalSpent >= budget.allocatedAmount) {
                    budget.isBudgetClosed = true
                }
            }
        }
    }

    fun updateBudget(id: Int, budget: Budget?): Boolean {
        val foundBudget = findBudget(id)

        if ((foundBudget != null) && (budget != null)) {
            foundBudget.budgetID = budget.budgetID
            foundBudget.budgetTitle = budget.budgetTitle
            foundBudget.allocatedAmount = budget.allocatedAmount
            return true
        }

        return false
    }

    fun listAllBudgets() =
        if (budgets.isEmpty()) {
            "┃ There is currently no budgets stored!"
        } else {
            formatListString(budgets)
        }

    fun listActiveBudgets() =
        if (numberOfActiveBudgets() == 0) "┃ There is currently no active budgets stored!"
        else formatListString(budgets.filter { budget -> !budget.isBudgetClosed })

    fun listClosedBudgets() =
        if (numberOfClosedBudgets() == 0) "┃ There is currently no closed budgets stored!"
        else formatListString(budgets.filter { budget -> budget.isBudgetClosed })

    fun listByMostSpent(): String =
        if (numberOfActiveBudgets() == 0) "┃ There is currently no active budgets stored!"
        else {
            var listByMostSpent = ""
            for (budget in budgets) {
                for (entry in budget.entries) {
                    listByMostSpent += "┃ ${budget.budgetTitle}\n┃ ${budget.entries.sortedByDescending { it.amountSpent }}\n"
                }
            }
            listByMostSpent
        }

    fun fullBudget() {
        for (budget in budgets) {
            for (entry in budget.entries) {
                val totalSpent = budget.entries.sumOf { it.amountSpent }
                if (totalSpent >= budget.allocatedAmount) {
                    budget.isBudgetFull = true
                }
            }
        }
    }

    fun listFullBudgets() =
        if (numberOfFullBudgets() == 0) "┃ There is currently no full budgets stored!"
        else formatListString(budgets.filter { budget -> budget.isBudgetFull })

    fun searchByTitle (searchString: String) =
        formatListString(
            budgets.filter { budget -> budget.budgetTitle.contains(searchString, ignoreCase = true) })

    fun searchEntriesByLocation(searchString: String): String {
        return if (numberOfBudgets() == 0) "┃ There is currently no active budgets stored!"
        else {
            var listOfEntries = ""
            for (budget in budgets) {
                for (entry in budget.entries) {
                    if (entry.location.contains(searchString, ignoreCase = true)) {
                        listOfEntries += "${budget.budgetTitle}: \n\t${entry}\n"
                    }
                }
            }
            if (listOfEntries == "") "┃ There is currently no entry with this location: $searchString"
            else listOfEntries
        }
    }

    fun budgetOverview(): String =
        if (numberOfActiveBudgets() == 0) "┃ There is currently no active budgets stored!"
        else {
            var budgetOverview = ""
            for (budget in budgets) {
                budgetOverview += "┃ ${budget.budgetTitle}\n┃ Number of Transactions: ${budget.entries.size}\n" +
                                  "┃ Total Amount Spent: €${budget.entries.sumOf { it.amountSpent }}\n" +
                                  "┃ Highest Amount Spent in One Transaction: €${budget.entries.maxOf { it.amountSpent }}\n"+
                                  "\n"

                }
            budgetOverview
        }



    @Throws(Exception::class)
    fun load() {
        budgets = serializer.read() as ArrayList<Budget>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(budgets)
    }
}
