package com.hoor.waterbyhoor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object WhatsAppHelper {

    // Coach's WhatsApp number, in wa.me format (no "+", no spaces).
    private const val COACH_PHONE = "201275985319"

    /** Builds today's summary message, matching the requested template. */
    fun buildDailyMessage(drank: Int, goal: Int): String {
        val percent = if (goal > 0) (drank * 100) / goal else 0
        return "💧 •°•°•°•°•°•°•°•°•°•° 💧\n" +
            "💦 متابعة الماء اليوم\n\n" +
            "•°•°•°•°•°•°•°•°•°•°•°\n" +
            "🥤 الكمية المشروبة\n" +
            "$drank ml\n" +
            "📊 نسبة الإنجاز\n" +
            "$percent%\n" +
            "•°•°•°•°•°•°•°•°•°•°•°\n" +
            "💧 •°•°•°•°•°•°•°•°•°•° 💧"
    }

    /**
     * Opens WhatsApp with the message pre-filled, ready to send.
     * Never crashes if WhatsApp (or a browser) isn't available.
     */
    fun sendToCoach(context: Context, message: String) {
        try {
            val uri = Uri.parse("https://wa.me/$COACH_PHONE?text=" + Uri.encode(message))
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.applicationContext.startActivity(intent)
        } catch (e: Exception) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context.applicationContext,
                    "تعذّر فتح واتساب على هذا الجهاز",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
