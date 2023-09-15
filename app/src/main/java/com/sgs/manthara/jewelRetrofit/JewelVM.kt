package com.sgs.manthara.jewelRetrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class JewelVM(private val jewelSoftRepo: JewelRepo) : ViewModel() {
    private val testLogin = MutableStateFlow<Resources<ModelClass>>(Resources.Loading())
    val testLoginFlow: StateFlow<Resources<ModelClass>> = testLogin

    suspend fun jewels(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        mobile: String,
        s: String,

        ) = viewModelScope.launch {
        try {
            val response =
                jewelSoftRepo.jewelSoft(type, cid, deviceId, ln, lt, mobile, s)
            testLogin.value = Resources.Success(response)
        } catch (exception: Exception) {
            testLogin.value = Resources.Error(exception.message.toString())
        }
    }

    private val otpRec = MutableStateFlow<Resources<OtpReceive>>(Resources.Loading())
    val otpRecFlow: StateFlow<Resources<OtpReceive>> = otpRec

    suspend fun otp(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        otp: String,
        lid: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.otpRec(type, cid, deviceId, ln, lt, uid, otp, lid)
            otpRec.value = Resources.Success(response)
        } catch (exception: Exception) {
            otpRec.value = Resources.Error(exception.message.toString())
        }
    }

    private val schemeType = MutableStateFlow<Resources<List<SchemeDetails>>>(Resources.Loading())
    val schemeTypeFlow: StateFlow<Resources<List<SchemeDetails>>>
        get() = schemeType

    suspend fun schemeType(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        schType: String,
    ) = viewModelScope.launch {
        try {
            val response =
                jewelSoftRepo.schemeType(type, cid, deviceId, ln, lt, uid, schType)
            schemeType.value = Resources.Success(response)
        } catch (exception: Exception) {
            schemeType.value = Resources.Error(exception.message.toString())
        }
    }

    private val textileDetails = MutableStateFlow<Resources<SchemeType>>(Resources.Loading())
    val textileDetailsFlow: StateFlow<Resources<SchemeType>>
        get() = textileDetails

    suspend fun textilesDetails(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        id: String,
        uid: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.schemeDetails(type, cid, deviceId, ln, lt, id, uid)
            textileDetails.value = Resources.Success(response)
        } catch (e: Exception) {
            textileDetails.value = Resources.Error(e.message.toString())
        }
    }


    private val textileType = MutableStateFlow<Resources<List<SchemeDetails>>>(Resources.Loading())
    val textileTypeFlow: StateFlow<Resources<List<SchemeDetails>>>
        get() = textileType

    suspend fun textilesType(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        schType: String,
    ) = viewModelScope.launch {
        try {
            val response =
                jewelSoftRepo.textileType(type, cid, deviceId, ln, lt, uid, schType)
            textileType.value = Resources.Success(response)
        } catch (exception: Exception) {
            textileType.value = Resources.Error(exception.message.toString())
        }
    }

    private val schemeDetails = MutableStateFlow<Resources<SchemeType>>(Resources.Loading())
    val schemeDetFlow: StateFlow<Resources<SchemeType>>
        get() = schemeDetails

    suspend fun schemeDetails(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        id: String,
        uid: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.schemeDetails(type, cid, deviceId, ln, lt, id, uid)
            schemeDetails.value = Resources.Success(response)
        } catch (e: Exception) {
            schemeDetails.value = Resources.Error(e.message.toString())
        }
    }


    private val todayRateGold = MutableStateFlow<Resources<TodayRate>>(Resources.Loading())
    val todayRateGoldFlow: StateFlow<Resources<TodayRate>>
        get() = todayRateGold

    suspend fun rateGold(
        type: String,
        subType: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.todayRate(type, subType, cid, deviceId, ln, lt, uid)
            todayRateGold.value = Resources.Success(response)
        } catch (e: Exception) {
            todayRateGold.value = Resources.Error(e.message.toString())
        }
    }

    private val todayRateDiamond = MutableStateFlow<Resources<TodayRate>>(Resources.Loading())
    val todayRateDiamondFlow: StateFlow<Resources<TodayRate>>
        get() = todayRateDiamond

    suspend fun rateDiamond(
        type: String,
        subType: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.todayRate(type, subType, cid, deviceId, ln, lt, uid)
            todayRateDiamond.value = Resources.Success(response)
        } catch (e: Exception) {
            todayRateDiamond.value = Resources.Error(e.message.toString())
        }
    }

    private val todayRatePlatinum = MutableStateFlow<Resources<TodayRate>>(Resources.Loading())
    val todayRatePlatinumFlow: StateFlow<Resources<TodayRate>>
        get() = todayRatePlatinum

    suspend fun ratePlatinum(
        type: String,
        subType: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.todayRate(type, subType, cid, deviceId, ln, lt, uid)
            todayRatePlatinum.value = Resources.Success(response)
        } catch (e: Exception) {
            todayRatePlatinum.value = Resources.Error(e.message.toString())
        }
    }

    private val todayRateSliver = MutableStateFlow<Resources<TodayRate>>(Resources.Loading())
    val todayRateSliverFlow: StateFlow<Resources<TodayRate>>
        get() = todayRateSliver

    suspend fun rateSilver(
        type: String,
        subType: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.todayRate(type, subType, cid, deviceId, ln, lt, uid)
            todayRateSliver.value = Resources.Success(response)
        } catch (e: Exception) {
            todayRateSliver.value = Resources.Error(e.message.toString())
        }
    }

    private val schemeTypeAuto =
        MutableStateFlow<Resources<List<SchemeTypeAuto>>>(Resources.Loading())
    val schemeTypeAutoFlow: StateFlow<Resources<List<SchemeTypeAuto>>>
        get() = schemeTypeAuto

    suspend fun schemeTypeAuto(
        type: String,
        subType: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.schemeTypeAuto(type, subType, cid, deviceId, ln, lt, uid)
            schemeTypeAuto.value = Resources.Success(response)
        } catch (e: Exception) {
            schemeTypeAuto.value = Resources.Error(e.message.toString())
        }
    }

    private val saveScheme = MutableStateFlow<Resources<OtpReceive>>(Resources.Loading())
    val saveSchemeFlow: StateFlow<Resources<OtpReceive>>
        get() = saveScheme

    suspend fun saveScheme(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        name: String,
        maturityDate: String,
        remark: String,
        emi: String,
        freeEmi: String,
        startDate: String,
        schemeName: String,
        schemeType: String,
        monthlyEmi: String,
        totalEmi: String,
        purity: String,
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.saveScheme(
                type,
                cid,
                deviceId,
                ln,
                lt,
                uid,
                name,
                maturityDate,
                remark,
                emi,
                freeEmi,
                startDate,
                schemeName,
                schemeType,
                monthlyEmi,
                totalEmi,
                purity
            )
            saveScheme.value = Resources.Success(response)
        } catch (e: Exception) {
            saveScheme.value = Resources.Error(e.message.toString())
        }
    }

    private val enrollmentScheme =
        MutableStateFlow<Resources<List<EnrollmentScheme>>>(Resources.Loading())
    val enrollmentSchemeFlow: StateFlow<Resources<List<EnrollmentScheme>>>
        get() = enrollmentScheme

    suspend fun enrollmentScheme(
        type: String,
        cid: String,
        sub_type: String,
        sch: String,
        date: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.enrollmentScheme(
                type, cid, sub_type, sch, date, deviceId, ln, lt, uid
            )
            enrollmentScheme.value = Resources.Success(response)
        } catch (e: Exception) {
            enrollmentScheme.value = Resources.Error(e.message.toString())
        }
    }

    private val pendingDue =
        MutableStateFlow<Resources<List<PendingDue>>>(Resources.Loading())
    val pendingDueFlow: StateFlow<Resources<List<PendingDue>>>
        get() = pendingDue

    suspend fun pendingDue(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.pendingDue(
                type, cid, deviceId, ln, lt, uid
            )
            pendingDue.value = Resources.Success(response)
        } catch (e: Exception) {
            pendingDue.value = Resources.Error(e.message.toString())
        }
    }

    private val jewellType =
        MutableStateFlow<Resources<List<SubCategory>>>(Resources.Loading())
    val jewellTypeFlow: StateFlow<Resources<List<SubCategory>>>
        get() = jewellType

    suspend fun jewellType(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        category: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.jewellType(
                type, cid, deviceId, ln, lt, uid, category
            )
            jewellType.value = Resources.Success(response)
        } catch (e: Exception) {
            jewellType.value = Resources.Error(e.message.toString())
        }
    }

    private val _addImage = MutableStateFlow<Resources<List<Save>>>(Resources.Loading())
    val addImageFlow: StateFlow<Resources<List<Save>>>
        get() = _addImage

    suspend fun addImagesProperty(

        type: RequestBody,
        cid: RequestBody,
        ln: RequestBody,
        lt: RequestBody,
        device: RequestBody,
        uid: RequestBody,
        count: RequestBody,
        jewelType: RequestBody,
        gram: RequestBody,
        description: RequestBody,
        pro_img: MutableList<MultipartBody.Part>,

        ) = viewModelScope.launch {
        try {
            val response =
                jewelSoftRepo.getImage(
                    type,
                    cid,
                    ln,
                    lt,
                    device,
                    uid,
                    count,
                    jewelType,
                    gram,
                    description,
                    pro_img
                )
            _addImage.value = Resources.Success(response)
        } catch (e: Exception) {
            _addImage.value = Resources.Error(e.message.toString())
        }
    }

    private val paidAmount =
        MutableStateFlow<Resources<List<PaidAmount>>>(Resources.Loading())
    val paidAmountFlow: StateFlow<Resources<List<PaidAmount>>>
        get() = paidAmount

    suspend fun paidAmount(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.paidAmount(
                type, cid, deviceId, ln, lt, uid,
            )
            paidAmount.value = Resources.Success(response)
        } catch (e: Exception) {
            paidAmount.value = Resources.Error(e.message.toString())
        }
    }

    private val newJewellArrival =
        MutableStateFlow<Resources<List<NewJewellArrival>>>(Resources.Loading())
    val newJewellArrivalFlow: StateFlow<Resources<List<NewJewellArrival>>>
        get() = newJewellArrival

    suspend fun newJewellArrival(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        proCat: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.newJewelArrival(
                type, cid, deviceId, ln, lt, uid, proCat
            )
            newJewellArrival.value = Resources.Success(response)
        } catch (e: Exception) {
            newJewellArrival.value = Resources.Error(e.message.toString())
        }
    }

    private val selectedJewel =
        MutableStateFlow<Resources<ProductDetails>>(Resources.Loading())
    val selectedJewelFlow: StateFlow<Resources<ProductDetails>>
        get() = selectedJewel

    suspend fun selectedJewel(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        id: String,
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.selectedJewel(
                type, cid, deviceId, ln, lt, uid, id
            )
            selectedJewel.value = Resources.Success(response)
        } catch (e: Exception) {
            selectedJewel.value = Resources.Error(e.message.toString())
        }
    }

    private val totalWeight =
        MutableStateFlow<Resources<List<TotalWeight>>>(Resources.Loading())
    val totalWeightFlow: StateFlow<Resources<List<TotalWeight>>>
        get() = totalWeight

    suspend fun totalWeight(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,

        ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.totalWeight(
                type, cid, deviceId, ln, lt, uid,
            )
            totalWeight.value = Resources.Success(response)
        } catch (e: Exception) {
            totalWeight.value = Resources.Error(e.message.toString())
        }
    }

    private val preBook =
        MutableStateFlow<Resources<PreBook>>(Resources.Loading())
    val preBookFlow: StateFlow<Resources<PreBook>>
        get() = preBook

    suspend fun preBook(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        id: String

    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.preBook(
                type, cid, deviceId, ln, lt, uid, id
            )
            preBook.value = Resources.Success(response)
        } catch (e: Exception) {
            preBook.value = Resources.Error(e.message.toString())
        }
    }


    private val showPerBook =
        MutableStateFlow<Resources<List<ShowPerBook>>>(Resources.Loading())
    val showPerBookFlow: StateFlow<Resources<List<ShowPerBook>>>
        get() = showPerBook

    suspend fun showPerBook(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,

        ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.showPerBook(
                type, cid, deviceId, ln, lt, uid,
            )
            showPerBook.value = Resources.Success(response)
        } catch (e: Exception) {
            showPerBook.value = Resources.Error(e.message.toString())
        }
    }

    private val newTextilesArrival =
        MutableStateFlow<Resources<List<NewJewellArrival>>>(Resources.Loading())
    val newTextilesArrivalFlow: StateFlow<Resources<List<NewJewellArrival>>>
        get() = newTextilesArrival

    suspend fun newTextilesArrival(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        proCat: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.newJewelArrival(
                type, cid, deviceId, ln, lt, uid, proCat
            )
            newTextilesArrival.value = Resources.Success(response)
        } catch (e: Exception) {
            newTextilesArrival.value = Resources.Error(e.message.toString())
        }
    }

    private val selectedTextile =
        MutableStateFlow<Resources<TextileDetails>>(Resources.Loading())
    val selectedTextileFlow: StateFlow<Resources<TextileDetails>>
        get() = selectedTextile

    suspend fun selectedTextile(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        id: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.selectedTextile(
                type, cid, deviceId, ln, lt, uid, id
            )
            selectedTextile.value = Resources.Success(response)
        } catch (e: Exception) {
            selectedTextile.value = Resources.Error(e.message.toString())
        }
    }

    private val offerJewell =
        MutableStateFlow<Resources<List<OfferJewell>>>(Resources.Loading())
    val offerJewellFlow: StateFlow<Resources<List<OfferJewell>>>
        get() = offerJewell

    suspend fun offerJewell(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        proCat: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.offerJewell(
                type, cid, deviceId, ln, lt, uid, proCat
            )
            offerJewell.value = Resources.Success(response)
        } catch (e: Exception) {
            offerJewell.value = Resources.Error(e.message.toString())
        }
    }

    private val offerTextile =
        MutableStateFlow<Resources<List<OfferJewell>>>(Resources.Loading())
    val offerTextileFlow: StateFlow<Resources<List<OfferJewell>>>
        get() = offerTextile

    suspend fun offerTextile(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        proCat: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.offerJewell(
                type, cid, deviceId, ln, lt, uid, proCat
            )
            offerTextile.value = Resources.Success(response)
        } catch (e: Exception) {
            offerTextile.value = Resources.Error(e.message.toString())
        }
    }


    private val wishList =
        MutableStateFlow<Resources<WishlistAdd>>(Resources.Loading())
    val wishListFlow: StateFlow<Resources<WishlistAdd>>
        get() = wishList

    suspend fun wishList(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        id: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.wishList(
                type, cid, deviceId, ln, lt, uid, id
            )
            wishList.value = Resources.Success(response)
        } catch (e: Exception) {
            wishList.value = Resources.Error(e.message.toString())
        }
    }

    private val showWishList =
        MutableStateFlow<Resources<List<ShowWishList>>>(Resources.Loading())
    val showWishListFlow: StateFlow<Resources<List<ShowWishList>>>
        get() = showWishList

    suspend fun showWishList(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.showWishList(
                type, cid, deviceId, ln, lt, uid
            )
            showWishList.value = Resources.Success(response)
        } catch (e: Exception) {
            showWishList.value = Resources.Error(e.message.toString())
        }
    }

    private val closeWishList =
        MutableStateFlow<Resources<List<CloseWishList>>>(Resources.Loading())
    val closeWishListFlow: StateFlow<Resources<List<CloseWishList>>>
        get() = closeWishList

    suspend fun closeWishList(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        id: String
    ) {
        try {
            val response = jewelSoftRepo.closeWishList(type, cid, deviceId, ln, lt, uid, id)
            closeWishList.value = Resources.Success(response)
        } catch (e: Exception) {
            closeWishList.value = Resources.Error(e.message.toString())
        }
    }

    private val booked = MutableStateFlow<Resources<CloseWishList>>(Resources.Loading())
    val bookedFlow: StateFlow<Resources<CloseWishList>>
        get() = booked

    suspend fun booked(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        proId: String,
        proName: String,
        lId: String,
        price: String
    ) {
        try {
            val response =
                jewelSoftRepo.booked(type, cid, deviceId, ln, lt, uid, proId, proName, lId, price)
            booked.value = Resources.Success(response)
        } catch (e: Exception) {
            booked.value = Resources.Error(e.message.toString())
        }
    }


    private val feedbacks = MutableStateFlow<Resources<List<CloseWishList>>>(Resources.Loading())
    val feedbackFlow: StateFlow<Resources<List<CloseWishList>>>
        get() = feedbacks

    suspend fun feedback(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        lId: String,
        feedback: String,
    ) {
        try {
            val response =
                jewelSoftRepo.feedback(type, cid, deviceId, ln, lt, uid, lId, feedback)
            feedbacks.value = Resources.Success(response)
        } catch (e: Exception) {
            feedbacks.value = Resources.Error(e.message.toString())
        }
    }

    private val viewPager =
        MutableStateFlow<Resources<List<ViewPager>>>(Resources.Loading())
    val viewPagerFlow: StateFlow<Resources<List<ViewPager>>>
        get() = viewPager

    suspend fun viewPagers(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        id: String,
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.viewPager(
                type, cid, deviceId, ln, lt, uid,id
            )
            viewPager.value = Resources.Success(response)
        } catch (e: Exception) {
            viewPager.value = Resources.Error(e.message.toString())
        }
    }
}