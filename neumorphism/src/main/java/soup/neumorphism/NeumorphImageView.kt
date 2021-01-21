package soup.neumorphism

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import soup.neumorphism.internal.util.NeumorphResources

class NeumorphImageView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = R.attr.neumorphImageViewStyle,
    private val defStyleRes: Int = R.style.Widget_Neumorph_ImageView
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var isInitialized: Boolean = false
    private lateinit var shapeDrawable: NeumorphShapeDrawable

    private var insetStart = 0
    private var insetEnd = 0
    private var insetTop = 0
    private var insetBottom = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        this.initShapeDrawable()
    }

    override fun setBackground(drawable: Drawable?) {
        setBackgroundDrawable(drawable)
    }

    override fun setBackgroundDrawable(drawable: Drawable?) {
        Log.i(LOG_TAG, "Setting a custom background is not supported.")
    }

    fun setNeumorphBackgroundDrawable(drawable: Drawable?) {
        shapeDrawable.setBackgroundDrawable(drawable)
    }
    private fun setBackgroundInternal(drawable: Drawable?) {
        super.setBackgroundDrawable(drawable)
    }

    fun setShapeAppearanceModel(shapeAppearanceModel: NeumorphShapeAppearanceModel) {
        shapeDrawable.setShapeAppearanceModel(shapeAppearanceModel)
    }

    fun getShapeAppearanceModel(): NeumorphShapeAppearanceModel {
        return shapeDrawable.getShapeAppearanceModel()
    }

    override fun setBackgroundColor(color: Int) {
        shapeDrawable.setFillColor(ColorStateList.valueOf(color))
    }

    fun setBackgroundColor(backgroundColor: ColorStateList?) {
        shapeDrawable.setFillColor(backgroundColor)
    }

    fun getBackgroundColor(): ColorStateList? {
        return shapeDrawable.getFillColor()
    }

    fun setStrokeColor(strokeColor: ColorStateList?) {
        shapeDrawable.setStrokeColor(strokeColor)
    }

    fun getStrokeColor(): ColorStateList? {
        return shapeDrawable.getStrokeColor()
    }

    fun setStrokeWidth(strokeWidth: Float) {
        shapeDrawable.setStrokeWidth(strokeWidth)
    }

    fun getStrokeWidth(): Float {
        return shapeDrawable.getStrokeWidth()
    }

    fun setShapeType(@ShapeType shapeType: Int) {
        shapeDrawable.setShapeType(shapeType)
    }

    @ShapeType
    fun getShapeType(): Int {
        return shapeDrawable.getShapeType()
    }

    fun setInset(left: Int, top: Int, right: Int, bottom: Int) {
        internalSetInset(left, top, right, bottom)
    }

    private fun updateInsets(left: Int, top: Int, right: Int, bottom: Int): Boolean {
        var changed = false
        if (insetStart != left) {
            changed = true
            insetStart = left
        }
        if (insetTop != top) {
            changed = true
            insetTop = top
        }
        if (insetEnd != right) {
            changed = true
            insetEnd = right
        }
        if (insetBottom != bottom) {
            changed = true
            insetBottom = bottom
        }

        return changed
    }

    private fun internalSetInset(left: Int, top: Int, right: Int, bottom: Int) {
        val changed = updateInsets(left, top, right, bottom)
        if (changed) {
            shapeDrawable.setInset(left, top, right, bottom)
            requestLayout()
            invalidateOutline()
        }
    }

    fun setShadowElevation(shadowElevation: Float) {
        shapeDrawable.setShadowElevation(shadowElevation)
    }

    fun getShadowElevation(): Float {
        return shapeDrawable.getShadowElevation()
    }

    fun setShadowColorLight(@ColorInt shadowColor: Int) {
        shapeDrawable.setShadowColorLight(shadowColor)
    }

    fun setShadowColorDark(@ColorInt shadowColor: Int) {
        shapeDrawable.setShadowColorDark(shadowColor)
    }

    override fun setTranslationZ(translationZ: Float) {
        super.setTranslationZ(translationZ)
        if (isInitialized) {
            shapeDrawable.setTranslationZ(translationZ)
        }
    }

    private fun initShapeDrawable() {
        if (isInitialized || measuredHeight * measuredWidth == 0) return

        val a = context.obtainStyledAttributes(
                attrs, R.styleable.NeumorphImageView, defStyleAttr, defStyleRes
        )

        val backgroundDrawable = a.getDrawable(R.styleable.NeumorphImageButton_neumorph_backgroundDrawable)
        val fillColor = a.getColorStateList(R.styleable.NeumorphFloatingActionButton_neumorph_backgroundColor)
        val strokeColor = a.getColorStateList(R.styleable.NeumorphFloatingActionButton_neumorph_strokeColor)
        val strokeWidth = a.getDimension(R.styleable.NeumorphFloatingActionButton_neumorph_strokeWidth, 0f)
        val shapeType =
                a.getInt(R.styleable.NeumorphFloatingActionButton_neumorph_shapeType, ShapeType.DEFAULT)
        val inset = a.getDimensionPixelSize(
                R.styleable.NeumorphFloatingActionButton_neumorph_inset, 0
        )
        val insetStart = a.getDimensionPixelSize(
                R.styleable.NeumorphFloatingActionButton_neumorph_insetStart, -1
        )
        val insetEnd = a.getDimensionPixelSize(
                R.styleable.NeumorphFloatingActionButton_neumorph_insetEnd, -1
        )
        val insetTop = a.getDimensionPixelSize(
                R.styleable.NeumorphFloatingActionButton_neumorph_insetTop, -1
        )
        val insetBottom = a.getDimensionPixelSize(
                R.styleable.NeumorphFloatingActionButton_neumorph_insetBottom, -1
        )
        val shadowElevation = a.getDimension(
                R.styleable.NeumorphFloatingActionButton_neumorph_shadowElevation, 0f
        )
        val shadowColorLight = NeumorphResources.getColor(
                context, a,
                R.styleable.NeumorphFloatingActionButton_neumorph_shadowColorLight,
                R.color.design_default_color_shadow_light
        )
        val shadowColorDark = NeumorphResources.getColor(
                context, a,
                R.styleable.NeumorphFloatingActionButton_neumorph_shadowColorDark,
                R.color.design_default_color_shadow_dark
        )
        a.recycle()

        updateInsets(
                if (insetStart >= 0) insetStart else inset,
                if (insetTop >= 0) insetTop else inset,
                if (insetEnd >= 0) insetEnd else inset,
                if (insetBottom >= 0) insetBottom else inset
        )

        shapeDrawable = NeumorphDrawableFactory.createReusable(
                context,
                attrs,
                defStyleAttr,
                defStyleRes,
                measuredWidth,
                measuredHeight,
                isInEditMode,
                shapeType,
                shadowElevation,
                shadowColorLight,
                shadowColorDark,
                backgroundDrawable,
                fillColor,
                strokeWidth,
                strokeColor,
                translationZ,
                this.insetStart,
                this.insetTop,
                this.insetEnd,
                this.insetBottom
        )

        setBackgroundInternal(shapeDrawable)
        isInitialized = true
    }

    companion object {
        private const val LOG_TAG = "NeumorphImageView"
    }
}
