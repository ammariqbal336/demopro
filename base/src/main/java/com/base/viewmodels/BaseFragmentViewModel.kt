package com.base.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.base.events.BaseEvent
import com.base.viewmodels.BaseViewModel

open class BaseFragmentViewModel<T> : BaseViewModel() {
    protected var _nav_event: MutableLiveData<BaseEvent<T>> = MutableLiveData()
    var nav_event: LiveData<BaseEvent<T>> = _nav_event

}