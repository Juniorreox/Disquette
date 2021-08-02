package fr.juniorreox.disquette.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import fr.juniorreox.disquette.*
import fr.juniorreox.disquette.repository.disqueRepository
import fr.juniorreox.disquette.repository.disqueRepository.singleton.thisUser

class profile_connected(
    private val context: MainActivity
): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState:
        Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile_connected, container, false)
        val profilepicture = view.findViewById<ImageView>(R.id.picture)
        val name = view.findViewById<TextView>(R.id.userName)
        val signout = view.findViewById<Button>(R.id.sign_out)

        //val me = thisUser.profile

        val image =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if(data == null || data.data == null) {}
                val selectedImage = data?.data
                profilepicture.setImageURI(selectedImage)

                val repo = disqueRepository()
                repo.uploadImage(selectedImage)
                //Log.w("TAG", "profile_connected 2 -> ThisUser.profile :  $me")

            }
        }

        profilepicture.setOnClickListener{
                val intent = Intent()
                intent.type = "image/"
                intent.action = Intent.ACTION_GET_CONTENT
                image.launch(Intent.createChooser(intent,"Select picture"))

        }

        //Log.w("TAG", "profile_connected -> ThisUser.profile :  $me")
        Glide.with(context).load(Uri.parse(thisUser.profile)).error(R.drawable.profile_image).into(profilepicture) //image de profile
        name.text = thisUser.userName // pseudo



        signout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            //ce qui suit doit changer le fragment afficher dans la partie profile de la main activite... on passe de prifile_connected a profile
            val intent =  Intent(activity, launching::class.java)
            activity?.startActivity(intent)
        }

        name.onRightDrawableClicked{
            Log.w("TAG", "profile_connected 3 -> name clicked")
            val popUp = namePopup(context, name)
            popUp.window?.setBackgroundDrawable(ActivityCompat.getDrawable(context, R.drawable.opacity)) // tres utile pour avoir la forme voulu du dialog
            popUp.show()
        }


        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    fun TextView.onRightDrawableClicked(onClicked: (view: TextView) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is TextView) {
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