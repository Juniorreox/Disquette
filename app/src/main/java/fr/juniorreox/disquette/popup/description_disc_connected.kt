package fr.juniorreox.disquette.popup

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import fr.juniorreox.disquette.R
import fr.juniorreox.disquette.adapter.disqueAdapter


class description_disc_connected(disqueAdapter: disqueAdapter)
    : Dialog(disqueAdapter.contextAdapter) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.description_disc_connected)



    }

}