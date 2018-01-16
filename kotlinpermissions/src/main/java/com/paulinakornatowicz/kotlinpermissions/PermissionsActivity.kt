package com.paulinakornatowicz.kotlinpermissions

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast

abstract class PermissionsActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION = 1111
    private val NEEDED_PERMISSIONS = 2222
    var pCallback: PermissionCallBack? = null
    var permissionsNeed: MutableList<String> = mutableListOf()

    fun requestPermissions(arrays: Array<String>, permissionCallback: PermissionCallBack) {
        permissionsNeed.clear()
        pCallback = permissionCallback
        arrays
                .filter { ActivityCompat.checkSelfPermission(applicationContext, it) != PackageManager.PERMISSION_GRANTED }
                .forEach { permissionsNeed.add(it) }
        if (permissionsNeed.size > 0) {
            Log.v("request", "permissions")
            requestNeededPermission(permissionsNeed)
        } else {
            toast("Permissions Granted")
        }
    }

    fun requestPermissions(permission: String, permissionCallback: PermissionCallBack) {
        pCallback = permissionCallback
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(permission),
                    REQUEST_PERMISSION)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission),
                    REQUEST_PERMISSION)
        }
    }

    private fun requestNeededPermission(permissionsNeed: MutableList<String>) {
        // if (ActivityCompat.shouldShowRequestPermissionRationale(this@PermissionsActivity,permissionsNeed.toTypedArray()))
        ActivityCompat.requestPermissions(this@PermissionsActivity, permissionsNeed.toTypedArray(), NEEDED_PERMISSIONS)
    }

    private fun AppCompatActivity.toast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (grantResults.isNotEmpty()) {
            Log.v("results", "" + grantResults[0] + grantResults.toString())
            if (requestCode == REQUEST_PERMISSION) {
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pCallback?.permissionGranted()
                } else {
                    pCallback?.permissionDenied()
                }
            } else if (requestCode == NEEDED_PERMISSIONS) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pCallback?.permissionGranted()
                } else {
                    pCallback?.permissionDenied()
                }

            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}
