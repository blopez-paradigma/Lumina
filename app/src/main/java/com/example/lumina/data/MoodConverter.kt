package com.example.lumina.data

import androidx.room.TypeConverter

class MoodConverter {
    @TypeConverter
    fun fromMood(mood: Mood): String {
        return mood.name
    }

    @TypeConverter
    fun toMood(mood: String): Mood {
        return Mood.valueOf(mood)
    }
}
