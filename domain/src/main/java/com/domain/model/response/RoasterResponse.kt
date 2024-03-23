package com.domain.model.response

import com.squareup.moshi.JsonClass
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@JsonClass(generateAdapter = true)
data class RoasterResponse(
    val classRosterAttendeeID : Int? = 0,
    val checkOutToo : Boolean? = false,
    val classRosterID : Int? = 0,
    val classRosterName : String? = "",
    val classDateTime : String? = "",
    val classStartTime : String? = "",
    val classEndTime : String? = "",
    val registeredContacts : List<RegisteredContactsResponse>? = emptyList(),
){

    fun getClassDuration(): String{
        return "${dateFormat(classStartTime)} - ${dateFormat(classEndTime)}"
    }

    fun dateFormat(time: String?): String{
        return if(!classDateTime.isNullOrEmpty()) {
            print("User Info time")
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a")
            var convertedDate: Date? = null
            convertedDate = sdf.parse(time)
            val standardDateFormat = SimpleDateFormat("hh:mm a")

            print("User Info time ${standardDateFormat.format(convertedDate)}")
            standardDateFormat.format(convertedDate)

        } else {
            ""
        }
    }
}
