package com.base.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.base.R
import com.base.databinding.MessageDialogBinding
import com.base.events.BaseDataEvents
import com.base.extensions.dpToPx
import com.base.extensions.gone
import com.base.extensions.visible
import com.base.factory.ToastFactory
import com.base.util.KeyboardUtil
import com.base.viewmodels.BaseViewModel
import javax.inject.Inject


abstract class BaseFragment : Fragment() {

    val TAG = this::class.java.name

    @Inject
    lateinit var keyboardUtil: KeyboardUtil

    @Inject
    lateinit var toastFactory: ToastFactory

    lateinit var binding: ViewDataBinding
    var activityViewModel: BaseViewModel? = null

    var messageDialog: AlertDialog? = null
    lateinit var messageDialogBinding: MessageDialogBinding

     var currentActivity:AppCompatActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), layoutID(), null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activateHideKeyboardUponTouchingScreen(view)

        initializeMessageDialog(requireContext())
        initializeComponents()
        setUpListeners()
        setObservers()

    }
    protected inline fun <reified T : BaseViewModel> attachNavViewModel(): T {
        activity?.let {
            activityViewModel = ViewModelProviders.of(it).get(T::class.java)
        }

        return activityViewModel as T ?: throw ExceptionInInitializerError("Unable to get viewmodel")
    }

    fun screenTransactions() = (activity as AppCompatActivity)

    abstract fun layoutID(): Int
    open fun setUpListeners() {}
    abstract fun initializeComponents()
    abstract fun setObservers()

    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    private fun activateHideKeyboardUponTouchingScreen(view: View) {

        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                keyboardUtil.closeKeyboard(requireActivity())
                return@setOnTouchListener false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val v = view.getChildAt(i)
                activateHideKeyboardUponTouchingScreen(v)
            }
        }
    }

    fun observeDataEvents(viewModel: BaseViewModel) {
        viewModel.obDataEvent.observe(this, Observer {
            var event = it.getEventIfNotHandled()
            if (event != null)
                when (event) {
                    BaseDataEvents.ShowLoader -> showLoader()

                    BaseDataEvents.HideLoader ->
                        hideLoader()
                    is BaseDataEvents.Exception ->
                        showException(event.message)

                    is BaseDataEvents.Error ->
                        showError(event.message)

                    is BaseDataEvents.Toast -> showToast(event.message)
                    is BaseDataEvents.ToastSuccess -> showSuccessToast(event.message)
                    is BaseDataEvents.ShowSuccessDialog -> showMessageDialog(event.message,event.messagedetail,event.buttonText,event.actionListener)

                    BaseDataEvents.ForceLogout -> activityViewModel?.forceLogout()

                    else -> {
                    }
                }
        })
    }

    fun onProgressCanceled() {}

    protected fun hideKeyboard() {
        keyboardUtil.closeKeyboard(requireActivity())
    }

    protected fun showToast(message: String) {
        toastFactory.create(message)
    }
    protected fun showToastLong(message: String,id:Int) {
        toastFactory.create(message,id, duration = Toast.LENGTH_LONG)
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
    protected open fun showSuccessToastLong(message: String) {
        showToastLong(message,R.drawable.success_icon)
    }

    open fun loaderView(): View = binding.root.findViewById(R.id.loaderView)

    protected fun showLoader() {
        loaderView().visible()
    }

    protected fun hideLoader() {
        loaderView().gone()
    }
    fun initializeMessageDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        messageDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.message_dialog,
            null,
            false
        )
        builder.setView(messageDialogBinding.root)
        builder.setCancelable(true)
        messageDialog = builder.create()
        messageDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        messageDialog?.window?.attributes?.windowAnimations = R.style.animated_popup

        messageDialogBinding.actionButton.setOnClickListener {
            messageDialog?.dismiss()
        }

    }

    fun showMessageDialog(message: String,messageDetail:String,buttonText:String, actionListener: () -> Unit) {
        messageDialogBinding.message.text = message
        messageDialogBinding.messagedetail.text = messageDetail
        messageDialogBinding.actionButton.text = buttonText
        messageDialog?.show()
        messageDialog?.window?.setLayout(350.dpToPx(), 400.dpToPx())
        messageDialog?.setOnDismissListener {
            actionListener()
        }
    }





}
