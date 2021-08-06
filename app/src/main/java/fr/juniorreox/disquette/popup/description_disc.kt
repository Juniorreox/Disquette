package fr.juniorreox.disquette.popup

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.juniorreox.disquette.R
import fr.juniorreox.disquette.adapter.disqueAdapter
import fr.juniorreox.disquette.modele.disqueModele
import fr.juniorreox.disquette.modele.userModele
import fr.juniorreox.disquette.repository.disqueRepository
import fr.juniorreox.disquette.repository.disqueRepository.singleton.adminContain
import fr.juniorreox.disquette.repository.disqueRepository.singleton.anUser


class description_disc(disqueAdapter: disqueAdapter, private val currentDisc: disqueModele)
    : Dialog(disqueAdapter.contextAdapter) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.description_disc)

        val profilepicture = findViewById<ImageView>(R.id.picture_desc)
        val name = findViewById<TextView>(R.id.userName_desc)
        val contain = findViewById<TextView>(R.id.containUser)

        contain.text =currentDisc.contenu


        adminContain.forEach {
            if(  it.id.toInt() == currentDisc.identifiant){
                    // Your Code
                it.idSender.let {
                    disqueRepository.singleton.databaseUser.child(it).addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            val n =  snapshot.getValue(userModele::class.java)
                            if (n != null) {
                                anUser = n// faire gaff
                            }


                        }

                        override fun onCancelled(error: DatabaseError) {
                            //TODO("Not yet implemented")
                        }
                    })
                }
                    Glide.with(context).load(Uri.parse(anUser.profile)).error(R.drawable.profile_image).into(profilepicture) //image de profile
                    name.text = anUser.userName


            }
        }





    }

}