package com.hoor.waterbyhoor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.updateAll
import com.hoor.waterbyhoor.widget.WaterWidget
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WaterScreen()
                }
            }
        }
    }
}

@Composable
private fun WaterScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var drank by remember { mutableIntStateOf(WaterRepository.getDailyWater(context)) }
    var goal by remember { mutableIntStateOf(WaterRepository.getDailyGoal(context)) }
    var defaultAmount by remember { mutableIntStateOf(WaterRepository.getSelectedAmount(context)) }

    fun refreshWidget() {
        scope.launch { WaterWidget().updateAll(context) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Text("💧 الماء", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        Text("اليوم: $drank ml", fontSize = 18.sp)
        Spacer(Modifier.height(4.dp))
        Text("الهدف اليومي: $goal ml", fontSize = 18.sp)

        Spacer(Modifier.height(32.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        Text("تعديل الهدف", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        StepperRow(
            value = goal,
            step = 100,
            min = WaterRepository.MIN_GOAL,
            max = WaterRepository.MAX_GOAL,
            onValueChange = { newValue ->
                goal = newValue
                WaterRepository.setDailyGoal(context, newValue)
                refreshWidget()
            }
        )

        Spacer(Modifier.height(24.dp))

        Text("تعديل كمية الإضافة الافتراضية", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        StepperRow(
            value = defaultAmount,
            step = 10,
            min = WaterRepository.MIN_AMOUNT,
            max = WaterRepository.MAX_AMOUNT,
            onValueChange = { newValue ->
                defaultAmount = newValue
                WaterRepository.setSelectedAmount(context, newValue)
                refreshWidget()
            }
        )

        Spacer(Modifier.height(32.dp))
        Text(
            "أضيفي Widget \"WaterByHoor\" إلى الشاشة الرئيسية لتسجيل كمية الماء يوميًا من دون فتح التطبيق.",
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StepperRow(
    value: Int,
    step: Int,
    min: Int,
    max: Int,
    onValueChange: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(onClick = { onValueChange((value - step).coerceIn(min, max)) }) {
            Text("-")
        }
        Text(
            "$value ml",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp
        )
        OutlinedButton(onClick = { onValueChange((value + step).coerceIn(min, max)) }) {
            Text("+")
        }
    }
}
