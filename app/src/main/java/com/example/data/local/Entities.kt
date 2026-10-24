package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Fit Athlete",
    val age: Int = 26,
    val heightCm: Float = 175f,
    val weightKg: Float = 76.5f,
    val targetWeightKg: Float = 80.0f,
    val experienceLevel: String = "INTERMEDIATE",
    val fitnessGoal: String = "MUSCLE_GAIN",
    val dietaryPreference: String = "HIGH_PROTEIN",
    val availableEquipment: String = "FULL_GYM",
    val workoutDaysPerWeek: Int = 5,
    val dailyCalorieTarget: Int = 2450,
    val dailyProteinTarget: Int = 160,
    val dailyCarbsTarget: Int = 280,
    val dailyFatsTarget: Int = 70,
    val workoutStreak: Int = 5,
    val lastWorkoutDateEpoch: Long = System.currentTimeMillis()
)

@Entity(tableName = "nutrition_logs")
data class NutritionLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodName: String,
    val servingSize: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fats: Float,
    val mealType: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutTitle: String,
    val durationMinutes: Int,
    val totalVolumeKg: Float,
    val setsCompleted: Int,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "body_metrics")
data class BodyMetricEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weightKg: Float,
    val chestCm: Float? = null,
    val waistCm: Float? = null,
    val armsCm: Float? = null,
    val thighsCm: Float? = null,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "personal_records")
data class PersonalRecordEntity(
    @PrimaryKey val exerciseName: String,
    val weightKg: Float,
    val reps: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean = false,
    val progress: Int = 0,
    val maxProgress: Int = 100,
    val unlockedDate: Long? = null
)
