package com.versus.games.lines.utils

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.StyleableRes
import com.versus.games.lines.R

fun Float.dp(context: Context): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
)

fun dp2px(dp: Int, context: Context) = (dp.toFloat() * context.resources.displayMetrics.density)

fun Long.argb(): Int = this.toInt()

fun Context.styledAttributesInt(attrs: AttributeSet?, @StyleableRes a: IntArray, index: Int, defValue: Int = 0):Int {
        val typedArray = this.obtainStyledAttributes(attrs, a)
        val count = typedArray.getInt(index, defValue)
        typedArray.recycle()
        return count

}