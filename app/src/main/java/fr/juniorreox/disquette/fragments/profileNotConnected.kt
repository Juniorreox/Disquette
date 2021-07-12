package fr.juniorreox.disquette.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.EmailAuthProvider
import fr.juniorreox.disquette.repository.disqueRepository.singleton.User
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseUser
import fr.juniorreox.disquette.R

class profileNotConnected : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState:
        Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_not_connected, container, false)
        val register_button = view.findViewById<Button>(R.id.register)
        val register_username = view.findViewById<EditText>(R.id.input_pseudo)
        val register_email = view.findViewById<EditText>(R.id.input_email)
        val register_pass = view.findViewById<EditText>(R.id.input_pass)

        register_button.setOnClickListener{
            val username : String = register_username.text.toString()
            val email : String = register_email.text.toString()
            val pass : String = register_pass.text.toString()

            if(username == ""){
                Toast.makeText(activity,"S'il vous plait veuillez entrez un pseudo",Toast.LENGTH_LONG).show()

            }else if(email ==""){
                Toast.makeText(activity,"S'il vous plait veuillez entrez une adresse mail",Toast.LENGTH_LONG).show()

            }else if(pass ==""){
                Toast.makeText(activity,"S'il vous plait veuillez entrez un mot de passe",Toast.LENGTH_LONG).show()

            }else{
                val credential = EmailAuthProvider.getCredential(email, pass)

                User.currentUser!!.linkWithCredential(credential).
                addOnCompleteListener{task->
                    if(task.isSuccessful){
                        val firebaseUserUid = User.currentUser!!.uid
                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"] = firebaseUserUid
                        userHashMap["userName"] = username
                        userHashMap["mail"] = email
                        userHashMap["password"] = pass
                        userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/disquette-b83fa.appspot.com/o/profile_image.jpg?alt=media&token=ab1ba168-aee3-42cd-aba5-77edfa3256d6"

                        databaseUser.child(firebaseUserUid).updateChildren(userHashMap).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity,"Vous etes inscrit",Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        Toast.makeText(activity,"Error message:" + task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                    }

                }

            }
        }



        return view
    }

}