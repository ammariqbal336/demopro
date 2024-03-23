package com.base.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.base.R
import com.base.events.BaseDataEvents
import com.base.extensions.exhaustive
import com.base.factory.ToastFactory
import com.base.util.KeyboardUtil
import com.base.viewmodels.BaseViewModel
import javax.inject.Inject


abstract class BaseDialogFragment : DialogFragment() {

    protected var ctx: FragmentActivity? = null

    @Inject
    lateinit var keyboardUtil: KeyboardUtil

    @Inject
    lateinit var toastFactory: ToastFactory
    var activityViewModel: BaseViewModel? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.ctx = activity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeComponents()
        startObservingNavigation()
        startObservingFragmentNavigation()
    }

    protected abstract fun initializeComponents()
    protected abstract fun startObservingNavigation()
    protected abstract fun startObservingFragmentNavigation()
    protected open fun initializeViews() {}
    protected abstract fun showLoader()
    protected abstract fun hideLoader()

    protected inline fun <reified V : ViewModel> attachViewModel(viewModelFactory: ViewModelProvider.Factory): V =
        ViewModelProviders.of(this, viewModelFactory).get(V::class.java)

    protected inline fun <reified T : BaseViewModel> attachNavViewModel(): T {
        activity?.let {
            activityViewModel = ViewModelProviders.of(it).get(T::class.java)
        }

        return activityViewModel as T
    }

    fun screenTransaction() = (activity as AppCompatActivity)


//    fun screenTransaction(): AppCompatActivity? {
//        return activity as? AppCompatActivity
//    }
    fun popStack() {
        keyboardUtil.closeKeyboard(ctx!!)
        ctx!!.supportFragmentManager.popBackStack()
    }

    protected fun showToast(message: String) {
        toastFactory.create(message)
    }

    protected fun showToast(message: String,id:Int) {
        toastFactory.create(message,id)
    }



    protected open fun showSuccessToast(message: String) {
        showToast(message, R.drawable.success_icon)
    }

    protected open fun showException(message: String) {
        showToast(message, R.drawable.error_icon)
    }
    protected open fun showLoader(message: String) {
    }

    fun hideKeyBoard() {
        keyboardUtil.closeKeyboard(ctx!!)
    }




    fun activateHideKeyboardUponTouchingScreen(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                keyboardUtil.closeKeyboard(ctx!!)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                activateHideKeyboardUponTouchingScreen(innerView)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        keyboardUtil.closeKeyboard(requireActivity())
    }

    /*Observing Event which is handled by activity*/
    fun observeDataEvents(viewModel: BaseViewModel) {
        viewModel.obDataEvent.observe(requireActivity(), Observer {
            var event = it.getEventIfNotHandled()
            if (event != null)
                when (event) {
                    is BaseDataEvents.ShowLoader -> showLoader()
                    BaseDataEvents.HideLoader -> hideLoader()
                    is BaseDataEvents.Exception -> showException(event.message)
                    is BaseDataEvents.Error -> showError(event.message)
                    is BaseDataEvents.Toast -> showToast(event.message)
                    BaseDataEvents.ForceLogout -> activityViewModel?.forceLogout()

                    else -> println("NO Event found in BaseFragment")
                }.exhaustive
        })
    }

    //
    protected fun showError(message: String) {
        showToast(message)
    }



}