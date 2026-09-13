package com.researchflow.app.data.firebase

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecondaryFirebaseAuthService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var secondaryAuth: FirebaseAuth? = null

    private fun getAuth(): FirebaseAuth {
        if (secondaryAuth == null) {
            val options = FirebaseOptions.fromResource(context)
                ?: throw IllegalStateException(
                    "Firebase configuration not found"
                )

            val secondaryApp = try {
                FirebaseApp.getInstance("userCreation")
            } catch (e: IllegalStateException) {
                FirebaseApp.initializeApp(
                    context,
                    options,
                    "userCreation"
                )
                    ?: throw IllegalStateException(
                        "Failed to initialize secondary Firebase app"
                    )
            }

            secondaryAuth = FirebaseAuth.getInstance(secondaryApp)
        }

        return secondaryAuth!!
    }

    suspend fun createUser(
        email: String,
        password: String
    ): String {
        val result = getAuth()
            .createUserWithEmailAndPassword(
                email,
                password
            )
            .await()

        return result.user?.uid
            ?: throw IllegalStateException(
                "User creation failed"
            )
    }

    fun signOut() {
        secondaryAuth?.signOut()
    }
}