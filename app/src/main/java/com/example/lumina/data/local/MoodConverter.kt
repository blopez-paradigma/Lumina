package com.example.lumina.data.local

import com.example.lumina.data.model.Mood

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
