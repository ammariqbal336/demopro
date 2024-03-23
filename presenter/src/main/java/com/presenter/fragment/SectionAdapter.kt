package com.presenter.fragment

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.adapters.BaseAdapter
import com.domain.model.response.RegisteredContactsResponse
import com.domain.model.response.RoasterResponse
import com.presenter.R
import com.presenter.databinding.ItemSectionBinding

class SectionAdapter(var onTap:(RoasterResponse)->Unit) :BaseAdapter<RoasterResponse,ItemSectionBinding>(R.layout.item_section) {

    override fun onItemInflated(
        items: RoasterResponse,
        position: Int,
        binding: ItemSectionBinding
    ) {
        binding.model = items
        if(items.registeredContacts?.isNotEmpty() == true) {
            setDetailAdapter(binding, items.registeredContacts  ?: emptyList())
        }
    }

    override fun onClick(item: RoasterResponse) {
        super.onClick(item)
        onTap(item)

    }

    fun setDetailAdapter(binding: ItemSectionBinding,detail: List<RegisteredContactsResponse>){

        lateinit var sectionDetailAdapter: SectionDetailAdapter
        sectionDetailAdapter = SectionDetailAdapter {
        }
        binding.rvDetail.adapter = sectionDetailAdapter
        binding.rvDetail.layoutManager = GridLayoutManager(binding.root.context,4)
        sectionDetailAdapter.addItems(detail)
    }
}