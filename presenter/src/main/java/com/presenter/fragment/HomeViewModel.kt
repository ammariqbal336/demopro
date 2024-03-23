package com.presenter.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.base.events.BaseEvent
import com.base.viewmodels.BaseViewModel
import com.domain.model.response.RoasterResponse
import com.domain.usecase.UseCase
import com.domain.usecase.home.UserRoasterUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRoasterUC: UserRoasterUC
): BaseViewModel() {

    private val _events = MutableLiveData<BaseEvent<HomeEvents>>()
    val events: LiveData<BaseEvent<HomeEvents>> = _events
    var list : List<RoasterResponse> = emptyList();
    init {
        Log.d("User Response","Before")
        getUserRoaster()
    }

    fun getUserRoaster(){

        viewModelScope.launch {
           showLoader()
            userRoasterUC(UseCase.None()).catch {
                hideLoader()
               handleExceptions(it)
            }.collect {
                hideLoader()
                list = it?.result ?: emptyList()
                onRoasterResponse(it?.result ?: emptyList<RoasterResponse>() )

            }
        }
    }

    fun onRoasterResponse(list: List<RoasterResponse>){
        _events.value = BaseEvent(HomeEvents.GetRoasterResponse(list))
    }

    fun filterData(nameToFilter: String){

        if(nameToFilter.isEmpty()){
            onRoasterResponse(list)
            return
        }
        val filteredClassData = list.filter { classData ->
            classData.registeredContacts!!.any { contact ->
                (contact.name?.contains(nameToFilter, ignoreCase = true) == true || contact.contactId.toString().contains(nameToFilter))
            }
        }.map { classData ->
            classData.copy(registeredContacts = classData.registeredContacts?.filter { it.name?.contains(nameToFilter, ignoreCase = true) == true || it.contactId.toString().contains(nameToFilter) })
        }
        Log.d("User Response Filter",filteredClassData.toString())
        onRoasterResponse(filteredClassData)
    }
}