package com.hoor.waterbyhoor

import android.content.Context
import android.content.SharedPreferences

/**
 * Single source of truth for all app data.
 * Both MainActivity and the home screen widget read and write through
 * this same SharedPreferences file, so they always agree on the data.
 */
object WaterRepository {

    private const val PREFS_NAME = "water_prefs"
    private const val KEY_DAILY_WATER = "daily_water"
    private const val KEY_DAILY_GOAL = "daily_goal"
    private const val KEY_SELECTED_AMOUNT = "selected_amount"
    private const val KEY_LAST_DATE = "last_updated_date"

    const val DEFAULT_GOAL = 1700
    const val DEFAULT_SELECTED_AMOUNT = 250

    const val MIN_GOAL = 500
    const val MAX_GOAL = 5000
    const val MIN_AMOUNT = 10
    const val MAX_AMOUNT = 1000

    private fun prefs(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Must be called before any read/write of daily_water.
     * If the "logical day" (which rolls over at 5:00 AM, see DateUtils)
     * has changed since the last update, resets today's total to 0.
     * This does NOT depend on the app ever being opened.
     */
    fun ensureFreshDay(context: Context) {
        val p = prefs(context)
        val today = DateUtils.currentDayKey()
        val lastDate = p.getString(KEY_LAST_DATE, null)
        if (lastDate != today) {
            p.edit()
                .putInt(KEY_DAILY_WATER, 0)
                .putString(KEY_LAST_DATE, today)
                .apply()
        }
    }

    fun getDailyWater(context: Context): Int {
        ensureFreshDay(context)
        return prefs(context).getInt(KEY_DAILY_WATER, 0)
    }

    fun getDailyGoal(context: Context): Int =
        prefs(context).getInt(KEY_DAILY_GOAL, DEFAULT_GOAL)

    fun setDailyGoal(context: Context, goal: Int) {
        val clamped = goal.coerceIn(MIN_GOAL, MAX_GOAL)
        prefs(context).edit().putInt(KEY_DAILY_GOAL, clamped).apply()
    }

    fun getSelectedAmount(context: Context): Int =
        prefs(context).getInt(KEY_SELECTED_AMOUNT, DEFAULT_SELECTED_AMOUNT)

    fun setSelectedAmount(context: Context, amount: Int) {
        val clamped = amount.coerceIn(MIN_AMOUNT, MAX_AMOUNT)
        prefs(context).edit().putInt(KEY_SELECTED_AMOUNT, clamped).apply()
    }

    /** Adds [amount] ml to today's total (used by the widget's "+ إضافة" button). */
    fun addWater(context: Context, amount: Int) {
        ensureFreshDay(context)
        val current = getDailyWater(context)
        val newValue = (current + amount).coerceAtLeast(0)
        prefs(context).edit().putInt(KEY_DAILY_WATER, newValue).apply()
    }
}
