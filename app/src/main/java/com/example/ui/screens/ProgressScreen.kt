package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AchievementEntity
import com.example.ui.MainViewModel
import com.example.ui.components.FitForgeCard
import com.example.ui.components.InteractiveWeightChart
import com.example.ui.components.NeonOutlinedButton
import com.example.ui.components.SectionHeader
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.HighProteinOrange
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.NeonGreenDark
import com.example.ui.theme.NeonGreenPrimary
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProgressScreen(viewModel: MainViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val metrics by viewModel.allBodyMetrics.collectAsState()
    val prs by viewModel.allPersonalRecords.collectAsState()
    val achievements by viewModel.allAchievements.collectAsState()
    val sessions by viewModel.allWorkoutSessions.collectAsState()

    var showLogMetricDialog by remember { mutableStateOf(false) }
    var showAddPrDialog by remember { mutableStateOf(false) }

    val targetWeight = profile?.targetWeightKg ?: 80f
    val chartPoints = metrics.takeLast(7).mapIndexed { idx, item ->
        val sdf = SimpleDateFormat("MM/dd", Locale.getDefault())
        val dateLabel = sdf.format(Date(item.timestamp))
        Pair(dateLabel, item.weightKg)
    }

    val latestMetric = metrics.lastOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                title = "Body Progress & PRs",
                subtitle = "Track weight changes, body measurements, and strength personal records"
            )
        }

        // Weight Trend Interactive Chart Card
        item {
            FitForgeCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "WEIGHT TRANSFORMATION",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonGreenPrimary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "${profile?.weightKg ?: 74.5f} kg",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NeonGreenPrimary)
                                .clickable { showLogMetricDialog = true }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "+ Log Weight",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PureBlack
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    InteractiveWeightChart(
                        dataPoints = chartPoints,
                        targetWeight = targetWeight
                    )
                }
            }
        }

        // Body Measurements Tape Card
        item {
            FitForgeCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeader(
                        title = "Body Circumference Measurements",
                        subtitle = "Track muscle gain across key muscle groups",
                        actionText = "+ Update",
                        onActionClick = { showLogMetricDialog = true }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MeasurementBox("Chest", latestMetric?.chestCm ?: 102f, "cm", Modifier.weight(1f))
                        MeasurementBox("Arms", latestMetric?.armsCm ?: 37.5f, "cm", Modifier.weight(1f))
                        MeasurementBox("Waist", latestMetric?.waistCm ?: 83.2f, "cm", Modifier.weight(1f))
                        MeasurementBox("Thighs", latestMetric?.thighsCm ?: 58.5f, "cm", Modifier.weight(1f))
                    }
                }
            }
        }

        // Personal Records (PRs) Shelf
        item {
            FitForgeCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeader(
                        title = "Personal Strength Records",
                        subtitle = "Your all-time heaviest compound lifts",
                        actionText = "+ Add PR",
                        onActionClick = { showAddPrDialog = true }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    prs.forEach { pr ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurfaceElevated)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(ElectricCyan.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = pr.exerciseName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = "${pr.reps} Rep Max",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Text(
                                text = "${pr.weightKg} kg",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = NeonGreenBright
                            )
                        }
                    }
                }
            }
        }

        // Achievements & Badges Grid
        item {
            FitForgeCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeader(
                        title = "Achievements & Milestones",
                        subtitle = "Badges earned through consistency and dedication"
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    achievements.forEach { ach ->
                        AchievementItemRow(achievement = ach)
                    }
                }
            }
        }

        // Recent Workout History
        item {
            FitForgeCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeader(
                        title = "Completed Workout History",
                        subtitle = "Logs of past lifting sessions"
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    if (sessions.isEmpty()) {
                        Text("No completed workouts yet. Start one now!", color = TextSecondary)
                    } else {
                        sessions.forEach { sess ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DarkSurfaceElevated)
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = sess.workoutTitle,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = "${sess.durationMinutes} mins • ${sess.setsCompleted} sets • ${sess.totalVolumeKg.toInt()} kg volume",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                    if (sess.notes.isNotBlank()) {
                                        Text(
                                            text = "\"${sess.notes}\"",
                                            fontSize = 11.sp,
                                            color = NeonGreenBright,
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Add Metric Dialog
    if (showLogMetricDialog) {
        var inputWeight by remember { mutableStateOf("${profile?.weightKg ?: 74.5f}") }
        var inputChest by remember { mutableStateOf("${latestMetric?.chestCm ?: 102f}") }
        var inputWaist by remember { mutableStateOf("${latestMetric?.waistCm ?: 83.2f}") }
        var inputArms by remember { mutableStateOf("${latestMetric?.armsCm ?: 37.5f}") }
        var inputThighs by remember { mutableStateOf("${latestMetric?.thighsCm ?: 58.5f}") }
        var inputNote by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showLogMetricDialog = false },
            title = {
                Text("Log Body Weight & Measurements", fontWeight = FontWeight.Bold, color = TextWhite)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = inputWeight,
                        onValueChange = { inputWeight = it },
                        label = { Text("Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreenPrimary,
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = inputChest,
                            onValueChange = { inputChest = it },
                            label = { Text("Chest (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonGreenPrimary,
                                unfocusedBorderColor = DarkSurfaceBorder,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )
                        OutlinedTextField(
                            value = inputArms,
                            onValueChange = { inputArms = it },
                            label = { Text("Arms (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonGreenPrimary,
                                unfocusedBorderColor = DarkSurfaceBorder,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = inputWaist,
                            onValueChange = { inputWaist = it },
                            label = { Text("Waist (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonGreenPrimary,
                                unfocusedBorderColor = DarkSurfaceBorder,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )
                        OutlinedTextField(
                            value = inputThighs,
                            onValueChange = { inputThighs = it },
                            label = { Text("Thighs (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonGreenPrimary,
                                unfocusedBorderColor = DarkSurfaceBorder,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = inputNote,
                        onValueChange = { inputNote = it },
                        label = { Text("Note (e.g. morning weigh-in, post-cut)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreenPrimary,
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val w = inputWeight.toFloatOrNull() ?: 75f
                        val c = inputChest.toFloatOrNull()
                        val wa = inputWaist.toFloatOrNull()
                        val a = inputArms.toFloatOrNull()
                        val t = inputThighs.toFloatOrNull()
                        viewModel.logBodyMetric(w, c, wa, a, t, inputNote)
                        showLogMetricDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreenPrimary)
                ) {
                    Text("SAVE MEASUREMENTS", color = PureBlack, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogMetricDialog = false }) {
                    Text("CANCEL", color = TextSecondary)
                }
            },
            containerColor = DarkSurfaceElevated
        )
    }

    // Add PR Dialog
    if (showAddPrDialog) {
        var exerciseName by remember { mutableStateOf("Incline Dumbbell Press") }
        var weightKg by remember { mutableStateOf("36") }
        var reps by remember { mutableStateOf("8") }

        AlertDialog(
            onDismissRequest = { showAddPrDialog = false },
            title = {
                Text("Log Personal Record (PR)", fontWeight = FontWeight.Bold, color = TextWhite)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = exerciseName,
                        onValueChange = { exerciseName = it },
                        label = { Text("Lift / Exercise Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreenPrimary,
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = weightKg,
                            onValueChange = { weightKg = it },
                            label = { Text("Weight (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonGreenPrimary,
                                unfocusedBorderColor = DarkSurfaceBorder,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )
                        OutlinedTextField(
                            value = reps,
                            onValueChange = { reps = it },
                            label = { Text("Reps") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonGreenPrimary,
                                unfocusedBorderColor = DarkSurfaceBorder,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val w = weightKg.toFloatOrNull() ?: 50f
                        val r = reps.toIntOrNull() ?: 5
                        if (exerciseName.isNotBlank()) {
                            viewModel.logPersonalRecord(exerciseName, w, r)
                            showAddPrDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreenPrimary)
                ) {
                    Text("SAVE RECORD", color = PureBlack, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPrDialog = false }) {
                    Text("CANCEL", color = TextSecondary)
                }
            },
            containerColor = DarkSurfaceElevated
        )
    }
}

@Composable
private fun MeasurementBox(label: String, value: Float, unit: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurfaceElevated)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 11.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(2.dp))
            Text("$value", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text(unit, fontSize = 10.sp, color = NeonGreenBright)
        }
    }
}

@Composable
private fun AchievementItemRow(achievement: AchievementEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(DarkSurfaceElevated)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        if (achievement.isUnlocked) NeonGreenPrimary.copy(alpha = 0.2f) else DarkSurface,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (achievement.isUnlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (achievement.isUnlocked) NeonGreenPrimary else TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = achievement.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (achievement.isUnlocked) TextWhite else TextSecondary
                )
                Text(
                    text = achievement.description,
                    fontSize = 11.sp,
                    color = TextMuted,
                    lineHeight = 14.sp
                )
            }
        }

        if (achievement.isUnlocked) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(NeonGreenDark)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("UNLOCKED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            }
        } else {
            val pct = (achievement.progress.toFloat() / achievement.maxProgress.coerceAtLeast(1)).coerceIn(0f, 1f)
            Text(
                text = "${(pct * 100).toInt()}%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
        }
    }
}
