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
fun PetPaw(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "pets",
            defaultWidth = 20.0.dp,
            defaultHeight = 20.0.dp,
            viewportWidth = 20.0f,
            viewportHeight = 20.0f
        ).apply {
            materialPath {
                moveTo(7.333f, 19.833f)
                quadToRelative(-1.541f, 0f, -2.645f, -1.104f)
                quadToRelative(-1.105f, -1.104f, -1.105f, -2.687f)
                quadToRelative(0f, -1.542f, 1.105f, -2.646f)
                quadToRelative(1.104f, -1.104f, 2.687f, -1.104f)
                quadToRelative(1.583f, 0f, 2.667f, 1.104f)
                quadToRelative(1.083f, 1.104f, 1.083f, 2.687f)
                quadToRelative(0f, 1.584f, -1.104f, 2.667f)
                quadToRelative(-1.104f, 1.083f, -2.688f, 1.083f)
                close()
                moveTo(14.917f, 13f)
                quadToRelative(-1.584f, 0f, -2.688f, -1.104f)
                reflectiveQuadToRelative(-1.104f, -2.688f)
                quadToRelative(0f, -1.583f, 1.104f, -2.687f)
                quadToRelative(1.104f, -1.104f, 2.688f, -1.104f)
                quadToRelative(1.583f, 0f, 2.687f, 1.104f)
                quadToRelative(1.104f, 1.104f, 1.104f, 2.687f)
                quadToRelative(0f, 1.584f, -1.104f, 2.688f)
                reflectiveQuadTo(14.917f, 13f)
                close()
                moveToRelative(10.166f, 0f)
                quadToRelative(-1.541f, 0f, -2.645f, -1.104f)
                quadToRelative(-1.105f, -1.104f, -1.105f, -2.688f)
                quadToRelative(0f, -1.583f, 1.105f, -2.687f)
                quadToRelative(1.104f, -1.104f, 2.687f, -1.104f)
                quadToRelative(1.583f, 0f, 2.667f, 1.104f)
                quadToRelative(1.083f, 1.104f, 1.083f, 2.687f)
                quadToRelative(0f, 1.584f, -1.104f, 2.688f)
                reflectiveQuadTo(25.083f, 13f)
                close()
                moveToRelative(7.584f, 6.833f)
                quadToRelative(-1.584f, 0f, -2.688f, -1.104f)
                reflectiveQuadToRelative(-1.104f, -2.646f)
                quadToRelative(0f, -1.5f, 1.104f, -2.645f)
                quadToRelative(1.104f, -1.146f, 2.688f, -1.146f)
                quadToRelative(1.583f, 0f, 2.687f, 1.104f)
                quadToRelative(1.104f, 1.104f, 1.104f, 2.687f)
                quadToRelative(0f, 1.584f, -1.104f, 2.667f)
                quadToRelative(-1.104f, 1.083f, -2.687f, 1.083f)
                close()
                moveTo(11.208f, 36.667f)
                quadToRelative(-1.75f, 0f, -2.916f, -1.334f)
                quadTo(7.125f, 34f, 7.125f, 32.208f)
                quadToRelative(0f, -1.875f, 1.187f, -3.291f)
                quadToRelative(1.188f, -1.417f, 2.48f, -2.792f)
                quadToRelative(1f, -1f, 1.812f, -2.167f)
                quadToRelative(0.813f, -1.166f, 1.688f, -2.333f)
                quadToRelative(1.083f, -1.542f, 2.458f, -2.833f)
                quadTo(18.125f, 17.5f, 20f, 17.5f)
                reflectiveQuadToRelative(3.292f, 1.292f)
                quadToRelative(1.416f, 1.291f, 2.5f, 2.875f)
                quadToRelative(0.833f, 1.125f, 1.646f, 2.291f)
                quadToRelative(0.812f, 1.167f, 1.812f, 2.209f)
                quadToRelative(1.292f, 1.333f, 2.458f, 2.75f)
                quadToRelative(1.167f, 1.416f, 1.167f, 3.291f)
                quadToRelative(0f, 1.792f, -1.146f, 3.125f)
                quadToRelative(-1.146f, 1.334f, -2.896f, 1.334f)
                quadToRelative(-2.208f, 0f, -4.395f, -0.375f)
                quadToRelative(-2.188f, -0.375f, -4.396f, -0.375f)
                quadToRelative(-2.25f, 0f, -4.438f, 0.375f)
                quadToRelative(-2.187f, 0.375f, -4.396f, 0.375f)
                close()
            }
        }.build()
    }
}