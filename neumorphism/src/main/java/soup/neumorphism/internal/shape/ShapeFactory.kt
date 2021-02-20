package soup.neumorphism.internal.shape

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import soup.neumorphism.CornerFamily
import soup.neumorphism.NeumorphShapeDrawable
import soup.neumorphism.ShapeType
import soup.neumorphism.internal.shape.ShapeFactory.Parameters.SIZE_CACHING_STEP
import soup.neumorphism.internal.util.BitmapUtils.clipToRadius
import soup.neumorphism.internal.util.BitmapUtils.toBitmap
import java.lang.ref.SoftReference

internal object ShapeFactory {

    private val reusable_shapes by lazy {
        HashMap<Int, SoftReference<Shape>>()
    }

    private val reusable_bitmaps by lazy {
        HashMap<Int, SoftReference<Bitmap>>()
    }

    fun createNewShape(
            drawableState: NeumorphShapeDrawable.NeumorphShapeDrawableState,
            bounds: Rect
    ): Shape {
        val shape = when (val shapeType = drawableState.shapeType) {
            ShapeType.FLAT -> FlatShape(drawableState)
            ShapeType.PRESSED -> PressedShape(drawableState)
            ShapeType.BASIN -> BasinShape(drawableState)
            else -> throw IllegalArgumentException("ShapeType($shapeType) is invalid.")
        }

        shape.updateShadowBitmap(bounds)
        return shape
    }

    fun createReusableShape(
            drawableState: NeumorphShapeDrawable.NeumorphShapeDrawableState,
            bounds: Rect
    ): Shape {
        var hashCode = drawableState.hashCode()
        hashCode = 31 * hashCode + bounds.calculateHashCode(SIZE_CACHING_STEP)

        return reusable_shapes[hashCode]?.get() ?: createNewShape(drawableState, bounds).also { newShape ->
            reusable_shapes[hashCode] = SoftReference(newShape)
        }
    }

    fun createNewBitmap(
            rect: RectF,
            cornerFamily: Int,
            cornerRadius: Float,
            drawable: Drawable
    ): Bitmap {
        val rectWidth = rect.width().toInt()
        val rectHeight = rect.height().toInt()
        val bitmap = drawable.toBitmap(rectWidth, rectHeight)

        val cornerSize = if (cornerFamily == CornerFamily.OVAL) bitmap.height / 2f
        else cornerRadius

        return bitmap.clipToRadius(cornerSize)
    }

    fun createReusableBitmap(
            rect: RectF,
            cornerFamily: Int,
            cornerRadius: Float,
            drawable: Drawable
    ): Bitmap {
        var hashCode = rect.calculateHashCode(SIZE_CACHING_STEP)
        hashCode = 31 * hashCode + cornerFamily
        hashCode = 31 * hashCode + cornerRadius.hashCode()
        hashCode = 31 * hashCode + drawable.hashCode()

        return reusable_bitmaps[hashCode]?.get() ?: createNewBitmap(rect, cornerFamily, cornerRadius, drawable).also { newBitmap ->
            reusable_bitmaps[hashCode] = SoftReference(newBitmap)
        }
    }

    private fun Rect.calculateHashCode(sizeStep: Int): Int {
        var result = width() / sizeStep
        result = 31 * result + height() / sizeStep
        return result
    }

    private fun RectF.calculateHashCode(sizeStep: Int): Int {
        var result = width().toInt() / sizeStep
        result = 31 * result + height().toInt() / sizeStep
        return result
    }

    object Parameters {
        const val SIZE_CACHING_STEP = 24
    }

}