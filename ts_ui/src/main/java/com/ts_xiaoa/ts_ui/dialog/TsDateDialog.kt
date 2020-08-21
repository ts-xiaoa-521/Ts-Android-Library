package com.ts_xiaoa.ts_ui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IntDef
import com.ts_xiaoa.ts_recycler_view.widget.wheel.WheelStringAdapter
import com.ts_xiaoa.ts_ui.R
import com.ts_xiaoa.ts_ui.base.BaseBottomDialogFragment
import com.ts_xiaoa.ts_ui.databinding.TsDialogDateBinding
import java.util.*

/**
 * create by ts_xiaoA on 2020-07-30 10:52
 * email：443502578@qq.com
 * desc：时间选择弹框
 */
class TsDateDialog private constructor() : BaseBottomDialogFragment() {

    override fun getLayoutId(): Int {
        return R.layout.ts_dialog_date
    }

    val binding by lazy { rootBinding as TsDialogDateBinding }

    //选择器类型
    private var pickerMode = 0

    //开始时间
    private var startTimeMillis: Long = 0

    //结束时间
    private var endTimeMillis: Long = 0

    //选中的时间
    private var selectTimeMillis: Long = 0

    //界面title
    private var dialogTitle: CharSequence? = ""

    //左边按钮的文字
    private var leftText: CharSequence? = "取消"

    //右边按钮的文字
    private var rightText: CharSequence? = "完成"

    //数据集合
    private var yearList: MutableList<Int>? = null
    private var monthList: MutableList<Int>? = null
    private var dayList: MutableList<Int>? = null
    private var hourList: MutableList<Int>? = null
    private var minuteList: MutableList<Int>? = null

    //显示adapter
    private var yearAdapter: WheelStringAdapter<Int>? = null
    private var monthAdapter: WheelStringAdapter<Int>? = null
    private var dayAdapter: WheelStringAdapter<Int>? = null
    private var hourAdapter: WheelStringAdapter<Int>? = null
    private var minuteAdapter: WheelStringAdapter<Int>? = null

    //选择完成按钮点击事件回调
    private var onSelectedFinishClickListener: (
        (year: Int, month: Int, day: Int, house: Int, minute: Int) -> Unit
    )? = null


