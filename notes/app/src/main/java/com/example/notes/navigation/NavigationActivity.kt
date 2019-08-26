package com.example.notes.navigation

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.notes.R
import com.example.notes.create.CreateActivity
import com.example.notes.notes.NoteListFragment
import com.example.notes.tasks.TaskListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationActivity : AppCompatActivity(), TaskListFragment.TouchActionDelegate,
    NoteListFragment.TouchActionDelegate {

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_task -> {
                loadFragment(TaskListFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notes -> {
                loadFragment(NoteListFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        loadFragment(TaskListFragment.newInstance())
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun gotoCreateActivity(fragment: String) {
        startActivity(Intent(applicationContext, CreateActivity::class.java).apply {
            putExtra(FRAGMENT_TYPE_KEY, fragment)
        })
    }

    override fun onAddButtonClicked(value: String) {
        gotoCreateActivity(value)
    }

    companion object {
        const val FRAGMENT_TYPE_KEY = "f_t_k"
        const val FRAGMENT_TYPE_NOTE = "f_t_n"
        const val FRAGMENT_TYPE_TASK = "f_t_t"
    }


}
