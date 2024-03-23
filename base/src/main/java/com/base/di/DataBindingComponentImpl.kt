package com.base.di

import androidx.databinding.DataBindingComponent
import com.base.adapters.DataBindingAdapters
import javax.inject.Inject

class DataBindingComponentImpl @Inject constructor(@JvmField var dataBindingAdapters: DataBindingAdapters) : DataBindingComponent {
     override fun getDataBindingAdapters(): DataBindingAdapters  = dataBindingAdapters
}