    init {
        yearList = mutableListOf()
        monthList = mutableListOf()
        dayList = mutableListOf()
        hourList = mutableListOf()
        minuteList = mutableListOf()
        yearAdapter = WheelStringAdapter(data = yearList) { _, itemBinding, item ->
            itemBinding.tvText.text = String.format(Locale.CHINA, "%d年", item)
        }
        monthAdapter = WheelStringAdapter(data = monthList) { _, itemBinding, item ->
            itemBinding.tvText.text = String.format(Locale.CHINA, "%d月", item)
        }
        dayAdapter = WheelStringAdapter(data = dayList) { _, itemBinding, item ->
            itemBinding.tvText.text = String.format(Locale.CHINA, "%d日", item)
        }
        hourAdapter = WheelStringAdapter(data = hourList) { _, itemBinding, item ->
            itemBinding.tvText.text = String.format(Locale.CHINA, "%02d时", item)
        }
        minuteAdapter = WheelStringAdapter(data = minuteList) { _, itemBinding, item ->
            itemBinding.tvText.text = String.format(Locale.CHINA, "%02d分", item)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.tvTitle.text = dialogTitle
        binding.tvCancel.text = leftText ?: "取消"
        binding.tvFinish.text = rightText ?: "完成"
        binding.wvYear.adapter = yearAdapter
        binding.wvMonth.adapter = monthAdapter
        binding.wvDay.adapter = dayAdapter
        binding.wvHour.adapter = hourAdapter
        binding.wvMinute.adapter = minuteAdapter
        //选中的日期
        val selectCalendar = Calendar.getInstance()
        selectCalendar.timeInMillis = selectTimeMillis
        //初始化年份
        refreshYear()
        binding.wvYear.selectPosition = yearList!!.indexOf(selectCalendar[Calendar.YEAR])
        //初始化月份
        refreshMonth()
        binding.wvMonth.selectPosition = monthList!!.indexOf(selectCalendar[Calendar.MONTH] + 1)
        //初始化日期
        refreshDay()
        binding.wvDay.selectPosition = dayList!!.indexOf(selectCalendar[Calendar.DAY_OF_MONTH])
        //初始化小时
        refreshHour()
        binding.wvHour.selectPosition = hourList!!.indexOf(selectCalendar[Calendar.HOUR_OF_DAY])
        //初始化分钟
        refreshMinute()
        binding.wvMinute.selectPosition = minuteList!!.indexOf(selectCalendar[Calendar.MINUTE])
        when (pickerMode) {
            MODE_DATE -> {
                binding.wvYear.visibility = View.VISIBLE
                binding.wvMonth.visibility = View.VISIBLE
                binding.wvDay.visibility = View.VISIBLE
                binding.wvHour.visibility = View.GONE
                binding.wvMinute.visibility = View.GONE
            }
            MODE_TIME -> {
                binding.wvYear.visibility = View.GONE
                binding.wvMonth.visibility = View.GONE
                binding.wvDay.visibility = View.GONE
                binding.wvHour.visibility = View.VISIBLE
                binding.wvMinute.visibility = View.VISIBLE
            }
            MODE_DATE_TIME -> {
                binding.wvYear.visibility = View.VISIBLE
                binding.wvMonth.visibility = View.VISIBLE
                binding.wvDay.visibility = View.VISIBLE
                binding.wvHour.visibility = View.VISIBLE
                binding.wvMinute.visibility = View.VISIBLE
            }
        }
    }

    override fun initEvent(savedInstanceState: Bundle?) {
        //年滚动时
        binding.wvYear.onSelectedChangedListener = {
            refreshMonth()
            refreshDay()
            refreshHour()
            refreshMinute()
        }
        //月滚动时
        binding.wvMonth.onSelectedChangedListener = {
            refreshDay()
            refreshHour()
            refreshMinute()
        }
        //日滚动时
        binding.wvDay.onSelectedChangedListener = {
            refreshHour()
            refreshMinute()
        }
        //时滚动时
        binding.wvHour.onSelectedChangedListener = { refreshMinute() }
        //确定
        binding.tvFinish.setOnClickListener {
            onSelectedFinishClickListener?.invoke(
                yearList!![binding.wvYear.selectPosition],
                monthList!![binding.wvMonth.selectPosition],
                dayList!![binding.wvDay.selectPosition],
                if (pickerMode == MODE_DATE) 0 else hourList!![binding.wvHour.selectPosition],
                if (pickerMode == MODE_DATE) 0 else minuteList!![binding.wvMinute.selectPosition]
            )
            dismissAllowingStateLoss()
        }
        //取消
        binding.tvCancel.setOnClickListener { v -> dismiss() }
    }

    //初始化年份
    private fun refreshYear() {
        yearList!!.clear()
        val startCalendar = Calendar.getInstance()
        startCalendar.timeInMillis = startTimeMillis
        val endCalendar = Calendar.getInstance()
        endCalendar.timeInMillis = endTimeMillis
        for (i in startCalendar[Calendar.YEAR] until endCalendar[Calendar.YEAR] + 1) {
            yearList!!.add(i)
        }
        yearAdapter!!.notifyDataSetChanged()
    }

    //刷新月份
    private fun refreshMonth() {
        monthList!!.clear()
        val startCalendar = Calendar.getInstance()
        startCalendar.timeInMillis = startTimeMillis
        val endCalendar = Calendar.getInstance()
        endCalendar.timeInMillis = endTimeMillis
        //如果当前选中的是结束年。则月份不能超过结束年的月份,否则显示12个月
        if (startCalendar[Calendar.YEAR] == endCalendar[Calendar.YEAR]) {
            for (i in startCalendar[Calendar.MONTH]..endCalendar[Calendar.MONTH]) {
                monthList!!.add(i + 1)
            }
        } else if (yearList!![binding.wvYear.selectPosition] == startCalendar[Calendar.YEAR]) {
            for (i in startCalendar[Calendar.MONTH]..11) {
                monthList!!.add(i + 1)
            }
        } else if (yearList!![binding.wvYear.selectPosition] == endCalendar[Calendar.YEAR]) {
            for (i in 0 until endCalendar[Calendar.MONTH] + 1) {
                monthList!!.add(i + 1)
            }
        } else {
            for (i in 0..11) {
                monthList!!.add(i + 1)
            }
        }
        monthAdapter!!.notifyDataSetChanged()
    }

    //刷新日期
    private fun refreshDay() {
        dayList!!.clear()
        val startCalendar = Calendar.getInstance()
        startCalendar.timeInMillis = startTimeMillis
        val endCalendar = Calendar.getInstance()
        endCalendar.timeInMillis = endTimeMillis
        val endYear = endCalendar[Calendar.YEAR]
        val endMonth = endCalendar[Calendar.MONTH]
        if (startCalendar[Calendar.YEAR] == endCalendar[Calendar.YEAR]
            && startCalendar[Calendar.DAY_OF_YEAR] == endCalendar[Calendar.DAY_OF_YEAR]
            && startCalendar[Calendar.MONTH] == endCalendar[Calendar.MONTH]
        ) {
            for (i in startCalendar[Calendar.DAY_OF_MONTH] until endCalendar[Calendar.DAY_OF_MONTH] + 1) {
                dayList!!.add(i)
            }
        } else if (startCalendar[Calendar.YEAR] == yearList!![binding.wvYear.selectPosition]
            && startCalendar[Calendar.MONTH] == monthList!![binding.wvMonth.selectPosition] - 1
        ) {
            for (i in startCalendar[Calendar.DAY_OF_MONTH] until startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1) {
                dayList!!.add(i)
            }
        } else if (endYear == yearList!![binding.wvYear.selectPosition]
            && endMonth == monthList!![binding.wvMonth.selectPosition] - 1
        ) {
            for (i in 0 until endCalendar[Calendar.DAY_OF_MONTH]) {
                dayList!!.add(i + 1)
            }
        } else {
            endCalendar.clear()
            endCalendar.set(Calendar.YEAR, yearList!![binding.wvYear.selectPosition])
            endCalendar.set(Calendar.MONTH, monthList!![binding.wvMonth.selectPosition] - 1)
            //用下面的代码有问题，设置的值不会立刻生效
//            endCalendar[Calendar.YEAR] = yearList!![binding.wvYear.selectPosition]
//            endCalendar[Calendar.MONTH] = monthList!![binding.wvMonth.selectPosition] - 1
            val maxDays = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            Log.e(TAG, "refreshDay: ${endCalendar[Calendar.YEAR]}年${endCalendar[Calendar.MONTH]}月 ${maxDays}日")
            for (i in 0 until maxDays) {
                dayList!!.add(i + 1)
            }
        }
        dayAdapter!!.notifyDataSetChanged()
    }

    //刷新小时
    private fun refreshHour() {
        if (pickerMode == MODE_DATE) return
        hourList!!.clear()
        val startCalendar = Calendar.getInstance()
        startCalendar.timeInMillis = startTimeMillis
        val endCalendar = Calendar.getInstance()
        endCalendar.timeInMillis = endTimeMillis
        val year = yearList!![binding.wvYear.selectPosition]
        val month = monthList!![binding.wvMonth.selectPosition]
        val day = dayList!![binding.wvDay.selectPosition]
        //如果选中的是开始日期
        if (startCalendar[Calendar.YEAR] == endCalendar[Calendar.YEAR]
            && startCalendar[Calendar.DAY_OF_YEAR] == endCalendar[Calendar.DAY_OF_YEAR]
        ) {
            //如果开始和结束日期为同一天
            val startHour = startCalendar[Calendar.HOUR_OF_DAY]
            val endHour = endCalendar[Calendar.HOUR_OF_DAY]
            for (i in startHour until endHour) {
                hourList!!.add(i)
            }
        } else if (startCalendar[Calendar.YEAR] == year && startCalendar[Calendar.MONTH] == month - 1 && startCalendar[Calendar.DAY_OF_MONTH] == day
        ) {
            //如果选中的日期为开始的日期
            val startHour = startCalendar[Calendar.HOUR_OF_DAY]
            for (i in startHour..23) {
                hourList!!.add(i)
            }
        } else if (endCalendar[Calendar.YEAR] == year && endCalendar[Calendar.MONTH] == month - 1 && endCalendar[Calendar.DAY_OF_MONTH] == day
        ) {
            //如果选中的日期为结束日期
            val endHour = startCalendar[Calendar.HOUR_OF_DAY]
            for (i in 0 until endHour) {
                hourList!!.add(i)
            }
        } else {
            for (i in 0..23) {
                hourList!!.add(i)
            }
        }
        hourAdapter!!.notifyDataSetChanged()
    }

    //刷新分钟数
    private fun refreshMinute() {
        if (pickerMode == MODE_DATE) return
        minuteList!!.clear()
        val startCalendar = Calendar.getInstance()
        startCalendar.timeInMillis = startTimeMillis
        val endCalendar = Calendar.getInstance()
        endCalendar.timeInMillis = endTimeMillis
        val year = yearList!![binding.wvYear.selectPosition]
        val month = monthList!![binding.wvMonth.selectPosition]
        val day = dayList!![binding.wvDay.selectPosition]
        val hour = hourList!![binding.wvHour.selectPosition]
        //如果选中的是开始日期
        if (startCalendar[Calendar.YEAR] == endCalendar[Calendar.YEAR] && startCalendar[Calendar.DAY_OF_YEAR] == endCalendar[Calendar.DAY_OF_YEAR] && startCalendar[Calendar.HOUR_OF_DAY] == endCalendar[Calendar.HOUR_OF_DAY]
        ) {
            //如果开始和结束日期为同一天同一小时
            val startMinute = startCalendar[Calendar.MINUTE]
            val endMinute = endCalendar[Calendar.MINUTE]
            for (i in startMinute until endMinute) {
                minuteList!!.add(i)
            }
        } else if (startCalendar[Calendar.YEAR] == year && startCalendar[Calendar.MONTH] == month - 1 && startCalendar[Calendar.DAY_OF_MONTH] == day && startCalendar[Calendar.HOUR_OF_DAY] == hour
        ) {
            //如果选中的日期为开始的日期
            val startMinute = startCalendar[Calendar.MINUTE]
            for (i in startMinute..59) {
                minuteList!!.add(i)
            }
        } else if (endCalendar[Calendar.YEAR] == year && endCalendar[Calendar.MONTH] == month - 1 && endCalendar[Calendar.DAY_OF_MONTH] == day && endCalendar[Calendar.HOUR_OF_DAY] == hour
        ) {
            //如果选中的日期为结束日期
            val endMinute = startCalendar[Calendar.MINUTE]
            for (i in 0 until endMinute) {
                minuteList!!.add(i)
            }
        } else {
            for (i in 0..59) {
                minuteList!!.add(i)
            }
        }
        minuteAdapter!!.notifyDataSetChanged()
    }

    @IntDef(MODE_DATE, MODE_TIME, MODE_DATE_TIME)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class PickerMode

    class Builder {


        //界面title
        private var dialogTitle: CharSequence? = null

        //左边按钮的文字
        private var leftText: CharSequence? = null

        //右边按钮的文字
        private var rightText: CharSequence? = null

        //类型：默认MODE_DATE 只选择日期
        private var pickerMode: Int = MODE_DATE

        //开始时间
        private var startTimeMillis: Long = 0

        //结束时间
        private var endTimeMillis: Long = 0

        //选中的时间
        private var selectTimeMillis: Long = 0

        //设置结束时间为当前时间之后的多长时间
        private var afterField = -1000
        private var afterAmount = -1000

        //选择完成按钮点击事件回调
        private var onSelectedFinishClickListener: (
            (year: Int, month: Int, day: Int, house: Int, minute: Int) -> Unit
        )? = null

        //左边按钮文字
        fun setTitleText(titleText: CharSequence? = null): Builder {
            this.dialogTitle = titleText
            return this
        }

        //左边按钮文字
        fun setLeftText(leftText: CharSequence? = null): Builder {
            this.leftText = leftText
            return this
        }

        //右边按钮文字
        fun setRightText(rightText: CharSequence? = null): Builder {
            this.rightText = rightText
            return this
        }


        fun setPickerMode(@PickerMode mode: Int): Builder {
            pickerMode = mode
            return this
        }

        /**
         * 设置开始时间
         *
         * @param startTimeMillis 开始时间的时间戳
         * @return DateDialog.Builder
         */
        fun setStartTime(startTimeMillis: Long): Builder {
            this.startTimeMillis = startTimeMillis
            return this
        }

        /**
         * 设置开始时间
         *
         * @param year  年
         * @param month 月
         * @param day   日
         * @return DateDialog.Builder
         */
        fun setStartTime(year: Int, month: Int, day: Int): Builder {
            val calendar = Calendar.getInstance()
            require(year.toString().length == 4) { "年份只有四位数，别乱搞啊" }
            calendar[Calendar.YEAR] = year
            require(!(month < 0 || month > 12)) { "月份的范围是1-12，心累" }
            calendar[Calendar.MONTH] = month - 1
            val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            require(!(day < 0 || day > maxDays)) {
                String.format(
                    Locale.CHINA,
                    "%d年%d月的日期范围为：1-%d",
                    year,
                    month,
                    maxDays
                )
            }
            calendar[Calendar.DAY_OF_MONTH] = day
            startTimeMillis = calendar.timeInMillis
            return this
        }

        /**
         * 设置结束时间
         *
         * @param endTimeMillis 开始时间的时间戳
         * @return DateDialog.Builder
         */
        fun setEndTime(endTimeMillis: Long): Builder {
            this.endTimeMillis = endTimeMillis
            return this
        }

        /**
         * 设置结束时间
         * 如field = Calendar.YEAR amount = 1
         * 表示结束时间在一年后
         * @param field  单位 如 Calendar.YEAR, 年
         * @param amount 数量 如：1
         *
         * @return
         */
        fun setEndTimeAfter(field: Int, amount: Int): Builder {
            afterField = field
            afterAmount = amount
            return this
        }

        /**
         * 设置结束时间
         *
         * @param year  年
         * @param month 月
         * @param day   日
         * @return DateDialog.Builder
         */
        fun setEndTime(year: Int, month: Int, day: Int): Builder {
            val calendar = Calendar.getInstance()
            require(year.toString().length == 4) { "年份只有四位数，别乱搞啊" }
            calendar[Calendar.YEAR] = year
            require(!(month < 0 || month > 12)) { "月份的范围是1-12，心累" }
            calendar[Calendar.MONTH] = month - 1
            val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            require(!(day < 0 || day > maxDays)) {
                String.format(
                    Locale.CHINA,
                    "%d年%d月的日期范围为：1-%d",
                    year,
                    month,
                    maxDays
                )
            }
            calendar[Calendar.DAY_OF_MONTH] = day
            endTimeMillis = calendar.timeInMillis
            return this
        }

        /**
         * 设置选中的时间
         *
         * @param selectTimeMillis 选中的时间戳
         * @return DateDialog.Builder
         */
        fun setSelectTime(selectTimeMillis: Long): Builder {
            this.selectTimeMillis = selectTimeMillis
            return this
        }

        /**
         * 设置选中的时间
         *
         * @param year  年
         * @param month 月
         * @param day   日
         * @return DateDialog.Builder
         */
        fun setSelectTime(year: Int, month: Int, day: Int): Builder {
            val calendar = Calendar.getInstance()
            require(year.toString().length == 4) { "年份只有四位数，别乱搞啊" }
            calendar[Calendar.YEAR] = year
            require(!(month < 0 || month > 12)) { "月份的范围是1-12，心累" }
            calendar[Calendar.MONTH] = month - 1
            val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            require(!(day < 0 || day > maxDays)) {
                String.format(
                    Locale.CHINA,
                    "%d年%d月的日期范围为：1-%d",
                    year,
                    month,
                    maxDays
                )
            }
            calendar[Calendar.DAY_OF_MONTH] = day
            selectTimeMillis = calendar.timeInMillis
            return this
        }

        /**
         * 确定按钮的点击事件
         *
         * @param onSelectedFinishClickListener 回调监听
         * @return DateDialog.Builder
         */
        fun setOnSelectedFinishClickListener(onSelectedFinishClickListener: (year: Int, month: Int, day: Int, house: Int, minute: Int) -> Unit): Builder {
            this.onSelectedFinishClickListener = onSelectedFinishClickListener
            return this
        }

        /**
         * 构建一个DateDialog对象
         *
         * @return DateDialog
         */
        @SuppressLint("WrongConstant")
        fun build(): TsDateDialog {
            //默认开始时间1900年
            if (startTimeMillis == 0L) {
                val start = Calendar.getInstance()
                start[Calendar.YEAR] = 1900
                start[Calendar.MONTH] = 0
                start[Calendar.DAY_OF_MONTH] = 1
                startTimeMillis = start.timeInMillis
            }
            //默认结束时间，当前时间
            if (endTimeMillis == 0L) {
                if (afterField != -1000) {
                    val instance = Calendar.getInstance()
                    instance.timeInMillis = startTimeMillis
                    instance.add(afterField, afterAmount)
                    endTimeMillis = instance.timeInMillis
                } else {
                    endTimeMillis = Calendar.getInstance().timeInMillis
                }
            }
            //默认选中时间 开始时间
            if (selectTimeMillis == 0L) {
                selectTimeMillis = startTimeMillis
            }
            require(!(selectTimeMillis < startTimeMillis || selectTimeMillis > endTimeMillis)) { "选中的时间必须要在开始时间和结束时间范围之内" }
            val dateDialog = TsDateDialog()
            dateDialog.startTimeMillis = this.startTimeMillis
            dateDialog.endTimeMillis = this.endTimeMillis
            dateDialog.selectTimeMillis = this.selectTimeMillis
            dateDialog.onSelectedFinishClickListener = this.onSelectedFinishClickListener
            dateDialog.pickerMode = this.pickerMode
            return dateDialog
        }

    }

    companion object {
        //是显示日期（默认）
        const val MODE_DATE = 1

        //只显示时间
        const val MODE_TIME = 2

        //时间和日期
        const val MODE_DATE_TIME = 3
    }


}