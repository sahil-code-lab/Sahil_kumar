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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ActiveExerciseItem
import com.example.ui.MainViewModel
import com.example.ui.components.FitForgeCard
import com.example.ui.components.NeonPrimaryButton
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FlameRed
import com.example.ui.theme.HighProteinOrange
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.NeonGreenDark
import com.example.ui.theme.NeonGreenPrimary
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@Composable
fun ActiveWorkoutScreen(
    viewModel: MainViewModel,
    onFinish: () -> Unit
) {
    val title by viewModel.activeWorkoutTitle.collectAsState()
    val elapsed by viewModel.elapsedSeconds.collectAsState()
    val exercises by viewModel.activeExercises.collectAsState()
    val restRemaining by viewModel.restSecondsRemaining.collectAsState()

    var showFinishDialog by remember { mutableStateOf(false) }
    var sessionNotes by remember { mutableStateOf("") }

    val minutes = elapsed / 60
    val seconds = elapsed % 60
    val timerString = String.format("%02d:%02d", minutes, seconds)

    var totalVolume = 0f
    var completedSetsCount = 0
    var totalSetsCount = 0
    exercises.forEach { ex ->
        ex.sets.forEach { set ->
            totalSetsCount++
            if (set.isCompleted) {
                completedSetsCount++
                totalVolume += (set.weightKg * set.reps)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Workout Session Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurfaceElevated)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "LIVE FORGE SESSION",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreenPrimary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "⏱ $timerString",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NeonGreenBright
                        )
                        Text(
                            text = "•  Sets: $completedSetsCount / $totalSetsCount",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "•  ${totalVolume.toInt()} kg",
                            fontSize = 13.sp,
                            color = ElectricCyan
                        )
                    }
                }

                Button(
                    onClick = { showFinishDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonGreenPrimary,
                        contentColor = PureBlack
                    ),
                    modifier = Modifier.testTag("finish_workout_btn")
                ) {
                    Text("FINISH", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // Rest Timer Floating Banner
        if (restRemaining > 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NeonGreenDark)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = "Rest", tint = TextWhite, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Resting: ${restRemaining}s remaining",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "+30s",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureBlack,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonGreenBright)
                                .clickable { viewModel.startRestTimer(restRemaining + 30) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                        Text(
                            text = "Skip",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkSurface)
                                .clickable { viewModel.cancelRestTimer() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Exercises Lazy List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            itemsIndexed(exercises) { exIndex, exercise ->
                ActiveExerciseCard(
                    exercise = exercise,
                    onToggleSet = { setIndex ->
                        viewModel.toggleSetCompletion(exIndex, setIndex, exercise.restSeconds)
                    },
                    onWeightChange = { setIndex, newWeight ->
                        exercise.sets[setIndex].weightKg = newWeight
                    },
                    onRepsChange = { setIndex, newReps ->
                        exercise.sets[setIndex].reps = newReps
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // Finish Workout Dialog
    if (showFinishDialog) {
        AlertDialog(
            onDismissRequest = { showFinishDialog = false },
            title = {
                Text("Complete Workout Session?", fontWeight = FontWeight.Bold, color = TextWhite)
            },
            text = {
                Column {
                    Text(
                        text = "Great lift! You completed $completedSetsCount sets with ${totalVolume.toInt()} kg total volume in $minutes minutes.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = sessionNotes,
                        onValueChange = { sessionNotes = it },
                        placeholder = { Text("Add workout notes (e.g. felt great on bench)") },
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
                        showFinishDialog = false
                        viewModel.finishWorkoutSession(sessionNotes)
                        onFinish()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreenPrimary)
                ) {
                    Text("SAVE & FINISH", color = PureBlack, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFinishDialog = false }) {
                    Text("RESUME", color = TextSecondary)
                }
            },
            containerColor = DarkSurfaceElevated
        )
    }
}

@Composable
private fun ActiveExerciseCard(
    exercise: ActiveExerciseItem,
    onToggleSet: (Int) -> Unit,
    onWeightChange: (Int, Float) -> Unit,
    onRepsChange: (Int, Int) -> Unit
) {
    FitForgeCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = exercise.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "${exercise.muscleGroup} • Target: ${exercise.targetReps} reps • ${exercise.restSeconds}s rest",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            // Sets Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("SET", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.width(36.dp))
                Text("WEIGHT (KG)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.weight(1.2f))
                Text("REPS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.weight(1f))
                Text("DONE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.width(44.dp))
            }

            // Sets rows
            exercise.sets.forEachIndexed { setIdx, set ->
                var currentWeight by remember(set.weightKg) { mutableStateOf(set.weightKg) }
                var currentReps by remember(set.reps) { mutableStateOf(set.reps) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (set.isCompleted) NeonGreenDark.copy(alpha = 0.15f) else Color.Transparent)
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Set Number
                    Text(
                        text = "#${set.setNumber}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (set.isCompleted) NeonGreenBright else TextWhite,
                        modifier = Modifier.width(36.dp)
                    )

                    // Weight adjuster
                    Row(
                        modifier = Modifier.weight(1.2f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SmallStepperButton(
                            icon = Icons.Default.Remove,
                            onClick = {
                                val next = (currentWeight - 2.5f).coerceAtLeast(0f)
                                currentWeight = next
                                onWeightChange(setIdx, next)
                            }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${currentWeight.toInt()}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            modifier = Modifier.width(28.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        SmallStepperButton(
                            icon = Icons.Default.Add,
                            onClick = {
                                val next = currentWeight + 2.5f
                                currentWeight = next
                                onWeightChange(setIdx, next)
                            }
                        )
                    }

                    // Reps adjuster
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SmallStepperButton(
                            icon = Icons.Default.Remove,
                            onClick = {
                                val next = (currentReps - 1).coerceAtLeast(1)
                                currentReps = next
                                onRepsChange(setIdx, next)
                            }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$currentReps",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            modifier = Modifier.width(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        SmallStepperButton(
                            icon = Icons.Default.Add,
                            onClick = {
                                val next = currentReps + 1
                                currentReps = next
                                onRepsChange(setIdx, next)
                            }
                        )
                    }

                    // Checkmark Complete button
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (set.isCompleted) NeonGreenPrimary else DarkSurfaceElevated)
                            .border(1.dp, if (set.isCompleted) NeonGreenBright else DarkSurfaceBorder, RoundedCornerShape(8.dp))
                            .clickable { onToggleSet(setIdx) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Complete Set",
                            tint = if (set.isCompleted) PureBlack else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallStepperButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(DarkSurfaceElevated)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
    }
}
