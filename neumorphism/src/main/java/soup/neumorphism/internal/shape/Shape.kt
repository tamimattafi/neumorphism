package soup.neumorphism.internal.shape

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import soup.neumorphism.NeumorphShapeDrawable.NeumorphShapeDrawableState

internal interface Shape {
    fun setDrawableState(newDrawableState: NeumorphShapeDrawableState)
    fun draw(canvas: Canvas, outlinePath: Path, bounds: Rect)
    fun updateShadowBitmap(bounds: Rect)
}
