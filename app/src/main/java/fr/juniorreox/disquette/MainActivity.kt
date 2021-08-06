package fr.juniorreox.disquette


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
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


class MainActivity : AppCompatActivity() {
    private lateinit var pagerView : ViewPager2
    private lateinit var adapter : pagerAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var tablayout: TabLayout


    private val myactivity : MainActivity = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)
        //Hook
        pagerView = findViewById(R.id.fragment_containner)
        adapter = pagerAdapter(supportFragmentManager, lifecycle)
        //bottomNavigationView = findViewById(R.id.bottom_nav)
        tablayout = findViewById(R.id.tab_layout)

        addFragmentsToAdapter()

        pagerView.adapter = adapter

        //setBottomNavView()


        isUsersigned() //internet



        setPager()

        tabView()
    }

    private fun tabView() {
        TabLayoutMediator(tablayout,pagerView
        ) { tab, position ->
            when (position) {
                0 -> { //Chat
                    //modifie tab
                    //tab.text = "Chat"
                    tab.contentDescription = "Chat"
                    tab.setIcon(R.drawable.mountain)

                    //use them
                    val badge = tab.orCreateBadge
                    badge.number = 10
                    /*
                    badge.setContentDescriptionNumberless(contentDescription)
                    badge.setContentDescriptionQuantityStringsResource(R.string.content_description)
                    badge.setContentDescriptionExceedsMaxBadgeNumberStringResource(R.string.content_description)

                     */
                }//fin Home

                1 -> {//Home
                    //modifie tab
                    //tab.text = "Acceuil"
                    tab.setIcon(R.drawable.menuhome)
                    tab.contentDescription = "Acceuil"
                }//fin User

                2 -> { //Profile
                    //modifie tab
                    //tab.text = "Profil"
                    tab.setIcon(R.drawable.user)
                    tab.contentDescription = "Profil"
                }//fin Chat
            }
        }.attach()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus)
            hideToolBr()
    }

    //cache la barre de navigation et passe en full scree
    private fun hideToolBr() {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        val uiOptions = window.decorView.systemUiVisibility
        var newUiOptions = uiOptions
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        val isImmersiveModeEnabled = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY == uiOptions

        // Navigation bar hiding:  Backwards compatible to ICS.
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        // Status bar hiding: Backwards compatible to Jellybean
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = newUiOptions
        //END_INCLUDE (set_ui_flags)
    }


    private fun addFragmentsToAdapter() {
        adapter.add { Chat() }//position 0
        adapter.add { Home(myactivity) }//position 1
        adapter.add { profile() }//position 2
    }

    private fun setBottomNavView() {
        //menu to pager
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.chat_menu->pager(0)
                R.id.home_menu->pager(1)
                R.id.profile_menu->pager(2)
            }
            true
        }
        bottomNavigationView.selectedItemId = R.id.home_menu


        //pagerView
/*
        if(pagerView.currentItem == 0){
            bottomNavigationView.selectedItemId = R.id.chat_menu
        }
        if(pagerView.currentItem == 1){
            bottomNavigationView.selectedItemId = R.id.home_menu
        }
        if(pagerView.currentItem == 2){
            bottomNavigationView.selectedItemId = R.id.profile_menu
        }

 */
    }

    private fun setPager() {
        Handler(Looper.getMainLooper()).postDelayed({
            // Your Code
            pager(1)
        }, 100)
    }

    private fun pager(pos: Int) {
        pagerView.setCurrentItem(pos, false) // position du fragment de depart: affichage horizontale [0 ; 1 ; 2 ]
    }

    private fun isUsersigned() {
        val firebaseUserUid = disqueRepository.singleton.User.currentUser!!.uid

        disqueRepository.singleton.databaseUser.child(firebaseUserUid).child("hasSigned").addValueEventListener(object :
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
    }

}