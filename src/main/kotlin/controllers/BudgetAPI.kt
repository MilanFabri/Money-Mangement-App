package controllers

import models.Budget
import utils.Utilities.formatListString
import utils.Utilities.isValidListIndex

class BudgetAPI {
    private var budgets = ArrayList<Budget>()

    fun findBudget(budgetID : Int) =  budgets.find{ budget -> budget.budgetID == budgetID }
    fun numberOfBudgets(): Int{
        return budgets.size
    }

    fun numberOfExpiredBudgets(): Int = budgets.count { budget: Budget -> !budget.isBudgetExpired }

    private var lastId = 0
    private fun getId() = lastId++

    fun addBudget(budget: Budget): Boolean {
        budget.budgetID = getId()
        return budgets.add(budget)
    }

    fun deleteBudget(id: Int) = budgets.removeIf { budget -> budget.budgetID == id }

    fun isValidIndex(id: Int): Boolean {
        return isValidListIndex(id, budgets)
    }


    fun listAllBudgets() =
        if (budgets.isEmpty()) "There is currently no budgets stored!"
        else formatListString(budgets)
}