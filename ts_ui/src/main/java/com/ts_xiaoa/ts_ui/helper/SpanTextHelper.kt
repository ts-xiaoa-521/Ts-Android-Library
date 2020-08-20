package com.ts_xiaoa.ts_kotlin.helper

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.*
import android.view.View
import androidx.annotation.ColorInt

/**
 * create by ts_xiaoA on 2019-12-17 17:29
 * email：443502578@qq.com
 * desc：
 */
class SpanTextHelper {
    //声明一个SpannableStringBuilder对象
    private val spannableStringBuilder: SpannableStringBuilder = SpannableStringBuilder()

    fun appendText(text: String): SpanTextHelper {
        return append(text, 0)
    }

    fun appendColor(text: String, @ColorInt color: Int): SpanTextHelper {
        return append(text, color, 0)
    }

    fun appendSize(text: String, spSize: Int): SpanTextHelper {
        return append(text, 0, spSize)
    }

    fun appendLine(text: String, @ColorInt color: Int): SpanTextHelper {
        return append(text, color, 0, false, true)
    }

    fun appendLine(text: String, color: Int, clickableSpan: ClickableSpan?): SpanTextHelper {
        return append(text, color, 0, false, true, clickableSpan)
    }

    fun append(text: String, color: Int, clickProxy: () -> Unit): SpanTextHelper {
        return append(text, color, 0, null, null, object : SimpleClickableSpan() {
            override fun onClick(widget: View) {
                clickProxy()
            }
        })
    }

    fun append(
        text: String, @ColorInt color: Int,
        clickableSpan: ClickableSpan?
    ): SpanTextHelper {
        return append(text, color, 0, null, null, clickableSpan)
    }

    @JvmOverloads
    fun append(
        text: String, @ColorInt color: Int = 0,
        spSize: Int = 0,
        isBold: Boolean? = null,
        isUnderLine: Boolean? = null,
        clickableSpan: ClickableSpan? = null
    ): SpanTextHelper {
        if (text.isEmpty()) {
            return this
        }
        val start = spannableStringBuilder.length
        spannableStringBuilder.append(text)
        if (clickableSpan != null) {
            spannableStringBuilder.setSpan(
                clickableSpan,
                start,
                spannableStringBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (color != 0) {
            val colorSpan = ForegroundColorSpan(color)
            spannableStringBuilder.setSpan(
                colorSpan,
                start,
                spannableStringBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (spSize > 0) {
            val sizeSpan = AbsoluteSizeSpan(spSize, true)
            spannableStringBuilder.setSpan(
                sizeSpan,
                start,
                spannableStringBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (isBold != null) {
            if (isBold) {
                val styleSpan = StyleSpan(Typeface.BOLD)
                spannableStringBuilder.setSpan(
                    styleSpan,
                    start,
                    spannableStringBuilder.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                val styleSpan = StyleSpan(Typeface.NORMAL)
                spannableStringBuilder.setSpan(
                    styleSpan,
                    start,
                    spannableStringBuilder.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        if (isUnderLine != null) {
            if (isUnderLine) {
                val span = UnderlineSpan()
                spannableStringBuilder.setSpan(
                    span,
                    start,
                    spannableStringBuilder.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return this
    }

    fun build(): SpannableStringBuilder {
        return spannableStringBuilder
    }

    abstract class SimpleClickableSpan : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {}
    }

}