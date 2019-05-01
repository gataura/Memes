package com.memes.`fun`

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.memes.`fun`.fragments.FeedFragment
import com.memes.`fun`.fragments.SavedFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragmentFeed = FeedFragment()
    private val fragmentSaved = SavedFragment()
    private val fm = supportFragmentManager
    var active: Fragment = fragmentFeed




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fm.beginTransaction().add(R.id.main_fragment, fragmentSaved, "2").hide(fragmentSaved).commit()
        fm.beginTransaction().add(R.id.main_fragment, fragmentFeed, "1").commit()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_feed -> {
                fm.beginTransaction().hide(active).show(fragmentFeed).commit()
                active = fragmentFeed

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_saved -> {
                fm.beginTransaction().hide(active).detach(fragmentSaved).attach(fragmentSaved).show(fragmentSaved).commit()
                active = fragmentSaved


                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}
