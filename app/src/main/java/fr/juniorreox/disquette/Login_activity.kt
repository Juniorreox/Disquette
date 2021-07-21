package fr.juniorreox.disquette

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import fr.juniorreox.disquette.repository.disqueRepository
import fr.juniorreox.disquette.repository.disqueRepository.singleton.User

class Login_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login_button = findViewById<Button>(R.id.login)
        val back_button = findViewById<ImageView>(R.id.back_login)
        val register_email = findViewById<EditText>(R.id.input_email_login)
        val register_pass = findViewById<EditText>(R.id.input_pass_login)


        back_button.setOnClickListener{
            val intent =  Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        login_button.setOnClickListener{
            val email : String = register_email.text.toString()
            val pass : String = register_pass.text.toString()

            if(email ==""){
                Toast.makeText(this,"S'il vous plait veuillez entrez une adresse mail", Toast.LENGTH_LONG).show()

            }else if(pass ==""){
                Toast.makeText(this,"S'il vous plait veuillez entrez un mot de passe", Toast.LENGTH_LONG).show()

            }else{


                User.signInWithEmailAndPassword(email,pass).
                addOnCompleteListener{task->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Vous etes connectez", Toast.LENGTH_LONG).show()
                        val intent =  Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,"Error message:" + task.exception!!.message.toString(),
                            Toast.LENGTH_LONG).show()
                    }

                }

            }

        }

    }

}