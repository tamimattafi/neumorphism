package soup.neumorphism

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes

object NeumorphDrawableFactory {

    private val reusable_drawables by lazy {
        HashMap<Int, NeumorphShapeDrawable>()
    }

    fun createReusable(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int,
        @StyleRes defStyleRes: Int,
        width: Int,
        height: Int,
        isInEditMode: Boolean,
        @ShapeType shapeType: Int,
        shadowElevation: Float,
        @ColorInt shadowColorLight: Int,
        @ColorInt shadowColorDark: Int,
        backgroundDrawable: Drawable?,
        fillColor: ColorStateList?,
        strokeWidth: Float,
        strokeColor: ColorStateList?,
        translationZ: Float,
        insetLeft: Int,
        insetTop: Int,
        insetRight: Int,
        insetBottom: Int
    ): NeumorphShapeDrawable {
        var hashCode = width
        hashCode = 31 * hashCode + height
        hashCode = 31 * hashCode + isInEditMode.hashCode()
        hashCode = 31 * hashCode + shapeType
        hashCode = 31 * hashCode + shadowElevation.hashCode()
        hashCode = 31 * hashCode + shadowColorLight
        hashCode = 31 * hashCode + shadowColorDark
        hashCode = 31 * hashCode + (backgroundDrawable?.hashCode() ?: 0)
        hashCode = 31 * hashCode + (fillColor?.hashCode() ?: 0)
        hashCode = 31 * hashCode + strokeWidth.hashCode()
        hashCode = 31 * hashCode + (strokeColor?.hashCode() ?: 0)
        hashCode = 31 * hashCode + translationZ.hashCode()
        hashCode = 31 * hashCode + insetLeft
        hashCode = 31 * hashCode + insetTop
        hashCode = 31 * hashCode + insetRight
        hashCode = 31 * hashCode + insetBottom

        val drawable = reusable_drawables[hashCode] ?: NeumorphShapeDrawable(context, attrs, defStyleAttr, defStyleRes).apply {
            setInEditMode(isInEditMode)
            setShapeType(shapeType)
            setShadowElevation(shadowElevation)
            setShadowColorLight(shadowColorLight)
            setShadowColorDark(shadowColorDark)
            setBackgroundDrawable(backgroundDrawable)
            setFillColor(fillColor)
            setStroke(strokeWidth, strokeColor)
            setTranslationZ(translationZ)
            setInset(insetLeft, insetTop, insetRight, insetBottom)
            reusable_drawables[hashCode] = this
        }

        return drawable.clone()
    }

}