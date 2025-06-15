package com.qiandai.zhudb.ui.theme.utils

import com.qiandai.zhudb.ui.theme.MyApplication
import java.io.File

class GlobalParams private constructor() {
    
    companion object {
        
        private val file by lazy {
            val file = File("${MyApplication.appContext.getExternalFilesDir("")}/id_device.txt")
            if (true != file.parentFile?.exists()) {
                file.mkdirs()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            file
        }
        
        fun getDeviceUniqueId() = file.readText()
    
        fun setDeviceUniqueId(deviceId: String) {
            Thread { file.writeText(deviceId) }.start()
        }
    }
}