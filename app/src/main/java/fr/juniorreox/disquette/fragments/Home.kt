package fr.juniorreox.disquette.fragments

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import fr.juniorreox.disquette.*
import fr.juniorreox.disquette.adapter.disqueAdapter
import fr.juniorreox.disquette.adapter.itemDisqueDecoration
import fr.juniorreox.disquette.repository.disqueRepository
import fr.juniorreox.disquette.repository.disqueRepository.singleton.disqueList


class Home(
    private val context: MainActivity
) : Fragment() {
    private val adapter = disqueAdapter(disqueList.sortedByDescending { it.identifiant })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //recuperer le recycler view pour gerer la liste des disquettes
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        //verticalRecyclerView.adapter = disqueAdapter(context, disqueList) //utile pour l'utilisation de glide
        verticalRecyclerView.adapter = adapter
        verticalRecyclerView.layoutManager = LinearLayoutManager(activity)
        verticalRecyclerView.addItemDecoration(itemDisqueDecoration())



        //a revoir
        val refresh: SwipeRefreshLayout = view.findViewById(R.id.refresh_fragment_home)
        refresh.setOnRefreshListener {
            load(refresh.isRefreshing)

            Handler().postDelayed({
                //chercher une alternative au handler() au cas ou il y a un disfonctionnement
                refresh.isRefreshing = false
                verticalRecyclerView.adapter = adapter
                Log.d(ContentValues.TAG, "Refresh: nombre de disque dans la liste:${disqueList.size}")
            },2000)

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
