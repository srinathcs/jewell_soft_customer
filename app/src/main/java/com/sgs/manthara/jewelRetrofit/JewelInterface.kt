package com.sgs.manthara.jewelRetrofit

import android.telephony.mbms.MbmsErrors.StreamingErrors
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface JewelInterface {

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun jewelSoft(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("mobile") mobile: String,
        @Field("token") token: String,
    ): ModelClass

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun otpRec(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") ledId: String,
        @Field("otp") otp: String
    ): OtpReceive

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun schemeDetails(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") ledId: String,
    ): List<SchemeDetails>

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun schemeType(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("id") id: String,
        @Field("uid") ledId: String,
    ): SchemeType

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun goldRate(
        @Field("type") type: String,
        @Field("sub_type") subType: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String
    ): TodayRate

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun schemeTypeAuto(
        @Field("type") type: String,
        @Field("sub_type") subType: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String
    ): List<SchemeTypeAuto>

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun saveScheme(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String,
        @Field("name") name: String,
        @Field("maturity_date") maturityDate: String,
        @Field("remark") remark: String,
        @Field("emi") emi: String,
        @Field("free_emi") freeEmi: String,
        @Field("start_date") startDate: String,
        @Field("sch_name") schemeName: String,
        @Field("sch_type") schemeType: String,
        @Field("monthly_emi") monthlyEmi: String,
        @Field("total_emi") totalEmi: String,
        @Field("purity") purity: String,
    ): OtpReceive

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun enrollmentScheme(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("sub_type") sub_type: String,
        @Field("sch") sch: String,
        @Field("date") date: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String,
    ): List<EnrollmentScheme>

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun pendingDue(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String
    ): List<PendingDue>

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun jewellType(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String,
        @Field("category") category: String
    ): List<SubCategory>

    @Multipart
    @POST("jewel_index.php")
    suspend fun addCustomDesign(
        @Part("type") type: RequestBody,
        @Part("cid") cid: RequestBody,
        @Part("device_id") device: RequestBody,
        @Part("ln") ln: RequestBody,
        @Part("lt") lt: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("count") count: RequestBody,
        @Part("jewel_type") jewelType: RequestBody,
        @Part("gram") gram: RequestBody,
        @Part("description") description: RequestBody,
        @Part jewel_image: MutableList<MultipartBody.Part>,
    ): List<Save>

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun paidAmount(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String
    ): List<PaidAmount>

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun newJewelArrival(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String,
        @Field("pro_cat") proCat: String
    ): List<NewJewellArrival>

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun selectedJewel(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String,
        @Field("id") id: String,
    ): ProductDetails

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun totalWeight(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String,
    ): List<TotalWeight>

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun preBook(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String,
        @Field("id") id: String,
    ): PreBook

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun showPerBook(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String,
    ): List<ShowPerBook>

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun selectedTextile(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String,
        @Field("id") id: String
    ): TextileDetails

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun offerJewel(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String,
        @Field("pro_cat") id: String
    ): List<OfferJewell>

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun wishList(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String,
        @Field("id") id: String
    ): WishlistAdd

    @FormUrlEncoded
    @POST("jewel_index.php")
    suspend fun showWishList(
        @Field("type") type: String,
        @Field("cid") cid: String,
        @Field("device_id") deviceId: String,
        @Field("ln") ln: String,
        @Field("lt") lt: String,
        @Field("uid") uid: String,
    ): List<ShowWishList>
}