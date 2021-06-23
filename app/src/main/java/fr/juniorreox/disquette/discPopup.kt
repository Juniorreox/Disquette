package fr.juniorreox.disquette

import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import fr.juniorreox.disquette.modele.discAdminModele
import fr.juniorreox.disquette.repository.disqueRepository
import fr.juniorreox.disquette.repository.disqueRepository.singleton.countAdmin

class discPopup(
    context: MainActivity)
    : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_disc_add)
        
        addDisc()

    }

     private fun addDisc() {
        val repo = disqueRepository()
        val addDisc = findViewById<ImageView>(R.id.add_disc)

         addDisc.setOnClickListener {
                val disc = findViewById<EditText>(R.id.disquettes).text.toString().trim { it <= ' ' }
                val newDisc = discAdminModele(disc,"0","0")
                Log.d(ContentValues.TAG, " The contain of the disc with the trim  is: $disc")
                if(disc != ""){
                    repo.updateDisqueAdmin(newDisc)
                    countAdmin+=1
                    dismiss()
            }
        }




    }
}