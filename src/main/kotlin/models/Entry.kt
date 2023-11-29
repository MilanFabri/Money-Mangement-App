package models

data class Entry(
    var entryID: Int,
    var entryDesc: String,
    var location: String,
    var dateSpent: Int,
    var amountSpent: Int,
    var transactionType: String){

   override fun toString(): String {
   return "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ \n" +
          "  $entryID ┃ $entryDesc ┃ Location : $location ┃ Date : $dateSpent ┃ Amount Spent : €$amountSpent ┃ Payment Method : $transactionType \n" +
          "    ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
}
}

