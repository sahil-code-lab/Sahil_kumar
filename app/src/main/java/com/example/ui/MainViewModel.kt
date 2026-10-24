package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.FitForgeAiEngine
import com.example.data.local.BodyMetricEntity
import com.example.data.local.FitForgeDatabase
import com.example.data.local.FoodPresets
import com.example.data.local.NutritionLogEntity
import com.example.data.local.PersonalRecordEntity
import com.example.data.local.UserProfileEntity
import com.example.data.local.WorkoutSessionEntity
import com.example.data.model.DailyNutritionSummary
import com.example.data.model.DietaryPreference
import com.example.data.model.EquipmentType
import com.example.data.model.ExperienceLevel
import com.example.data.model.FitnessGoal
import com.example.data.model.FoodItem
import com.example.data.model.RoutinePlan
import com.example.data.repository.FitForgeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ActiveWorkoutSet(
    val setNumber: Int,
    var weightKg: Float,
    var reps: Int,
    var isCompleted: Boolean = false
)

data class ActiveExerciseItem(
    val name: String,
    val muscleGroup: String,
    val targetReps: String,
    val restSeconds: Int,
    val sets: MutableList<ActiveWorkoutSet>
)

data class ChatMessageItem(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FitForgeDatabase.getDatabase(application, viewModelScope)
    val repository = FitForgeRepository(database.fitForgeDao())

    val userProfile = repository.userProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val nutritionSummary = repository.dailyNutritionSummary.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DailyNutritionSummary()
    )

    val todayNutritionLogs = repository.todayNutritionLogs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allWorkoutSessions = repository.allWorkoutSessions.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allBodyMetrics = repository.allBodyMetrics.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allPersonalRecords = repository.allPersonalRecords.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allAchievements = repository.allAchievements.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Current navigation tab
    // 0: Dashboard, 1: Workout Planner, 2: Nutrition Tracker, 3: Progress & PRs, 4: AI Coach, 5: Profile/Calc, 6: Active Workout Session
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    fun setTab(index: Int) {
        _currentTab.value = index
    }

    // AI Workout Planner State
    private val _selectedGoal = MutableStateFlow(FitnessGoal.MUSCLE_GAIN)
    val selectedGoal: StateFlow<FitnessGoal> = _selectedGoal.asStateFlow()

    private val _selectedLevel = MutableStateFlow(ExperienceLevel.INTERMEDIATE)
    val selectedLevel: StateFlow<ExperienceLevel> = _selectedLevel.asStateFlow()

    private val _selectedEquipment = MutableStateFlow(EquipmentType.FULL_GYM)
    val selectedEquipment: StateFlow<EquipmentType> = _selectedEquipment.asStateFlow()

    private val _selectedDays = MutableStateFlow(5)
    val selectedDays: StateFlow<Int> = _selectedDays.asStateFlow()

    private val _generatedPlan = MutableStateFlow<RoutinePlan>(
        FitForgeAiEngine.generateRoutine(FitnessGoal.MUSCLE_GAIN, ExperienceLevel.INTERMEDIATE, EquipmentType.FULL_GYM, 5)
    )
    val generatedPlan: StateFlow<RoutinePlan> = _generatedPlan.asStateFlow()

    private val _isGeneratingPlan = MutableStateFlow(false)
    val isGeneratingPlan: StateFlow<Boolean> = _isGeneratingPlan.asStateFlow()

    fun setPlannerPreferences(goal: FitnessGoal, level: ExperienceLevel, eq: EquipmentType, days: Int) {
        _selectedGoal.value = goal
        _selectedLevel.value = level
        _selectedEquipment.value = eq
        _selectedDays.value = days
    }

    fun generateNewPlan() {
        viewModelScope.launch {
            _isGeneratingPlan.value = true
            delay(500)
            _generatedPlan.value = FitForgeAiEngine.generateRoutine(
                _selectedGoal.value,
                _selectedLevel.value,
                _selectedEquipment.value,
                _selectedDays.value
            )
            _isGeneratingPlan.value = false
        }
    }

    // Active Workout Tracker State
    private val _isWorkoutActive = MutableStateFlow(false)
    val isWorkoutActive: StateFlow<Boolean> = _isWorkoutActive.asStateFlow()

    private val _activeWorkoutTitle = MutableStateFlow("Push Day A (Hypertrophy)")
    val activeWorkoutTitle: StateFlow<String> = _activeWorkoutTitle.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0)
    val elapsedSeconds: StateFlow<Int> = _elapsedSeconds.asStateFlow()

    private val _activeExercises = MutableStateFlow<List<ActiveExerciseItem>>(emptyList())
    val activeExercises: StateFlow<List<ActiveExerciseItem>> = _activeExercises.asStateFlow()

    private val _restSecondsRemaining = MutableStateFlow(0)
    val restSecondsRemaining: StateFlow<Int> = _restSecondsRemaining.asStateFlow()

    private var workoutTimerJob: Job? = null
    private var restTimerJob: Job? = null

    fun startWorkoutSession(title: String, exercisesList: List<ActiveExerciseItem>? = null) {
        _activeWorkoutTitle.value = title
        _elapsedSeconds.value = 0
        _isWorkoutActive.value = true
        _currentTab.value = 6 // Go to active workout screen

        val initialExercises = exercisesList ?: listOf(
            ActiveExerciseItem(
                name = "Barbell Bench Press",
                muscleGroup = "Chest",
                targetReps = "8-10",
                restSeconds = 90,
                sets = mutableListOf(
                    ActiveWorkoutSet(1, 80f, 10),
                    ActiveWorkoutSet(2, 85f, 8),
                    ActiveWorkoutSet(3, 85f, 8),
                    ActiveWorkoutSet(4, 90f, 6)
                )
            ),
            ActiveExerciseItem(
                name = "Incline Dumbbell Press",
                muscleGroup = "Chest",
                targetReps = "10-12",
                restSeconds = 75,
                sets = mutableListOf(
                    ActiveWorkoutSet(1, 28f, 12),
                    ActiveWorkoutSet(2, 30f, 10),
                    ActiveWorkoutSet(3, 30f, 9)
                )
            ),
            ActiveExerciseItem(
                name = "Standing Overhead Military Press",
                muscleGroup = "Shoulders",
                targetReps = "8-10",
                restSeconds = 90,
                sets = mutableListOf(
                    ActiveWorkoutSet(1, 50f, 10),
                    ActiveWorkoutSet(2, 55f, 8),
                    ActiveWorkoutSet(3, 55f, 7)
                )
            ),
            ActiveExerciseItem(
                name = "Cable Lateral Raises",
                muscleGroup = "Shoulders",
                targetReps = "12-15",
                restSeconds = 60,
                sets = mutableListOf(
                    ActiveWorkoutSet(1, 10f, 15),
                    ActiveWorkoutSet(2, 12.5f, 12),
                    ActiveWorkoutSet(3, 12.5f, 12)
                )
            ),
            ActiveExerciseItem(
                name = "Cable Triceps Pushdown",
                muscleGroup = "Arms",
                targetReps = "12-15",
                restSeconds = 60,
                sets = mutableListOf(
                    ActiveWorkoutSet(1, 25f, 15),
                    ActiveWorkoutSet(2, 30f, 12),
                    ActiveWorkoutSet(3, 30f, 11)
                )
            )
        )
        _activeExercises.value = initialExercises

        workoutTimerJob?.cancel()
        workoutTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _elapsedSeconds.value += 1
            }
        }
    }

    fun toggleSetCompletion(exerciseIndex: Int, setIndex: Int, restDuration: Int = 75) {
        val currentList = _activeExercises.value.map { exercise ->
            exercise.copy(sets = exercise.sets.toMutableList())
        }
        val targetSet = currentList.getOrNull(exerciseIndex)?.sets?.getOrNull(setIndex)
        if (targetSet != null) {
            targetSet.isCompleted = !targetSet.isCompleted
            _activeExercises.value = currentList

            if (targetSet.isCompleted) {
                startRestTimer(restDuration)
            }
        }
    }

    fun startRestTimer(seconds: Int) {
        restTimerJob?.cancel()
        _restSecondsRemaining.value = seconds
        restTimerJob = viewModelScope.launch {
            while (_restSecondsRemaining.value > 0) {
                delay(1000)
                _restSecondsRemaining.value -= 1
            }
        }
    }

    fun cancelRestTimer() {
        restTimerJob?.cancel()
        _restSecondsRemaining.value = 0
    }

    fun finishWorkoutSession(notes: String = "") {
        workoutTimerJob?.cancel()
        restTimerJob?.cancel()

        val durationMinutes = (_elapsedSeconds.value / 60).coerceAtLeast(1)
        var totalSets = 0
        var totalVolume = 0f

        _activeExercises.value.forEach { exercise ->
            exercise.sets.forEach { set ->
                if (set.isCompleted) {
                    totalSets += 1
                    totalVolume += (set.weightKg * set.reps)
                }
            }
        }

        viewModelScope.launch {
            repository.recordWorkoutSession(
                WorkoutSessionEntity(
                    workoutTitle = _activeWorkoutTitle.value,
                    durationMinutes = durationMinutes,
                    totalVolumeKg = totalVolume,
                    setsCompleted = totalSets,
                    notes = notes
                )
            )

            // Update user streak in profile
            val current = userProfile.value
            if (current != null) {
                repository.updateProfile(
                    current.copy(
                        workoutStreak = current.workoutStreak + 1,
                        lastWorkoutDateEpoch = System.currentTimeMillis()
                    )
                )
            }
        }

        _isWorkoutActive.value = false
        _currentTab.value = 0 // Return to dashboard
    }

    // AI Fitness Chatbot State
    private val _chatMessages = MutableStateFlow<List<ChatMessageItem>>(
        listOf(
            ChatMessageItem(
                id = "m1",
                isUser = false,
                text = "Welcome to FitForge AI! I am Forge AI, your personal fitness, hypertrophy, and nutrition coach. Ask me about progressive overload, breaking plateaus, high-protein Indian diets, creatine, or workout splits!"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessageItem>> = _chatMessages.asStateFlow()

    private val _isChatThinking = MutableStateFlow(false)
    val isChatThinking: StateFlow<Boolean> = _isChatThinking.asStateFlow()

    fun sendChatMessage(query: String) {
        if (query.isBlank()) return
        val userMsg = ChatMessageItem(id = System.currentTimeMillis().toString(), isUser = true, text = query.trim())
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _isChatThinking.value = true
            val responseText = FitForgeAiEngine.getCoachResponse(query)
            val coachMsg = ChatMessageItem(
                id = (System.currentTimeMillis() + 1).toString(),
                isUser = false,
                text = responseText
            )
            _chatMessages.value = _chatMessages.value + coachMsg
            _isChatThinking.value = false
        }
    }

    // Nutrition actions
    fun addFoodLog(item: FoodItem, mealType: String, multiplier: Float = 1.0f) {
        viewModelScope.launch {
            val log = NutritionLogEntity(
                foodName = item.name,
                servingSize = if (multiplier == 1f) item.defaultServing else "${(multiplier * 100).toInt()}% serving",
                calories = (item.calories * multiplier).toInt(),
                protein = item.protein * multiplier,
                carbs = item.carbs * multiplier,
                fats = item.fats * multiplier,
                mealType = mealType
            )
            repository.addNutritionLog(log)
        }
    }

    fun addCustomFoodLog(name: String, calories: Int, protein: Float, carbs: Float, fats: Float, mealType: String) {
        viewModelScope.launch {
            repository.addNutritionLog(
                NutritionLogEntity(
                    foodName = name,
                    servingSize = "1 custom portion",
                    calories = calories,
                    protein = protein,
                    carbs = carbs,
                    fats = fats,
                    mealType = mealType
                )
            )
        }
    }

    fun removeFoodLog(id: Long) {
        viewModelScope.launch {
            repository.removeNutritionLog(id)
        }
    }

    // Body metric actions
    fun logBodyMetric(weight: Float, chest: Float?, waist: Float?, arms: Float?, thighs: Float?, note: String = "") {
        viewModelScope.launch {
            repository.addBodyMetric(
                BodyMetricEntity(
                    weightKg = weight,
                    chestCm = chest,
                    waistCm = waist,
                    armsCm = arms,
                    thighsCm = thighs,
                    note = note
                )
            )
            // Also update current profile weight
            val current = userProfile.value
            if (current != null) {
                repository.updateProfile(current.copy(weightKg = weight))
            }
        }
    }

    // Personal record action
    fun logPersonalRecord(exercise: String, weight: Float, reps: Int) {
        viewModelScope.launch {
            repository.savePersonalRecord(
                PersonalRecordEntity(exerciseName = exercise, weightKg = weight, reps = reps)
            )
        }
    }

    // Profile & Calculator target update
    fun updateProfileAndCalculateTargets(
        name: String,
        age: Int,
        heightCm: Float,
        weightKg: Float,
        targetWeightKg: Float,
        goal: FitnessGoal,
        exp: ExperienceLevel,
        diet: DietaryPreference,
        equipment: EquipmentType,
        workoutDays: Int,
        customCalories: Int? = null,
        customProtein: Int? = null,
        customCarbs: Int? = null,
        customFats: Int? = null
    ) {
        viewModelScope.launch {
            val calc = FitForgeRepository.calculateMacros(weightKg, heightCm, age, goal, workoutDays)
            val current = userProfile.value
            val updated = UserProfileEntity(
                id = 1,
                name = name,
                age = age,
                heightCm = heightCm,
                weightKg = weightKg,
                targetWeightKg = targetWeightKg,
                experienceLevel = exp.name,
                fitnessGoal = goal.name,
                dietaryPreference = diet.name,
                availableEquipment = equipment.name,
                workoutDaysPerWeek = workoutDays,
                dailyCalorieTarget = customCalories ?: calc.calories,
                dailyProteinTarget = customProtein ?: calc.proteinGrams,
                dailyCarbsTarget = customCarbs ?: calc.carbsGrams,
                dailyFatsTarget = customFats ?: calc.fatsGrams,
                workoutStreak = current?.workoutStreak ?: 5,
                lastWorkoutDateEpoch = current?.lastWorkoutDateEpoch ?: System.currentTimeMillis()
            )
            repository.updateProfile(updated)
        }
    }
}
