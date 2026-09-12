package bloomy.cozyspace.todoList.utils

enum class Frequency(val days: Int) {
    Never(0),
    EveryDay(1),
    EveryWeek(7),
    EveryMonth(30),
    EveryYear(365);

    companion object {
        fun fromDays(days: Int): Frequency =
            entries.find { it.days == days } ?: Never
    }
}
