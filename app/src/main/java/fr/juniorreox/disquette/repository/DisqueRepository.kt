package fr.juniorreox.disquette.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import fr.juniorreox.disquette.adapter.chatAdapter
import fr.juniorreox.disquette.adapter.disqueAdapter
import fr.juniorreox.disquette.modele.discAdminModele
import fr.juniorreox.disquette.modele.disqueModele
import fr.juniorreox.disquette.repository.disqueRepository.singleton.Checked
import fr.juniorreox.disquette.repository.disqueRepository.singleton.User
import fr.juniorreox.disquette.repository.disqueRepository.singleton.countAdmin
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseNum
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseDisc
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseUser
import fr.juniorreox.disquette.repository.disqueRepository.singleton.disqueList
import fr.juniorreox.disquette.repository.disqueRepository.singleton.lastElement
import fr.juniorreox.disquette.repository.disqueRepository.singleton.number
import fr.juniorreox.disquette.modele.checkDisc
import fr.juniorreox.disquette.modele.userModele
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseChat
import java.util.*


class disqueRepository {


    object singleton {
        // se connecter a la reference disque
        //mettre l'url de connexion
        private val database = FirebaseDatabase.getInstance("https://disquette-b83fa-default-rtdb.europe-west1.firebasedatabase.app/")


        val databaseChat =
            database.getReference("Chat")
        // !!!!!!! attention ceci est le chemin absolu de la connexion a ta base de donner
        //acces a toutes les donnees
        val databaseNum =
            database.getReference("DiscNumber")

        //acces a l'arbre des disque
        val databaseDisc =
            database.getReference("Disques") //java

        //acces a l'arbre des utilisateurs
        val databaseUser =
            database.getReference("Users")

        //creer une liste qui va contenir nos disque
        var disqueList = arrayListOf<disqueModele>()

        //l'utilisateur actuel
        var User = Firebase.auth

        //liste qui permet de sauvegarder temporairement les disques verifier pendant la mise a jour de la liste utilisateur dans updatelist
        var Checked = arrayListOf<checkDisc>()

        //le nombre de disque total ( il y a le disque 0)
        var number :Int = 0

        //le nombre de disc dans admin
        var countAdmin = 0

        //l'identifiant du derniere element de la liste de disque utilisateur
        var lastElement: Int = 0

        //Le nom d'utilisateur
        lateinit var thisUser :userModele
    }



