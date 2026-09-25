package com.hoor.waterbyhoor.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * Entry point the Android system talks to (APPWIDGET_UPDATE broadcasts etc.).
 * All real UI logic lives in WaterWidget / WidgetContent.
 */
class WaterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WaterWidget()
}
