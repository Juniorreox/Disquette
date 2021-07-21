package fr.juniorreox.disquette

import android.app.Dialog
import android.os.Bundle
import android.view.Window

class picturePopup(
context: MainActivity)
: Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_picture_add)


    }
}