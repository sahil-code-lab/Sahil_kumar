package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.components.StreakBadge
import com.example.ui.screens.ActiveWorkoutScreen
import com.example.ui.screens.AiCoachScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.NutritionScreen
import com.example.ui.screens.ProfileCalculatorScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.WorkoutPlannerScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.FitForgeTheme
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.NeonGreenDark
import com.example.ui.theme.NeonGreenPrimary
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitForgeTheme {
                FitForgeApp(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitForgeApp(viewModel: MainViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isWorkoutActive by viewModel.isWorkoutActive.collectAsState()
    val profile by viewModel.userProfile.collectAsState()
    val elapsed by viewModel.elapsedSeconds.collectAsState()

    // Handle back button when inside secondary tab or active workout
    BackHandler(enabled = currentTab != 0) {
        viewModel.setTab(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = DarkBackground,
        topBar = {
            if (currentTab != 6) { // Don't show top bar during active workout
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = DarkBackground,
                        titleContentColor = TextWhite
                    ),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(NeonGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = "FitForge AI Logo",
                                    tint = PureBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "FITFORGE",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp,
                                        letterSpacing = 1.sp,
                                        color = TextWhite
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "AI",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp,
                                        color = NeonGreenPrimary
                                    )
                                }
                                Text(
                                    text = "Muscle & Fitness Coach",
                                    fontSize = 10.sp,
                                    color = NeonGreenBright
                                )
                            }
                        }
                    },
                    actions = {
                        StreakBadge(streakCount = profile?.workoutStreak ?: 6)
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                )
            }
        },
        bottomBar = {
            if (currentTab != 6) {
                Column {
                    // Floating banner if a workout is running in background
                    AnimatedVisibility(
                        visible = isWorkoutActive,
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it })
                    ) {
                        val min = elapsed / 60
                        val sec = elapsed % 60
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NeonGreenDark)
                                .clickable { viewModel.setTab(6) }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(NeonGreenBright)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Workout in progress (${String.format("%02d:%02d", min, sec)})",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                            Text(
                                text = "RESUME →",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = TextWhite
                            )
                        }
                    }

                    NavigationBar(
                        containerColor = DarkSurface,
                        contentColor = TextWhite,
                        windowInsets = NavigationBarDefaults.windowInsets
                    ) {
                        val navItems = listOf(
                            Triple(0, "Home", Icons.Default.Dashboard),
                            Triple(1, "Workout", Icons.Default.FitnessCenter),
                            Triple(2, "Nutrition", Icons.Default.Restaurant),
                            Triple(3, "Progress", Icons.Default.TrendingUp),
                            Triple(4, "AI Coach", Icons.Default.AutoAwesome),
                            Triple(5, "Profile", Icons.Default.Person)
                        )

                        navItems.forEach { (index, label, icon) ->
                            val isSelected = currentTab == index
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.setTab(index) },
                                icon = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PureBlack,
                                    selectedTextColor = NeonGreenBright,
                                    indicatorColor = NeonGreenPrimary,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted
                                ),
                                modifier = Modifier.testTag("nav_item_$index")
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> DashboardScreen(
                    viewModel = viewModel,
                    onStartWorkout = {
                        viewModel.startWorkoutSession("Push Day A (Chest & Delts)")
                    },
                    onNavigateTab = { index -> viewModel.setTab(index) }
                )
                1 -> WorkoutPlannerScreen(
                    viewModel = viewModel,
                    onStartSpecificWorkout = { title, exercises ->
                        viewModel.startWorkoutSession(title, exercises)
                    }
                )
                2 -> NutritionScreen(viewModel = viewModel)
                3 -> ProgressScreen(viewModel = viewModel)
                4 -> AiCoachScreen(viewModel = viewModel)
                5 -> ProfileCalculatorScreen(viewModel = viewModel)
                6 -> ActiveWorkoutScreen(
                    viewModel = viewModel,
                    onFinish = { viewModel.setTab(0) }
                )
            }
        }
    }
}
