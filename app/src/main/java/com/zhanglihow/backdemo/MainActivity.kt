package com.zhanglihow.backdemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.content.ComponentName


class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        whiteBtn.isClickable = true
        batteryBtn.isClickable = true

        whiteBtn.setOnClickListener {
            if (!isIgnoringBatteryOptimizations()) {
                Log.i("whiteBtn", "不在白名单中")
                requestIgnoreBatteryOptimizations()
            } else {
                Log.i("whiteBtn", "在白名单中")
            }
        }

        batteryBtn.setOnClickListener {
            when {
                isHuawei() -> goHuaweiSetting()
                isXiaomi() -> goXiaomiSetting()
                isMeizu() -> goMeizuSetting()
                isOPPO() -> goOPPOSetting()
                isSamsung() -> goSamsungSetting()
                isVIVO() -> goVIVOSetting()
                isLeTV() -> goLetvSetting()
                isSmartisan() -> goSmartisanSetting()
            }
        }
    }

    /**
     * 检查是否在白名单内
     */
    private fun isIgnoringBatteryOptimizations(): Boolean {
        var isIgnoring = false
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager?
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName)
        }
        return isIgnoring
    }

    /**
     * 跳转到设置白名单中
     */
    private fun requestIgnoreBatteryOptimizations() {
        try {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_CODE == requestCode) {
            if (isIgnoringBatteryOptimizations()) {
                Log.i("", "已开启白名单")
            } else {
                Log.i("", "没有开启白名单")
            }
        }
    }

    /**
     * 跳转到指定应用的首页
     */
    private fun showActivity(packageName: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(intent)
    }

    /**
     * 跳转到指定应用的指定页面
     */
    private fun showActivity(packageName: String, activityDir: String) {
        val intent = Intent()
        intent.component = ComponentName(packageName, activityDir)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    //华为
    @SuppressLint("DefaultLocale")
    fun isHuawei(): Boolean {
        return if (Build.BRAND == null) {
            false
        } else {
            Build.BRAND.toLowerCase() == "huawei" || Build.BRAND.toLowerCase() == "honor"
        }
    }

    private fun goHuaweiSetting() {
        try {
            showActivity(
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
            )
        } catch (e: Exception) {
            showActivity(
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.optimize.bootstart.BootStartActivity"
            )
        }
    }

    //小米
    fun isXiaomi(): Boolean {
        return Build.BRAND != null && Build.BRAND.toLowerCase() == "xiaomi"
    }

    private fun goXiaomiSetting() {
        showActivity(
            "com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity"
        )
    }

    //oppo
    fun isOPPO(): Boolean {
        return Build.BRAND != null && Build.BRAND.toLowerCase() == "oppo"
    }

    private fun goOPPOSetting() {
        try {
            showActivity("com.coloros.phonemanager")
        } catch (e1: Exception) {
            try {
                showActivity("com.oppo.safe")
            } catch (e2: Exception) {
                try {
                    showActivity("com.coloros.oppoguardelf")
                } catch (e3: Exception) {
                    showActivity("com.coloros.safecenter")
                }
            }
        }
    }

    //vivo
    fun isVIVO(): Boolean {
        return Build.BRAND != null && Build.BRAND.toLowerCase() == "vivo"
    }

    private fun goVIVOSetting() {
        showActivity("com.iqoo.secure")
    }

    //魅族
    public fun isMeizu(): Boolean {
        return Build.BRAND != null && Build.BRAND.toLowerCase() == "meizu"
    }

    private fun goMeizuSetting() {
        showActivity("com.meizu.safe")
    }

    //三星
    public fun isSamsung(): Boolean {
        return Build.BRAND != null && Build.BRAND.toLowerCase() == "samsung"
    }

    private fun goSamsungSetting() {
        try {
            showActivity("com.samsung.android.sm_cn");
        } catch (e: Exception) {
            showActivity("com.samsung.android.sm");
        }
    }

    //乐视
    public fun isLeTV(): Boolean {
        return Build.BRAND != null && Build.BRAND.toLowerCase() == "letv"
    }

    private fun goLetvSetting() {
        showActivity(
            "com.letv.android.letvsafe",
            "com.letv.android.letvsafe.AutobootManageActivity"
        )
    }

    //锤子
    fun isSmartisan(): Boolean {
        return Build.BRAND != null && Build.BRAND.toLowerCase() == "smartisan"
    }

    private fun goSmartisanSetting() {
        showActivity("com.smartisanos.security")
    }


}
