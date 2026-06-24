package com.example.lumina.data.local

import com.example.lumina.domain.model.Mood
import androidx.room.TypeConverter

class MoodConverter {
    @TypeConverter
    fun fromMood(mood: Mood): String = mood.name

    @TypeConverter
    fun toMood(mood: String): Mood = Mood.valueOf(mood)
}
