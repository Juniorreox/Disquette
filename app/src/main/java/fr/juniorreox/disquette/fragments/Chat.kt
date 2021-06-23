package fr.juniorreox.disquette.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.juniorreox.disquette.R
import fr.juniorreox.disquette.adapter.chatAdapter
import fr.juniorreox.disquette.modele.messageModele
import fr.juniorreox.disquette.repository.disqueRepository.singleton.User
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseChat
import java.util.*

class Chat : Fragment() {
    private val adapter  = chatAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_chat, container, false)




        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view_chat)
        verticalRecyclerView.layoutManager = LinearLayoutManager(activity)

        val btnSend = view.findViewById<ImageView>(R.id.add_message)
        val txtMessage = view.findViewById<EditText>(R.id.enter_message)

        //repo.getChat(adapter)
        databaseChat.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.clear()
                //recolter la liste
                //le handler permet d'attendre 4 seconde le temps de charger la liste
                for (ds in snapshot.children) {
                    //construire un objet disque
                    val message = ds.getValue(messageModele::class.java)
                    //Log.d(ContentValues.TAG, "updateData value is: $disque")

                    //Verifier que le disque n'est pas null
                    if (message != null ) {
                        //ajouter le disque a notre liste
                        adapter.addMessage(message)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        verticalRecyclerView.adapter = adapter

        btnSend.setOnClickListener {
            if (txtMessage.text.isNotEmpty()) {
                val message = messageModele(
                    User.uid!!,
                    txtMessage.text.toString(),
                    Calendar.getInstance().timeInMillis
                )
                txtMessage.setText("")
                databaseChat.push().setValue(message)
            }


        }
            return view
    }


}

