package fr.juniorreox.disquette

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import fr.juniorreox.disquette.adapter.pagerAdapter
import fr.juniorreox.disquette.fragments.Chat
import fr.juniorreox.disquette.fragments.Home
import fr.juniorreox.disquette.fragments.profileNotConnected


class MainActivity : AppCompatActivity() {
    private lateinit var pagerView : ViewPager2
    private lateinit var tablayout : TabLayout
    private lateinit var adapter : pagerAdapter

    private lateinit var chat : ImageView
    private lateinit var user : ImageView
    private lateinit var home : ImageView
    private lateinit var ajouterDisquette : ImageView

    //a la creation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //cette activite gere la liste des disques c'est pour cela qu'elle utilise activity_main.xml
        setContentView(R.layout.activity_main)

        @Suppress("DEPRECATION") //supprime la barre des methodes "depreciees"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController

            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE


            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }


//Initialisation des imageview en lateinit
        chat = findViewById(R.id.chat_icon)
        user = findViewById(R.id.user_icon)
        home = findViewById(R.id.app_image)
        ajouterDisquette = findViewById(R.id.add_image)

        //gestion de de l'ajout avec le boutton d'ajout
        ajouterDisquette.setOnClickListener {
            //afficher popup
            discPopup(this).show()
        }



        //pager start
//Initialisation des variables en lateinit
        pagerView = findViewById(R.id.fragment_containner)
        tablayout = findViewById(R.id.tabview)
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                if(p0?.position ==0){
                    chat.setImageResource(R.drawable.mountain)
                }

                if(p0?.position ==2){
                    user.setImageResource(R.drawable.user)
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.position ==0){
                    showSystemUI()
                    chat.setImageResource(R.drawable.mountain_on)
                    ajouterDisquette.visibility = View.INVISIBLE

                    home.setOnClickListener{
                        pagerView.currentItem = 1
                    }

                    user.setOnClickListener{
                        pagerView.currentItem = 2
                    }
                }

                if(tab?.position ==1){
                    hideSystemUI()
                    ajouterDisquette.visibility = View.VISIBLE

                    chat.setOnClickListener{
                        pagerView.currentItem = 0
                    }

                    user.setOnClickListener{
                        pagerView.currentItem = 2
                    }
                }

                if(tab?.position ==2){
                    hideSystemUI()
                    user.setImageResource(R.drawable.usercolor)
                    ajouterDisquette.visibility = View.INVISIBLE

                    home.setOnClickListener{
                        pagerView.currentItem = 1
                    }

                    chat.setOnClickListener{
                        pagerView.currentItem = 0
                    }
                }


            }
        })
        tablayout.setSelectedTabIndicatorColor(Color.parseColor("#d0e30e"))//la couleur duchangement de frangment dans le tab
        adapter = pagerAdapter(supportFragmentManager, lifecycle)

        adapter.add { Chat() }//position 0
        adapter.add { Home(this) }//position 1
        adapter.add { profileNotConnected() }//position 2

        pagerView.adapter = adapter
        pagerView.currentItem = 1 // position du fragment de depart: affichage horizontale [0 ; 1 ; 2 ]



    }


    override fun onResume() {
        super.onResume()

        //NE PAS ENLEVER
        TabLayoutMediator(tablayout,pagerView
        ) { tab, position ->
            when (position) {
                0 -> { //chat
                    //modifie tab
                }//fin chat

                1 -> {//home
                    //modifie tab
                }//fin home

                2 -> { //user
                    //modifie tab
                }//fin user
            }
        }.attach()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus)
            hideSystemUI()

    }
//cache la barre de navigation et passe en full scree
    private fun hideSystemUI() {
        @Suppress("DEPRECATION") //supprime la barre des methodes "depreciees"
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        @Suppress("DEPRECATION") //supprime la barre des methodes "depreciees"
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }


    //gestion du button de sortie
    override fun onBackPressed() {
        val manager: FragmentManager = supportFragmentManager;

        if (manager.backStackEntryCount == 2) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();

        } else {
            finish();
            moveTaskToBack(true);
        }
    }
}