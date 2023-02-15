package org.quram.common.model

import org.quram.common.model.HizbQuarter

data class QuarterMapping(
    val number: Int,
    val page: Int,
    val quarters: List<HizbQuarter>
)
