package com.ts_xiaoa.ts_recycler_view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ts_xiaoa.ts_recycler_view.R;
import com.ts_xiaoa.ts_recycler_view.listener.OnOffsetChangedListener;
import com.ts_xiaoa.ts_recycler_view.manager.BannerLayoutManager;

/**
 * Created by ts_xiaoA on 2020/2/27 on 9:28
 * E-Mail Address：443502578@qq.com
 * Desc: 与BannerLayoutManager配套使用的指示器
 */
public class BannerIndicatorView extends View implements OnOffsetChangedListener {


    private final String TAG = getClass().getSimpleName();

    //指示器圆点大小
    private int indicatorSize;
    //圆点间距
    private int itemSpace;
    //指示器颜色
    private int indicatorColor;
    //指示器选中时的颜色
    private int indicatorSelectedColor;

    private float bannerOffsetX = 0;
    private int targetPosition = 0;
    //轮播图数量
    private BannerLayoutManager bannerLayoutManager;
    private Paint paint;
    private Paint paintSelector;
    private RectF rectF;

    public BannerIndicatorView(Context context) {
        super(context);
    }

    public BannerIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BannerIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerIndicatorView);
        itemSpace = typedArray.getDimensionPixelSize(R.styleable.BannerIndicatorView_indicator_space, 10);
        indicatorSize = typedArray.getDimensionPixelSize(R.styleable.BannerIndicatorView_indicator_size, 10);
        indicatorColor = typedArray.getColor(R.styleable.BannerIndicatorView_indicator_color, 0xFFD2D2D2);
        indicatorSelectedColor = typedArray.getColor(R.styleable.BannerIndicatorView_indicator_selected_color, 0xFF4CAF50);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (bannerLayoutManager != null) {
            int itemCount = bannerLayoutManager.getItemCount();
            int width = getPaddingLeft() + getPaddingRight() + (itemCount) * itemSpace + indicatorSize * (itemCount + 1);
            int height = getPaddingTop() + getPaddingBottom() + indicatorSize;
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bannerLayoutManager != null) {
            int itemCount = bannerLayoutManager.getItemCount();
            if (paint == null) {
                paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStyle(Paint.Style.FILL);
            }
            paint.reset();
            paint.setColor(indicatorColor);
            int circleX = getPaddingLeft() + indicatorSize / 2;
            int circleY = getPaddingTop() + indicatorSize / 2;
            int circleR = indicatorSize / 2;
            for (int i = 0; i <= itemCount; i++) {
                canvas.drawCircle(circleX, circleY, circleR, paint);
                circleX += itemSpace + indicatorSize;
            }
            if (paintSelector == null) {
                paintSelector = new Paint(Paint.ANTI_ALIAS_FLAG);
            }
            paint.reset();
            paint.setAntiAlias(true);
            paint.setColor(indicatorSelectedColor);
            paint.setStyle(Paint.Style.FILL);
            if (rectF == null) {
                rectF = new RectF();// 设置个新的长方形
            }
            int selectorWidth = indicatorSize * 2 + itemSpace;
            rectF.left = getPaddingLeft() + (bannerOffsetX) * (selectorWidth - indicatorSize) + targetPosition * selectorWidth - targetPosition * indicatorSize;
            rectF.top = getPaddingTop();
            rectF.right = rectF.left + indicatorSize * 2 + itemSpace;
            rectF.bottom = getPaddingTop() + indicatorSize;
            canvas.drawRoundRect(rectF, 1f * indicatorSize / 2, 1f * indicatorSize / 2, paint);
            if (itemCount - 1 == targetPosition && bannerOffsetX > 0) {
                rectF.left = 0;
                rectF.top = getPaddingTop();
                rectF.right = selectorWidth * bannerOffsetX;
                rectF.bottom = getPaddingTop() + indicatorSize;
                canvas.drawRoundRect(rectF, 1f * indicatorSize / 2, 1f * indicatorSize / 2, paint);
            }
        }
    }

    public void setUpWithBannerLayout(BannerLayoutManager bannerLayout, RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        this.bannerLayoutManager = bannerLayout;
        this.bannerLayoutManager.setOnOffsetChangedListener(this);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                requestLayout();
            }
        });
    }

    public void setUpWithBannerLayout(RecyclerView bannerRecyclerView) {
        this.bannerLayoutManager = (BannerLayoutManager) bannerRecyclerView.getLayoutManager();
        if (this.bannerLayoutManager != null) {
            this.bannerLayoutManager.setOnOffsetChangedListener(this);
        }
        if (bannerRecyclerView.getAdapter() != null) {
            bannerRecyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    requestLayout();
                }
            });
        }
    }

    @Override
    public void onOffset(float offsetX, int targetPosition) {
        this.bannerOffsetX = offsetX;
        this.targetPosition = targetPosition;
        invalidate();
    }
}
