package com.example.ai

import com.example.data.model.EquipmentType
import com.example.data.model.ExerciseDetail
import com.example.data.model.ExperienceLevel
import com.example.data.model.FitnessGoal
import com.example.data.model.MuscleGroup
import com.example.data.model.RoutineDay
import com.example.data.model.RoutinePlan
import kotlinx.coroutines.delay

object FitForgeAiEngine {

    fun generateRoutine(
        goal: FitnessGoal,
        level: ExperienceLevel,
        equipment: EquipmentType,
        daysPerWeek: Int
    ): RoutinePlan {
        return when {
            daysPerWeek >= 5 -> generatePPLPlan(goal, level, equipment)
            daysPerWeek == 4 -> generateUpperLowerPlan(goal, level, equipment)
            else -> generateFullBodyPlan(goal, level, equipment)
        }
    }

    private fun generatePPLPlan(
        goal: FitnessGoal,
        level: ExperienceLevel,
        equipment: EquipmentType
    ): RoutinePlan {
        val pushDay = RoutineDay(
            dayNumber = 1,
            title = "Push A — Chest, Shoulders & Triceps",
            focus = "Horizontal press emphasis & clavicular head",
            exercises = listOf(
                ExerciseDetail(
                    id = "ex_p1",
                    name = if (equipment == EquipmentType.DUMBBELLS_ONLY) "Dumbbell Flat Bench Press" else "Barbell Bench Press",
                    muscleGroup = MuscleGroup.CHEST,
                    sets = 4,
                    targetReps = if (goal == FitnessGoal.STRENGTH) "4-6" else "8-10",
                    restSeconds = 120,
                    instructions = "Retract scapula, arch lower back slightly, drive through heels. Lower to lower chest, pause 0.5s, press with explosive control.",
                    progressiveOverloadTip = "When you hit top of rep range across all sets with pristine form, add 2.5kg total.",
                    equipment = equipment
                ),
                ExerciseDetail(
                    id = "ex_p2",
                    name = "Incline Dumbbell Press",
                    muscleGroup = MuscleGroup.CHEST,
                    sets = 3,
                    targetReps = "8-12",
                    restSeconds = 90,
                    instructions = "Set bench at 30 degrees. Keep dumbbells tucked at 45 degrees, stretch at bottom, contract upper pecs without clanking bells.",
                    progressiveOverloadTip = "Increase weight by 2kg per dumbbell after hitting 12 reps on first 2 sets.",
                    equipment = equipment
                ),
                ExerciseDetail(
                    id = "ex_p3",
                    name = if (equipment == EquipmentType.FULL_GYM) "Standing Overhead Military Press" else "Seated Dumbbell Shoulder Press",
                    muscleGroup = MuscleGroup.SHOULDERS,
                    sets = 3,
                    targetReps = "8-10",
                    restSeconds = 90,
                    instructions = "Brace core and glutes. Press straight up in scapular plane, clearing face, lock out overhead with head pushed slightly forward.",
                    progressiveOverloadTip = "Focus on zero momentum and slow 3-second eccentrics before upping load.",
                    equipment = equipment
                ),
                ExerciseDetail(
                    id = "ex_p4",
                    name = if (equipment == EquipmentType.FULL_GYM) "Cable Lateral Raises" else "Dumbbell Lean Lateral Raises",
                    muscleGroup = MuscleGroup.SHOULDERS,
                    sets = 4,
                    targetReps = "12-15",
                    restSeconds = 60,
                    instructions = "Slight forward torso lean, raise elbows to shoulder height. Lead with pinkies/elbows, control the descent completely.",
                    progressiveOverloadTip = "Double progression: increase reps first from 12 to 15, then move to next weight increment.",
                    equipment = equipment
                ),
                ExerciseDetail(
                    id = "ex_p5",
                    name = if (equipment == EquipmentType.FULL_GYM) "Cable Triceps Rope Pushdown" else "Overhead Dumbbell Triceps Extension",
                    muscleGroup = MuscleGroup.ARMS,
                    sets = 3,
                    targetReps = "10-14",
                    restSeconds = 60,
                    instructions = "Pin elbows to ribs, fully extend triceps and flare rope at lockout. Avoid swaying shoulders.",
                    progressiveOverloadTip = "Add a 2-second isometric squeeze at maximum contraction.",
                    equipment = equipment
                )
            )
        )

        val pullDay = RoutineDay(
            dayNumber = 2,
            title = "Pull A — Back, Rear Delts & Biceps",
            focus = "Vertical pull & lat thickness",
            exercises = listOf(
                ExerciseDetail(
                    id = "ex_pu1",
                    name = "Conventional Deadlift or Barbell Bent-Over Row",
                    muscleGroup = MuscleGroup.BACK,
                    sets = 4,
                    targetReps = if (goal == FitnessGoal.STRENGTH) "5" else "6-8",
                    restSeconds = 150,
                    instructions = "Hinge at hips, maintain neutral spine, pull elbows toward hips rather than ceiling to recruit lats maximally.",
                    progressiveOverloadTip = "Prioritize lumbar neutrality over ego lifting. Micro-load 1-2kg weekly.",
                    equipment = equipment
                ),
                ExerciseDetail(
                    id = "ex_pu2",
                    name = if (equipment == EquipmentType.FULL_GYM) "Wide-Grip Lat Pulldown" else "Weighted Pull-Up or Banded Chin-Up",
                    muscleGroup = MuscleGroup.BACK,
                    sets = 3,
                    targetReps = "8-11",
                    restSeconds = 90,
                    instructions = "Slight arch, depress scapula before pulling. Drive elbows down to side pockets, deep stretch at top.",
                    progressiveOverloadTip = "Aim for +1 rep per session until hitting 11 reps on set 1.",
                    equipment = equipment
                ),
                ExerciseDetail(
                    id = "ex_pu3",
                    name = "Chest-Supported Dumbbell Row",
                    muscleGroup = MuscleGroup.BACK,
                    sets = 3,
                    targetReps = "10-12",
                    restSeconds = 75,
                    instructions = "Chest flat against incline bench. Eliminates lower back fatigue so rhomboids and mid-traps do 100% of the work.",
                    progressiveOverloadTip = "Pause for 1 second at full squeeze at the top of each rep.",
                    equipment = equipment
                ),
                ExerciseDetail(
                    id = "ex_pu4",
                    name = "Incline Dumbbell Biceps Curl",
                    muscleGroup = MuscleGroup.ARMS,
                    sets = 3,
                    targetReps = "10-12",
                    restSeconds = 60,
                    instructions = "Lay on incline bench, let arms hang back to place biceps long head in a loaded stretch. Supinate wrist as you curl.",
                    progressiveOverloadTip = "Never swing shoulders; keep upper arm perpendicular to floor.",
                    equipment = equipment
                ),
                ExerciseDetail(
                    id = "ex_pu5",
                    name = "Face Pulls with External Rotation",
                    muscleGroup = MuscleGroup.SHOULDERS,
                    sets = 3,
                    targetReps = "15-18",
                    restSeconds = 60,
                    instructions = "Pull rope toward forehead, ending with hands above elbows in a double-biceps salute for healthy rotator cuffs.",
                    progressiveOverloadTip = "Focus purely on mind-muscle connection and rear delt burn rather than heavy stack.",
                    equipment = equipment
                )
            )
        )

        val legsDay = RoutineDay(
            dayNumber = 3,
            title = "Legs & Core — Quads, Hamstrings & Calves",
            focus = "Squat mechanics & posterior chain",
            exercises = listOf(
                ExerciseDetail(
                    id = "ex_l1",
                    name = if (equipment == EquipmentType.DUMBBELLS_ONLY) "Goblet Squat or Bulgarian Split Squat" else "Barbell Back Squat",
                    muscleGroup = MuscleGroup.LEGS,
                    sets = 4,
                    targetReps = if (goal == FitnessGoal.STRENGTH) "5" else "6-10",
                    restSeconds = 150,
                    instructions = "Feet shoulder-width apart, knees track over toes. Break at hips and knees simultaneously, achieve parallel depth.",
                    progressiveOverloadTip = "Add 2.5kg to working weight when reaching 10 reps with zero form breakdown.",
                    equipment = equipment
                ),
                ExerciseDetail(
                    id = "ex_l2",
                    name = "Romanian Deadlift (RDL)",
                    muscleGroup = MuscleGroup.LEGS,
                    sets = 3,
                    targetReps = "8-10",
                    restSeconds = 120,
                    instructions = "Slight knee bend, push hips straight back into wall behind you until intense hamstring stretch, pull hips through.",
                    progressiveOverloadTip = "Keep bar/dumbbells glued against thighs throughout entire descent.",
                    equipment = equipment
                ),
                ExerciseDetail(
                    id = "ex_l3",
                    name = if (equipment == EquipmentType.FULL_GYM) "Leg Press or Hack Squat" else "Walking Dumbbell Lunges",
                    muscleGroup = MuscleGroup.LEGS,
                    sets = 3,
                    targetReps = "10-12",
                    restSeconds = 90,
                    instructions = "Deep knee flexion, controlled eccentric, drive through whole foot.",
                    progressiveOverloadTip = "Slow 3-second negatives to increase metabolic stress and hypertrophy stimulus.",
                    equipment = equipment
                ),
                ExerciseDetail(
                    id = "ex_l4",
                    name = "Standing Calf Raises",
                    muscleGroup = MuscleGroup.LEGS,
                    sets = 4,
                    targetReps = "12-16",
                    restSeconds = 60,
                    instructions = "Drop heels deep below step for 2-second stretch, rise onto balls of feet, hold 1-second contraction.",
                    progressiveOverloadTip = "Eliminate Achilles tendon bounce with a full dead-stop at the bottom.",
                    equipment = equipment
                ),
                ExerciseDetail(
                    id = "ex_l5",
                    name = "Hanging Leg Raises or Cable Woodchoppers",
                    muscleGroup = MuscleGroup.CORE,
                    sets = 3,
                    targetReps = "12-15",
                    restSeconds = 60,
                    instructions = "Curl pelvis upward toward chest, do not just swing hip flexors. Squeeze abs at top.",
                    progressiveOverloadTip = "Progress from bent-knee raises to straight-leg raises.",
                    equipment = equipment
                )
            )
        )

        return RoutinePlan(
            id = "plan_ppl_ai",
            title = "FitForge AI — Push/Pull/Legs Hypertrophy Protocol",
            goal = goal,
            daysPerWeek = 5,
            level = level,
            equipment = equipment,
            description = "Scientifically optimized 5-day rotating split targeting each muscle group with 10-18 weekly sets for maximum muscle growth and strength adaptation.",
            days = listOf(pushDay, pullDay, legsDay)
        )
    }

