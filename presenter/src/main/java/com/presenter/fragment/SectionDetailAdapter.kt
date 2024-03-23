package com.presenter.fragment

import com.base.adapters.BaseAdapter
import com.domain.model.response.RegisteredContactsResponse
import com.presenter.R
import com.presenter.databinding.ItemSectionDetailBinding

class SectionDetailAdapter(var onTap:(RegisteredContactsResponse)->Unit) :BaseAdapter<RegisteredContactsResponse,ItemSectionDetailBinding>(R.layout.item_section_detail) {
    override fun onItemInflated(
        items: RegisteredContactsResponse,
        position: Int,
        binding: ItemSectionDetailBinding
    ) {
        binding.model = items
    }

    override fun onClick(item: RegisteredContactsResponse) {
        super.onClick(item)
        onTap(item)

    }
}