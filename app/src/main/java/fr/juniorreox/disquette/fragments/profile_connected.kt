package fr.juniorreox.disquette.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import fr.juniorreox.disquette.*
import fr.juniorreox.disquette.repository.disqueRepository.singleton.thisUser

class profile_connected: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState:
        Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_connected, container, false)
        val profile_picture = view.findViewById<ImageView>(R.id.picture)
        val name = view.findViewById<TextView>(R.id.userName)
        val sign_out = view.findViewById<Button>(R.id.sign_out)

        profile_picture.setOnClickListener{
            picturePopup(activity as MainActivity).show()
        }

        name.setText(thisUser.userName)


        sign_out.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            //ce qui suit doit changer le fragment afficher dans la partie profile de la main activite... on passe de prifile_connected a profile
            val intent =  Intent(activity, launching::class.java)
            activity?.startActivity(intent)
        }


        return view
    }
}