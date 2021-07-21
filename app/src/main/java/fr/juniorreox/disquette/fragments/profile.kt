package fr.juniorreox.disquette.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import fr.juniorreox.disquette.Login_activity
import fr.juniorreox.disquette.MainActivity
import fr.juniorreox.disquette.repository.disqueRepository.singleton.User
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseUser
import fr.juniorreox.disquette.R
import fr.juniorreox.disquette.Register_activity

class profile : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState:
        Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val login = view.findViewById<Button>(R.id.login_profile)
        val register = view.findViewById<Button>(R.id.register_profile)

        login.setOnClickListener{
            val intent =  Intent(activity, Login_activity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }

        register.setOnClickListener{
            val intent =  Intent(activity, Register_activity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }




        return view
    }

}