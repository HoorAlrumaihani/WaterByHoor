package com.hoor.waterbyhoor.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.action.Action
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.defaultWeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.semantics.contentDescription
import androidx.glance.semantics.semantics
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.hoor.waterbyhoor.WaterRepository
import com.hoor.waterbyhoor.widget.actions.AddWaterAction
import com.hoor.waterbyhoor.widget.actions.SelectAmountAction
import com.hoor.waterbyhoor.widget.actions.SendWhatsAppAction

/** The most important part of the app: a minimal, modern home screen widget. */
class WaterWidget : GlanceAppWidget() {

    // Re-renders with the widget's exact current size on every resize,
    // so we can branch between the "compact" and "full" layouts below.
    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                WidgetContent(context)
            }
        }
    }
}

@Composable
private fun WidgetContent(context: Context) {
    // Checked on every render, so the daily reset never depends on the
    // main app being opened - only on the widget being drawn.
    WaterRepository.ensureFreshDay(context)

    val drank = WaterRepository.getDailyWater(context)
    val goal = WaterRepository.getDailyGoal(context)
    val selected = WaterRepository.getSelectedAmount(context)

    val size = LocalSize.current
    val isCompact = size.width < 180.dp

    val progressFraction = if (goal > 0) drank.toFloat() / goal.toFloat() else 0f
    val percent = if (goal > 0) (drank * 100) / goal else 0

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.widgetBackground)
            .cornerRadius(20.dp)
            .padding(12.dp)
    ) {
        // 1) Amount + 2) Goal
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "💧", style = TextStyle(fontSize = 18.sp))
            Spacer(modifier = GlanceModifier.width(6.dp))
            Text(
                text = "$drank / $goal ml",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlanceTheme.colors.onSurface
                )
            )
        }

        Spacer(modifier = GlanceModifier.height(6.dp))

        // 3) Progress
        LinearProgressIndicator(
            progress = progressFraction.coerceIn(0f, 1f),
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(8.dp)
                .semantics { contentDescription = "نسبة الإنجاز $percent بالمئة" },
            color = GlanceTheme.colors.primary,
            backgroundColor = GlanceTheme.colors.surfaceVariant
        )

        Spacer(modifier = GlanceModifier.height(4.dp))

        Text(
            text = "$percent%",
            style = TextStyle(fontSize = 11.sp, color = GlanceTheme.colors.onSurfaceVariant)
        )

        if (!isCompact) {
            // 4) Amount to add
            Spacer(modifier = GlanceModifier.height(10.dp))
            Text(
                text = "الكمية: $selected ml",
                style = TextStyle(fontSize = 11.sp, color = GlanceTheme.colors.onSurfaceVariant)
            )
            Spacer(modifier = GlanceModifier.height(4.dp))
            AmountRow(amounts = listOf(10, 50, 100, 200), selected = selected)
            Spacer(modifier = GlanceModifier.height(4.dp))
            AmountRow(amounts = listOf(250, 300, 330), selected = selected)
            Spacer(modifier = GlanceModifier.height(8.dp))
        } else {
            Spacer(modifier = GlanceModifier.height(8.dp))
        }

        // 5) Add button + 6) WhatsApp button
        Row(modifier = GlanceModifier.fillMaxWidth()) {
            ActionChip(
                label = "+ إضافة",
                description = "إضافة $selected مل إلى كمية اليوم",
                modifier = GlanceModifier.defaultWeight(),
                onClick = actionRunCallback<AddWaterAction>()
            )
            if (!isCompact) {
                Spacer(modifier = GlanceModifier.width(6.dp))
                ActionChip(
                    label = "أرسل للمدربة",
                    description = "إرسال ملخص اليوم إلى المدربة عبر واتساب",
                    modifier = GlanceModifier.defaultWeight(),
                    onClick = actionRunCallback<SendWhatsAppAction>()
                )
            }
        }
    }
}

@Composable
private fun AmountRow(amounts: List<Int>, selected: Int) {
    Row(modifier = GlanceModifier.fillMaxWidth()) {
        amounts.forEachIndexed { index, amount ->
            AmountChip(
                amount = amount,
                isSelected = amount == selected,
                modifier = GlanceModifier.defaultWeight()
            )
            if (index != amounts.lastIndex) {
                Spacer(modifier = GlanceModifier.width(4.dp))
            }
        }
    }
}

@Composable
private fun AmountChip(amount: Int, isSelected: Boolean, modifier: GlanceModifier) {
    Box(
        modifier = modifier
            .background(if (isSelected) GlanceTheme.colors.primary else GlanceTheme.colors.surfaceVariant)
            .cornerRadius(10.dp)
            .clickable(
                actionRunCallback<SelectAmountAction>(
                    actionParametersOf(SelectAmountAction.AMOUNT_KEY to amount)
                )
            )
            .padding(vertical = 6.dp)
            .semantics { contentDescription = "اختيار $amount مل" },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$amount",
            style = TextStyle(
                fontSize = 11.sp,
                color = if (isSelected) GlanceTheme.colors.onPrimary else GlanceTheme.colors.onSurfaceVariant,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

@Composable
private fun ActionChip(
    label: String,
    description: String,
    modifier: GlanceModifier,
    onClick: Action
) {
    Box(
        modifier = modifier
            .background(GlanceTheme.colors.primary)
            .cornerRadius(14.dp)
            .clickable(onClick)
            .padding(vertical = 8.dp)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 12.sp,
                color = GlanceTheme.colors.onPrimary,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
