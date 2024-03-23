package com.base.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.base.events.BaseDataEvents
import com.base.events.BaseEvent
import com.domain.exceptions.*
import java.io.EOFException

open class BaseViewModel : ViewModel() {
    val TAG = this::class.java.name
    protected val _dataEvent: MutableLiveData<BaseEvent<BaseDataEvents>> = MutableLiveData()
    val obDataEvent: LiveData<BaseEvent<BaseDataEvents>> = _dataEvent

    suspend fun handleExceptions(exception: Throwable): HashMap<String, String> {
        val errors: HashMap<String, String> = HashMap()
        when (exception) {
            is ConnectivityException ->{
                showError(exception.message ?: "")
                errors.put("error", exception.message ?: "")
            }
            is _401Exception, is _422Exception ->{
                showError(exception.message ?: "")
                errors.put("error", exception.message ?: "")
            }
            is _404Exception, is ServerException ->{
                showError(exception.message ?: "")
                errors.put("error", exception.message ?: "")
               // Sentry.captureException(exception)
            }
            is UnProcessableEntityException -> {
                errors.putAll(exception.errorMap)
                exception.errorMap.forEach {
                    showError(it.value)
                }
               // Sentry.captureException(exception)
            }

            is EOFException -> {
                errors.put("error", exception.message ?: "")
            }
            else->{
                showError(exception.message ?: "")
                errors.put("error", exception.message ?: "")
                //Sentry.captureException(exception)
            }
        }


        return errors
    }

    protected fun showError(message: String) {
        if(message.contains("Unauthorized") || message.contains("Unauthenticated")) {
            forceLogout()
        } else {
            _dataEvent.value = (BaseEvent(BaseDataEvents.Error(message)))
        }
    }
    protected fun showException(exception: Exception) {

    }

    protected fun showLoader() {
        _dataEvent.value = BaseEvent(BaseDataEvents.ShowLoader)
    }

    protected fun hideLoader() {
        _dataEvent.value = BaseEvent(BaseDataEvents.HideLoader)
    }


    protected fun showToast(message: String) {
        _dataEvent.value = BaseEvent(BaseDataEvents.Toast(message))
    }

    protected fun showToastSuccess(message: String) {
        _dataEvent.value = BaseEvent(BaseDataEvents.ToastSuccess(message))
    }

    private fun logout() {
        _dataEvent.value = BaseEvent(BaseDataEvents.ForceLogout)
    }

    protected fun showSuccessDialog(message: String,messageDetail:String,buttonText:String = "",actionListenr : ()->Unit) {
        _dataEvent.value = BaseEvent(BaseDataEvents.ShowSuccessDialog(message,messageDetail,buttonText,actionListenr))
    }


    //    this method is dummy just to get forcelogout in baseViewModel,
//    actual method exist in BaseActivityViewModel which will be called by Base Activity whenever
//    logout is requried for example unauthenticated error
    open fun forceLogout() {
        _dataEvent.value = BaseEvent(BaseDataEvents.ForceLogout)
    }
}