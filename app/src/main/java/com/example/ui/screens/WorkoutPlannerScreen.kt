package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.EquipmentType
import com.example.data.model.ExerciseDetail
import com.example.data.model.ExperienceLevel
import com.example.data.model.FitnessGoal
import com.example.ui.ActiveExerciseItem
import com.example.ui.ActiveWorkoutSet
import com.example.ui.MainViewModel
import com.example.ui.components.FitForgeCard
import com.example.ui.components.NeonOutlinedButton
import com.example.ui.components.NeonPrimaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.theme.DarkSurface
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

@Composable
fun WorkoutPlannerScreen(
    viewModel: MainViewModel,
    onStartSpecificWorkout: (String, List<ActiveExerciseItem>) -> Unit
) {
    val goal by viewModel.selectedGoal.collectAsState()
    val level by viewModel.selectedLevel.collectAsState()
    val equipment by viewModel.selectedEquipment.collectAsState()
    val days by viewModel.selectedDays.collectAsState()
    val plan by viewModel.generatedPlan.collectAsState()
    val isGenerating by viewModel.isGeneratingPlan.collectAsState()

    var selectedDayIndex by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                title = "AI Workout Routine Planner",
                subtitle = "Periodized programming tailored to your goals & gear"
            )
        }

        // Configuration Filters Card
        item {
            FitForgeCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "CUSTOMIZE PARAMETERS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreenPrimary,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Target Goal", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FitnessGoal.values().forEach { g ->
                            FilterChip(
                                label = g.displayName,
                                isSelected = g == goal,
                                onClick = { viewModel.setPlannerPreferences(g, level, equipment, days) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Experience Level", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ExperienceLevel.values().forEach { l ->
                            FilterChip(
                                label = l.displayName.substringBefore(" "),
                                isSelected = l == level,
                                onClick = { viewModel.setPlannerPreferences(goal, l, equipment, days) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Available Equipment", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EquipmentType.values().forEach { eq ->
                            FilterChip(
                                label = eq.displayName,
                                isSelected = eq == equipment,
                                onClick = { viewModel.setPlannerPreferences(goal, level, eq, days) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Days Per Week", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(3, 4, 5, 6).forEach { d ->
                            FilterChip(
                                label = "$d Days",
                                isSelected = d == days,
                                onClick = { viewModel.setPlannerPreferences(goal, level, equipment, d) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    NeonPrimaryButton(
                        text = if (isGenerating) "GENERATING ROUTINE..." else "GENERATE AI ROUTINE",
                        onClick = { viewModel.generateNewPlan() },
                        enabled = !isGenerating,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "generate_routine_btn",
                        leadingIcon = {
                            if (isGenerating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = PureBlack,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PureBlack)
                            }
                        }
                    )
                }
            }
        }

        // Generated Plan Overview Card
        item {
            FitForgeCard(
                borderColor = NeonGreenPrimary,
                borderWidth = 1.dp,
                backgroundColor = DarkSurfaceElevated
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = plan.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = plan.description,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    // Day Selector Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        plan.days.forEachIndexed { index, day ->
                            val isSelected = index == selectedDayIndex
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) NeonGreenPrimary else DarkSurface)
                                    .clickable { selectedDayIndex = index }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Day ${day.dayNumber}: ${day.focus}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) PureBlack else TextWhite
                                )
                            }
                        }
                    }
                }
            }
        }

        // Selected Day Exercise List
        val currentDay = plan.days.getOrNull(selectedDayIndex) ?: plan.days.firstOrNull()
        if (currentDay != null) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = currentDay.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "${currentDay.exercises.size} Exercises Planned",
                            fontSize = 12.sp,
                            color = NeonGreenBright
                        )
                    }
                    NeonOutlinedButton(
                        text = "START THIS DAY",
                        onClick = {
                            val activeItems = currentDay.exercises.map { ex ->
                                ActiveExerciseItem(
                                    name = ex.name,
                                    muscleGroup = ex.muscleGroup.displayName,
                                    targetReps = ex.targetReps,
                                    restSeconds = ex.restSeconds,
                                    sets = (1..ex.sets).map { s ->
                                        ActiveWorkoutSet(setNumber = s, weightKg = 40f, reps = 10)
                                    }.toMutableList()
                                )
                            }
                            onStartSpecificWorkout(currentDay.title, activeItems)
                        },
                        leadingIcon = {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = NeonGreenPrimary, modifier = Modifier.size(16.dp))
                        }
                    )
                }
            }

            items(currentDay.exercises) { exercise ->
                ExerciseCard(exercise = exercise)
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ExerciseCard(exercise: ExerciseDetail) {
    FitForgeCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = exercise.muscleGroup.displayName,
                        fontSize = 12.sp,
                        color = ElectricCyan,
                        fontWeight = FontWeight.Medium
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DarkSurfaceElevated)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${exercise.sets} Sets × ${exercise.targetReps}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = "Rest", tint = TextSecondary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${exercise.restSeconds}s rest", fontSize = 11.sp, color = TextSecondary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = "Gear", tint = TextSecondary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(exercise.equipment.displayName.substringBefore(" "), fontSize = 11.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Instructions
            Text(
                text = exercise.instructions,
                fontSize = 12.sp,
                color = TextWhite.copy(alpha = 0.85f),
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(6.dp))
            // Progressive overload tip banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(NeonGreenDark.copy(alpha = 0.15f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Overload",
                    tint = NeonGreenBright,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Overload: ${exercise.progressiveOverloadTip}",
                    fontSize = 11.sp,
                    color = NeonGreenBright,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) NeonGreenPrimary else DarkSurfaceElevated)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) PureBlack else TextWhite
        )
    }
}
