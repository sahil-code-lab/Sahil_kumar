package com.example.data.model

enum class FitnessGoal(val displayName: String, val description: String) {
    MUSCLE_GAIN("Muscle Gain", "Hypertrophy-focused volume with caloric surplus"),
    STRENGTH("Pure Strength", "Heavy compound lifts & neurological adaptation"),
    FAT_LOSS("Fat Loss & Shred", "High intensity metabolic deficit training"),
    GENERAL_FITNESS("General Fitness & Health", "Balanced cardiovascular & muscular conditioning")
}

enum class ExperienceLevel(val displayName: String) {
    BEGINNER("Beginner (0-1 yr)"),
    INTERMEDIATE("Intermediate (1-3 yrs)"),
    ADVANCED("Advanced (3+ yrs)")
}

enum class DietaryPreference(val displayName: String) {
    STANDARD("Standard / Non-Veg"),
    VEGETARIAN("Vegetarian (Lacto/Ovo)"),
    VEGAN("100% Plant-Based Vegan"),
    HIGH_PROTEIN("High-Protein Focused"),
    KETO("Ketogenic / Low-Carb"),
    JAIN("Jain Vegetarian")
}

enum class EquipmentType(val displayName: String) {
    FULL_GYM("Full Commercial Gym"),
    DUMBBELLS_ONLY("Dumbbells & Bench"),
    HOME_BODYWEIGHT("Bodyweight / Calisthenics"),
    RESISTANCE_BANDS("Resistance Bands & Home")
}

enum class MealType(val displayName: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACKS("Snacks & Pre/Post")
}

enum class MuscleGroup(val displayName: String) {
    CHEST("Chest"),
    BACK("Back"),
    LEGS("Legs & Glutes"),
    SHOULDERS("Shoulders"),
    ARMS("Biceps & Triceps"),
    CORE("Abs & Core"),
    FULL_BODY("Full Body")
}

data class ExerciseDetail(
    val id: String,
    val name: String,
    val muscleGroup: MuscleGroup,
    val sets: Int,
    val targetReps: String,
    val restSeconds: Int,
    val instructions: String,
    val progressiveOverloadTip: String,
    val equipment: EquipmentType = EquipmentType.FULL_GYM
)

data class RoutineDay(
    val dayNumber: Int,
    val title: String,
    val focus: String,
    val exercises: List<ExerciseDetail>
)

data class RoutinePlan(
    val id: String,
    val title: String,
    val goal: FitnessGoal,
    val daysPerWeek: Int,
    val level: ExperienceLevel,
    val equipment: EquipmentType,
    val description: String,
    val days: List<RoutineDay>
)

data class FoodItem(
    val id: String,
    val name: String,
    val hindiName: String = "",
    val defaultServing: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fats: Float,
    val isIndian: Boolean = false,
    val isHighProtein: Boolean = false,
    val category: String = "General"
)

data class MacroTarget(
    val calories: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatsGrams: Int
)

data class DailyNutritionSummary(
    val totalCalories: Int = 0,
    val totalProtein: Float = 0f,
    val totalCarbs: Float = 0f,
    val totalFats: Float = 0f,
    val targetCalories: Int = 2200,
    val targetProtein: Int = 140,
    val targetCarbs: Int = 240,
    val targetFats: Int = 65
)

data class StreakAndStats(
    val currentStreak: Int = 5,
    val totalWorkoutsCompleted: Int = 24,
    val totalVolumeLiftedKg: Long = 48500L,
    val weeklyConsistencyRate: Int = 85
)
