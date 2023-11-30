package controllers

import models.Budget
import models.Entry
import utils.Utilities.formatListString
import persistence.Serializer
import utils.Utilities

class BudgetAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var budgets = ArrayList<Budget>()

    fun findBudget(budgetID: Int) = budgets.find { budget -> budget.budgetID == budgetID }
    fun numberOfBudgets() = budgets.size
    fun numberOfClosedBudgets(): Int = budgets.count { budget: Budget -> budget.isBudgetClosed }
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

    @Throws(Exception::class)
    fun load() {
        budgets = serializer.read() as ArrayList<Budget>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(budgets)
    }
}
