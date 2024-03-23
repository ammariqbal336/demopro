package com.presenter.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.extensions.gone
import com.base.extensions.visible
import com.base.ui.BaseFragment
import com.domain.model.response.RoasterResponse
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.presenter.R
import com.presenter.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private val viewModel : HomeViewModel by viewModels()
    private lateinit var  sectionAdapter: SectionAdapter
    lateinit var homeBinding: FragmentHomeBinding
    lateinit var timer: CountDownTimer




    override fun layoutID() = R.layout.fragment_home

    override fun initializeComponents() {
        with(binding as FragmentHomeBinding){
            lifecycleOwner =this@HomeFragment
            homeBinding = this
        }

       searchUser()
    }

    override fun setObservers() {
        observeDataEvents(viewModel)

        viewModel.events.observe(this){
            when(val events = it.getEventIfNotHandled()){
                is HomeEvents.GetRoasterResponse -> {
                    if (events.list.isEmpty()) {
                        homeBinding.tvRecord.visible()
                    } else {
                        homeBinding.tvRecord.gone()
                    }
                        setAdapter(events.list)

                }
                else -> {

                }
            }
        }
    }

    private fun setAdapter(list: List<RoasterResponse>) {

        sectionAdapter = SectionAdapter {
        }
        homeBinding.rvUser.adapter = sectionAdapter
        homeBinding.rvUser.layoutManager = LinearLayoutManager(requireContext())


//        val itemDecorator = MaterialDividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
//            dividerInsetEnd = 56
//            dividerInsetStart = 56
//            dividerThickness = 50
//            dividerColor = resources.getColor(android.R.color.transparent)
//        }
//        homeBinding.rvUser.addItemDecoration(itemDecorator)

        sectionAdapter.addItems(list)
    }

    fun searchUser(){
        homeBinding.etsearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                timer = object : CountDownTimer(1500, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        if (s.isNullOrEmpty()) {
                            viewModel.filterData("")
                        } else {
                            viewModel.filterData("$s")
                        }
                    }
                }
                timer.start()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (this@HomeFragment::timer.isInitialized) {
                    timer.cancel()
                }
            }
        })
    }

}