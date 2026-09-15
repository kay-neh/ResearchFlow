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

    suspend fun getStudentByUserId(
        userId: String
    ): Student? {
        return firestore.collection("students")
            .whereEqualTo("userId", userId)
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull()
            ?.toObject(Student::class.java)
    }

    suspend fun getStudentsBySupervisor(
        supervisorId: String
    ): List<Student> {
        return firestore.collection("students")
            .whereEqualTo("supervisorId", supervisorId)
            .get()
            .await()
            .toObjects(Student::class.java)
    }

    suspend fun getAllStudents(): List<Student> {
        return firestore.collection("students")
            .get()
            .await()
            .toObjects(Student::class.java)
    }

    suspend fun assignSupervisor(
        studentId: String,
        supervisorId: String
    ) {
        firestore.collection("students")
            .document(studentId)
            .update(
                "supervisorId",
                supervisorId
            )
            .await()
    }
}