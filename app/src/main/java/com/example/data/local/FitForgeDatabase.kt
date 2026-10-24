package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfileEntity::class,
        NutritionLogEntity::class,
        WorkoutSessionEntity::class,
        BodyMetricEntity::class,
        PersonalRecordEntity::class,
        AchievementEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitForgeDatabase : RoomDatabase() {
    abstract fun fitForgeDao(): FitForgeDao

    companion object {
        @Volatile
        private var INSTANCE: FitForgeDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): FitForgeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitForgeDatabase::class.java,
                    "fitforge_database"
                )
                    .addCallback(FitForgeDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class FitForgeDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.fitForgeDao())
                }
            }
        }

        private suspend fun populateInitialData(dao: FitForgeDao) {
            // Initial User Profile
            val profile = UserProfileEntity(
                id = 1,
                name = "Sahil",
                age = 24,
                heightCm = 178f,
                weightKg = 74.5f,
                targetWeightKg = 80.0f,
                experienceLevel = "INTERMEDIATE",
                fitnessGoal = "MUSCLE_GAIN",
                dietaryPreference = "HIGH_PROTEIN",
                availableEquipment = "FULL_GYM",
                workoutDaysPerWeek = 5,
                dailyCalorieTarget = 2650,
                dailyProteinTarget = 165,
                dailyCarbsTarget = 310,
                dailyFatsTarget = 75,
                workoutStreak = 6,
                lastWorkoutDateEpoch = System.currentTimeMillis()
            )
            dao.insertOrUpdateProfile(profile)

            // Initial Achievements
            val achievements = listOf(
                AchievementEntity("ach_1", "Iron Pioneer", "Complete your first workout session in FitForge", true, 1, 1, System.currentTimeMillis() - 86400000L * 14),
                AchievementEntity("ach_2", "Protein Savage", "Hit your daily protein target 5 days in a row", true, 5, 5, System.currentTimeMillis() - 86400000L * 2),
                AchievementEntity("ach_3", "Streak Beast", "Maintain a 7-day workout streak", false, 6, 7, null),
                AchievementEntity("ach_4", "Century Lifter", "Log 10,000 kg total cumulative workout volume", true, 10000, 10000, System.currentTimeMillis() - 86400000L * 4),
                AchievementEntity("ach_5", "Indian Power", "Log a high-protein Indian meal (Paneer/Dal/Chicken)", true, 1, 1, System.currentTimeMillis() - 86400000L * 1),
                AchievementEntity("ach_6", "Overload Master", "Achieve a new Personal Record on compound lifts", true, 1, 1, System.currentTimeMillis() - 86400000L * 3),
                AchievementEntity("ach_7", "Consistency King", "Log 20 completed workouts", false, 14, 20, null)
            )
            dao.insertInitialAchievements(achievements)

            // Initial Personal Records
            dao.insertOrUpdatePersonalRecord(PersonalRecordEntity("Barbell Bench Press", 95f, 5, System.currentTimeMillis() - 86400000L * 3))
            dao.insertOrUpdatePersonalRecord(PersonalRecordEntity("Barbell Back Squat", 125f, 6, System.currentTimeMillis() - 86400000L * 5))
            dao.insertOrUpdatePersonalRecord(PersonalRecordEntity("Conventional Deadlift", 160f, 3, System.currentTimeMillis() - 86400000L * 8))
            dao.insertOrUpdatePersonalRecord(PersonalRecordEntity("Overhead Shoulder Press", 62.5f, 5, System.currentTimeMillis() - 86400000L * 2))
            dao.insertOrUpdatePersonalRecord(PersonalRecordEntity("Weighted Pull-Up", 25f, 6, System.currentTimeMillis() - 86400000L * 4))

            // Initial Body Metric logs for progress charts (last 6 weeks)
            val now = System.currentTimeMillis()
            val dayMs = 86400000L
            dao.insertBodyMetric(BodyMetricEntity(weightKg = 72.0f, chestCm = 98f, waistCm = 82f, armsCm = 35.5f, thighsCm = 56f, note = "Starting bulk phase", timestamp = now - dayMs * 35))
            dao.insertBodyMetric(BodyMetricEntity(weightKg = 72.8f, chestCm = 99f, waistCm = 82.2f, armsCm = 36.0f, thighsCm = 56.5f, note = "Consistent caloric surplus", timestamp = now - dayMs * 28))
            dao.insertBodyMetric(BodyMetricEntity(weightKg = 73.4f, chestCm = 100f, waistCm = 82.5f, armsCm = 36.5f, thighsCm = 57f, note = "Strength increasing nicely", timestamp = now - dayMs * 21))
            dao.insertBodyMetric(BodyMetricEntity(weightKg = 73.9f, chestCm = 100.5f, waistCm = 82.8f, armsCm = 36.8f, thighsCm = 57.5f, note = "PPL split going strong", timestamp = now - dayMs * 14))
            dao.insertBodyMetric(BodyMetricEntity(weightKg = 74.2f, chestCm = 101.5f, waistCm = 83f, armsCm = 37.2f, thighsCm = 58f, note = "Arms and chest noticeably fuller", timestamp = now - dayMs * 7))
            dao.insertBodyMetric(BodyMetricEntity(weightKg = 74.5f, chestCm = 102f, waistCm = 83.2f, armsCm = 37.5f, thighsCm = 58.5f, note = "Current morning weigh-in", timestamp = now))

            // Initial Nutrition logs for today
            dao.insertNutritionLog(NutritionLogEntity(foodName = "Oats with Milk & Banana", servingSize = "1 large bowl (300g)", calories = 340, protein = 14f, carbs = 58f, fats = 6.5f, mealType = "Breakfast", timestamp = now - 3600000L * 4))
            dao.insertNutritionLog(NutritionLogEntity(foodName = "Boiled Whole Egg", servingSize = "2 large eggs (100g)", calories = 148, protein = 12.6f, carbs = 0.8f, fats = 10f, mealType = "Breakfast", timestamp = now - 3600000L * 4))
            dao.insertNutritionLog(NutritionLogEntity(foodName = "Home Style Chicken Curry", servingSize = "1 bowl (200g)", calories = 240, protein = 24f, carbs = 8f, fats = 12f, mealType = "Lunch", timestamp = now - 3600000L * 2))
            dao.insertNutritionLog(NutritionLogEntity(foodName = "Roti / Chapati", servingSize = "2 medium (70g)", calories = 170, protein = 6.4f, carbs = 35f, fats = 0.8f, mealType = "Lunch", timestamp = now - 3600000L * 2))
            dao.insertNutritionLog(NutritionLogEntity(foodName = "Curd / Plain Homemade Dahi", servingSize = "1 bowl (150g)", calories = 95, protein = 5.2f, carbs = 6.5f, fats = 5f, mealType = "Lunch", timestamp = now - 3600000L * 2))

            // Recent workout sessions
            dao.insertWorkoutSession(WorkoutSessionEntity(workoutTitle = "Push Hypertrophy (Chest/Delts/Triceps)", durationMinutes = 55, totalVolumeKg = 7850f, setsCompleted = 18, notes = "Benched 95kg x 5 reps! Great pump.", timestamp = now - dayMs * 1))
            dao.insertWorkoutSession(WorkoutSessionEntity(workoutTitle = "Pull Power (Back & Biceps)", durationMinutes = 60, totalVolumeKg = 8420f, setsCompleted = 19, notes = "Heavy weighted pull-ups felt solid.", timestamp = now - dayMs * 2))
            dao.insertWorkoutSession(WorkoutSessionEntity(workoutTitle = "Legs & Abs Devastation", durationMinutes = 58, totalVolumeKg = 9100f, setsCompleted = 20, notes = "Squat depth on point. High quad burn.", timestamp = now - dayMs * 4))
        }
    }
}
