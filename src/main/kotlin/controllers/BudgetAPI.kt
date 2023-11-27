package controllers

import models.Budget
import utils.Utilities.formatListString

class BudgetAPI {
    private var budgets = ArrayList<Budget>()

    fun findBudget(budgetID : Int) =  budgets.find{ budget -> budget.budgetID == budgetID }
    fun numberOfBudgets(): Int{
        return budgets.size
    }

    fun numberOfClosedBudgets(): Int = budgets.count { budget: Budget -> !budget.isBudgetClosed }

    private var lastId = 1
    private fun getId() = lastId++

    fun addBudget(budget: Budget): Boolean {
        budget.budgetID = getId()
        return budgets.add(budget)
    }

    fun deleteBudget(id: Int) = budgets.removeIf { budget -> budget.budgetID == id }

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
        if (budgets.isEmpty()) "There is currently no budgets stored!"
        else formatListString(budgets)
}