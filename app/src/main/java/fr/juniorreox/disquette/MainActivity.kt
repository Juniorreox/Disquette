package fr.juniorreox.disquette

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.juniorreox.disquette.adapter.pagerAdapter
import fr.juniorreox.disquette.fragments.Chat
import fr.juniorreox.disquette.fragments.Home
import fr.juniorreox.disquette.fragments.profile
import fr.juniorreox.disquette.fragments.profile_connected
import fr.juniorreox.disquette.repository.disqueRepository
import fr.juniorreox.disquette.repository.disqueRepository.singleton.User
import fr.juniorreox.disquette.repository.disqueRepository.singleton.databaseUser


class MainActivity : AppCompatActivity() {
    private lateinit var pagerView : ViewPager2
    private lateinit var tablayout : TabLayout
    private lateinit var adapter : pagerAdapter

    private lateinit var chat : ImageView
    private lateinit var user : ImageView
    private lateinit var home : ImageView
    private lateinit var cl: ConstraintLayout

    private val myactivity : MainActivity = this



    //a la creation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //cette activite gere la liste des disques c'est pour cela qu'elle utilise activity_main.xml
        setContentView(R.layout.activity_main)

        suprressBar()


        //Initialisation des variable en lateinit
        chat = findViewById(R.id.chat_icon)
        user = findViewById(R.id.user_icon)
        home = findViewById(R.id.app_image)
        cl= findViewById(R.id.ConstraintLayout_activity_main)
        pagerView = findViewById(R.id.fragment_containner)
        tablayout = findViewById(R.id.tabview)


        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                if(tab?.position ==0){
                    showSystemUI()
                    chat.setImageResource(R.drawable.mountain_on)
                    user.setOnClickListener{
                        pagerView.currentItem = 2

                    }

                    home.setOnClickListener{
                        pagerView.currentItem = 1

                    }
                }

                if(tab?.position ==1){
                    hideSystemUI()
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

                    home.setOnClickListener{
                        pagerView.currentItem = 1

                    }

                    chat.setOnClickListener{
                        pagerView.currentItem = 0

                    }
                }
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
                    user.setOnClickListener{
                        pagerView.currentItem = 2

                    }

                    home.setOnClickListener{
                        pagerView.currentItem = 1

                    }
                }

                if(tab?.position ==1){
                    hideSystemUI()
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

                    home.setOnClickListener{
                        pagerView.currentItem = 1

                    }

                    chat.setOnClickListener{
                        pagerView.currentItem = 0

                    }
                }
            }
        })
        tablayout.setSelectedTabIndicatorColor(Color.parseColor("#DBDEC5"))//la couleur duchangement de frangment dans le tab
        adapter = pagerAdapter(supportFragmentManager, lifecycle)

        adapter.add { Chat() }//position 0
        adapter.add { Home(myactivity) }//position 1
        adapter.add { profile() }//position 2

        val firebaseUserUid = User.currentUser!!.uid

        databaseUser.child(firebaseUserUid).child("hasSigned").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    val state = snapshot.getValue(Boolean::class.java)
                    if (state != null) {
                        if(state){
                            adapter.remove(2)
                            adapter.add { profile_connected(myactivity) }//position 2
                        }else{
                            adapter.remove(2)
                            adapter.add { profile() }//position 2
                        }
                    }


            }

            override fun onCancelled(error: DatabaseError) {
                adapter.remove(2)
                adapter.add { profile() }//position 2

            }
        })


        pagerView.adapter = adapter
        Handler(Looper.getMainLooper()).postDelayed({
            // Your Code
            pagerView.setCurrentItem(1, false) // position du fragment de depart: affichage horizontale [0 ; 1 ; 2 ]
        }, 100)




    }

    private fun suprressBar() {
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
    }


    override fun onResume() {
        super.onResume()

        val repo = disqueRepository()
        repo.theUser()

        //NE PAS ENLEVER
        TabLayoutMediator(tablayout,pagerView
        ) { tab, position ->
            when (position) {
                0 -> { //Home
                    //modifie tab
                }//fin Home

                1 -> {//User
                    //modifie tab
                }//fin User

                2 -> { //Chat
                    //modifie tab
                }//fin Chat
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