    private fun generateUpperLowerPlan(
        goal: FitnessGoal,
        level: ExperienceLevel,
        equipment: EquipmentType
    ): RoutinePlan {
        val upper = RoutineDay(
            dayNumber = 1,
            title = "Upper Body Strength & Size",
            focus = "Compound horizontal & vertical balance",
            exercises = listOf(
                ExerciseDetail("ex_u1", "Barbell Bench Press", MuscleGroup.CHEST, 4, "6-8", 120, "Retract shoulder blades, control eccentric, drive up.", "Add 2.5kg when hitting 8 reps.", equipment),
                ExerciseDetail("ex_u2", "Barbell Bent-Over Row", MuscleGroup.BACK, 4, "6-8", 120, "Torso at 45 degrees, pull bar to upper waist.", "Focus on full lat squeeze.", equipment),
                ExerciseDetail("ex_u3", "Overhead Shoulder Press", MuscleGroup.SHOULDERS, 3, "8-10", 90, "Lock out overhead with tight core and glutes.", "Strict form, zero leg bounce.", equipment),
                ExerciseDetail("ex_u4", "Lat Pulldown or Pull-Ups", MuscleGroup.BACK, 3, "8-12", 75, "Depress scapula, drive elbows down.", "Try 1 extra rep per workout.", equipment),
                ExerciseDetail("ex_u5", "Dumbbell Biceps & Triceps Superset", MuscleGroup.ARMS, 3, "10-12", 60, "Alternate curls and skull crushers.", "Full range of motion.", equipment)
            )
        )

        val lower = RoutineDay(
            dayNumber = 2,
            title = "Lower Body & Core Power",
            focus = "Squats, hinge & posterior chain",
            exercises = listOf(
                ExerciseDetail("ex_low1", "Barbell Back Squat", MuscleGroup.LEGS, 4, "6-8", 150, "Descend below parallel with upright chest.", "Add 2.5kg when all sets hit 8 reps.", equipment),
                ExerciseDetail("ex_low2", "Romanian Deadlift", MuscleGroup.LEGS, 3, "8-10", 120, "Deep hamstring stretch, hinge at hips.", "Keep spine neutral.", equipment),
                ExerciseDetail("ex_low3", "Bulgarian Split Squats", MuscleGroup.LEGS, 3, "10-12", 90, "Elevate rear foot, drop front thigh to parallel.", "Great for unilateral hypertrophy.", equipment),
                ExerciseDetail("ex_low4", "Standing Calf Raises", MuscleGroup.LEGS, 4, "12-15", 60, "2-second pause in full stretch.", "Eliminate bounce.", equipment),
                ExerciseDetail("ex_low5", "Plank & Ab Wheel Rollouts", MuscleGroup.CORE, 3, "45-60s", 60, "Tuck pelvis and brace hard.", "Add 5 seconds weekly.", equipment)
            )
        )

        return RoutinePlan(
            id = "plan_ul_ai",
            title = "FitForge AI — 4-Day Upper / Lower Split",
            goal = goal,
            daysPerWeek = 4,
            level = level,
            equipment = equipment,
            description = "High frequency 4-day split hitting every muscle group twice a week with optimal recovery time between sessions.",
            days = listOf(upper, lower)
        )
    }

