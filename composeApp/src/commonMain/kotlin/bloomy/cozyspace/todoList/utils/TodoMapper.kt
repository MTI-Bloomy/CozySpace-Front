package bloomy.cozyspace.todoList.utils

import bloomy.cozyspace.domain.RoomType

fun RoomType.toCategory(): Category = when (this) {
    RoomType.KITCHEN -> Category.Kitchen
    RoomType.BATHROOM -> Category.Bathroom
    RoomType.BEDROOM -> Category.Bedroom
    RoomType.GARDEN -> Category.Garden
    RoomType.WORK -> Category.Work
}

fun Int?.toFrequency(): Frequency = Frequency.entries.firstOrNull { it.days == this }
    ?: Frequency.Never

fun Category.toRoomType(): RoomType = when (this) {
    Category.Kitchen -> RoomType.KITCHEN
    Category.Bathroom -> RoomType.BATHROOM
    Category.Bedroom -> RoomType.BEDROOM
    Category.Garden -> RoomType.GARDEN
    Category.Work -> RoomType.WORK
}
