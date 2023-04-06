package com.monnl.habitual.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.monnl.habitual.data.models.models.Habit

@Composable
fun HabitLinearProgressBar(habit: Habit, modifier: Modifier) {

    val targetProgress = habit.completeTimes?.toFloat()!! / habit.targetTimes?.toFloat()!!

    val progress = remember { Animatable(0f) }

    LaunchedEffect(habit) {
        progress.animateTo(
            targetValue = targetProgress,
            animationSpec = tween(1200)
        )
    }

    Column(modifier = modifier) {
        LinearProgressIndicator(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth(),
            strokeCap = StrokeCap.Round,
            progress = progress.value
        )
    }
}