    private fun generateFullBodyPlan(
        goal: FitnessGoal,
        level: ExperienceLevel,
        equipment: EquipmentType
    ): RoutinePlan {
        val fullBodyA = RoutineDay(
            dayNumber = 1,
            title = "Full Body Foundations A",
            focus = "Squat + Horizontal Press + Horizontal Pull",
            exercises = listOf(
                ExerciseDetail("ex_fb1", "Barbell Squat or Goblet Squat", MuscleGroup.LEGS, 3, "8-10", 120, "Control descent, solid base.", "Add 2.5kg once 10 reps achieved.", equipment),
                ExerciseDetail("ex_fb2", "Dumbbell Flat Bench Press", MuscleGroup.CHEST, 3, "8-12", 90, "Controlled chest stretch.", "Focus on pectoral contraction.", equipment),
                ExerciseDetail("ex_fb3", "One-Arm Dumbbell Row", MuscleGroup.BACK, 3, "8-12", 90, "Pull dumbbell to hip pocket.", "Full stretch at bottom.", equipment),
                ExerciseDetail("ex_fb4", "Dumbbell Lateral Raise", MuscleGroup.SHOULDERS, 3, "12-15", 60, "Slight forward lean, raise to shoulder level.", "High rep burn.", equipment),
                ExerciseDetail("ex_fb5", "Plank Hold", MuscleGroup.CORE, 3, "45s", 45, "Keep body in straight line.", "Increase time each week.", equipment)
            )
        )

        return RoutinePlan(
            id = "plan_fb_ai",
            title = "FitForge AI — 3-Day Full Body Conditioning",
            goal = goal,
            daysPerWeek = 3,
            level = level,
            equipment = equipment,
            description = "Time-efficient 3-day routine building maximum foundational strength, muscle tone, and systemic metabolic adaptation.",
            days = listOf(fullBodyA)
        )
    }

