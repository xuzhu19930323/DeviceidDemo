package com.qiandai.zhudb.ui.theme.utils

import android.app.Activity
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.qiandai.zhudb.ui.theme.MyApplication
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * 通用工具类，定义一些程序所需用到的方法和常量
 * @author hz 2022-11-7
 */
object CommUtil {
    
    private const val TAG = "CommUtil"
    private const val EMPTY_STRING = ""
    
    const val TIME_PATTERN_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss"
    /**
     * enum:空格
     */
    const val EMPTY_SPLIT = " "

    private var customLastClickTime: Long = 0

    /**
     * 通用info的log日志
     */
    fun logI(msg: String, tag: String = TAG) {
        println("$tag 打印 $msg")
    }

    /**
     * 防止多次点击:为true则为多次点击，
     * 只响应第一次行为，剩余事件应该拦截
     */
    fun isCustomFastClick(delayTime: Long = 200L): Boolean {
        var flag = true
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - customLastClickTime >= delayTime) {
            flag = false
        }
        customLastClickTime = currentClickTime
        return flag
    }
    
    /**
     * 中国大陆手机号正则，不含港澳台地区
     */
    fun isValidChineseMobile(phoneNumber: String): Boolean {
        val pattern = "^1[3-9]\\d{9}$".toRegex()
        return pattern.matches(phoneNumber)
    }
    
    /**
     * 中国邮政编码正则
     */
    fun isValidPostalCode(postalCode: String): Boolean {
        val pattern = "^[1-9]\\d{5}$".toRegex()
        return pattern.matches(postalCode)
    }

    fun isValidIDCard(idCard: String): Boolean {
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        val pattern =
            ("(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)" +
                    "|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)").toRegex()

        return pattern.matches(idCard)
    }

    /**
     * 剔除空格
     */
    fun replaceEmpty(string: String?) = string?.replace(EMPTY_SPLIT, EMPTY_STRING) ?: EMPTY_STRING

    /**
     * 时间戳转换为日期字符串(2022-06-16)
     */
    fun getFormatTime(date: Long, pattern: String = "yyyy-MM-dd"): String? {
        val format = SimpleDateFormat(pattern, Locale.CHINA)
        return format.format(date)
    }

    /**
     * 清除edittext的焦点
     * @param rootView View?
     * @param viewIds IntArray
     */
    fun clearFocus(rootView: View? = null, @IdRes vararg viewIds: Int) {
        for (viewId in viewIds) {
            rootView?.findViewById<EditText>(viewId)?.clearFocus()
        }
    }

    /**
     * 判断是否为奇数
     */
    fun isOddNumber(value: Int) = value % 2 != 0

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    fun getSystemModel(): String? {
        return Build.MODEL
    }

    /**
     * 获取手机厂商名
     *
     * @return 手机厂商名
     */
    fun getDeviceManufacturer(): String? {
        return Build.MANUFACTURER
    }
    
    /**
     *  在Android11上获取IMEI号等设备信息需要android.permission.READ_PRIVILEGED_PHONE_STATE权限，
     *  而这个权限又只授予系统级应用。项目中如果targetSdkVersion值小于29获取到的是null，大于28报SecurityException错误
     */
    @JvmStatic
    fun getAndroidUniqueID(): String {
//        val uniqueId = GlobalParams.getDeviceUniqueId()
//        logI("hzz 从缓存取出 uniqueId=$uniqueId")
//        if (uniqueId.isNotEmpty()) {
//            return uniqueId
//        }
        
        //在target 33拿不到权限，直接获取会崩溃
//        val telephonyManager = MyApplication.appContext.getSystemService(TELEPHONY_SERVICE)
//                as? TelephonyManager ?: return ""
////        val mSzImei = telephonyManager.deviceId
//        val simSerialNumber = telephonyManager.simSerialNumber
    
        //在Android 12及以上版本中，getMacAddress() 方法将返回“02:00:00:00:00:00”作为MAC地址，从而导致某些功能无法正常使用
//        val wifiManager = MyApplication.appContext.getSystemService(Context.WIFI_SERVICE)
//                as? WifiManager ?: return ""
//        val mSzWLANMAC = wifiManager.connectionInfo.macAddress
        
//        logI("hzz mSzWLANMAC=$mSzWLANMAC")
        var androidId: String? = Settings.Secure.getString(
            MyApplication.appContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val mSzDevIDShort = with(StringBuilder()) {
            append("35")
            append(Build.BOARD.length % 10)
            append(Build.BRAND.length % 10)
            append(Build.CPU_ABI.length % 10)
            append(Build.DEVICE.length % 10)
            append(Build.DEVICE.length % 10)
            append(Build.DISPLAY.length % 10)
            append(Build.HOST.length % 10)
            append(Build.ID.length % 10)
            append(Build.MANUFACTURER.length % 10)
            append(Build.MODEL.length % 10)
            append(Build.PRODUCT.length % 10)
            append(Build.TAGS.length % 10)
            append(Build.TYPE.length % 10)
            append(Build.USER.length % 10) //13位
            toString()
        }
//        androidId=135277cad1ecd097
//        mSzDevIDShort=3536177174688244  更换为arm-64，mSzDevIDShort=3536977174688244
//        4A44BCDD61D8F6AA8FF5F9A5328AB70F， D70A082919FE64409A438F2BF51FD4F2
        logI("hzz androidId=$androidId")
        logI("hzz mSzDevIDShort=$mSzDevIDShort")
        androidId = md5(mSzDevIDShort.plus(androidId))
        logI("hzz uniqueId=$androidId")
//        GlobalParams.setDeviceUniqueId(androidId)
        return androidId
    }
//    D266C252C945ADBBFFE2A7EE0F1C20C8 小米
//    D266C252C945ADBBFFE2A7EE0F1C20C8
//    4A44BCDD61D8F6AA8FF5F9A5328AB70F 华为
    private fun md5(mSzLongID: String): String {
        var messageDigest: MessageDigest? = null
        try {
            messageDigest = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
           //ignore
        }
        return messageDigest?.let { m ->
            m.update(mSzLongID.toByteArray(), 0, mSzLongID.length)
            val pMd5Data = m.digest()
            var mSzUniqueID = ""
            for (i in pMd5Data.indices) {
                val b = 0xFF and pMd5Data[i].toInt()
                if (b <= 0xF) mSzUniqueID += "0"
                mSzUniqueID += Integer.toHexString(b)
            }
            mSzUniqueID = mSzUniqueID.uppercase(Locale.getDefault())
            mSzUniqueID
        }?: ""
    }

    /**
     * 判断GPS是否打开
     */
    fun isGpsEnabled(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val accessibleProviders = lm.getProviders(true)
        return accessibleProviders.size > 0
    }
    
    /**
     * 获取url参数value
     */
    fun getQueryParam(url: String?, key: String?): String? {
        if (url.isNullOrEmpty() || key.isNullOrEmpty()) {
            return null
        }
        return Uri.parse(url)?.getQueryParameter(key)
    }

    /**
     * 得到后缀 eg:11.jpeg, print jpeg
     */
    fun getSuffix(fileName: String) = fileName.substring(fileName.lastIndexOf(".") + 1)

    /**
     * 获取小数点位数
     */
    fun scale(double: Double) = double.toString().length - double.toString().indexOf(".") - 1
    

    /**
     * 获取设备非原始的屏幕尺寸，是去除了虚拟按键后的高度
     * @param context 上下文
     * @return 屏幕尺寸
     */
    fun getHeight(context: Activity): IntArray {
        val outMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels
        val heightPixels = outMetrics.heightPixels
        logI(
            "widthPixels = $widthPixels,heightPixels = $heightPixels,statusBarHeight=${
                getStatusBarHeight(context)
            }"
        )
        return intArrayOf(widthPixels, heightPixels)
    }

    /**
     * 获取设备原始的屏幕尺寸
     * @param context 上下文
     * @return 屏幕尺寸
     */
    fun getRealHeight(context: Activity): IntArray {
        val outMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.windowManager.defaultDisplay.getRealMetrics(outMetrics)
        }
        val widthPixels = outMetrics.widthPixels
        val heightPixels = outMetrics.heightPixels
        logI(
            "widthPixels = $widthPixels,heightPixels = $heightPixels,statusBarHeight=${
                getStatusBarHeight(context)
            }"
        )
        return intArrayOf(widthPixels, heightPixels)
    }

    /**
     * 获取设备状态栏高度
     * @param context 上下文
     * @return 状态栏高度
     */
    fun getStatusBarHeight(context: Activity): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 对象深拷贝
     * @param src 被拷贝对象
     * @return 拷贝后对象
     * @throws RuntimeException
     */
    @Throws(RuntimeException::class)
    fun deepCopy(src: Any?): Any? {
        if (src !is Serializable) return null
        val memoryBuffer = ByteArrayOutputStream()
        var out: ObjectOutputStream? = null
        var `in`: ObjectInputStream? = null
        val dist: Any?
        try {
            out = ObjectOutputStream(memoryBuffer)
            out.writeObject(src)
            out.flush()
            `in` = ObjectInputStream(ByteArrayInputStream(memoryBuffer.toByteArray()))
            dist = `in`.readObject()
        } catch (e: Exception) {
            throw RuntimeException(e)
        } finally {
            if (out != null) try {
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (`in` != null) try {
                `in`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return dist
    }

    /**
     * 在协程中使用
     */
    @Throws(RuntimeException::class)
    suspend fun deepCopyInLaunch(src: Any?): Any? {
        return deepCopy(src)
    }
    
    fun getItemView(context: Context, parent: ViewGroup?, @LayoutRes layoutResId: Int): View {
        return LayoutInflater.from(context).inflate(layoutResId, parent, false)
    }
    
    /**
     * 判断一个字符串是否为一个数值，正负数，小数都可以验证
     * @param string 字符串
     * @return true: 是数值类型 false：不是
     */
    fun isNumberDecimal(string: String?): Boolean {
        if (!string.isNullOrEmpty()) {
            val digitalPattern = Pattern.compile("^[-+]?[0-9]+(\\.[0-9]+)?$")
            val matcher = digitalPattern.matcher(string)
            return matcher.matches()
        }
        return false
    }
    
    /**
     * 忽略电池优化
     */
    fun ignoreBatteryOptimization(activity: Activity, block: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = activity.getSystemService(POWER_SERVICE) as PowerManager
            val hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.packageName)
            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
            if (!hasIgnored) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:" + activity.packageName)
                if (intent.resolveActivity(activity.packageManager) != null) {
                    activity.startActivityForResult(intent, 10000)
                }
            } else {
                logI("高德地图 手机已开启电池省电忽略")
                block()
            }
        }
    }
}