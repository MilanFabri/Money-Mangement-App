import mu.KotlinLogging
import utils.ScannerInput
import java.lang.System.exit

private val logger = KotlinLogging.logger {}

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
    logger.info { "addNote() function invoked" }
}

fun listBudgets(){
    logger.info { "listNotes() function invoked" }
}

fun updateBudget(){
    logger.info { "updateNote() function invoked" }
}

fun deleteBudget(){
    logger.info { "deleteNote() function invoked" }
}

fun exitApp(){
    logger.info { "exitApp() function invoked" }
    exit(0)
}
