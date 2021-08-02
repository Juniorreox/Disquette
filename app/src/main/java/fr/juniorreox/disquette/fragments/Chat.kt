package fr.juniorreox.disquette.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.juniorreox.disquette.R
import fr.juniorreox.disquette.adapter.chatAdapter
import fr.juniorreox.disquette.modele.messageModele
import fr.juniorreox.disquette.modele.userModele
import fr.juniorreox.disquette.repository.disqueRepository
import fr.juniorreox.disquette.repository.disqueRepository.singleton.User
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseChat
import fr.juniorreox.disquette.repository.disqueRepository.singleton.thisUser
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
        //ligne pour commencer le chat par le bas du recycler
        val llm = LinearLayoutManager(activity)
        llm.stackFromEnd = true     // items gravity sticks to bottom
        llm.reverseLayout = false   // item list sorting (new messages start from the bottom)
        verticalRecyclerView.layoutManager = llm


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
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }

        })

        verticalRecyclerView.adapter = adapter

        txtMessage.onRightDrawableClicked {
            /*
            if(/* pas connecte*/){
                //afficher un message vous devez etre connecter
            }else{
                //les lignes qui suivent
            }*/
            if (txtMessage.text.isNotEmpty()) {
                val message = messageModele(
                    User.uid!!,
                    txtMessage.text.toString(),
                    Calendar.getInstance().timeInMillis,
                    thisUser.userName
                )
                txtMessage.setText("")
                databaseChat.push().setValue(message)
            }


        }


            return view
    }

    // A revoir, cette ligne n'est pas ok pourr les personnes aveugles
    @SuppressLint("ClickableViewAccessibility")
    fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }

}

