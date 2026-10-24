package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FitnessGoal
import com.example.ui.MainViewModel
import com.example.ui.components.FitForgeCard
import com.example.ui.components.InteractiveWeightChart
import com.example.ui.components.MacroProgressBar
import com.example.ui.components.NeonPrimaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.StreakBadge
import com.example.ui.theme.CarbYellow
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FatPurple
import com.example.ui.theme.HighProteinOrange
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.NeonGreenPrimary
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onStartWorkout: () -> Unit,
    onNavigateTab: (Int) -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()
    val nutrition by viewModel.nutritionSummary.collectAsState()
    val bodyMetrics by viewModel.allBodyMetrics.collectAsState()
    val workouts by viewModel.allWorkoutSessions.collectAsState()

    val userName = profile?.name ?: "Athlete"
    val streak = profile?.workoutStreak ?: 6
    val currentWeight = profile?.weightKg ?: 74.5f
    val targetWeight = profile?.targetWeightKg ?: 80.0f
    val goal = try {
        FitnessGoal.valueOf(profile?.fitnessGoal ?: "MUSCLE_GAIN").displayName
    } catch (e: Exception) {
        "Muscle Gain"
    }

    val chartPoints = bodyMetrics.takeLast(6).mapIndexed { idx, item ->
        val label = "W${idx + 1}"
        Pair(label, item.weightKg)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // User Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WELCOME BACK,",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = NeonGreenPrimary
                    )
                    Text(
                        text = userName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhite
                    )
                    Text(
                        text = "Goal: $goal",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
                StreakBadge(streakCount = streak)
            }
        }

        // Today's Scheduled Workout Card
        item {
            FitForgeCard(
                borderColor = NeonGreenPrimary,
                borderWidth = 1.5.dp,
                backgroundColor = DarkSurfaceElevated,
                modifier = Modifier.testTag("today_workout_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(NeonGreenPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = "Workout",
                                    tint = PureBlack,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "TODAY'S FORGE ROUTINE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonGreenPrimary,
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "Push Hypertrophy (Chest & Delts)",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "5 Exercises • 18 Sets • Focus on Barbell Bench Press, Incline DB Press, and Lateral Raises.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    NeonPrimaryButton(
                        text = "START TODAY'S WORKOUT",
                        onClick = onStartWorkout,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "start_workout_button",
                        leadingIcon = {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = PureBlack)
                        }
                    )
                }
            }
        }

        // Quick Actions Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionButton(
                    icon = Icons.Default.Restaurant,
                    label = "Log Meal",
                    color = HighProteinOrange,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTab(2) }
                )
                QuickActionButton(
                    icon = Icons.Default.TrendingUp,
                    label = "Log Weight",
                    color = ElectricCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTab(3) }
                )
                QuickActionButton(
                    icon = Icons.Default.AutoAwesome,
                    label = "AI Planner",
                    color = NeonGreenPrimary,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTab(1) }
                )
                QuickActionButton(
                    icon = Icons.Default.Speed,
                    label = "AI Coach",
                    color = FatPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTab(4) }
                )
            }
        }

        // Daily Nutrition & Macro Progress
        item {
            FitForgeCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeader(
                        title = "Today's Nutrition",
                        subtitle = "Tracked towards your muscle building goals",
                        actionText = "Log Foods",
                        onActionClick = { onNavigateTab(2) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${nutrition.totalCalories}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                            Text(
                                text = "of ${nutrition.targetCalories} kcal goal",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                        val remaining = (nutrition.targetCalories - nutrition.totalCalories).coerceAtLeast(0)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurfaceElevated)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "$remaining kcal left",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonGreenBright
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    MacroProgressBar(
                        label = "Protein (Target 2.2g/kg)",
                        current = nutrition.totalProtein,
                        target = nutrition.targetProtein.toFloat(),
                        unit = "g",
                        barColor = HighProteinOrange
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    MacroProgressBar(
                        label = "Carbohydrates (Muscle Glycogen)",
                        current = nutrition.totalCarbs,
                        target = nutrition.targetCarbs.toFloat(),
                        unit = "g",
                        barColor = CarbYellow
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    MacroProgressBar(
                        label = "Fats (Hormone Health)",
                        current = nutrition.totalFats,
                        target = nutrition.targetFats.toFloat(),
                        unit = "g",
                        barColor = FatPurple
                    )
                }
            }
        }

        // Weight Progress & Goals Card
        item {
            FitForgeCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeader(
                        title = "Body Weight Trend",
                        subtitle = "Current: ${currentWeight}kg • Target: ${targetWeight}kg",
                        actionText = "View All",
                        onActionClick = { onNavigateTab(3) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    InteractiveWeightChart(
                        dataPoints = chartPoints,
                        targetWeight = targetWeight
                    )
                }
            }
        }

        // Smart Recommendations Card
        item {
            FitForgeCard(
                backgroundColor = DarkSurfaceElevated,
                borderColor = DarkSurfaceBorder
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Tip",
                            tint = NeonGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SMART FORGE INSIGHT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreenPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Great job on your 6-day streak! Your bench press is up +2.5kg. Make sure to hit 40g protein with your post-workout meal (e.g. Soya Chunks curry or Whey + Milk) to maximize muscle protein synthesis window.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = TextWhite
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FitForgeCard(
        modifier = modifier,
        backgroundColor = DarkSurface,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
        }
    }
}
