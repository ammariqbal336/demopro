package com.base.events

sealed class BaseDataEvents {

    object ShowLoader : BaseDataEvents()
    object HideLoader : BaseDataEvents()
    class ShowSuccessDialog(val message: String,var messagedetail: String,var buttonText :String = "Done",var actionListener : () -> Unit): BaseDataEvents()
    class Exception(val message: String) : BaseDataEvents()
    class Error(val message: String) : BaseDataEvents()
    class Toast(val message: String) : BaseDataEvents()
    class ToastSuccess(val message: String) : BaseDataEvents()
    object ForceLogout : BaseDataEvents()
}