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
    return readNextInt(
        """ 
             >┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
             >┃ Money Management App      V4.0 ┃
             >┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
             >┃ Menu:                          ┃
             >┃   1) Budget Options            ┃
             >┃   2) Entry Options             ┃
             >┃   3) Listing Options           ┃
             >┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
             >┃ System:                        ┃
             >┃   8) Save                      ┃
             >┃   9) Load                      ┃
             >┃   0) Exit                      ┃
             >┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
             > ==>> """.trimMargin(">")
    )
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> budgetOptions()
            2 -> entryOptions()
            3 -> listOptions()
            5 -> save()
            6 -> load()
            0 -> exitApp()
            else -> println("┃ Invalid option entered: $option")
        }
    } while (true)
}

fun budgetOptions(){
        val option = readNextInt(
            """
                  >┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
                  >┃         BUDGET OPTIONS         ┃
                  >┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
                  >┃   1) Create new Budget         ┃
                  >┃   2) Update a Budget           ┃
                  >┃   3) Delete a Budget           ┃
                  >┃   4) Close a Budget            ┃
                  >┃   5) Auto Close Full Budgets   ┃
                  >┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
         > ==▶ """.trimMargin(">"))

        when (option) {
            1 -> createBudget();
            2 -> updateBudget();
            3 -> deleteBudget();
            4 -> closeBudget();
            5 -> autoClose();
            else -> println("┃ Invalid option entered: " + option);
        }
    }

fun entryOptions(){
    val option = readNextInt(
        """
                  >┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
                  >┃         ENTRY OPTIONS          ┃
                  >┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
                  >┃   1) Add new Entry to Budget   ┃
                  >┃   2) Delete an Entry           ┃
                  >┃   3) Update an Entry           ┃
                  >┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
         > ==▶ """.trimMargin(">"))

    when (option) {
        1 -> addEntry();
        2 -> deleteEntry();
        3 -> updateEntry();
        else -> println("┃ Invalid option entered: " + option);
    }
}

fun listOptions(){
    val option = readNextInt(
        """
                  >┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
                  >┃         LISTING OPTIONS        ┃
                  >┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫
                  >┃   1) List All Budgets          ┃
                  >┃   2) List Active Budgets       ┃
                  >┃   3) List Closed Budgets       ┃
                  >┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
         > ==▶ """.trimMargin(">"))

    when (option) {
        1 -> listBudgets();
        2 -> listActiveBudgets();
        3 -> listClosedBudgets();
        else -> println("┃ Invalid option entered: " + option);
    }
}

fun createBudget() {
    // logger.info { "createBudget() function invoked" }
    val budgetTitle = readNextLine("┃ Enter the Title for your Budget: ")
    val allocatedAmount = readNextInt("┃ Enter allocated amount to spend for your Budget: ")

    val isAdded =
        budgetAPI.addBudget(Budget(budgetID = 0, budgetTitle = budgetTitle, allocatedAmount = allocatedAmount))

    if (isAdded) {
        println("┃ Budget was created Successfully!")
    } else {
        println("┃ Budget Creation Failed! Try again")
    }
}

fun listBudgets() {
    // logger.info { "listBudgets() function invoked" }
    println(budgetAPI.listAllBudgets())
}

fun listActiveBudgets() {
    println(budgetAPI.listActiveBudgets())
}

fun listClosedBudgets() {
    println(budgetAPI.listClosedBudgets())
}

fun autoClose() {
    budgetAPI.autoCloseBudget()
}

fun updateBudget() {
    // logger.info { "updateBudget() function invoked" }
    listBudgets()
    if (budgetAPI.numberOfBudgets() > 0) {
        val id = readNextInt("┃ Enter the ID of the budget you wish to Update: ")
        if (budgetAPI.findBudget(id) != null) {
            val budgetTitle = readNextLine("┃ Enter a new Title for your budget: ")
            val allocatedAmount = readNextInt("┃ Enter a new Allocated Amount for your budget: ")

            if (budgetAPI.updateBudget(id, Budget(0, budgetTitle, allocatedAmount))) {
                println("┃ Budget was updated Successful!")
            } else {
                println("┃ Budget update Failed! Try again")
            }
        } else {
            println("┃ There is currently no Budgets with that ID!")
        }
    }
}

fun deleteBudget() {
    listBudgets()
    if (budgetAPI.numberOfBudgets() > 0) {
        val id = readNextInt("┃ Enter the ID of the Budget you wish to Delete: ")
        val budgetToDelete = budgetAPI.deleteBudget(id)
        if (budgetToDelete) {
            println("┃ Budget was deleted Successfully")
        } else {
            println("┃ Budget deletion Failed! Try again")
        }
    }
}

fun closeBudget() {
    listActiveBudgets()
    if (budgetAPI.numberOfActiveBudgets() > 0) {
        val id = readNextInt("┃ Enter the ID of the Budget you wish to Close: ")
        if (budgetAPI.closeBudget(id)) {
            println("┃ Budget was closed Successfully")
        } else {
            println("┃ Budget closing Failed! Try again")
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
                entryDesc = readNextLine("┃ Entry Description: "),
                location = readNextLine("┃ Location Spent: "),
                dateSpent = readNextInt("┃ Date Spent: "),
                amountSpent = readNextInt("┃ Amount Spent: "),
                transactionType = readNextLine("┃ Transaction Type?: ")
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
                println("┃ Entry was deleted Successfully")
            } else {
                println("┃ Entry deletion Failed! Try again")
            }
        }
    }
}

fun updateEntry() {
    val budget: Budget? = askUserToChooseBudget()
    if (budget != null) {
        val entry: Entry? = askUserToChooseEntry(budget)
        if (entry != null) {
            val newDesc = readNextLine("┃ Enter a new Description for Entry: ")
            val newAmountSpent = readNextInt("┃ Enter a new amount spent: ")
            val newDateSpent = readNextInt("┃ Enter a new date spent: ")
            val newLocation = readNextLine("┃ Enter a new location: ")
            val newTransactionType = readNextLine("┃ Enter a new transaction type: ")
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
                println("┃ Entry was updated Successful!")
            } else {
                println("┃ Entry update Failed! Try again")
            }
        } else {
            println("┃ There is currently no Entries with that ID!")
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
        System.err.println("┃ Error writing to file: $e")
    }
}

fun load() {
    try {
        budgetAPI.load()
    } catch (e: Exception) {
        System.err.println("┃ Error reading from file: $e")
    }
}

private fun askUserToChooseBudget(): Budget? {
    listBudgets()
    if (budgetAPI.numberOfActiveBudgets() > 0) {
        val budget = budgetAPI.findBudget(readNextInt("┃ Enter the ID of the budget you wish to select: "))
        if (budget != null) {
            if (budget.isBudgetClosed) {
                println("┃ This budget has already been closed")
            } else {
                return budget
            }
        } else {
            println("┃ The budget with that ID is not valid! Try again")
        }
    }
    return null
}

private fun askUserToChooseEntry(budget: Budget): Entry? {
    if (budget.numberOfEntries() > 0) {
        print(budget.listEntries())
        return budget.findOne(readNextInt("┃ Enter the ID of the entry you wish to select: "))
    } else {
        println("┃ There is no entries inside this Budget")
        return null
    }
}

