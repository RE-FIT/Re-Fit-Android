package com.example.refit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.refit.databinding.ActivityMainBinding
import com.example.refit.presentation.common.BaseActivity
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bnvMain.setupWithNavController(getNavController())
        handleNavigationBarVisibility()

        initBottomNavigationBackground()
    }

    private fun getNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment

        return navHostFragment.navController
    }

    private fun handleNavigationBarVisibility() {
        getNavController().addOnDestinationChangedListener { _, destination, _ ->
            binding.bnvMain.visibility =
                when (destination.id) {
                    R.id.nav_community, R.id.nav_my_page, R.id.nav_closet -> View.VISIBLE
                    else -> View.GONE
                }
        }
    }

    private fun initBottomNavigationBackground() {
        val radius = resources.getDimension(R.dimen.big_radius)
        val bottomNavBackground: MaterialShapeDrawable = binding.bnvMain.background as MaterialShapeDrawable
        bottomNavBackground.shapeAppearanceModel = bottomNavBackground.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .build()
    }
}