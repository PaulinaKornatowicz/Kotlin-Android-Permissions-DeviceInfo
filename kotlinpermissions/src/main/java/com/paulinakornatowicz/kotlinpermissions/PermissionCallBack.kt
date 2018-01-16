package com.paulinakornatowicz.kotlinpermissions


interface PermissionCallBack {

    fun permissionGranted() {
    }

    fun permissionDenied() {
    }
}