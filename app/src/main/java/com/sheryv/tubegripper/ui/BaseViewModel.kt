package com.sheryv.tubegripper.ui

import android.arch.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    abstract fun viewCreated()
}