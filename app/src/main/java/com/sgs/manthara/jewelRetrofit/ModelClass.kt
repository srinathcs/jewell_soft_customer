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
    val name :String,
    val mobile:String

    )

data class Save(
    val message: String
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

data class SubCategory(
    val sub_category: String,
    val id: String
)

data class PaidAmount(
    val chit_id: String,
    val sch_type: String,
    val sch_name: String,
    val due_no: String,
    val due_date: String,
    val amount: String
)

data class NewJewellArrival(
    val id: String,
    val sub_cat: String,
    val proname: String,
    val proprice: String,
    val proquan: String,
    val img1: String,
    val img2: String,
    val img3: String,
    val img4: String,
    val wishlist_status: String,
)

data class Product(
    val id: String,
    val sub_cat: String,
    val proname: String,
    val proprice: String,
    val proquan: String,
    val img1: String,
    val img2: String,
    val img3: String,
    val img4: String
)

data class ProductDetails(
    val `0`: Product,
    val img: List<String>
)

data class TotalWeight(
    val chit_id: String,
    val sch_type: String,
    val sch_name: String,
    val paid_due: String,
    val paid: String,
    val total_weigth: String,
    val error_message: Boolean
)

data class PreBook(
    val error: Boolean,
    val message: String
)

data class ShowPerBook(
    val id: String,
    val mid: String,
    val date: String,
    val product_cat: String,
    val sub_cat: String,
    val proname: String,
    val proprice: String,
    val order_status: String,
    val img1: String,
)

data class Textile(
    val id: String,
    val sub_cat: String,
    val proname: String,
    val proprice: String,
    val proquan: String,
    val img1: String,
    val img2: String,
    val img3: String,
    val img4: String
)

data class TextileDetails(
    val `0`: Textile,
    val img: List<String>
)

data class OfferJewell(
    val id: String,
    val sub_cat: String,
    val proname: String,
    val offer_precetage: String,
    val proprice: String,
    val proquan: String,
    val img1: String,
    val img2: String,
    val img3: String,
    val img4: String,
    val wishlist_status: String
)

data class WishlistAdd(
    val error: Boolean,
    val message: String
)

data class ShowWishList(
    val id: String,
    val mid: String,
    val date: String,
    val product_cat: String,
    val sub_cat: String,
    val proname: String,
    val proprice: String,
    val img1: String,
    val img2: String,
    val img3: String,
    val img4: String,
    val booked_status: String
)

data class CloseWishList(
    val error: Boolean,
    val message: String
)
data class ViewPager (
    var img: String? = null
        )