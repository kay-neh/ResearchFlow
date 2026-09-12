package com.researchflow.app.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.researchflow.app.data.model.Student
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreStudentService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createStudent(student: Student) {
        firestore.collection("students")
            .document(student.studentId)
            .set(student)
            .await()
    }

    suspend fun getStudent(studentId: String): Student? {
        return firestore.collection("students")
            .document(studentId)
            .get()
            .await()
            .toObject(Student::class.java)
    }
}