    /**
     * AI Fitness Coach responses with deep sports science, nutrition, hypertrophy,
     * and recovery knowledge, including Indian food strategies.
     */
    suspend fun getCoachResponse(userQuery: String): String {
        delay(600) // realistic snappy AI thinking time
        val q = userQuery.lowercase()

        return when {
            q.contains("bench") && (q.contains("plateau") || q.contains("increase") || q.contains("stuck")) -> {
                """
                **FitForge Coach Protocol: Breaking Your Bench Press Plateau**
                
                1. **Technique & Setup:**
                   - Ensure your shoulder blades are tightly retracted and depressed ("pack them into your back pockets").
                   - Establish violent leg drive: plant feet flat and push backward into the floor as you drive the bar off your chest.
                
                2. **Volume & Frequency:**
                   - If you bench once a week, bump to **2x/week**: Day 1 heavy strength (3-5 reps), Day 2 volume/hypertrophy (8-10 reps) or Spoto/Pause bench.
                
                3. **Target The Weak Point:**
                   - **Stuck on chest?** Add 2-second paused bench presses and dumbbell flyes.
                   - **Stuck at midpoint?** Strengthen anterior delts with standing military press.
                   - **Stuck at lockout?** Blast triceps with weighted dips and close-grip bench.
                
                4. **Micro-Loading:**
                   - Don't jump 5kg at a time. Use 1.25kg fractional plates for steady progressive overload.
                """.trimIndent()
            }
            q.contains("indian") || (q.contains("vegetarian") && (q.contains("protein") || q.contains("diet"))) -> {
                """
                **FitForge High-Protein Indian Vegetarian Muscle Blueprint**
                
                Hitting 130-160g+ protein on an Indian vegetarian diet is completely achievable with smart food combining:
                
                **Top High-Protein Indian Staples:**
                - **Soya Chunks:** The vegetarian MVP — ~52g protein per 100g dry weight! Extremely lean and economical.
                - **Paneer (Low-Fat or Raw):** 18-20g protein per 100g. Pair with grilled peppers or in bhurji.
                - **Curd / Homemade Dahi / Greek Yogurt:** 8-15g protein per cup. Rich in gut-friendly probiotics for nutrient absorption.
                - **Lentils & Legumes (Chana, Rajma, Moong Dal):** 20-24g raw (~9-12g cooked bowl). Combine with rice/roti to form complete amino acid profiles.
                - **Toned Milk & Whey Protein:** 1 scoop whey in milk provides an effortless 33g instant complete protein.
                - **Sprouts Salad:** Moong & kala chana with chopped onions, tomatoes, and lemon for micronutrients and digestive enzymes.
                
                *Sample Day:* Breakfast: Oats + Milk + Scoop Whey (38g) | Lunch: 2 Roti + Rajma + 100g Paneer (35g) | Snack: Sprouts salad + Roasted Chana (18g) | Dinner: Rice + Soya Chunks Curry + Curd (42g). **Total = 133g+ Protein!**
                """.trimIndent()
            }
            q.contains("progressive overload") -> {
                """
                **What is Progressive Overload & How to Apply It:**
                
                Progressive overload is the fundamental physiological driver of muscle hypertrophy and strength gains. You must gradually increase the mechanical stress placed on your musculoskeletal system over time.
                
                **The 5 Ways to Apply Progressive Overload:**
                1. **Increase Weight:** Lift 82.5kg instead of 80kg for the same reps.
                2. **Increase Repetitions:** Hit 10 reps with 80kg instead of 8 reps.
                3. **Increase Sets/Volume:** Perform 4 sets instead of 3 sets at the same weight.
                4. **Improve Range of Motion & Tempo:** Deeper squat depth or a strict 3-second negative eccentric.
                5. **Reduce Rest Times:** Lift the same volume with 75s rest instead of 90s (greater metabolic density).
                
                *Golden Rule:* Always earn the right to increase weight by mastering strict form at the top of your target rep range first!
                """.trimIndent()
            }
            q.contains("creatine") -> {
                """
                **FitForge Evidence-Based Guide: Creatine Monohydrate**
                
                - **What it does:** Increases intramuscular phosphocreatine stores, replenishing ATP rapidly during high-intensity lifts (sets of 1-8 reps). Enhances peak power, work capacity, and intracellular hydration.
                - **Recommended Dosage:** 3 to 5 grams daily, taken consistently.
                - **Is Loading Phase Necessary?** Not required! A loading phase (20g/day for 5 days) saturates stores in 1 week, but simply taking 5g daily will fully saturate your muscles within 3-4 weeks without gastrointestinal distress.
                - **Timing:** Any time of day is fine, though taking it post-workout with carbohydrates and protein slightly improves uptake.
                - **Hydration:** Drink at least 3-3.5 liters of water daily, as creatine draws water into muscle cells (intracellular volumization).
                """.trimIndent()
            }
            q.contains("fat loss") || q.contains("shred") || q.contains("cut") -> {
                """
                **FitForge Fat Loss & Muscle Preservation Protocol**
                
                1. **Caloric Deficit:** Aim for a moderate deficit of 300-500 kcal below your TDEE (targeting ~0.5kg loss per week). Avoid aggressive starvation crashes which trigger muscle loss and metabolic slowdown.
                2. **High Protein Intake:** Keep protein high (2.0 - 2.2g per kg bodyweight). Protein has the highest thermic effect of food (TEF) and shields lean muscle tissue in a deficit.
                3. **Lift Heavy in the Gym:** Do NOT switch to "light weights for toning". Continue lifting with heavy intensity to signal your body to retain muscle mass.
                4. **Daily Step Count (NEAT):** Aim for 8,000 to 10,000 steps daily. Non-exercise physical activity burns substantial fat without spiking cortisol or appetite.
                """.trimIndent()
            }
            q.contains("recovery") || q.contains("sleep") || q.contains("sore") -> {
                """
                **FitForge Muscle Recovery & DOMS Optimization**
                
                - **Sleep is King:** 7-9 hours of quality sleep is when over 80% of natural growth hormone (GH) pulse occurs and muscle protein synthesis peaks.
                - **Active Recovery:** Light walking, foam rolling, and mobility work increase blood flow to flush metabolic byproducts without causing further muscle trauma.
                - **Hydration & Electrolytes:** Aim for 3-4L of water with adequate sodium, potassium, and magnesium.
                - **Protein Distribution:** Consume 25-40g of protein every 3-4 hours to keep muscle protein synthesis (MPS) elevated throughout the day.
                """.trimIndent()
            }
            else -> {
                """
                **FitForge AI Coach Insight:**
                
                Great question! For your current goals:
                - **Consistency is 90% of the battle:** Sticking to your programmed workout split and hitting within 100 kcal of your daily targets will outwork any fancy shortcut.
                - **Target Protein:** Aim for 1.8g to 2.2g per kg of bodyweight distributed across 3-4 meals.
                - **Rest & Adaptation:** Muscles grow while resting and recovering in bed, not while tearing down fibers in the gym!
                
                What specific muscle group, routine question, or nutrition target would you like me to break down next?
                """.trimIndent()
            }
        }
    }
}
