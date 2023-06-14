package com.github.search.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.search.R
import com.github.search.ui.fragment.SearchFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, SearchFragment())
                .commit()
        }
    }
}