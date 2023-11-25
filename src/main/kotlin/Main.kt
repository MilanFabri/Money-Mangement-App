import models.Budget
import models.Entry
import mu.KotlinLogging
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
private val budgetAPI = controllers.BudgetAPI()

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |       Money Management App     |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Create a Budget           |
         > |   2) List all Budgets          |
         > |   3) Update a Budget           |
         > |   4) Delete a Budget           |
         > |   5) Add Entry to Budget       |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> createBudget()
            2  -> listBudgets()
            3  -> updateBudget()
            4  -> deleteBudget()
            5  -> addEntry()
            0  -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun createBudget(){
    //logger.info { "createBudget() function invoked" }
    val budgetID = ScannerInput.readNextInt("Enter ID for your Budget: ")
    val budgetTitle = ScannerInput.readNextLine("Enter the Title for your Budget: ")
    val allocatedAmount = ScannerInput.readNextInt("Enter allocated amount to spend for your Budget: ")

    val isAdded = budgetAPI.add(Budget(budgetID = budgetID, budgetTitle = budgetTitle, allocatedAmount = allocatedAmount))

    if (isAdded) {
        println("Budget Created Successfully!")
    } else {
        println("Budget Creation Failed! Try again")
    }
}

fun listBudgets(){
    //logger.info { "listBudgets() function invoked" }
    println(budgetAPI.listAllBudgets())
}

fun updateBudget(){
    //logger.info { "updateBudget() function invoked" }
}

fun deleteBudget(){
    //logger.info { "deleteBudget() function invoked" }
}

fun addEntry(){
    //logger.info { "addEntry() function invoked" }
    val budget: Budget? = askUserToChooseBudget()
    if (budget != null) {
        budget.addEntry(Entry(entryID = readNextInt("\t Budget ID: "),
            entryDesc = readNextLine ("\t Entry Description: "),
            location = readNextLine("\t Location Spent: "),
            dateSpent = readNextInt("\t Date Spent: "),
            amountSpent = readNextInt("\t Amount Spent: "),
            transactionType = readNextLine("\t How did you pay? :")))
    }
}

fun exitApp(){
    //logger.info { "exitApp() function invoked" }
    exit(0)
}

private fun askUserToChooseBudget(): Budget? {
    budgetAPI.listAllBudgets()
    if (budgetAPI.numberOfExpiredBudgets() > 0) {
        val budget = budgetAPI.findBudget(readNextInt("\nEnter the ID of the budget you wish to add an Entry: "))
        if (budget != null) {
            if (budget.isBudgetExpired) {
                println("This budget has already expired")
            } else {
                return budget
            }
        } else {
            println("The budget with that ID is not valid! Try again")
        }
    }
    return null
}
