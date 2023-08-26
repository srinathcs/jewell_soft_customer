package com.sgs.manthara.jewelRetrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
        otp: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.otpRec(type, cid, deviceId, ln, lt, uid, otp)
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
        uid: String

    ) = viewModelScope.launch {
        try {
            val response =
                jewelSoftRepo.schemeType(type, cid, deviceId, ln, lt, uid)
            schemeType.value = Resources.Success(response)
        } catch (exception: Exception) {
            schemeType.value = Resources.Error(exception.message.toString())
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

    private val schemeTypeAuto = MutableStateFlow<Resources<List<SchemeTypeAuto>>>(Resources.Loading())
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
            val response = jewelSoftRepo.saveScheme(type, cid, deviceId, ln, lt, uid, name, maturityDate, remark, emi, freeEmi, startDate, schemeName, schemeType, monthlyEmi, totalEmi, purity)
            saveScheme.value = Resources.Success(response)
        } catch (e: Exception) {
            saveScheme.value = Resources.Error(e.message.toString())
        }
    }

    private val enrollmentScheme =
        MutableStateFlow<Resources<List<EnrollmentScheme>>>(Resources.Loading())
    val enrollmentSchemeFlow: StateFlow<Resources<List<EnrollmentScheme>>> = enrollmentScheme

    suspend fun enrollmentScheme(
        type: String,
        cid: String,
        sub_type: String,
        sch:String,
        date: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = viewModelScope.launch {
        try {
            val response = jewelSoftRepo.enrollmentScheme(
                type, cid, sub_type, sch ,date,deviceId, ln, lt, uid
            )
            enrollmentScheme.value = Resources.Success(response)
        } catch (e: Exception) {
            enrollmentScheme.value = Resources.Error(e.message.toString())
        }
    }

    private val pendingDue =
        MutableStateFlow<Resources<List<PendingDue>>>(Resources.Loading())
    val pendingDueFlow: StateFlow<Resources<List<PendingDue>>> = pendingDue

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
                type, cid,deviceId, ln, lt, uid
            )
            pendingDue.value = Resources.Success(response)
        } catch (e: Exception) {
            pendingDue.value = Resources.Error(e.message.toString())
        }
    }
}