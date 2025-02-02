package soup.neumorphism

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.Dimension
import androidx.annotation.StyleRes

open class NeumorphShapeAppearanceModel {

    class Builder {

        var cornerFamily: CornerFamily = CornerFamily.ROUNDED
        var cornerSize: Float = 0f

        fun setAllCorners(
            cornerFamily: CornerFamily,
            @Dimension cornerSize: Float
        ): Builder {
            return setAllCorners(cornerFamily)
                .setAllCornerSizes(cornerSize)
        }

        fun setAllCorners(cornerFamily: CornerFamily): Builder {
            return apply {
                this.cornerFamily = cornerFamily
            }
        }

        fun setAllCornerSizes(cornerSize: Float): Builder {
            return apply {
                this.cornerSize = cornerSize
            }
        }

        fun build(): NeumorphShapeAppearanceModel {
            return NeumorphShapeAppearanceModel(this)
        }
    }

    private val cornerFamily: CornerFamily
    private val cornerSize: Float

    private constructor(builder: Builder) {
        cornerFamily = builder.cornerFamily
        cornerSize = builder.cornerSize
    }

    constructor() {
        cornerFamily = CornerFamily.ROUNDED
        cornerSize = 0f
    }

    fun getCornerFamily(): CornerFamily {
        return cornerFamily
    }

    fun getCornerSize(): Float {
        return cornerSize
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NeumorphShapeAppearanceModel

        if (cornerFamily != other.cornerFamily) return false
        if (cornerSize != other.cornerSize) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cornerFamily.hashCode()
        result = 31 * result + cornerSize.hashCode()
        return result
    }

    companion object {

        fun builder(): Builder {
            return Builder()
        }

        fun builder(
            context: Context,
            attrs: AttributeSet?,
            @AttrRes defStyleAttr: Int,
            @StyleRes defStyleRes: Int,
            defaultCornerSize: Float = 0f
        ): Builder {
            val a = context.obtainStyledAttributes(
                attrs, R.styleable.NeumorphShape, defStyleAttr, defStyleRes
            )
            val shapeAppearanceResId = a.getResourceId(
                R.styleable.NeumorphShape_neumorph_shapeAppearance, 0
            )
            a.recycle()
            return builder(
                context,
                shapeAppearanceResId,
                defaultCornerSize
            )
        }

        private fun builder(
            context: Context,
            @StyleRes shapeAppearanceResId: Int,
            defaultCornerSize: Float
        ): Builder {
            val a = context.obtainStyledAttributes(
                shapeAppearanceResId,
                R.styleable.NeumorphShapeAppearance
            )
            try {
                val cornerFamily = a.getInt(
                    R.styleable.NeumorphShapeAppearance_neumorph_cornerFamily,
                    CornerFamily.ROUNDED.ordinal
                ).let { ordinal ->
                    CornerFamily.values()[ordinal]
                }

                val cornerSize =
                    getCornerSize(
                        a,
                        R.styleable.NeumorphShapeAppearance_neumorph_cornerSize,
                        defaultCornerSize
                    )
                return Builder()
                    .setAllCorners(cornerFamily, cornerSize)
            } finally {
                a.recycle()
            }
        }

        private fun getCornerSize(
            a: TypedArray, index: Int, defaultValue: Float
        ): Float {
            val value = a.peekValue(index) ?: return defaultValue
            return if (value.type == TypedValue.TYPE_DIMENSION) {
                TypedValue.complexToDimensionPixelSize(
                    value.data, a.resources.displayMetrics
                ).toFloat()
            } else {
                defaultValue
            }
        }
    }
}