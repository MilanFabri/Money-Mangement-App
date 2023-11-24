package controllers

import models.Budget
import utils.Utilities.formatListString

class BudgetAPI {
    private var budgets = ArrayList<Budget>()

    private var lastId = 1
    private fun getId() = lastId++

    fun add(budget: Budget): Boolean {
        budget.budgetID = getId()
        return budgets.add(budget)
    }

    fun listAllBudgets() =
        if (budgets.isEmpty()) "There is currently no budgets stored!"
        else formatListString(budgets)
}