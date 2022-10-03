package com.example.kudata.repository.datasource.papago

import com.example.kudata.api.NetworkModule
import com.example.kudata.entity.PapagoRequest
import com.example.kudata.entity.TranslateResult
import com.example.kudata.utils.PAPAGO_ID
import com.example.kudata.utils.PAPAGO_SECRET

class PapagoDatasourceImpl : PapagoDatasource {
    override suspend fun getPapagoTranslateResult(text : String, source : String, target : String): TranslateResult?
            = NetworkModule.providePapagoService().papagoService(
        PAPAGO_ID,
        PAPAGO_SECRET,
        PapagoRequest(source = source, target = target, text = text)
    ).body()?.message?.result
}