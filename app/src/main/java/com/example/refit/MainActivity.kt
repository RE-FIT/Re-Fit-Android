package com.example.refit

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.refit.databinding.ActivityMainBinding
import com.example.refit.presentation.common.BaseActivity
import com.example.refit.presentation.common.WindowUtil.setStatusBarColor
import com.example.refit.presentation.findidpassword.adapter.FragmentPageAdapter
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.tabs.TabLayout


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
                    R.id.nav_closet -> {
                        setStatusBarColor(R.color.green1)
                        View.VISIBLE
                    }

                    R.id.nav_community, R.id.nav_my_page, R.id.communityInfoFragment -> {
                        setStatusBarColor(R.color.default_dark)
                        View.VISIBLE
                    }

                    R.id.clothRegistrationFragment -> {
                        setStatusBarColor(R.color.default_dark)
                        View.GONE
                    }

                    else -> View.GONE
                }
        }
    }

    private fun initBottomNavigationBackground() {
        val radius = resources.getDimension(R.dimen.big_radius)
        val bottomNavBackground: MaterialShapeDrawable =
            binding.bnvMain.background as MaterialShapeDrawable
        bottomNavBackground.shapeAppearanceModel = bottomNavBackground.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .build()
    }
}