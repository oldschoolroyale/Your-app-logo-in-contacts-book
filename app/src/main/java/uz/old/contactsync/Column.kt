package uz.old.contactsync

data class Column(
    val normalNumber: String,
    val phoneNoType: Int,
    val customLabelName: String?,
    val typeLabel: CharSequence?,
    val accountType: String?
)