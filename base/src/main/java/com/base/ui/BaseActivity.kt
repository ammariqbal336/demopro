package com.base.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.base.R
import com.base.broadcast_receivers.InternetStateReceiver
import com.base.databinding.MessageDialogBinding
import com.base.events.BaseDataEvents
import com.base.extensions.gone
import com.base.extensions.runSafely
import com.base.extensions.visible
import com.base.factory.ToastFactory
import com.base.viewmodels.BaseViewModel
import com.muddassir.connection_checker.ConnectionState
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity() {
    val TAG: String = this.javaClass.simpleName


    var status = ConnectionState.CONNECTED
    var isInternetConnected = false

    @Inject
    lateinit var toastFactory: ToastFactory

    @Inject
    lateinit var internetStateReceiver: InternetStateReceiver

    lateinit var binding: ViewDataBinding



    var messageDialog : AlertDialog? = null
    lateinit var messageDialogBinding : MessageDialogBinding

//    @Inject
//    lateinit var bindingComponentImpl: DataBindingComponentImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      // try {
        binding = DataBindingUtil.setContentView(this, layoutID())
        initializeMessageDialog(this)
        initializeComponents()
        setObservers()
        setUpListeners()
       // checkConnection(this,configURL)
        runSafely(exceptionBlock = {
            showToast("${it.message}")
        }) {
            adjustFontScale(resources.configuration)
        }


    }






    open fun setUpListeners() {}
    abstract protected fun initializeComponents()
    abstract protected fun setObservers()
    abstract protected fun layoutID(): Int
    open fun loaderView(): View = findViewById(R.id.loaderView)

    open fun internetStatus(state: ConnectionState) {}
    /**
     *This function will be called by base activity if account manager
     * doesnt contain account, This function will call viewModel's forceLagou
     * which will clear local database
     */
    protected abstract fun forceLogout()

    open fun onInternetConnected() {
        isInternetConnected = true
    }

    open fun onInternetDisconnected() {
        isInternetConnected = false
    }

    fun observeDataEvents(viewModel: BaseViewModel) {
        viewModel.obDataEvent.observe(this, Observer {
            var event = it.getEventIfNotHandled()
            if (event != null)
                when (event) {
                    BaseDataEvents.ShowLoader -> showLoader()
                    BaseDataEvents.HideLoader -> hideLoader()
                    is BaseDataEvents.Exception -> showException(event.message)
                    is BaseDataEvents.Error -> showError(event.message)
                    is BaseDataEvents.Toast -> showToast(event.message)
                    is BaseDataEvents.Toast -> showSuccessToast(event.message)
                    BaseDataEvents.ForceLogout -> forceLogout()
                    is BaseDataEvents.ShowSuccessDialog-> {showMessageDialog()}
                    else -> {}
                }
        })
    }


    protected fun showToast(message: String) {
        toastFactory.create(message)
    }

    protected fun showToast(message: String,id:Int) {
        toastFactory.create(message,id)
    }

    open protected fun showException(message: String) {
        showToast(message, R.drawable.error_icon)
    }

    protected open fun showError(message: String) {
        showToast(message,R.drawable.error_icon)
    }
    protected open fun showSuccessToast(message: String) {
        showToast(message,R.drawable.success_icon)
    }

    protected fun showLoader() {
        loaderView().visible()
    }

    protected fun hideLoader() {
        loaderView().gone()
    }

    fun initializeMessageDialog(context: Context){
        val builder = AlertDialog.Builder(context)
        messageDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.message_dialog,null,false)
        builder.setView(messageDialogBinding.root)
        builder.setCancelable(true)
        messageDialog = builder.create()
    }

    fun showMessageDialog(){
        messageDialog?.show()
    }


    override fun onResume() {
        super.onResume()
//        adding client to get network connectivity events
        //statusHandling(status)
        internetStateReceiver.addReciver(this)
    }

    override fun onStop() {
        super.onStop()
//        removing internet connection client
        internetStateReceiver.removeReceiver(this)

    }


    fun adjustFontScale(configuration: Configuration) {
        if (configuration.fontScale > 1f || configuration.fontScale < 1f) {
            configuration.fontScale = .5f
            val metrics = resources.displayMetrics
            val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getRealMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            resources.updateConfiguration(configuration, metrics)
        }
    }
    private fun disableFontScaling() {
        val configuration = Configuration(resources.configuration)
        configuration.fontScale = 1.0f
        applyOverrideConfiguration(configuration)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        val config = Configuration(overrideConfiguration)
        config.fontScale = 1.0f
        super.applyOverrideConfiguration(config)
    }

    override fun attachBaseContext(newBase: Context?) {

        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)

        super.attachBaseContext(newBase)

    }

//    override fun onConnectionState(state: ConnectionState) {
//       statusHandling(state)
//    }

    private fun statusHandling(state: ConnectionState){
        if(state == ConnectionState.CONNECTED && status == ConnectionState.CONNECTED){
            status = state
        }
        else {
            status = state
            internetStatus(state)
        }
    }


}