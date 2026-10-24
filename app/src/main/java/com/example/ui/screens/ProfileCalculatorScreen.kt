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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.DietaryPreference
import com.example.data.model.EquipmentType
import com.example.data.model.ExperienceLevel
import com.example.data.model.FitnessGoal
import com.example.data.repository.FitForgeRepository
import com.example.ui.MainViewModel
import com.example.ui.components.FitForgeCard
import com.example.ui.components.NeonPrimaryButton
import com.example.ui.components.SectionHeader
import com.example.ui.theme.CarbYellow
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.FatPurple
import com.example.ui.theme.HighProteinOrange
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.NeonGreenDark
import com.example.ui.theme.NeonGreenPrimary
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite

@Composable
fun ProfileCalculatorScreen(viewModel: MainViewModel) {
    val profile by viewModel.userProfile.collectAsState()

    var name by remember(profile) { mutableStateOf(profile?.name ?: "Sahil") }
    var ageStr by remember(profile) { mutableStateOf("${profile?.age ?: 24}") }
    var heightStr by remember(profile) { mutableStateOf("${profile?.heightCm ?: 178f}") }
    var weightStr by remember(profile) { mutableStateOf("${profile?.weightKg ?: 74.5f}") }
    var targetWeightStr by remember(profile) { mutableStateOf("${profile?.targetWeightKg ?: 80.0f}") }

    var selectedGoal by remember(profile) {
        mutableStateOf(
            try { FitnessGoal.valueOf(profile?.fitnessGoal ?: "MUSCLE_GAIN") } catch (e: Exception) { FitnessGoal.MUSCLE_GAIN }
        )
    }
    var selectedLevel by remember(profile) {
        mutableStateOf(
            try { ExperienceLevel.valueOf(profile?.experienceLevel ?: "INTERMEDIATE") } catch (e: Exception) { ExperienceLevel.INTERMEDIATE }
        )
    }
    var selectedDiet by remember(profile) {
        mutableStateOf(
            try { DietaryPreference.valueOf(profile?.dietaryPreference ?: "HIGH_PROTEIN") } catch (e: Exception) { DietaryPreference.HIGH_PROTEIN }
        )
    }
    var selectedEquipment by remember(profile) {
        mutableStateOf(
            try { EquipmentType.valueOf(profile?.availableEquipment ?: "FULL_GYM") } catch (e: Exception) { EquipmentType.FULL_GYM }
        )
    }
    var workoutDays by remember(profile) { mutableIntStateOf(profile?.workoutDaysPerWeek ?: 5) }

    // Custom Target overrides
    var customCalories by remember(profile) { mutableStateOf("${profile?.dailyCalorieTarget ?: 2650}") }
    var customProtein by remember(profile) { mutableStateOf("${profile?.dailyProteinTarget ?: 165}") }
    var customCarbs by remember(profile) { mutableStateOf("${profile?.dailyCarbsTarget ?: 310}") }
    var customFats by remember(profile) { mutableStateOf("${profile?.dailyFatsTarget ?: 75}") }

    var saveSuccess by remember { mutableStateOf(false) }

    // Live calculated recommendation based on inputs
    val currentWeight = weightStr.toFloatOrNull() ?: 75f
    val currentHeight = heightStr.toFloatOrNull() ?: 178f
    val currentAge = ageStr.toIntOrNull() ?: 25
    val liveCalculatedMacros = remember(currentWeight, currentHeight, currentAge, selectedGoal, workoutDays) {
        FitForgeRepository.calculateMacros(currentWeight, currentHeight, currentAge, selectedGoal, workoutDays)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                title = "User Profile & Macro Engine",
                subtitle = "Scientific BMR & TDEE calculation with custom macronutrient targets"
            )
        }

        // Profile Details Card
        item {
            FitForgeCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ATHLETE PROFILE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreenPrimary,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Athlete Name") },
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
                            value = ageStr,
                            onValueChange = { ageStr = it },
                            label = { Text("Age") },
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
                            value = heightStr,
                            onValueChange = { heightStr = it },
                            label = { Text("Height (cm)") },
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
                            value = weightStr,
                            onValueChange = { weightStr = it },
                            label = { Text("Current Weight (kg)") },
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
                            value = targetWeightStr,
                            onValueChange = { targetWeightStr = it },
                            label = { Text("Target Weight (kg)") },
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

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Primary Goal", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FitnessGoal.values().forEach { g ->
                            val isSel = g == selectedGoal
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) NeonGreenPrimary else DarkSurfaceElevated)
                                    .clickable { selectedGoal = g }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = g.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) PureBlack else TextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Dietary Preference", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DietaryPreference.values().forEach { d ->
                            val isSel = d == selectedDiet
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) HighProteinOrange else DarkSurfaceElevated)
                                    .clickable { selectedDiet = d }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = d.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) PureBlack else TextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Equipment Access", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EquipmentType.values().forEach { eq ->
                            val isSel = eq == selectedEquipment
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) NeonGreenPrimary else DarkSurfaceElevated)
                                    .clickable { selectedEquipment = eq }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = eq.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) PureBlack else TextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Workout Frequency (Days per week)", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(3, 4, 5, 6).forEach { d ->
                            val isSel = d == workoutDays
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) NeonGreenPrimary else DarkSurfaceElevated)
                                    .clickable { workoutDays = d }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$d Days",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) PureBlack else TextWhite
                                )
                            }
                        }
                    }
                }
            }
        }

        // TDEE & Macro Calculator Card
        item {
            FitForgeCard(
                borderColor = NeonGreenPrimary,
                borderWidth = 1.dp,
                backgroundColor = DarkSurfaceElevated
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ESTIMATED TARGETS (MIFFLIN-ST JEOR)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreenPrimary,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonGreenDark)
                                .clickable {
                                    customCalories = "${liveCalculatedMacros.calories}"
                                    customProtein = "${liveCalculatedMacros.proteinGrams}"
                                    customCarbs = "${liveCalculatedMacros.carbsGrams}"
                                    customFats = "${liveCalculatedMacros.fatsGrams}"
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Use Computed",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MacroDisplayBox("Calories", "${liveCalculatedMacros.calories} kcal", NeonGreenBright, Modifier.weight(1f))
                        MacroDisplayBox("Protein", "${liveCalculatedMacros.proteinGrams} g", HighProteinOrange, Modifier.weight(1f))
                        MacroDisplayBox("Carbs", "${liveCalculatedMacros.carbsGrams} g", CarbYellow, Modifier.weight(1f))
                        MacroDisplayBox("Fats", "${liveCalculatedMacros.fatsGrams} g", FatPurple, Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Customize Your Active Daily Targets",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = customCalories,
                            onValueChange = { customCalories = it },
                            label = { Text("Calories") },
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
                            value = customProtein,
                            onValueChange = { customProtein = it },
                            label = { Text("Protein (g)") },
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
                            value = customCarbs,
                            onValueChange = { customCarbs = it },
                            label = { Text("Carbs (g)") },
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
                            value = customFats,
                            onValueChange = { customFats = it },
                            label = { Text("Fats (g)") },
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

                    Spacer(modifier = Modifier.height(18.dp))
                    NeonPrimaryButton(
                        text = if (saveSuccess) "PROFILE SAVED!" else "SAVE ATHLETE TARGETS",
                        onClick = {
                            viewModel.updateProfileAndCalculateTargets(
                                name = name,
                                age = ageStr.toIntOrNull() ?: 24,
                                heightCm = heightStr.toFloatOrNull() ?: 178f,
                                weightKg = weightStr.toFloatOrNull() ?: 74.5f,
                                targetWeightKg = targetWeightStr.toFloatOrNull() ?: 80f,
                                goal = selectedGoal,
                                exp = selectedLevel,
                                diet = selectedDiet,
                                equipment = selectedEquipment,
                                workoutDays = workoutDays,
                                customCalories = customCalories.toIntOrNull(),
                                customProtein = customProtein.toIntOrNull(),
                                customCarbs = customCarbs.toIntOrNull(),
                                customFats = customFats.toIntOrNull()
                            )
                            saveSuccess = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "save_profile_button",
                        leadingIcon = {
                            Icon(Icons.Default.Check, contentDescription = null, tint = PureBlack)
                        }
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
private fun MacroDisplayBox(label: String, value: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 10.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
