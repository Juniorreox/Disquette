package fr.juniorreox.disquette

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.Window
import android.widget.EditText
import fr.juniorreox.disquette.modele.discAdminModele
import fr.juniorreox.disquette.repository.disqueRepository
import fr.juniorreox.disquette.repository.disqueRepository.singleton.countAdmin

class discPopup(
    context: MainActivity)
    : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_disc_add2)
        
        addDisc()

    }

     private fun addDisc() {
        val repo = disqueRepository()
        val add = findViewById<EditText>(R.id.disquettes)

         add.onRightDrawableClicked {
             if (add.text.isNotEmpty()) {
                 val disc = add.text.toString().trim { it <= ' ' }
                 val newDisc = discAdminModele(disc, "0", "0")
                 Log.d(ContentValues.TAG, " The contain of the disc with the trim  is: $disc")
                 repo.updateDisqueAdmin(newDisc)
                 countAdmin += 1
                 dismiss()

             }
         }




    }

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