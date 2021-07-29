package fr.juniorreox.disquette

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.common.internal.Constants

class picturePopup(
context: MainActivity)
: Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_picture_add)

        val pickUpImageButton = findViewById<Button>(R.id.pickUpImage)

        //val image = activi

        pickUpImageButton.setOnClickListener {
            val intent = Intent()
            intent.type = "image/"
            intent.action = Intent.ACTION_GET_CONTENT

        }


    }



}