package controllers

import models.Budget
import utils.Utilities.formatListString

class BudgetAPI {
    private var budgets = ArrayList<Budget>()

    fun findBudget(budgetID : Int) =  budgets.find{ budget -> budget.budgetID == budgetID }
    fun numberOfBudgets(): Int{
        return budgets.size
    }

    fun numberOfExpiredBudgets(): Int = budgets.count { budget: Budget -> !budget.isBudgetExpired }

    private var lastId = 0
    private fun getId() = lastId++

    fun add(budget: Budget): Boolean {
        budget.budgetID = getId()
        return budgets.add(budget)
    }

    fun listAllBudgets() =
        if (budgets.isEmpty()) "There is currently no budgets stored!"
        else formatListString(budgets)
}