package com.ts_xiaoa.ts_base.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * create by ts_xiaoA on 2020-07-16 10:28
 * email：443502578@qq.com
 * desc：String相关顶层扩展函数
 */

//判断字符串是否为手机号
fun String?.isPhone(): Boolean {
    val phoneNumber = this ?: return false
    var isValid = false
    if (phoneNumber.length != 11) {
        return false
    }
    val expression = "(^(1)[0-9]{10}$)"
    val pattern = Pattern.compile(expression)
    val matcher = pattern.matcher(phoneNumber)
    if (matcher.matches()) {
        isValid = true
    }
    return isValid
}

//判断是否为邮箱
fun String?.isEmail(): Boolean {
    val email = this ?: return false
    var isValid = false
    val pattern =
        Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")
    val matcher = pattern.matcher(email)
    if (matcher.matches()) {
        isValid = true
    }
    return isValid
}

/**
 * 判断是否为身份证号
 */
@SuppressLint("SimpleDateFormat")
fun String?.isIdCard(): Boolean {
    val idStr = this ?: return false
    val valCodeArr =
        arrayOf("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2")
    val wi = arrayOf(
        "7", "9", "10", "5", "8", "4", "2", "1", "6",
        "3", "7", "9", "10", "5", "8", "4", "2"
    )
    var ai: String
    // ================ 号码的长度18位 ================
    // ================ 号码的长度18位 ================
    if (idStr.length != 18) {
        return false
    }
    // ================ 数字 除最后以为都为数字 ================
    // ================ 数字 除最后以为都为数字 ================
    ai = idStr.substring(0, 17)
    if (!ai.isNumber()) { //errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
        return false
    }
    // ================ 出生年月是否有效 ================
    // ================ 出生年月是否有效 ================
    val strYear = ai.substring(6, 10) // 年份

    val strMonth = ai.substring(10, 12) // 月份

    val strDay = ai.substring(12, 14) // 日

    if (!"$strYear-$strMonth-$strDay".isDate()) { //          errorInfo = "身份证生日无效。";
        return false
    }
    val gc = GregorianCalendar()
    val s = SimpleDateFormat("yyyy-MM-dd")
    try {
        if (gc[Calendar.YEAR] - strYear.toInt() > 150 || gc.time.time - s.parse("$strYear-$strMonth-$strDay").time < 0
        ) { //errorInfo = "身份证生日不在有效范围。";
            return false
        }
    } catch (e: NumberFormatException) {
        return false
    } catch (e: ParseException) {
        return false
    }
    if (strMonth.toInt() > 12 || strMonth.toInt() == 0) { //errorInfo = "身份证月份无效";
        return false
    }
    if (strDay.toInt() > 31 || strDay.toInt() == 0) { //errorInfo = "身份证日期无效";
        return false
    }
    // ================ 地区码时候有效 ================
    // ================ 地区码时候有效 ================
    val h: Hashtable<String, String> = getAreaCode()
    if (h[ai.substring(0, 2)] == null) { //errorInfo = "身份证地区编码错误。";
        return false
    }
    // ================ 判断最后一位的值 ================
    // ================ 判断最后一位的值 ================
    var totalmulAiWi = 0
    for (i in 0..16) {
        totalmulAiWi += ai[i].toString().toInt() * wi[i].toInt()
    }
    val modValue = totalmulAiWi % 11
    val strVerifyCode = valCodeArr[modValue]
    ai += strVerifyCode
    return ai == idStr
}

val String?.webData: String
    get() {
        return "<html><head><meta name=\"viewport\" content=\"width=device-width, " +
                "initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\" />" +
                "<style>img{max-width:100% !important;height:auto !important;}</style>$this</body></html>"
    }

/**
 * 功能：设置地区编码
 *
 * @return Hashtable 对象
 */
private fun getAreaCode(): Hashtable<String, String> {
    val hashtable: Hashtable<String, String> = Hashtable()
    hashtable["11"] = "北京"
    hashtable["12"] = "天津"
    hashtable["13"] = "河北"
    hashtable["14"] = "山西"
    hashtable["15"] = "内蒙古"
    hashtable["21"] = "辽宁"
    hashtable["22"] = "吉林"
    hashtable["23"] = "黑龙江"
    hashtable["31"] = "上海"
    hashtable["32"] = "江苏"
    hashtable["33"] = "浙江"
    hashtable["34"] = "安徽"
    hashtable["35"] = "福建"
    hashtable["36"] = "江西"
    hashtable["37"] = "山东"
    hashtable["41"] = "河南"
    hashtable["42"] = "湖北"
    hashtable["43"] = "湖南"
    hashtable["44"] = "广东"
    hashtable["45"] = "广西"
    hashtable["46"] = "海南"
    hashtable["50"] = "重庆"
    hashtable["51"] = "四川"
    hashtable["52"] = "贵州"
    hashtable["53"] = "云南"
    hashtable["54"] = "西藏"
    hashtable["61"] = "陕西"
    hashtable["62"] = "甘肃"
    hashtable["63"] = "青海"
    hashtable["64"] = "宁夏"
    hashtable["65"] = "新疆"
    hashtable["71"] = "台湾"
    hashtable["81"] = "香港"
    hashtable["82"] = "澳门"
    hashtable["91"] = "国外"
    return hashtable
}


/**
 * 判断是否为数字
 */
private fun String.isNumber(): Boolean {
    val pattern = Pattern.compile("[0-9]*")
    val isNum = pattern.matcher(this)
    return isNum.matches()
}

/**
 * 判断是否为日期
 */
private fun String.isDate(): Boolean {
    val pattern = Pattern
        .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$")
    val m = pattern.matcher(this)
    return m.matches()
}