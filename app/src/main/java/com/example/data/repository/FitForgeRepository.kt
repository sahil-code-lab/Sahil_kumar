package com.example.data.repository

import com.example.data.local.AchievementEntity
import com.example.data.local.BodyMetricEntity
import com.example.data.local.FitForgeDao
import com.example.data.local.NutritionLogEntity
import com.example.data.local.PersonalRecordEntity
import com.example.data.local.UserProfileEntity
import com.example.data.local.WorkoutSessionEntity
import com.example.data.model.DailyNutritionSummary
import com.example.data.model.FitnessGoal
import com.example.data.model.MacroTarget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.Calendar

class FitForgeRepository(private val dao: FitForgeDao) {

    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()
    val allWorkoutSessions: Flow<List<WorkoutSessionEntity>> = dao.getAllWorkoutSessions()
    val allBodyMetrics: Flow<List<BodyMetricEntity>> = dao.getAllBodyMetrics()
    val allPersonalRecords: Flow<List<PersonalRecordEntity>> = dao.getAllPersonalRecords()
    val allAchievements: Flow<List<AchievementEntity>> = dao.getAllAchievements()

    private fun getStartOfDay(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    val todayNutritionLogs: Flow<List<NutritionLogEntity>> = dao.getTodayNutritionLogs(getStartOfDay())

    val dailyNutritionSummary: Flow<DailyNutritionSummary> = combine(
        todayNutritionLogs,
        userProfile
    ) { logs, profile ->
        val cal = logs.sumOf { it.calories }
        val pro = logs.sumOf { it.protein.toDouble() }.toFloat()
        val carbs = logs.sumOf { it.carbs.toDouble() }.toFloat()
        val fats = logs.sumOf { it.fats.toDouble() }.toFloat()

        DailyNutritionSummary(
            totalCalories = cal,
            totalProtein = pro,
            totalCarbs = carbs,
            totalFats = fats,
            targetCalories = profile?.dailyCalorieTarget ?: 2500,
            targetProtein = profile?.dailyProteinTarget ?: 160,
            targetCarbs = profile?.dailyCarbsTarget ?: 280,
            targetFats = profile?.dailyFatsTarget ?: 70
        )
    }

    suspend fun updateProfile(profile: UserProfileEntity) {
        dao.insertOrUpdateProfile(profile)
    }

    suspend fun addNutritionLog(log: NutritionLogEntity) {
        dao.insertNutritionLog(log)
    }

    suspend fun removeNutritionLog(id: Long) {
        dao.deleteNutritionLog(id)
    }

    suspend fun recordWorkoutSession(session: WorkoutSessionEntity) {
        dao.insertWorkoutSession(session)
        // Update user profile streak and last workout date
        val currentProfile = userProfile.map { it }.let { /* get latest */ }
    }

    suspend fun addBodyMetric(metric: BodyMetricEntity) {
        dao.insertBodyMetric(metric)
    }

    suspend fun removeBodyMetric(id: Long) {
        dao.deleteBodyMetric(id)
    }

    suspend fun savePersonalRecord(pr: PersonalRecordEntity) {
        dao.insertOrUpdatePersonalRecord(pr)
    }

    suspend fun updateAchievementProgress(achievementId: String, addedProgress: Int) {
        // Will be called when workout or meal logged
    }

    companion object {
        /**
         * Calculates BMR using the Mifflin-St Jeor equation.
         * Default male formula: 10 * weight(kg) + 6.25 * height(cm) - 5 * age + 5
         */
        fun calculateMacros(
            weightKg: Float,
            heightCm: Float,
            age: Int,
            goal: FitnessGoal,
            workoutDays: Int
        ): MacroTarget {
            val bmr = (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5
            val activityMultiplier = when {
                workoutDays <= 2 -> 1.35f
                workoutDays <= 4 -> 1.55f
                workoutDays <= 5 -> 1.65f
                else -> 1.75f
            }
            val tdee = (bmr * activityMultiplier).toInt()

            val targetCalories = when (goal) {
                FitnessGoal.MUSCLE_GAIN -> tdee + 350
                FitnessGoal.STRENGTH -> tdee + 200
                FitnessGoal.FAT_LOSS -> (tdee - 450).coerceAtLeast(1500)
                FitnessGoal.GENERAL_FITNESS -> tdee
            }

            // Protein: 2.0g per kg for muscle gain/fat loss, 1.8g for strength, 1.6g for general
            val proteinMultiplier = when (goal) {
                FitnessGoal.MUSCLE_GAIN -> 2.2f
                FitnessGoal.FAT_LOSS -> 2.2f
                FitnessGoal.STRENGTH -> 2.0f
                FitnessGoal.GENERAL_FITNESS -> 1.7f
            }
            val proteinGrams = (weightKg * proteinMultiplier).toInt().coerceIn(100, 240)

            // Fats: ~25% of total calories (9 kcal/g)
            val fatsCalories = (targetCalories * 0.25f).toInt()
            val fatsGrams = (fatsCalories / 9).coerceIn(45, 100)

            // Carbs: Remaining calories (4 kcal/g)
            val remainingCalories = (targetCalories - (proteinGrams * 4) - (fatsGrams * 9)).coerceAtLeast(400)
            val carbsGrams = remainingCalories / 4

            return MacroTarget(
                calories = targetCalories,
                proteinGrams = proteinGrams,
                carbsGrams = carbsGrams,
                fatsGrams = fatsGrams
            )
        }
    }
}
