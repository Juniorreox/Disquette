package fr.juniorreox.disquette.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.juniorreox.disquette.*
import fr.juniorreox.disquette.adapter.disqueAdapter
import fr.juniorreox.disquette.adapter.itemDisqueDecoration
import fr.juniorreox.disquette.modele.disqueModele
import fr.juniorreox.disquette.repository.disqueRepository
import fr.juniorreox.disquette.repository.disqueRepository.singleton.User
import fr.juniorreox.disquette.repository.disqueRepository.singleton.countAdmin
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseDisc
import fr.juniorreox.disquette.repository.disqueRepository.singleton.disqueList


class Home(
    private val context: MainActivity
) : Fragment() {
    private val adapter = disqueAdapter()
    private lateinit var ajouterDisquette : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val repo = disqueRepository()

        ajouterDisquette = view.findViewById<ImageView>(R.id.add_image) //ajouter disque

        //gestion de de l'ajout avec le boutton d'ajout
         ajouterDisquette.setOnClickListener {
             //afficher popup
             discPopup(context).show()
         }

        //recuperer le recycler view pour gerer la liste des disquettes
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        //verticalRecyclerView.adapter = disqueAdapter(context, disqueList) //utile pour l'utilisation de glide
        verticalRecyclerView.setHasFixedSize(true)

        verticalRecyclerView.layoutManager = LinearLayoutManager(activity) // important
        verticalRecyclerView.addItemDecoration(itemDisqueDecoration())

        User.currentUser?.uid?.let {
            databaseDisc.child(it).addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    // Failed to read value
                    Log.w(ContentValues.TAG, "Failed to read value.", p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value == null) {
                        //!!!!!  GERER L'APPLICATION SANS DISC DE DEPART AVEC LE DISQUE0 CACHER DANS ADMIN !!!!!
                        // The child doesn't exist

                        if( countAdmin > 0) {
                            repo.addListDisc()
                            //repo.getData()
                        }
                    }else{
                        //repo.updatelist()
                        //ici
                        adapter.clear()
                        //recolter la liste
                        for (ds in p0.children) {
                            //construire un objet disque
                            val disque = ds.getValue(disqueModele::class.java)
                            Log.d(ContentValues.TAG, "updateData value is: $disque")

                            //Verifier que le disque n'est pas null
                            if (disque != null ) {
                                //ajouter le disque a notre liste
                                adapter.addDisque(disque)
                            }
                        }

                    }

                }

            })
        }
        adapter.sort()
        verticalRecyclerView.adapter = adapter // ?



        //a revoir
        val refresh: SwipeRefreshLayout = view.findViewById(R.id.refresh_fragment_home)
        refresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                // Your Code
                refresh.isRefreshing = false
                verticalRecyclerView.adapter = adapter
                Log.d(ContentValues.TAG, "Refresh: nombre de disque dans la liste:${disqueList.size}")
            }, 2000)

        }


        return view
    }



    private fun load(isRefreshed : Boolean){
        if(isRefreshed ){
            val repo= disqueRepository()
            repo.getData()

        }
    }



}
