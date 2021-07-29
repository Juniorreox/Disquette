package fr.juniorreox.disquette

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import fr.juniorreox.disquette.repository.disqueRepository
import fr.juniorreox.disquette.repository.disqueRepository.singleton.User
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseUser
import fr.juniorreox.disquette.repository.disqueRepository.singleton.storageRef

class Register_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val register_button = findViewById<Button>(R.id.register)
        val back_button = findViewById<ImageView>(R.id.back_register)
        val register_username = findViewById<EditText>(R.id.input_pseudo)
        val register_email = findViewById<EditText>(R.id.input_email)
        val register_pass = findViewById<EditText>(R.id.input_pass)

        back_button.setOnClickListener{
            val intent =  Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        register_button.setOnClickListener{
            val username : String = register_username.text.toString()
            val email : String = register_email.text.toString()
            val pass : String = register_pass.text.toString()
            val storageReference = storageRef.child("profile_image.jpg")

            if(username == ""){
                Toast.makeText(this,"S'il vous plait veuillez entrez un pseudo", Toast.LENGTH_LONG).show()

            }else if(email ==""){
                Toast.makeText(this,"S'il vous plait veuillez entrez une adresse mail", Toast.LENGTH_LONG).show()

            }else if(pass ==""){
                Toast.makeText(this,"S'il vous plait veuillez entrez un mot de passe", Toast.LENGTH_LONG).show()

            }else{
                val credential = EmailAuthProvider.getCredential(email, pass)

                disqueRepository.singleton.User.currentUser!!.linkWithCredential(credential).
                addOnCompleteListener{task->
                    if(task.isSuccessful){
                        val firebaseUserUid = User.currentUser!!.uid
                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"] = firebaseUserUid
                        userHashMap["userName"] = username
                        userHashMap["mail"] = email
                        userHashMap["password"] = pass
                        userHashMap["hasSigned"] = true
                        userHashMap["profile"] = "empty"

                        databaseUser.child(firebaseUserUid).updateChildren(userHashMap).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this,"Vous etes inscrit", Toast.LENGTH_LONG).show()
                                val intent =  Intent(this,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }else{
                        Toast.makeText(this,"Error message:" + task.exception!!.message.toString(),
                            Toast.LENGTH_LONG).show()
                    }

                }

            }
        }

    }

}