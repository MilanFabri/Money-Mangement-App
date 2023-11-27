package models

data class Entry(
    var entryID: Int,
    var entryDesc: String,
    var location: String,
    var dateSpent: Int,
    var amountSpent: Int,
    var transactionType: String
)
