package com.qiandai.zhudb

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.qiandai.zhudb.ui.theme.utils.CommUtil


//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import com.qiandai.zhudb.ui.theme.DeviceIdDemoTheme

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkPermission()) {
            println("hzz 设备号="+CommUtil.getAndroidUniqueID())
        } else {
            requestPermission()
        }
    
//        println("hzz 设备号="+CommUtil.getAndroidUniqueID())
    }
    
    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.READ_PHONE_STATE),
            1
        )
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被用户同意，可以执行相关操作
                println("hzz 设备号="+CommUtil.getAndroidUniqueID())
            } else {
                // 权限被用户拒绝，需要进一步操作，比如引导用户去设置页面开启权限
            }
        }
    }
}
