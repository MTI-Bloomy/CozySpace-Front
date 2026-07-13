package bloomy.cozyspace.home.components

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    val bmp = BitmapFactory.decodeByteArray(this, 0, size)
    return bmp.asImageBitmap()
}
