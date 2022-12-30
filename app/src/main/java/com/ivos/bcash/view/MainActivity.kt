package com.ivos.bcash.view

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.ivos.bcash.R
import com.ivos.bcash.databinding.ActivityMainBinding
import com.ivos.bcash.data.local.AppDatabase
import com.ivos.bcash.data.repo.ExpenseRepository
import com.ivos.bcash.util.viewModelFactory
import com.ivos.bcash.view.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val repo by lazy { ExpenseRepository(AppDatabase(this)) }
    private val viewModel: BaseViewModel by viewModels {
        viewModelFactory { BaseViewModel(this.application, repo) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_splash)
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            binding = ActivityMainBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)
            viewModel
            initViews()
        }

    }

    private fun initViews() {

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment?
            ?: return

        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNav,navController)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }





}