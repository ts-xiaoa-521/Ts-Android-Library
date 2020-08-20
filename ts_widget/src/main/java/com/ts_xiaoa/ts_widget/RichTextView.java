package com.ts_xiaoa.ts_widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;


/**
 * Created by ts_xiaoA on 2020/2/24 on 9:40
 * E-Mail Address：443502578@qq.com
 * Desc:设置drawable图标大小
 */
public class RichTextView extends AppCompatTextView {

    private int drawableHeight, drawableWidth;

    public RichTextView(Context context) {
        super(context);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RichTextView);
        drawableWidth = a.getDimensionPixelSize(R.styleable.RichTextView_rtv_drawable_width, 0);
        drawableHeight = a.getDimensionPixelSize(R.styleable.RichTextView_rtv_drawable_height, 0);
        int lineFlag = a.getInteger(R.styleable.RichTextView_rtv_line_flag, 0);
        switch (lineFlag) {
            case 1:
                //中划线
                getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                break;
            case 2:
                //下划线
                getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                break;
        }
        a.recycle();
        if (drawableWidth > 0 && drawableHeight > 0) {
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
            } else {
                drawables = getCompoundDrawablesRelative();
            }
            for (Drawable drawable : drawables) {
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawableWidth, drawableHeight);
                }
            }
            setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        }
    }

    public void setDrawableStart(int resourceId) {
        Drawable drawable = getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        setCompoundDrawables(drawable,
                null, null, null);
    }

    public void setDrawableTop(int resourceId) {
        Drawable drawable = getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        setCompoundDrawables(null,
                drawable, null, null);
//        setCompoundDrawablePadding(4);
    }

    public void setDrawableEnd(int resourceId) {
        Drawable drawable = getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        setCompoundDrawables(null,
                null, drawable, null);
//        setCompoundDrawablePadding(4);
    }

    public void setDrawableBottom(int resourceId) {
        Drawable drawable = getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        setCompoundDrawables(null,
                null, null, drawable);
//        setCompoundDrawablePadding(4);
    }
}
