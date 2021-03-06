package fr.juniorreox.disquette

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fr.juniorreox.disquette.modele.userModele
import fr.juniorreox.disquette.repository.disqueRepository
import fr.juniorreox.disquette.repository.disqueRepository.singleton.User
import fr.juniorreox.disquette.repository.disqueRepository.singleton.disqueList
import android.util.Pair
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseUser


class launching: AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 2000
    //token d'authentification
    private lateinit var auth: FirebaseAuth
    /*
    //animations
    private lateinit var topAnimation : Animation
    private lateinit var topAnimation2 : Animation
    private lateinit var topAnimation3 : Animation
    private lateinit var topAnimation4 : Animation
    private lateinit var topAnimation5 : Animation
    private lateinit var topAnimation6 : Animation

    private lateinit var middleAnimation : Animation
    private lateinit var bottomAnimation : Animation
    //Hooks
    private lateinit var first : View
    private lateinit var second : View
    private lateinit var third : View
    private lateinit var fourth : View
    private lateinit var fifth : View
    private lateinit var sixth : View
    private lateinit var logo : ImageView
    private lateinit var tag : TextView
    */



    //private var afterOnCreate: Boolean = false
    //verifie que la methode updatedata a ete lancee, attend 4 second(le temps de  charger les donnees) puis lance la mainactivity
    //var max: Int by Delegates.observable(0) { property, oldValue, newValue ->

    //}

    //a la creation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launching) //initialiser les lateinit var apres cette ligne de code, elle permet d avoir acces aux element contenu dans le layout utilise par leurs identifiants


        //cette activite est faite pour le chargement de la liste des disques, c'est pour cela qu'il utilise launching.xml
/*
        //Les animations
        topAnimation= AnimationUtils.loadAnimation(this,R.anim.top_animation)
        topAnimation2= AnimationUtils.loadAnimation(this,R.anim.top_animation2)
        topAnimation3= AnimationUtils.loadAnimation(this,R.anim.top_animation3)
        topAnimation4= AnimationUtils.loadAnimation(this,R.anim.top_animation4)
        topAnimation5= AnimationUtils.loadAnimation(this,R.anim.top_animation5)
        topAnimation6= AnimationUtils.loadAnimation(this,R.anim.top_animation6)

        middleAnimation= AnimationUtils.loadAnimation(this,R.anim.middle_animation)
        bottomAnimation= AnimationUtils.loadAnimation(this,R.anim.bottom_animation)

        //Hooks
        first = findViewById(R.id.first_line)
        second = findViewById(R.id.second_line)
        third = findViewById(R.id.third_line)
        fourth = findViewById(R.id.fourth_line)
        fifth = findViewById(R.id.fifth_line)
        sixth = findViewById(R.id.sixth_line)
        logo = findViewById(R.id.app_image)
        //tag = findViewById(R.id.tagLine)


        //setting animations
        first.startAnimation(topAnimation)
        second.startAnimation(topAnimation2)
        third.startAnimation(topAnimation3)
        fourth.startAnimation(topAnimation4)
        fifth.startAnimation(topAnimation5)
        sixth.startAnimation(topAnimation6)

        //logo.startAnimation(middleAnimation)

        //tag.startAnimation(bottomAnimation)
*/

    }


    override fun onResume() {
        super.onResume()
        auth = User
        //currentuser appeler dans le disque repository
        val currentUser = auth.currentUser
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(currentUser)
    }



    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus)
            hideToolBr()
    }

    private fun hideToolBr() {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        val uiOptions = window.decorView.systemUiVisibility
        var newUiOptions = uiOptions
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        val isImmersiveModeEnabled = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY == uiOptions

        // Navigation bar hiding:  Backwards compatible to ICS.
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        // Status bar hiding: Backwards compatible to Jellybean
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN

        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = newUiOptions
        //END_INCLUDE (set_ui_flags)
    }



    //gestion des utilisateurs
    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {


            Handler(Looper.getMainLooper()).postDelayed({
                val repo = disqueRepository()
                repo.theUser()
                repo.theAdminContain() //faire gaff pendant l'ecriture des regle de securite de verrrouiller l'acces en ecriture

                // Your Code
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, SPLASH_TIME_OUT)

        } else {
            //ici on cree un compte anonyme pour tout nouvelle utilisateur
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInAnonymously:success")
                        val user = userModele(User.currentUser?.uid)
                        user.uid?.let { databaseUser.child(it).setValue(user).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val repo = disqueRepository()
                                repo.addListDisc()
                                val current = User.currentUser
                                //on refait le teste d'identifiant
                                Toast.makeText(this,"Vous venez d'etre enregistrer", Toast.LENGTH_LONG).show()
                                updateUI(current)

                            }
                        } }




                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("--------------------", "signInAnonymously:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                        updateUI(null)
                    }
                }
        }
    }
}