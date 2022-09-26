package com.example.kudata.repository

import com.example.kudata.repository.datasource.PapagoDatasource
import javax.inject.Inject

class PapagoRepositoryImpl @Inject constructor(
    private val papagoDatasource: PapagoDatasource,
) {
    suspend fun getPapagoText(text: String, source: String, target: String): String?
     = papagoDatasource.getText(text = text, source = source, target = target)


}