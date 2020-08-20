package com.ts_xiaoa.ts_widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * create by ts_xiaoA on 2020-05-21 17:04
 * email：443502578@qq.com
 * desc：
 */
public class InputCodeView extends ViewGroup implements View.OnClickListener, TextWatcher {


    //验证码输入位数 默认4位
    private int inputCodeCount;
    //输入框间隔
    private int inputCodeSpace;
    //输入框背景样式
    private int inputCodeBackground;
    //验证码文字大小
    private float inputCodeTextSize;
    //验证码文字颜色
    private int inputCodeTextColor;
    //验证码文字风格
    private int inputCodeTextStyle;
    //验证码输入框大小
    private int inputTextViewSize;
    //输入完成的回调监听
    private OnInputFinishListener onInputFinishListener;
    //是否为密码
    private boolean inputTextIsPassword = false;

    private List<TextView> textViewList;
    private EditText editText;

    public InputCodeView(Context context) {
        super(context);
    }

    public InputCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (TextView textView : textViewList) {
            //计算单个验证码的大小
            int paddingSize = getPaddingStart() + getPaddingEnd();
            int spaceSize = (inputCodeCount - 1) * inputCodeSpace;
            inputTextViewSize = (getMeasuredWidth() - paddingSize - spaceSize) / inputCodeCount;
            //设置textView的大小
            int childMeasureSpec = MeasureSpec.makeMeasureSpec(inputTextViewSize, MeasureSpec.EXACTLY);
            measureChild(textView, childMeasureSpec, childMeasureSpec);
        }
        //计算高度(这里就没管固定高度的情况了，都通过计算得到高度)
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getPaddingTop() + inputTextViewSize + getPaddingBottom(), MeasureSpec.EXACTLY);
        measureChild(editText, widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int left = getPaddingStart();
            for (int i = 0; i < textViewList.size(); i++) {
                TextView textView = textViewList.get(i);
                textView.layout(left, getPaddingTop(), left + inputTextViewSize, getPaddingTop() + inputTextViewSize);
                left += inputTextViewSize + inputCodeSpace;
            }
            editText.layout(0, 0, editText.getMeasuredWidth(), editText.getMeasuredHeight());
        }
    }

    //初始化
    private void init(Context context, AttributeSet attributeSet) {
        //获取自定义属性的值
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.InputCodeView);
        inputCodeCount = typedArray.getInt(R.styleable.InputCodeView_inputCodeCount, 4);
        inputCodeTextSize = typedArray.getDimensionPixelSize(R.styleable.InputCodeView_inputCodeTextSize, 30);
        inputCodeSpace = typedArray.getDimensionPixelSize(R.styleable.InputCodeView_inputCodeSpace, 20);
        inputCodeTextColor = typedArray.getColor(R.styleable.InputCodeView_inputCodeTextColor, 0xff000000);
        inputTextIsPassword = typedArray.getBoolean(R.styleable.InputCodeView_inputCodePassword, false);
        inputCodeTextStyle = typedArray.getInt(R.styleable.InputCodeView_inputCodeTextStyle, 1);
        inputCodeBackground = typedArray.getResourceId(R.styleable.InputCodeView_inputCodeBackground, R.drawable.ts_view_input_code);
        typedArray.recycle();


        //添加TextView
        addTextView();
        //添加EditText
        addEditText();

        //设置当前输入位置
        setCurrentInputPosition(0);
        //设置点击事件
        setOnClickListener(this);
    }

    //添加EditText
    private void addEditText() {
        //添加EditText
        editText = new EditText(getContext());
        editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(inputCodeCount)});
        editText.setKeyListener(DigitsKeyListener.getInstance("1234567890"));
        editText.addTextChangedListener(this);
        editText.setAlpha(0f);
        editText.setLongClickable(false);

        //屏蔽长按复制粘贴等事件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            editText.setCustomInsertionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });


        addView(editText);
    }

    //添加TextView
    private void addTextView() {
        //添加TextView
        textViewList = new ArrayList<>();
        for (int i = 0; i < inputCodeCount; i++) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputCodeTextSize);
            textView.setTextColor(inputCodeTextColor);
            textView.getPaint().setFakeBoldText(inputCodeTextStyle == 0);
            textView.setBackgroundResource(inputCodeBackground);
            textViewList.add(textView);
            addView(textView);
        }
    }


    //弹出输入法
    public void showInputSoft() {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }
    }

    //设置当前输入的位置
    private void setCurrentInputPosition(int position) {
        for (int i = 0; i < textViewList.size(); i++) {
            textViewList.get(i).setSelected(i == position);
        }
    }


    @Override
    public void onClick(View v) {
        //当前控件点击事件-->显示软键盘
        showInputSoft();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }



    @Override
    public void afterTextChanged(Editable s) {
        //输入内容发送变化后，改变显示的内容
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (inputTextIsPassword) {
                textViewList.get(i).setText("●");
            } else {
                textViewList.get(i).setText(String.valueOf(c));
            }
        }
        for (int i = s.length(); i < textViewList.size(); i++) {
            textViewList.get(i).setText("");
        }
        //更新当前输入的位置
        setCurrentInputPosition(s.length());
        //判断是否输入完成触发事件
        if (s.length() == inputCodeCount && onInputFinishListener != null) {
            onInputFinishListener.onInoutFinish(s.toString());
        }
    }

    public interface OnInputFinishListener {
        void onInoutFinish(String inputCode);
    }

    public void setOnInputFinishListener(OnInputFinishListener onInputFinishListener) {
        this.onInputFinishListener = onInputFinishListener;
    }
}