    fun getData() {
        //chargement des donnees de cette utilisateur
        User.currentUser?.uid?.let {
            databaseDisc.child(it).addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    // Failed to read value
                    Log.w(
                        TAG, "Failed to read value.", p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value == null) {
                        //!!!!!  GERER L'APPLICATION SANS DISC DE DEPART AVEC LE DISQUE0 CACHER DANS ADMIN !!!!!
                        // The child doesn't exist

                        if( countAdmin > 0) {
                            addListDisc()
                            getData()
                        }
                    }else{
                        updatelist()
                        //ici
                        disqueList.clear()
                        //recolter la liste
                        //le handler permet d'attendre 4 seconde le temps de charger la liste
                        for (ds in p0.children) {
                            //construire un objet disque
                            val disque = ds.getValue(disqueModele::class.java)
                            Log.d(TAG, "updateData value is: $disque")

                            //Verifier que le disque n'est pas null
                            if (disque != null ) {
                                //ajouter le disque a notre liste
                                disqueList.add(disque)
                            }
                        }
                    }

                }

            })
        }
        //Fin du chargement des donnees
    }

    //modifier lafonctiosuivante pour changer uniquement la liste des disques contenu danslechild admin sur firebase
    fun updateDisqueAdmin(contain: discAdminModele) {
        incDiscNumber()
        discNumber()
        val discid = number

        databaseDisc.child("admin").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChild("disque$discid")) {
                    updateDisqueAdmin(contain)// faire gaff recursion infini !!!!!!!!! !!!!!!!! !!!!!!!!
                    Log.d(
                        TAG,
                        "updateDisqueAdmin: here, the child exist and the value is: disque$discid"
                    )
                } else {
                    contain.id = discid.toString()
                    databaseDisc.child("admin").child("disque$discid").setValue(contain)
                    Log.d(
                        TAG,
                        "updateDisqueAdmin: here, the child does not exist and the value is: disque$discid"
                    )
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                //handle databaseError
            }
        })//fin de recuperation du contenu


    }

    private fun updateDisqueAdminLike(contain: String, disc: String) =
        databaseDisc.child("admin").child(disc).child("like_number").setValue(contain)

    private fun updateDisqueAdminDislike(contain: String, disc: String) =
        databaseDisc.child("admin").child(disc).child("dislike_number").setValue(contain)

    //recupere la valeur actuelle du nombre de disc
    private fun discNumber() {
        databaseNum.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val num =  ds.getValue(Int::class.java)
                    if (num != null) {
                        number = num // faire gaff
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    //incremente le nombre de disque sur la base de donnee
    private fun incDiscNumber() {
        discNumber()
        databaseNum.child("number").setValue(number+1)
    }

    //Inserer un nouveau disque en bdd
    fun insertDisc(disc: disqueModele) {


        disc.id?.let {

            User.currentUser?.uid?.let { it1 -> databaseDisc.child(it1).child(it).setValue(disc) }

        }
    }

    private fun insertuser(users: userModele) =
        users.uid?.let { databaseUser.child(it).setValue(users) }

    fun addUser() {
        val user = userModele(User.currentUser?.uid)
        insertuser(user)
        //recuperer le contenu des differents disques et les ajouter dans l'espace disque utilisitateur avec les valeur par defaut du disque
        Handler().postDelayed({
            addListDisc()
        }, 4000)

    }

    fun addListDisc() {
        //je recupere la liste de tout les contenu des disques ajouter dans le child admin sur firebase
        databaseDisc.child("admin").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //recolter la liste
                for (ds in p0.children) {
                    //construire un objet contenu
                    val contain = ds.getValue(discAdminModele::class.java)
                    Log.d(TAG, "addListDisc: This is the value: $contain")
                    val key = ds.key
                    Log.d(TAG, "addListDisc: The key: $key")
                    countAdmin += 1

                    //Verifier que le disque n'est pas null
                    if (contain != null && contain.id.toInt() != 0) {
                        //ajouter le disque a notre liste
                        val disc = disqueModele(
                            key,
                            contain.contenu,
                            contain.dislike_number,
                            false,
                            false,
                            contain.id.toInt(),
                            contain.like_number,
                            false
                        )
                        insertDisc(disc)
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                //handle databaseError
            }
        })//fin de recuperation du contenu

    }

    // Revoir code de cette fonction pour l'exces d'appel si s'en est call in getdata
    private fun updatelist() {


        //je recupere la liste des donnees utilisateurs
        User.currentUser?.uid?.let { uid ->
            databaseDisc.child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        Checked.clear()
                        //recolter la liste
                        //le handler permet d'attendre 4 seconde le temps de charger la liste
                        for (ds in p0.children) {
                            //construire un objet disque
                            val discUser = ds.getValue(disqueModele::class.java)
                            Log.d(TAG, "updatelist: user disc is: $discUser")
                            //Verifier que le disque n'est pas null
                            discUser?.identifiant?.let { it1 -> Checked.add(checkDisc(it1)) }
                        }
                        //Checked.sortedByDescending { it.idDisc }
                        Checked.sortBy { it.idDisc }
                        Log.d(TAG, "updateliste: La taille de Checked est : ${Checked.size} ")
                        lastElement = Checked.last().idDisc //recupere l'id du derniere element
                        Log.d(TAG, "updateliste: (1) le derniere element est : $lastElement ")
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        //handle databaseError
                    }
                })
        }//fin de recuperation du contenu utilisateur

        //je recupere
        databaseDisc.child("admin").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //recolter la liste
                for (ds0 in p0.children) {
                    //construire un objet contenu
                    val discAdmin = ds0.getValue(discAdminModele::class.java)
                    //Verifier que le disque n'est pas null
                    if (discAdmin != null) {
                        for (ident in Checked) {
                            if (discAdmin.id.toInt() == ident.idDisc){
                                ident.state = true
                            }
                            if(discAdmin.id.toInt() > Checked.size && discAdmin.id.toInt() > lastElement){
                                Log.d(TAG, "updateliste: (2) le derniere element est : $lastElement ")
                                val id = discAdmin.id
                                val disc = disqueModele(
                                    "disque$id",
                                    discAdmin.contenu,
                                    discAdmin.dislike_number,
                                    false,
                                    false,
                                    discAdmin.id.toInt(),
                                    discAdmin.like_number,
                                    false
                                )
                                insertDisc(disc)
                            }
                        }

                    }//fin test discAdmin!= null; correspond au disque admin
                }
                //supprimer les disques qui reste dans checked
                for (ident in Checked) {
                    if(!ident.state){
                        removeDiscUser(ident.idDisc)
                        Log.d(TAG, "Supprime le disque $ident de la liste ")
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //handle databaseError
            }
        })//fin de recuperation du contenu admin

    }

    private fun removeDiscUser(identifiant: Int)  {
        User.currentUser?.uid?.let(){
            databaseDisc.child(it).child("disque$identifiant").removeValue()
    }
        Log.d(TAG, "C'est laaaaa meeeeereeeeeeee nooooirrrrrrrrrreeeee")
}


    //une fonction qui incremente les like/dislike admin si elle contient true et decremente les like/dislike si elle contient false
    //le deuxieme argument:true c'estpour les like, false c'est pour les dislike
    fun likeDislike(increment : Boolean, like : Boolean, discId :String){
        //connexion a la base de donnee et acces aux donnees
        databaseDisc.child("admin").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                //recolter la liste
                for(ds in p0.children){
                    //construire un objet contenu
                    val contain = ds.getValue(discAdminModele::class.java)
                    Log.d(TAG, "likeDislike: Value is: $contain")
                    val key = ds.key
                    Log.d(TAG, "likeDislike: Value of the key is: $key")


                    //Verifier que le disque admin n'est pas null
                    if(contain != null){
                        if(key == discId)
                        {
                            if(increment)
                            {
                                //on incremente
                                if(like)
                                {
                                    //on incremente les like
                                    val count = contain.like_number.toInt() + 1
                                    updateDisqueAdminLike(count.toString(), key)

                                }else{
                                    //on incremente les dislike
                                    val count = contain.dislike_number.toInt() + 1
                                    updateDisqueAdminDislike(count.toString(), key)
                                }
                            }else{
                                //on decremente
                                if(like)
                                {
                                    //on decremente les like
                                    val count = contain.like_number.toInt() - 1
                                    updateDisqueAdminLike(count.toString(), key)

                                }else{
                                    //on decremente les dislike
                                    val count = contain.dislike_number.toInt() - 1
                                    updateDisqueAdminDislike(count.toString(), key)
                                }
                            }//fin
                        }


                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //handle databaseError
            }
        })




    }



}