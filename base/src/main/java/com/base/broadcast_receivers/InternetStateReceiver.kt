package com.base.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.base.ui.BaseActivity
import com.base.util.InternetConnection
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InternetStateReceiver @Inject constructor() : BroadcastReceiver() {

    var receivers: WeakHashMap<BaseActivity, Context> = WeakHashMap()
    val _event = MutableLiveData(false)
    val event:LiveData<Boolean> = _event

    @Inject
    lateinit var internetConnection: InternetConnection

    val INTERNET_RECEIVER = "android.net.conn.CONNECTIVITY_CHANGE"


    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.getAction() != null && intent.getAction() == INTERNET_RECEIVER) {
            if (internetConnection.isNetworkConnected()) {
                _event.value = true
//                internet connected
                for (receiver in receivers.entries)
                    receiver.key.onInternetConnected()

            } else {
                _event.value = false

//                internet not connected
                for (receiver in receivers.entries)
                    receiver.key.onInternetDisconnected()
            }

        }
    }

    fun addReciver(activity: BaseActivity) {
        receivers.put(activity, activity)

//        sending first event right after registering the client
        if (!internetConnection.isNetworkConnected()) {

//            sending event with delay so that snackbar animation works properly
            Handler().postDelayed(Runnable {
                //                checking if activity is still alive to avoid null pointer exception
                if (activity != null && receivers.contains(activity))
                    activity.onInternetDisconnected()
            }, 400)
        }
    }

    fun removeReceiver(activity: BaseActivity) {
        receivers.remove(activity)
    }

}