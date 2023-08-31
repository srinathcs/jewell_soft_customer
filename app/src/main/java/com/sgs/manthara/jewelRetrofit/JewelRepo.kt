package com.sgs.manthara.jewelRetrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody

class JewelRepo {
    suspend fun jewelSoft(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        mobile: String,
        token: String,
    ) = JewelSoftRetro.api.jewelSoft(type, cid, deviceId, ln, lt, mobile, token)

    suspend fun otpRec(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        otp: String
    ) = JewelSoftRetro.api.otpRec(type, cid, deviceId, ln, lt, uid, otp)

    suspend fun schemeType(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = JewelSoftRetro.api.schemeDetails(type, cid, deviceId, ln, lt, uid)

    suspend fun schemeDetails(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        id: String,
        uid: String
    ) = JewelSoftRetro.api.schemeType(type, cid, deviceId, ln, lt, id, uid)

    suspend fun todayRate(
        type: String,
        subType: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = JewelSoftRetro.api.goldRate(type, subType, cid, deviceId, ln, lt, uid)

    suspend fun schemeTypeAuto(
        type: String,
        subType: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = JewelSoftRetro.api.schemeTypeAuto(type, subType, cid, deviceId, ln, lt, uid)

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
    ) = JewelSoftRetro.api.saveScheme(
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
    ) = JewelSoftRetro.api.enrollmentScheme(type, cid, sub_type, sch, date, deviceId, ln, lt, uid)

    suspend fun pendingDue(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = JewelSoftRetro.api.pendingDue(type, cid, deviceId, ln, lt, uid)

    suspend fun jewellType(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String,
        category: String
    ) = JewelSoftRetro.api.jewellType(type, cid, deviceId, ln, lt, uid, category)

    suspend fun getImage(
        type:RequestBody,
        cid:RequestBody,
        ln:RequestBody,
        lt:RequestBody,
        device: RequestBody,
        uid:RequestBody,
        count: RequestBody,
        jewelType:RequestBody,
        gram:RequestBody,
        description:RequestBody,
        pro_img: MutableList<MultipartBody.Part>,
        )=JewelSoftRetro.api.addCustomDesign(type,cid,ln,lt,device,uid,count, jewelType, gram, description, pro_img)

    suspend fun paidAmount(
        type: String,
        cid: String,
        deviceId: String,
        ln: String,
        lt: String,
        uid: String
    ) = JewelSoftRetro.api.paidAmount(type, cid, deviceId, ln, lt, uid)
}