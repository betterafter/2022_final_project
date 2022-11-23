package com.example.kudata.entity

data class FcmResponse(
    var multicastId: String?,
    var success: Number?,
    var failure: Number?,
    var canonicalIds: Number?,
    var results: List<FcmMessageResult?>?
)



//"multicast_id": 4061360726165544618,
//"success": 1,
//"failure": 0,
//"canonical_ids": 0,
//"results": [
//{
//    "message_id": "0:1669127176477760%8edbf2b58edbf2b5"
//}
//]