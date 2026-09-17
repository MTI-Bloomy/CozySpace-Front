package bloomy.cozyspace.utils

import bloomy.cozyspace.domain.RoomType

fun parseRoomType(type: String): RoomType = when (type) {
    "Bathroom" -> RoomType.BATHROOM
    "Work" -> RoomType.WORK
    "Garden" -> RoomType.GARDEN
    "Bedroom" -> RoomType.BEDROOM
    "Kitchen" -> RoomType.KITCHEN
    else -> throw IllegalStateException("Unexpected type")
}
