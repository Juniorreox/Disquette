package fr.juniorreox.disquette

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import fr.juniorreox.disquette.adapter.pagerAdapter
import fr.juniorreox.disquette.fragments.Chat
import fr.juniorreox.disquette.fragments.Home
import fr.juniorreox.disquette.fragments.profileNotConnected

class testActivity : AppCompatActivity() {
    private lateinit var pagerView : ViewPager2
    private lateinit var tablayout : TabLayout
    private lateinit var adapter : pagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        pagerView = findViewById(R.id.viewpager)
        tablayout = findViewById(R.id.tablayout)
        tablayout.setSelectedTabIndicatorColor(Color.parseColor("#d0e30e"))
        adapter = pagerAdapter(supportFragmentManager, lifecycle)

        adapter.add { Chat() }//position 0
        //adapter.add { Home(this) }//position 1
        adapter.add { profileNotConnected() }//position 2

        pagerView.adapter = adapter
        pagerView.currentItem = 1 // position du fragment de depart: affichage horizontale [0 ; 1 ; 2 ]

        TabLayoutMediator(tablayout,pagerView
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Chat"
                    //tab.view =  (chat as TabLayout.TabView)
                }

                1 -> {
                    tab.text = "Home"
                }

                2 -> {
                    tab.text = "Profile"
                }
            }
        }.attach()


    }

}