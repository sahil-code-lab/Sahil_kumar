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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WbSunny
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FoodPresets
import com.example.data.local.NutritionLogEntity
import com.example.data.model.FoodItem
import com.example.data.model.MealType
import com.example.ui.MainViewModel
import com.example.ui.components.FitForgeCard
import com.example.ui.components.MacroProgressBar
import com.example.ui.components.NeonPrimaryButton
import com.example.ui.components.SectionHeader
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
fun NutritionScreen(viewModel: MainViewModel) {
    val nutrition by viewModel.nutritionSummary.collectAsState()
    val todayLogs by viewModel.todayNutritionLogs.collectAsState()

    var showAddFoodDialog by remember { mutableStateOf(false) }
    var selectedMealForAdd by remember { mutableStateOf("Breakfast") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                title = "Nutrition & Indian Foods Tracker",
                subtitle = "Fuel muscle hypertrophy and track exact macronutrients"
            )
        }

        // Calorie & Macro Target Progress Card
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
                                text = "DAILY CALORIC INTAKE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonGreenPrimary,
                                letterSpacing = 1.sp
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${nutrition.totalCalories}",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Text(
                                    text = " / ${nutrition.targetCalories} kcal",
                                    fontSize = 14.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                                )
                            }
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
                        label = "Protein",
                        current = nutrition.totalProtein,
                        target = nutrition.targetProtein.toFloat(),
                        unit = "g",
                        barColor = HighProteinOrange
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    MacroProgressBar(
                        label = "Carbohydrates",
                        current = nutrition.totalCarbs,
                        target = nutrition.targetCarbs.toFloat(),
                        unit = "g",
                        barColor = CarbYellow
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    MacroProgressBar(
                        label = "Fats",
                        current = nutrition.totalFats,
                        target = nutrition.targetFats.toFloat(),
                        unit = "g",
                        barColor = FatPurple
                    )
                }
            }
        }

        // Meal Categories
        listOf(
            Triple("Breakfast", Icons.Default.WbSunny, "Morning fueling"),
            Triple("Lunch", Icons.Default.LocalDining, "Midday energy"),
            Triple("Dinner", Icons.Default.Nightlife, "Evening recovery"),
            Triple("Snacks", Icons.Default.Restaurant, "Pre / Post workout")
        ).forEach { (mealName, icon, sub) ->
            item {
                MealSectionCard(
                    mealName = mealName,
                    icon = icon,
                    subtitle = sub,
                    logs = todayLogs.filter { it.mealType.equals(mealName, ignoreCase = true) },
                    onAddClick = {
                        selectedMealForAdd = mealName
                        showAddFoodDialog = true
                    },
                    onDeleteLog = { id -> viewModel.removeFoodLog(id) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showAddFoodDialog) {
        AddFoodModalDialog(
            initialMeal = selectedMealForAdd,
            onDismiss = { showAddFoodDialog = false },
            onAddPresetFood = { item, meal, mult ->
                viewModel.addFoodLog(item, meal, mult)
                showAddFoodDialog = false
            },
            onAddCustomFood = { name, cal, pro, carb, fat, meal ->
                viewModel.addCustomFoodLog(name, cal, pro, carb, fat, meal)
                showAddFoodDialog = false
            }
        )
    }
}

@Composable
private fun MealSectionCard(
    mealName: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    subtitle: String,
    logs: List<NutritionLogEntity>,
    onAddClick: () -> Unit,
    onDeleteLog: (Long) -> Unit
) {
    val totalMealCalories = logs.sumOf { it.calories }
    val totalMealProtein = logs.sumOf { it.protein.toDouble() }.toFloat()

    FitForgeCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(DarkSurfaceElevated, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = NeonGreenPrimary, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = mealName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = if (logs.isNotEmpty()) "$totalMealCalories kcal • ${totalMealProtein.toInt()}g protein" else subtitle,
                            fontSize = 11.sp,
                            color = if (logs.isNotEmpty()) HighProteinOrange else TextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurfaceElevated)
                        .clickable { onAddClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = NeonGreenPrimary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Food", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonGreenPrimary)
                    }
                }
            }

            if (logs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                logs.forEach { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = log.foodName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextWhite
                            )
                            Text(
                                text = "${log.servingSize} • ${log.calories} kcal • P: ${log.protein}g C: ${log.carbs}g F: ${log.fats}g",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        IconButton(
                            onClick = { onDeleteLog(log.id) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddFoodModalDialog(
    initialMeal: String,
    onDismiss: () -> Unit,
    onAddPresetFood: (FoodItem, String, Float) -> Unit,
    onAddCustomFood: (String, Int, Float, Float, Float, String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Indian Foods") }
    var selectedMeal by remember { mutableStateOf(initialMeal) }
    var servingMultiplier by remember { mutableFloatStateOf(1.0f) }

    var isCustomFoodMode by remember { mutableStateOf(false) }
    var customName by remember { mutableStateOf("") }
    var customCalories by remember { mutableStateOf("") }
    var customProtein by remember { mutableStateOf("") }
    var customCarbs by remember { mutableStateOf("") }
    var customFats by remember { mutableStateOf("") }

    val filteredList = remember(searchQuery, selectedFilter) {
        FoodPresets.items.filter { item ->
            val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) ||
                    item.hindiName.contains(searchQuery, ignoreCase = true) ||
                    item.category.contains(searchQuery, ignoreCase = true)

            val matchesCategory = when (selectedFilter) {
                "Indian Foods" -> item.isIndian
                "High Protein" -> item.isHighProtein
                "Breakfast" -> item.category == "Breakfast" || item.category == "Dairy"
                else -> true
            }

            matchesSearch && matchesCategory
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isCustomFoodMode) "Add Custom Food" else "Log Food & Macros",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                TextButton(onClick = { isCustomFoodMode = !isCustomFoodMode }) {
                    Text(
                        text = if (isCustomFoodMode) "Browse Presets" else "+ Custom Food",
                        color = NeonGreenPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Meal Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Breakfast", "Lunch", "Dinner", "Snacks").forEach { meal ->
                        val isSelected = meal == selectedMeal
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) NeonGreenPrimary else DarkSurface)
                                .clickable { selectedMeal = meal }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = meal,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) PureBlack else TextWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (isCustomFoodMode) {
                    // Custom food form
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text("Food Name (e.g. Protein Smoothie)") },
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
                            value = customCalories,
                            onValueChange = { customCalories = it },
                            label = { Text("Calories (kcal)") },
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
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            val cal = customCalories.toIntOrNull() ?: 0
                            val pro = customProtein.toFloatOrNull() ?: 0f
                            val carb = customCarbs.toFloatOrNull() ?: 0f
                            val fat = customFats.toFloatOrNull() ?: 0f
                            if (customName.isNotBlank() && cal > 0) {
                                onAddCustomFood(customName, cal, pro, carb, fat, selectedMeal)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreenPrimary)
                    ) {
                        Text("SAVE CUSTOM FOOD", color = PureBlack, fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Presets Search and List
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search Indian & global foods...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreenPrimary,
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    // Category Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Indian Foods", "High Protein", "Breakfast", "All Foods").forEach { cat ->
                            val isSel = cat == selectedFilter
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) HighProteinOrange else DarkSurfaceElevated)
                                    .clickable { selectedFilter = cat }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) PureBlack else TextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    // Serving size multiplier
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Portion:", fontSize = 11.sp, color = TextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf(0.5f, 1.0f, 1.5f, 2.0f).forEach { mult ->
                                val isSelected = mult == servingMultiplier
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isSelected) NeonGreenPrimary else DarkSurfaceElevated)
                                        .clickable { servingMultiplier = mult }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${mult}x",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) PureBlack else TextWhite
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    // Food Items list
                    Box(modifier = Modifier.height(240.dp)) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(filteredList) { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(DarkSurfaceElevated)
                                        .clickable {
                                            onAddPresetFood(item, selectedMeal, servingMultiplier)
                                        }
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.name,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                        if (item.hindiName.isNotBlank()) {
                                            Text(
                                                text = item.hindiName,
                                                fontSize = 11.sp,
                                                color = NeonGreenBright
                                            )
                                        }
                                        Text(
                                            text = "${item.defaultServing} • ${(item.calories * servingMultiplier).toInt()} kcal • P: ${(item.protein * servingMultiplier)}g",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(NeonGreenPrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Add", tint = PureBlack, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CLOSE", color = TextSecondary)
            }
        },
        containerColor = DarkSurfaceElevated
    )
}
