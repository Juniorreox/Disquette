package fr.juniorreox.disquette.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import fr.juniorreox.disquette.*
import fr.juniorreox.disquette.repository.disqueRepository
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseUser
import fr.juniorreox.disquette.repository.disqueRepository.singleton.storageRef
import fr.juniorreox.disquette.repository.disqueRepository.singleton.thisUser
import fr.juniorreox.disquette.repository.disqueRepository.singleton.uri

class profile_connected: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState:
        Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile_connected, container, false)
        val profilepicture = view.findViewById<ImageView>(R.id.picture)
        val name = view.findViewById<TextView>(R.id.userName)
        val signout = view.findViewById<Button>(R.id.sign_out)
/*
        val image =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if(data == null || data.data == null) {}
                val selectedImage = data?.data
                //Log.w(ContentValues.TAG, "Image selectionnee : $selectedImage")
                profilepicture.setImageURI(selectedImage)

                val repo = disqueRepository()
                repo.uploadImage(selectedImage)

                if(uri != null)
                    thisUser.uid?.let { databaseUser.child(it).child("profile").setValue(uri.toString()) }
                else thisUser.uid?.let { databaseUser.child(it).child("profile").setValue("toto") }
            }
        }

        profilepicture.setOnClickListener{
                val intent = Intent()
                intent.type = "image/"
                intent.action = Intent.ACTION_GET_CONTENT
                image.launch(Intent.createChooser(intent,"Select picture"))

        }
        val storageReference = storageRef.child("profile_image.jpg")
        //charge l'image de profile a partir du lien dans user->uid->profile firebase
        if(thisUser.profile != "empty" || thisUser.profile != "toto")
            activity?.let { Glide.with(it).load(thisUser.profile?.toUri()).into(profilepicture) }
        else
*/

        name.text = thisUser.userName


        signout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            //ce qui suit doit changer le fragment afficher dans la partie profile de la main activite... on passe de prifile_connected a profile
            val intent =  Intent(activity, launching::class.java)
            activity?.startActivity(intent)
        }


        return view
    }
}