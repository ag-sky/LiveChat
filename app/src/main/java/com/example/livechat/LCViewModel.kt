package com.example.livechat

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.livechat.data.Event
import com.example.livechat.data.USER_NODE
import com.example.livechat.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db : FirebaseFirestore,
) : ViewModel() {



    var inProgress = mutableStateOf(false)
    var eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    var userData = mutableStateOf<UserData?>(null)
    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
       currentUser?.uid?.let{
           getUserData(it)
       }
    }

    fun signUp(name: String, number: String, email: String, password: String) {
        inProgress.value = true
        if(name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "please fill all fields")
            return
        }
        inProgress.value = true
        db.collection(
            USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
                if(it.isEmpty){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

                        if (it.isSuccessful) {
                            signIn.value = true
                            createOrUpdateProfile(name,number)
                        } else {
                            handleException(it.exception, customMessage = "Sign up failed")
                        }
                    }

                }else{
                    handleException(customMessage = "number already exits!")
                    inProgress.value = false
                }
        }
    }

    fun loginIn(email : String, password : String){
        if( email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "please fill all fields")
            return
        }else{
            inProgress.value = true
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                if(it.isSuccessful){
                    signIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let{
                        getUserData(it)
                    }
                }else{
                    handleException(exception = it.exception, customMessage = "login failed!")
                }
            }
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("liveChatApp", "Live Chat Error/Exception", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val msg = if (customMessage.isNullOrEmpty()) errorMsg else customMessage
        eventMutableState.value = Event(msg)
        inProgress.value = false

    }

    fun createOrUpdateProfile(name: String?= null, number : String?= null, imageUrl: String?= null){
                var uId = auth.currentUser?.uid
        var userData= UserData(
             userId = uId,
            name = name?: userData.value?.name,
            number = number?: userData.value?.number,
            imageUrl = imageUrl?: userData.value?.imageUrl
        )

        uId?.let{
            inProgress.value = true
            db.collection(USER_NODE).document(uId).get().addOnSuccessListener {
                if(it.exists()){
                    // update user data
                }else{
                    db.collection(USER_NODE).document(uId).set(userData)
                    inProgress.value = false
                    getUserData(uId)
                }
            }
                .addOnFailureListener {
                    handleException(it, "Can not retrieve user sorry buddy!")
                }
        }
    }

    private fun getUserData(uid : String) {
        inProgress.value = true
        db.collection(
            USER_NODE).document(uid).addSnapshotListener{
                value , error ->
            if(error != null ){
                handleException(error, " can not retrieve user, sorry buddy ")
            }
            if ( value != null) {
                val user = value.toObject<UserData>()
                userData.value = user
                inProgress.value = false
            }
        }
    }
}

