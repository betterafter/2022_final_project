package com.example.kudata.repository.datasource.papago

import com.example.kudata.api.NetworkModule
import com.example.kudata.entity.PapagoLangRequest
import com.example.kudata.entity.PapagoLangResponse
import com.example.kudata.entity.PapagoRequest
import com.example.kudata.entity.TranslateResult
import com.example.kudata.utils.PAPAGO_ID
import com.example.kudata.utils.PAPAGO_ID_2
import com.example.kudata.utils.PAPAGO_SECRET
import com.example.kudata.utils.PAPAGO_SECRET_2

class PapagoDatasourceImpl : PapagoDatasource {
    override suspend fun getPapagoTranslateResult(text: String, source: String, target: String): TranslateResult? =
        NetworkModule.providePapagoService().papagoService(
            PAPAGO_ID_2,
            PAPAGO_SECRET_2,
            PapagoRequest(source = source, target = target, text = text)
        ).body()?.message?.result

    override suspend fun getPapagoLangResult(query: String): String? =
        NetworkModule.providePapagoService().papagoDetectLang(
            PAPAGO_ID_2,
            PAPAGO_SECRET_2,
            PapagoLangRequest(query = query)
        ).body()?.langCode
}