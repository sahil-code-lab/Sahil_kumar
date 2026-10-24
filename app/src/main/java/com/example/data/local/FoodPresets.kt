package com.example.data.local

import com.example.data.model.FoodItem

object FoodPresets {
    val items = listOf(
        // Indian Staples & High Protein
        FoodItem("f1", "Roti / Chapati", "रोटी (गेहूं)", "1 medium (35g)", 85, 3.2f, 17.5f, 0.4f, isIndian = true, category = "Grains"),
        FoodItem("f2", "Roti with Ghee", "घी वाली रोटी", "1 medium with 1 tsp ghee", 125, 3.2f, 17.5f, 5.0f, isIndian = true, category = "Grains"),
        FoodItem("f3", "Cooked White Rice", "चावल (पका हुआ)", "1 cup (150g)", 195, 4.1f, 43.0f, 0.4f, isIndian = true, category = "Grains"),
        FoodItem("f4", "Brown Rice (Cooked)", "ब्राउन राइस", "1 cup (150g)", 165, 3.8f, 35.0f, 1.4f, isIndian = true, category = "Grains"),
        FoodItem("f5", "Yellow Dal (Moong / Toor)", "दाल तड़का", "1 katori / bowl (150g)", 145, 8.5f, 20.0f, 3.5f, isIndian = true, isHighProtein = true, category = "Lentils"),
        FoodItem("f6", "Dal Makhani", "दाल मखनी", "1 bowl (150g)", 260, 9.0f, 22.0f, 15.0f, isIndian = true, category = "Lentils"),
        FoodItem("f7", "Boiled Whole Egg", "उबला अंडा", "1 large egg (50g)", 74, 6.3f, 0.4f, 5.0f, isHighProtein = true, category = "Protein"),
        FoodItem("f8", "Boiled Egg White", "अंडे की सफेदी", "1 large white (33g)", 17, 3.6f, 0.2f, 0.1f, isHighProtein = true, category = "Protein"),
        FoodItem("f9", "Chicken Breast (Grilled)", "चिकन ब्रेस्ट", "100g cooked", 165, 31.0f, 0.0f, 3.6f, isHighProtein = true, category = "Protein"),
        FoodItem("f10", "Home Style Chicken Curry", "चिकन करी", "1 bowl with gravy (200g)", 240, 24.0f, 8.0f, 12.0f, isIndian = true, isHighProtein = true, category = "Curry"),
        FoodItem("f11", "Raw Paneer (Cottage Cheese)", "कच्चा पनीर", "100g", 265, 18.3f, 3.4f, 20.8f, isIndian = true, isHighProtein = true, category = "Dairy"),
        FoodItem("f12", "Paneer Bhurji", "पनीर भुर्जी", "1 bowl (150g)", 290, 16.5f, 6.0f, 22.0f, isIndian = true, isHighProtein = true, category = "Curry"),
        FoodItem("f13", "Paneer Tikka (Tandoori)", "पनीर टिक्का", "5 pieces (150g)", 270, 19.0f, 8.0f, 18.0f, isIndian = true, isHighProtein = true, category = "Snacks"),
        FoodItem("f14", "Cow Milk (Toned)", "दूध (टोन्ड)", "1 glass (250ml)", 150, 8.0f, 12.0f, 7.5f, isIndian = true, category = "Dairy"),
        FoodItem("f15", "Curd / Plain Homemade Dahi", "दही", "1 bowl (150g)", 95, 5.2f, 6.5f, 5.0f, isIndian = true, category = "Dairy"),
        FoodItem("f16", "Greek Yogurt (Unsweetened)", "ग्रीक योगर्ट", "1 cup (150g)", 120, 15.0f, 6.0f, 4.0f, isHighProtein = true, category = "Dairy"),
        FoodItem("f17", "Rolled Oats (Dry)", "ओट्स", "50g", 190, 6.5f, 33.0f, 3.5f, category = "Breakfast"),
        FoodItem("f18", "Oats with Milk & Banana", "ओट्स दूध और केला", "1 large bowl (300g)", 340, 14.0f, 58.0f, 6.5f, category = "Breakfast"),
        FoodItem("f19", "Banana", "केला", "1 medium (118g)", 105, 1.3f, 27.0f, 0.3f, category = "Fruits"),
        FoodItem("f20", "Soya Chunks (Boiled)", "सोया चंक्स", "50g dry / 150g cooked", 172, 26.0f, 16.5f, 0.5f, isIndian = true, isHighProtein = true, category = "Protein"),
        FoodItem("f21", "Chana Masala (Chickpeas)", "चना मसाला / छोले", "1 bowl (180g)", 220, 11.5f, 32.0f, 6.0f, isIndian = true, isHighProtein = true, category = "Lentils"),
        FoodItem("f22", "Rajma Masala (Kidney Beans)", "राजमा", "1 bowl (180g)", 210, 10.8f, 30.0f, 5.5f, isIndian = true, isHighProtein = true, category = "Lentils"),
        FoodItem("f23", "Moong Dal Khichdi", "खिचड़ी", "1 bowl (200g)", 230, 8.2f, 38.0f, 5.0f, isIndian = true, category = "Grains"),
        FoodItem("f24", "Idli with Sambar", "इडली सांभर", "2 idlis + sambar (200g)", 180, 6.5f, 34.0f, 2.0f, isIndian = true, category = "Breakfast"),
        FoodItem("f25", "Masala Dosa", "मसाला डोसा", "1 medium with chutney", 310, 5.5f, 48.0f, 11.0f, isIndian = true, category = "Breakfast"),
        FoodItem("f26", "Whey Protein Isolate", "व्हे प्रोटीन", "1 scoop (32g)", 120, 25.0f, 2.0f, 1.0f, isHighProtein = true, category = "Supplements"),
        FoodItem("f27", "Peanut Butter (Natural)", "पीनट बटर", "2 tbsp (32g)", 190, 8.0f, 7.0f, 16.0f, isHighProtein = true, category = "Healthy Fats"),
        FoodItem("f28", "Almonds (Raw)", "बादाम", "1 handful (25g)", 145, 5.2f, 5.4f, 12.5f, category = "Healthy Fats"),
        FoodItem("f29", "Fish Curry (Rohu / Salmon)", "मछली करी", "1 bowl (180g)", 220, 22.0f, 4.0f, 13.0f, isIndian = true, isHighProtein = true, category = "Protein"),
        FoodItem("f30", "Mixed Sprouts Salad", "अंकुरित सलाद", "1 bowl (150g)", 110, 7.5f, 18.0f, 1.2f, isIndian = true, isHighProtein = true, category = "Salad")
    )
}
