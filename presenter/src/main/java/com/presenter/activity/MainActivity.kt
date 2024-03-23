package com.presenter.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.base.ui.BaseActivity
import com.presenter.R
import com.presenter.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val viewModel: MainActivityViewModel by viewModels()


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//      //  setupNavHost()
//    }


    private fun setupNavHost() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.faq_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun initializeComponents() {

        with(binding as ActivityMainBinding){
            lifecycleOwner =this@MainActivity
        }
         setupNavHost()

    }
    override fun setObservers() {
            observeDataEvents(viewModel)
    }

    override fun layoutID() = R.layout.activity_main

    override fun forceLogout() {
    }
}