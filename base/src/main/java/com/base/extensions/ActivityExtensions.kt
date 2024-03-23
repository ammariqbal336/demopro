package com.base.extensions

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.base.R
import com.base.ui.BaseFragment

/*Live data extension function which'll set initial value*/
fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply {
    setValue(initialValue)
}


fun AppCompatActivity.replaceFragment(
    fragment: Fragment, frameId: Int,
    addBacktack: Boolean = false,
    clearBackStack: Boolean = false,
    allowMultipleInstances: Boolean = false,
    popBackStackInclusive: String? = null
) {
    //        double tapping to open fragment twice wont work
    if (!allowMultipleInstances) {
//            checking if fragment is present in back stack
        if (supportFragmentManager.backStackEntryCount > 0 &&
            supportFragmentManager.getBackStackEntryAt(
                supportFragmentManager.backStackEntryCount - 1
            ).name?.equals(
                fragment::class.java.name
            )!!
        )
            return
//            checkin if fragment is not in backstack but present is fragments list meaning fragment is added but not placed in backstack
        if (supportFragmentManager.fragments.size > 0 &&
            supportFragmentManager.fragments.get(supportFragmentManager.fragments.size - 1)::class.java.name.equals(
                fragment::class.java.name
            )
        )
            return
    }

    supportFragmentManager.transact {
        if (addBacktack)
            addToBackStack(fragment::class.java.name)
        if (clearBackStack) {
            for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
                supportFragmentManager.popBackStack()
            }
        }

        if (popBackStackInclusive != null) {
            supportFragmentManager.popBackStack(
                popBackStackInclusive,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

        replace(frameId, fragment)
    }
}

fun AppCompatActivity.addFragment(
    fragment: Fragment, frameId: Int,
    addBacktack: Boolean = false,
    clearBackStack: Boolean = false,
    allowMultipleInstances: Boolean = false,
    popBackStackInclusive: String? = null
) {
    //        double tapping to open fragment twice wont work
    if (!allowMultipleInstances) {
//            checking if fragment is present in back stack
        if (supportFragmentManager.backStackEntryCount > 0 &&
            supportFragmentManager.getBackStackEntryAt(
                supportFragmentManager.backStackEntryCount - 1
            ).name?.equals(
                fragment::class.java.name
            )!!
        )
            return
//            checkin if fragment is not in backstack but present is fragments list meaning fragment is added but not placed in backstack
        if (supportFragmentManager.fragments.size > 0 &&
            supportFragmentManager.fragments.get(supportFragmentManager.fragments.size - 1)::class.java.name.equals(
                fragment::class.java.name
            )
        )
            return
    }

    supportFragmentManager.transact {
        if (addBacktack)
            addToBackStack(fragment::class.java.name)
        if (clearBackStack) {
            for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
                supportFragmentManager.popBackStack()
            }
        }

        if (popBackStackInclusive != null) {
            supportFragmentManager.popBackStack(
                popBackStackInclusive,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

        add(frameId, fragment)
    }
}

fun AppCompatActivity.replaceFragmentWithAnimation(
    fragment: Fragment,
    frameId: Int,
    addBacktack: Boolean,
    clearBackStack: Boolean,
    enter: Int,
    exit: Int,
    popEnter: Int,
    popExit: Int
) {
    supportFragmentManager.transact {
        setCustomAnimations(enter, exit, popEnter, popExit)
        if (addBacktack)
            addToBackStack(fragment::class.java.name)
        if (clearBackStack) {
            for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
                supportFragmentManager.popBackStack()
            }
        }

        replace(frameId, fragment)
    }
}


/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentTo(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}


fun AppCompatActivity.replaceFragmentWithSlideAnimation(
    fragment: Fragment,
    frameId: Int,
    addBackstack: Boolean,
    clearBackStack: Boolean,
    allowMultipleInstances: Boolean = false
) {
    //        double tapping to open fragment twice wont work
    if (!allowMultipleInstances) {
//            checking if fragment is present in back stack
        if (supportFragmentManager.backStackEntryCount > 0 &&
            supportFragmentManager.getBackStackEntryAt(
                supportFragmentManager.backStackEntryCount - 1
            ).name?.equals(
                fragment::class.java.name
            )!!
        )
            return
/*            checkin if fragment is not in backstack but present is fragments list meaning fragment is added but not placed in backstack*/
        if (supportFragmentManager.fragments.size > 0 &&
            supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1]::class.java.name == fragment::class.java.name
        )
            return
    }

    supportFragmentManager.transact {

        if (!clearBackStack)
            setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        else
            setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        if (addBackstack)
            addToBackStack(fragment::class.java.name)
        if (clearBackStack) {
            for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
                supportFragmentManager.popBackStack()
            }
        }


        replace(frameId, fragment)
    }
}


fun AppCompatActivity.addFragment(
    containerId: Int,
    fragment: Fragment,
    allowMultipleInstances: Boolean = false
) {
    supportFragmentManager.transact {
        add(containerId, fragment)
    }
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun AppCompatActivity.popStack() {
    supportFragmentManager.popBackStack()
}

fun AppCompatActivity.popStackImmediate() {
    supportFragmentManager.popBackStackImmediate()
}


fun AppCompatActivity.clearStack() {
    for (i in 0 until supportFragmentManager.getBackStackEntryCount()) {
        supportFragmentManager.popBackStack()
    }
}

fun AppCompatActivity.topFragment(): BaseFragment? {
    if (supportFragmentManager.fragments.size > 0)
        return supportFragmentManager.fragments[supportFragmentManager.fragments.size - 1] as BaseFragment

    return null
}

fun AppCompatActivity.openActivity(
    className: Class<out AppCompatActivity>,
    clearBackStack: Boolean = true,
    bundle: Bundle? = null,
) {
    val intent = Intent(this, className)
//    if (clearBackStack) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    bundle?.let {
        intent.putExtras(it)
    }
    startActivity(intent)
    if (clearBackStack)
        finish()
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    transactionView: View?,
    transactionName: String?
) {
    supportFragmentManager.transact {

        if (transactionView == null || transactionName == null)
            replace(frameId, fragment)
        else
            replace(frameId, fragment).addSharedElement(transactionView, transactionName)
    }
}
//fun AppCompatActivity.obtainViewModel() =
//    ViewModelProviders.of(this, AuthenticationViewModelFactory())

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()




}

fun AppCompatActivity.cancelAllNotificaiton(){
    try {
        val nMgr = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nMgr.cancelAll()
    }catch (e:Exception){
        e.printStackTrace()
    }




}
fun Any.runSafely(exceptionBlock: ((Exception) -> Unit)? = null, tryBlock: () -> Unit) {
    try {
        tryBlock.invoke()
    } catch (e: Exception) {
        exceptionBlock?.let {
            it.invoke(e)
        }
    }
}


