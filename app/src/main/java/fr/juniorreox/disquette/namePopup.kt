package fr.juniorreox.disquette

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseUser
import fr.juniorreox.disquette.repository.disqueRepository.singleton.thisUser

class namePopup(
    context: MainActivity,
   private val name: TextView
)
: Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_name)

        val namechanged = findViewById<EditText>(R.id.username)

        namechanged.onRightDrawableClicked {
            val username = namechanged.text.toString().trim { it <= ' ' }
            name.text = username
            thisUser.uid?.let { databaseUser.child(it).child("userName").setValue(username) }
            dismiss()

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