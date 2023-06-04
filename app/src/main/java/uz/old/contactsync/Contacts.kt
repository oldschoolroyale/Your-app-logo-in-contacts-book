package uz.old.contactsync

data class Contacts(
    private val id: String?,
    private val displayName: String?,
    private val numbers: ArrayList<String>?,
    private val numbersType: ArrayList<String>?,
    private val numbersTypeInts: ArrayList<Int>?,
    private val lastUpdatedTimestamp: Long?
) {
    fun getId() = id

    fun getNumbers() = numbers

    fun getNumbersTypeInts() = numbersTypeInts

    fun getDisplayName() = displayName

    class Builder(
        private var id: String? = null,
        private var displayName: String? = null,
        private var numbers: ArrayList<String>? = null,
        private var numbersType: ArrayList<String>? = null,
        private var numbersTypeInts: ArrayList<Int>? = null,
        private var lastUpdatedTimestamp: Long? = null
    ) {
        fun setId(id: String) = apply { this.id = id }

        fun setDisplayName(displayName: String) = apply { this.displayName = displayName }

        fun setNumbers(numbers: ArrayList<String>) = apply { this.numbers = numbers }

        fun setNumbersType(numbersType: ArrayList<String>) = apply { this.numbersType = numbersType }

        fun setNumbersTypeInts(numbersTypeInts: ArrayList<Int>) = apply { this.numbersTypeInts = numbersTypeInts }

        fun setLastUpdatedTimeStamp(lastUpdatedTimestamp: Long) = apply { this. lastUpdatedTimestamp = lastUpdatedTimestamp }

        fun build() = Contacts(id, displayName, numbers, numbersType, numbersTypeInts, lastUpdatedTimestamp)
    }
}
