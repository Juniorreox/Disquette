package fr.juniorreox.disquette.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.juniorreox.disquette.*
import fr.juniorreox.disquette.modele.disqueModele
import fr.juniorreox.disquette.repository.disqueRepository


class disqueAdapter(
        //val context : MainActivity,//utile pour l'utilisation de glide
        private var disqueList : List<disqueModele>
) : RecyclerView.Adapter<disqueAdapter.ViewHolder>(){

    //Boite pour ranger tout les composants a controler
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //le disque
        val contenuDisque_contain: TextView? = view.findViewById(R.id.contain)
        val contenuDisque_like: TextView? = view.findViewById(R.id.view_like)
        val contenuDisque_dislike: TextView? = view.findViewById(R.id.view_dislike)
        val contenuDisque_like_state = view.findViewById<ImageView>(R.id.like)
        val contenuDisque_dislike_state = view.findViewById<ImageView>(R.id.dislike)
        val contenuDisque_favori = view.findViewById<ImageView>(R.id.nofavori)
        val contenuDisque_identifiant: TextView? = view.findViewById(R.id.view_disque_count)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //revoir peut-etre l'attribut false 
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_disque, parent, false)

        return ViewHolder(view)
    }
    // Le nombre de disque afficher dans la liste deroulante
    override fun getItemCount(): Int = disqueList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Recuperer les information du disque en fonction de la position
        val currentDisc = disqueList[position]

        //Recuperer le repository
        val repo = disqueRepository()

        //Utiliser glide pour acceder u ressource internet
        //Exemple d'utilisation avecle contenu: on remplace le texte par l'url du text cible eg: "http//:juniorreox/disque1/contenu"
        //ligne de code dans ce cas sera:
        //???? La ligne suivante ne fonctionne que pour les images
        //Glide.with(context).load(Uri.parse(currentDisc.contenu)).into(holder.contenuDisque)

        //mettre a jour l'identifiant du disque
        holder.contenuDisque_identifiant?.text = currentDisc.identifiant.toString()

        //mettre a jour le nombre de like du disque
        holder.contenuDisque_like?.text = currentDisc.like_number

        //mettre a jour le nombre de dislike du disque
        holder.contenuDisque_dislike?.text = currentDisc.dislike_number

        //mettre a jour le contenu du disque
        holder.contenuDisque_contain?.text = currentDisc.contenu

        if(currentDisc.like_state){
            holder.contenuDisque_like_state.setImageResource(R.drawable.liked)
        }else{
            holder.contenuDisque_like_state.setImageResource(R.drawable.like)
            }

        if(currentDisc.dislike_state){
            holder.contenuDisque_dislike_state.setImageResource(R.drawable.unliked)
        }else{
            holder.contenuDisque_dislike_state.setImageResource(R.drawable.dislike)
        }

        if(currentDisc.favori){
            holder.contenuDisque_favori.setImageResource(R.drawable.heartfavori)
        }else{
            holder.contenuDisque_favori.setImageResource(R.drawable.heartnotfavori)
        }

        /*  FIN DE L'AFFICHAGE AU LANCEMENT DE L'APPLICATION */

       /*   GESTION DES CLIQUES                              */

        holder.contenuDisque_like_state.setOnClickListener{
            //on modifie la valeur de l'item1 : bool
            currentDisc.like_state = !currentDisc.like_state
            if(currentDisc.like_state){
                //on incremente la valeur
                val like = currentDisc.like_number.toInt() + 1
                //on modifie l'image
                holder.contenuDisque_like_state.setImageResource(R.drawable.liked)
                // on modifie la valeur du texte
                currentDisc.like_number = like.toString()
                holder.contenuDisque_like?.text = currentDisc.like_number
                currentDisc.id?.let { it1 -> repo.likeDislike(true,true, it1) }//mise a jour admin sur la bdd
                if(currentDisc.dislike_state)
                {
                    //on decremente la valeur
                    val dislike = currentDisc.dislike_number.toInt() - 1
                    //on modifie l'image
                    holder.contenuDisque_dislike_state.setImageResource(R.drawable.dislike)
                    //modifie la valeur du texte
                    currentDisc.dislike_number = dislike.toString()
                    holder.contenuDisque_dislike?.text = currentDisc.dislike_number
                    currentDisc.id?.let { it1 -> repo.likeDislike(false,false, it1) }//mise a jour admin sur la bdd
                    //on modifie la valeur de l'item2 : bool, on la met a faux
                    currentDisc.dislike_state = !currentDisc.dislike_state

                }

            }else{
                //on decremente la valeur
                val like = currentDisc.like_number.toInt() - 1
                //on modifie l'image
                holder.contenuDisque_like_state.setImageResource(R.drawable.like)
                // on modifie la valeur du texte
                currentDisc.like_number = like.toString()
                holder.contenuDisque_like?.text = currentDisc.like_number
                currentDisc.id?.let { it1 -> repo.likeDislike(false,true, it1) }//mise a jour admin sur la bdd
            }
            //mise a jour de la base de donnee
             repo.insertDisc(currentDisc)
        }

        holder.contenuDisque_dislike_state.setOnClickListener{
            //on modifie la valeur de l'item2 : bool
            currentDisc.dislike_state = !currentDisc.dislike_state

            if(currentDisc.dislike_state){
                //on incremente la valeur
                val dislike = currentDisc.dislike_number.toInt() + 1
                //on modifie l'image
                holder.contenuDisque_dislike_state.setImageResource(R.drawable.unliked)
                // on modifie la valeur du texte
                currentDisc.dislike_number = dislike.toString()
                holder.contenuDisque_dislike?.text = currentDisc.dislike_number
                currentDisc.id?.let { it1 -> repo.likeDislike(true,false, it1) }//mise a jour admin sur la bdd
                if(currentDisc.like_state)
                {
                    //on decremente la valeur
                    val like = currentDisc.like_number.toInt() - 1
                    //on modifie l'image
                    holder.contenuDisque_like_state.setImageResource(R.drawable.like)
                    //modifie la valeur du texte
                    currentDisc.like_number = like.toString()
                    holder.contenuDisque_like?.text = currentDisc.like_number
                    currentDisc.id?.let { it1 -> repo.likeDislike(false,true, it1) }//mise a jour admin sur la bdd
                    //on modifie la valeur de l'item2 : bool, on la met a faux
                    currentDisc.like_state = !currentDisc.like_state

                }

            }else{
                //on decremente la valeur
                val dislike = currentDisc.dislike_number.toInt() - 1
                //on modifie l'image
                holder.contenuDisque_dislike_state.setImageResource(R.drawable.dislike)
                // on modifie la valeur du texte
                currentDisc.dislike_number = dislike.toString()
                holder.contenuDisque_dislike?.text = currentDisc.dislike_number
                currentDisc.id?.let { it1 -> repo.likeDislike(false,false, it1) } //mise a jour admin sur la bdd
            }
            //mise a jour de la base de donnee
             repo.insertDisc(currentDisc)
        }

        holder.contenuDisque_favori.setOnClickListener{
            currentDisc.favori = !currentDisc.favori
            if(currentDisc.favori){
                holder.contenuDisque_favori.setImageResource(R.drawable.heartfavori)
            }else{
                holder.contenuDisque_favori.setImageResource(R.drawable.heartnotfavori)
            }
            //mise a jour de la base de donnee
            repo.insertDisc(currentDisc)
        }



    }// fin onBindViewHolder

    fun setDiscListItems(list :List<disqueModele>){
        disqueList = list

    }

}