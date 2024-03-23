package com.presenter.fragment

import com.domain.model.response.RoasterResponse

sealed class HomeEvents {

    class GetRoasterResponse(val list:  List<RoasterResponse> ): HomeEvents()
}