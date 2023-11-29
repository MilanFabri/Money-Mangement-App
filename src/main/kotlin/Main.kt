import models.Budget
import models.Entry
import mu.KotlinLogging
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.lang.System.exit
import persistence.XMLSerializer
import java.io.File
import controllers.BudgetAPI

private val logger = KotlinLogging.logger {}
private val budgetAPI = BudgetAPI(XMLSerializer(File("budgets.xml")))
//private val budgetAPI = BudgetAPI(JSONSerializer(File("budgets.json")))

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu(): Int {
    return ScannerInput.readNextInt(
        """ 
             > ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
             > ┃       Money Management App     ┃
             > ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
             > ┃ Budget Options                 ┃
             > ┃   1) Create a Budget           ┃
             > ┃   2) List all Budgets          ┃
             > ┃   3) Update a Budget           ┃
             > ┃   4) Delete a Budget           ┃
             > ┃   5) Close a Budget            ┃
             > ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
             > ┃ Entry Options                  ┃
             > ┃   6) Add Entry to Budget       ┃
             > ┃   7) Delete an Entry           ┃
             > ┃   8) Update an Entry           ┃
             > ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
             > ┃   9) Save                      ┃
             > ┃   10) Load                     ┃
             > ┃   0) Exit                      ┃
             > ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
             > ==>> """.trimMargin(">")
    )
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> createBudget()
            2 -> listBudgets()
            3 -> updateBudget()
            4 -> deleteBudget()
            5 -> closeBudget()
            6 -> addEntry()
            7 -> deleteEntry()
            8 -> updateEntry()
            9 -> save()
            10 -> load()
            0 -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

fun createBudget() {
    // logger.info { "createBudget() function invoked" }
    val budgetTitle = ScannerInput.readNextLine("Enter the Title for your Budget: ")
    val allocatedAmount = ScannerInput.readNextInt("Enter allocated amount to spend for your Budget: ")

    val isAdded =
        budgetAPI.addBudget(Budget(budgetID = 0, budgetTitle = budgetTitle, allocatedAmount = allocatedAmount))

    if (isAdded) {
        println("Budget was created Successfully!")
    } else {
        println("Budget Creation Failed! Try again")
    }
}

fun listBudgets() {
    // logger.info { "listBudgets() function invoked" }
    println(budgetAPI.listAllBudgets())
}

fun listActiveBudgets() {
    println(budgetAPI.listActiveBudgets())
}

fun updateBudget() {
    // logger.info { "updateBudget() function invoked" }
    listBudgets()
    if (budgetAPI.numberOfBudgets() > 0) {
        val id = readNextInt("Enter the ID of the budget you wish to Update: ")
        if (budgetAPI.findBudget(id) != null) {
            val budgetTitle = readNextLine("Enter a new Title for your budget: ")
            val allocatedAmount = readNextInt("Enter a new Allocated Amount for your budget: ")

            if (budgetAPI.updateBudget(id, Budget(0, budgetTitle, allocatedAmount))) {
                println("Budget was updated Successful!")
            } else {
                println("Budget update Failed! Try again")
            }
        } else {
            println("There is currently no Budgets with that ID!")
        }
    }
}

fun deleteBudget() {
    listBudgets()
    if (budgetAPI.numberOfBudgets() > 0) {
        val id = readNextInt("Enter the ID of the Budget you wish to Delete: ")
        val budgetToDelete = budgetAPI.deleteBudget(id)
        if (budgetToDelete) {
            println("Budget was deleted Successfully")
        } else {
            println("Budget deletion Failed! Try again")
        }
    }
}

fun closeBudget() {
    listActiveBudgets()
    if (budgetAPI.numberOfActiveBudgets() > 0) {
        val id = readNextInt("Enter the ID of the Budget you wish to Close: ")
        if (budgetAPI.closeBudget(id)) {
            println("Budget was closed Successfully")
        } else {
            println("Budget closing Failed! Try again")
        }
    }
}

fun addEntry() {
    // logger.info { "addEntry() function invoked" }
    val budget: Budget? = askUserToChooseBudget()
    if (budget != null) {
        budget.addEntry(
            Entry(
                entryID = 0,
                entryDesc = readNextLine("\t Entry Description: "),
                location = readNextLine("\t Location Spent: "),
                dateSpent = readNextInt("\t Date Spent: "),
                amountSpent = readNextInt("\t Amount Spent: "),
                transactionType = readNextLine("\t How did you pay? :")
            )
        )
    }
}

fun deleteEntry() {
    val budget: Budget? = askUserToChooseBudget()
    if (budget != null) {
        val entry: Entry? = askUserToChooseEntry(budget)
        if (entry != null) {
            val isDeleted = budget.deleteEntry(entry.entryID)
            if (isDeleted) {
                println("Delete Successful!")
            } else {
                println("Delete NOT Successful")
            }
        }
    }
}

fun updateEntry() {
    val budget: Budget? = askUserToChooseBudget()
    if (budget != null) {
        val entry: Entry? = askUserToChooseEntry(budget)
        if (entry != null) {
            val newDesc = readNextLine("Enter a new Description for Entry: ")
            val newAmountSpent = readNextInt("Enter a new amount spent: ")
            val newDateSpent = readNextInt("Enter a new date spent: ")
            val newLocation = readNextLine("Enter a new location: ")
            val newTransactionType = readNextLine("Enter a new transaction type: ")
            if (budget.updateEntry(
                    entry.entryID,
                    Entry(
                        entryID = 0,
                        entryDesc = newDesc,
                        amountSpent = newAmountSpent,
                        dateSpent = newDateSpent,
                        location = newLocation,
                        transactionType = newTransactionType
                    )
                )
            ) {
                println("Entry was updated Successful!")
            } else {
                println("Entry update Failed! Try again")
            }
        } else {
            println("There is currently no Entries with that ID!")
        }
    }
}

fun exitApp() {
    // logger.info { "exitApp() function invoked" }
    exit(0)
}

fun save() {
    try {
        budgetAPI.store()
    } catch (e: Exception) {
        System.err.println(" ┃ Error writing to file: $e")
    }
}

fun load() {
    try {
        budgetAPI.load()
    } catch (e: Exception) {
        System.err.println(" ┃ Error reading from file: $e")
    }
}

private fun askUserToChooseBudget(): Budget? {
    listBudgets()
    if (budgetAPI.numberOfClosedBudgets() > 0) {
        val budget = budgetAPI.findBudget(readNextInt("\nEnter the ID of the budget you wish to select: "))
        if (budget != null) {
            if (budget.isBudgetClosed) {
                println("This budget has already been closed")
            } else {
                return budget
            }
        } else {
            println("The budget with that ID is not valid! Try again")
        }
    }
    return null
}

private fun askUserToChooseEntry(budget: Budget): Entry? {
    if (budget.numberOfEntries() > 0) {
        print(budget.listEntries())
        return budget.findOne(readNextInt("\nEnter the ID of the entry you wish to select: "))
    } else {
        println("There is no entries inside this Budget")
        return null
    }
}

