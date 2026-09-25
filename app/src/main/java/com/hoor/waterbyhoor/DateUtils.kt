package com.hoor.waterbyhoor

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {

    /** The day rolls over at 5:00 AM local time, not at midnight. */
    private const val RESET_HOUR = 5

    /**
     * Returns a stable "yyyy-MM-dd" key for the current logical day.
     * Between midnight and 5:00 AM, this still returns yesterday's key.
     */
    fun currentDayKey(): String {
        val cal = Calendar.getInstance()
        if (cal.get(Calendar.HOUR_OF_DAY) < RESET_HOUR) {
            cal.add(Calendar.DAY_OF_YEAR, -1)
        }
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formatter.format(cal.time)
    }
}
