package com.hoor.waterbyhoor.widget.actions

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.hoor.waterbyhoor.WaterRepository
import com.hoor.waterbyhoor.WhatsAppHelper

/** Handles "أرسل للمدربة": builds today's message and opens WhatsApp with it ready to send. */
class SendWhatsAppAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val drank = WaterRepository.getDailyWater(context)
        val goal = WaterRepository.getDailyGoal(context)
        val message = WhatsAppHelper.buildDailyMessage(drank, goal)
        WhatsAppHelper.sendToCoach(context, message)
    }
}
