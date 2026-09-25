package com.hoor.waterbyhoor.widget.actions

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.hoor.waterbyhoor.WaterRepository
import com.hoor.waterbyhoor.widget.WaterWidget

/** Handles tapping one of the preset amount chips (10, 50, 100, 200, 250, 300, 330 ml). */
class SelectAmountAction : ActionCallback {

    companion object {
        val AMOUNT_KEY = ActionParameters.Key<Int>("selected_amount")
    }

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val amount = parameters[AMOUNT_KEY] ?: return
        WaterRepository.setSelectedAmount(context, amount)
        WaterWidget().update(context, glanceId)
    }
}
