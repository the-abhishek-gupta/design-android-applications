package com.labs.systemdesignandroid.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.labs.systemdesignandroid.domain.Student

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val grade: String
)

fun StudentEntity.toDomain() = Student(
    id = id,
    name = name,
    grade = grade
)

fun Student.toEntity() = StudentEntity(
    id = id,
    name = name,
    grade = grade
)
