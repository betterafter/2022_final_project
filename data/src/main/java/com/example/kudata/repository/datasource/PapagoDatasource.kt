package com.example.kudata.repository.datasource

import com.example.kudata.api.NetworkModule.createPapagoService
import com.example.kudata.entity.PapagoRequest
import com.example.kudata.utils.PAPAGO_ID
import com.example.kudata.utils.PAPAGO_SECRET

class PapagoDatasource {
    suspend fun getText(text : String, source : String, target : String): String?
        = createPapagoService().papagoService(
                    PAPAGO_ID,
                    PAPAGO_SECRET,
                    PapagoRequest(source = source, target = target, text = text)
                ).body()?.message?.result?.translatedText

}