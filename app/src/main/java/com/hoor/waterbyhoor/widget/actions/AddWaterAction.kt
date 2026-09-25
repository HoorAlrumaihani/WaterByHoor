package com.hoor.waterbyhoor.widget.actions

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.hoor.waterbyhoor.WaterRepository
import com.hoor.waterbyhoor.widget.WaterWidget

/**
 * Handles the "+ إضافة" button.
 * Runs entirely from the widget - never opens the main app.
 */
class AddWaterAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val amount = WaterRepository.getSelectedAmount(context)
        WaterRepository.addWater(context, amount)
        WaterWidget().update(context, glanceId)
    }
}
