package com.versus.games.lines.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.versus.games.lines.R
import com.versus.games.lines.utils.dp2px
import com.versus.games.lines.utils.styledAttributesInt
import kotlin.math.min


class Cell @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    View(context, attrs, defStyleAttr, defStyleRes) {

    enum class State {
        EMPTY, SMALL, NORMAL, JUMPING
    }

    companion object {
        @ColorInt
        const val BK_COLOR = 0xff909090.toInt()

        @ColorInt
        const val BORDER_COLOR_LIGHT = 0xffb0b0b0.toInt()

        @ColorInt
        const val BORDER_COLOR_DARK = 0xff707070.toInt()

        const val BORDER_WIDTH_DP = 2
    }

    private val bKPaint = Paint().apply {
        color = BK_COLOR
    }

    private val borderLightPaint = Paint().apply {
        color = BORDER_COLOR_LIGHT
    }

    private val borderDarkPaint = Paint().apply {
        color = BORDER_COLOR_DARK
    }

    private var state: State = State.EMPTY

    private var ballColor: Int = Color.BLACK

    private var ballPaint = Paint().apply {
        color = Color.BLUE
    }

    init {
        state = when (context.styledAttributesInt(
            attrs,
            R.styleable.Cell,
            R.styleable.Cell_cellState,
            0
        )) {
            1 -> State.SMALL
            2 -> State.NORMAL
            3 -> State.JUMPING
            else -> State.EMPTY
        }

    }

    var delta = 0f
    var stopJumping = false

    private val jumper = ValueAnimator.ofFloat(0f, 1f).apply {
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE

        addUpdateListener { v ->
            delta = v.animatedValue as Float
            invalidate()
        }

        addListener(object : Animator.AnimatorListener {
            var isReverse = false

            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(v: Animator, isReverse: Boolean) {
            }

            override fun onAnimationEnd(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {
                if (stopJumping && isReverse) {
                    state = State.NORMAL
                    end()
                    delta = 0f
                    invalidate()
                }
                isReverse = !isReverse
            }
        })
    }

    fun startJump() {
        when (state) {
            State.NORMAL -> {
                state = State.JUMPING
                stopJumping = false
                jumper.start()
            }
            State.JUMPING -> {
                stopJumping = true
            }
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRect(rect, bKPaint)
        drawBorder(canvas, dp2px(BORDER_WIDTH_DP, context), borderLightPaint, borderDarkPaint)
        when (state) {
            State.EMPTY -> {
            }
            State.NORMAL -> {
                drawBall(canvas, rect, ballPaint, 0f)
            }
            State.SMALL -> {
                val r = RectF(rect)
                r.inset(rect.width() * 0.3f, rect.height() * 0.3f)
                drawBall(canvas, r, ballPaint, 0f)
            }
            State.JUMPING -> {
                drawBall(canvas, rect, ballPaint, delta)
            }
        }
    }

    private fun drawBall(canvas: Canvas, rect: RectF, paint: Paint, d: Float) {
        val r = min(rect.width(), rect.height()) * 0.75f

        canvas.drawCircle(rect.centerX(), rect.centerY() * (1f - d * 0.15f), r * 0.5f, paint)
    }

    private fun drawBorder(
        canvas: Canvas,
        borderWidth: Float,
        paintLight: Paint,
        paintDark: Paint
    ) {
        val border = RectF(0f, 0f, width.toFloat(), height.toFloat())

        for (i in 0..borderWidth.toInt()) {
            canvas.apply {
                drawLine(border.left, border.top, border.right, border.top, paintLight)
                drawLine(border.left, border.top, border.left, border.bottom, paintLight)

                drawLine(border.right, border.top, border.right, border.bottom, paintDark)
                drawLine(border.left + 1, border.bottom, border.right + 1, border.bottom, paintDark)
            }

            border.left++
            border.top++
            border.right--
            border.bottom--
        }
    }
}