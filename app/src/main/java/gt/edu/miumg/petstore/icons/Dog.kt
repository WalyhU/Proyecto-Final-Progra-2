package gt.edu.miumg.petstore.icons

import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
fun Dog(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "sound_detection_dog_barking",
            defaultWidth = 20.0.dp,
            defaultHeight = 20.0.dp,
            viewportWidth = 20.0f,
            viewportHeight = 20.0f
        ).apply {
            materialPath {
                moveTo(8.417f, 36.583f)
                verticalLineTo(20.125f)
                lineToRelative(-0.584f, -0.542f)
                quadToRelative(-0.416f, -0.5f, -0.625f, -1.021f)
                quadTo(7f, 18.042f, 7f, 17.458f)
                quadToRelative(0f, -0.583f, 0.188f, -1.104f)
                quadToRelative(0.187f, -0.521f, 0.645f, -0.979f)
                lineToRelative(5.375f, -5.417f)
                horizontalLineToRelative(7.209f)
                lineToRelative(6.541f, -6.541f)
                lineToRelative(3.167f, 3.208f)
                quadToRelative(1.625f, 1.625f, 2.437f, 3.729f)
                quadToRelative(0.813f, 2.104f, 0.896f, 4.271f)
                quadToRelative(-0.083f, 2.125f, -0.896f, 4.229f)
                quadToRelative(-0.812f, 2.104f, -2.437f, 3.771f)
                lineToRelative(-3.5f, 3.5f)
                verticalLineToRelative(10.458f)
                close()
                moveToRelative(2.625f, -2.625f)
                horizontalLineToRelative(12.916f)
                verticalLineToRelative(-8.916f)
                lineToRelative(4.292f, -4.25f)
                quadToRelative(1.25f, -1.292f, 1.896f, -2.896f)
                quadToRelative(0.646f, -1.604f, 0.646f, -3.271f)
                quadToRelative(0f, -1.708f, -0.646f, -3.292f)
                quadTo(29.5f, 9.75f, 28.25f, 8.5f)
                lineToRelative(-1.292f, -1.333f)
                lineToRelative(-5.458f, 5.416f)
                horizontalLineToRelative(-7.208f)
                lineTo(13f, 13.917f)
                lineToRelative(3f, 3.041f)
                quadToRelative(1.208f, 1.167f, 1.792f, 2.604f)
                quadToRelative(0.583f, 1.438f, 0.583f, 3.021f)
                quadToRelative(0f, 1.459f, -0.542f, 2.875f)
                quadToRelative(-0.541f, 1.417f, -1.583f, 2.542f)
                lineToRelative(-5.208f, -5.208f)
                close()
            }
        }.build()
    }
}