package edu.ap.playcomic.data

import com.google.firebase.auth.FirebaseAuth
import edu.ap.playcomic.data.model.LoggedInUser
import kotlinx.coroutines.tasks.await
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            val authResult = FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(username, password)
                .await()

            val firebaseUser = authResult.user ?: throw Exception("User not found")
            val loggedInUser = LoggedInUser(firebaseUser.uid, firebaseUser.displayName ?: "Anonymous")

            Result.Success(loggedInUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}