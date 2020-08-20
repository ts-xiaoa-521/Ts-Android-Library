package com.ts_xiaoa.ts_widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;


/**
 * Created by ts_xiaoA on 2020/2/24 on 9:40
 * E-Mail Address：443502578@qq.com
 * Desc:设置drawable图标大小
 */
public class RichEditText extends AppCompatEditText {

    private int drawableHeight, drawableWidth;

    public RichEditText(Context context) {
        super(context);
    }

    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RichEditText);
        drawableWidth = a.getDimensionPixelSize(R.styleable.RichEditText_ret_drawable_width, 0);
        drawableHeight = a.getDimensionPixelSize(R.styleable.RichEditText_ret_drawable_height, 0);
        a.recycle();
        Drawable[] drawables;
        boolean isAllNull = true;
        for (Drawable drawable : getCompoundDrawablesRelative()) {
            if (drawable != null) {
                isAllNull = false;
                break;
            }
        }
        if (isAllNull) {
            drawables = getCompoundDrawables();
        }else{
            drawables = getCompoundDrawablesRelative();
        }
        for (Drawable drawable : drawables) {
            if (drawable != null) {
                drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            }
        }
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        //绘制Drawable宽高,位置
//        drawDrawable();
    }

    public void setDrawableStart(int resourceId) {
        Drawable drawable = getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        setCompoundDrawablesWithIntrinsicBounds(drawable,
                null, null, null);
        setCompoundDrawablePadding(4);
    }

    public void setDrawableTop(int resourceId) {
        Drawable drawable = getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        setCompoundDrawablesWithIntrinsicBounds(null,
                drawable, null, null);
        setCompoundDrawablePadding(4);
    }

    public void setDrawableEnd(int resourceId) {
        Drawable drawable = getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        setCompoundDrawablesWithIntrinsicBounds(null,
                null, drawable, null);
        setCompoundDrawablePadding(4);
    }

    public void setDrawableBottom(int resourceId) {
        Drawable drawable = getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, drawable);
        setCompoundDrawablePadding(4);
    }
}
