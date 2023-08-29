package com.sgs.manthara.jewelRetrofit

data class ModelClass(
    val cus_id: String,
    val cus_name: String,
    val cid: String,
    val comp_name: String,
    val led_id: String,
    val error: Boolean,
    val error_msg: String,
)

data class OtpReceive(
    val error: Boolean,
    val error_msg: String,
)

data class SchemeDetails(
    val id: String,
    val aid: String,
    val uid: String,
    val bid: String,
    val cid: String,
    val name: String,
    val emi: String,
    val free_emi: String,
    val metal_booked_purity: String,
    val scheme_type: String,
    val del: String,
    val is_d: String,
    val act: String,
    val dtime: String
)

data class SchemeType(
    val id: String,
    val name: String,
    val emi: String,
    val free_emi: String,
    val purity: String,
    val scheme_type: String
)

data class ShcemeDetails(
    var shceme_details: SchemeType? = null,
)

data class TodayRate(
    val date: String,
    val metal: String,
    val purity: String,
    val rate: String,
    val name: String
)

data class SchemeTypeAuto(
    val id: String,
    val name: String
)

data class EnrollmentScheme(
    val id: String,
    val aid: String,
    val uid: String,
    val bid: String,
    val cid: String,
    val name: String,
    val emi: String,
    val free_emi: String,
    val metal_booked_purity: String,
    val scheme_type: String,
    val del: String,
    val is_d: String,
    val act: String,
    val dtime: String,
    val mat_date: String
)

data class PendingDue(
    val chit_id: String,
    val sch_type: String,
    val sch_name: String,
    val due_no: String,
    val due_date: String,
    val amount: String,
    val message: String
)