import models.Budget
import mu.KotlinLogging
import utils.ScannerInput
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
    logger.info { "updateBudget() function invoked" }
}

fun deleteBudget(){
    logger.info { "deleteBudget() function invoked" }
}

fun exitApp(){
    logger.info { "exitApp() function invoked" }
    exit(0)
}
