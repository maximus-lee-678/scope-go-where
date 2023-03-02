package ict2105.team02.application.model

import java.util.*

data class ResultData (
    val fluidResult: String?,
    val fluidAction: String?,
    val fluidComment: String?,
    val quarantineRequired: Boolean?,
    val recordedBy: String?,
    val swabResult: Boolean?,
    val swabAction: String?,
    val swabCultureComment: String?,
    val repeatDateMS: Date?,
    val borescope: Boolean?,
    val waterATPRLU: Int?,
    val swapATPRLU: Int?,
    val resultDate: Date?,
    val swabDate: Date?,
        )