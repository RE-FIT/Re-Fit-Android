package com.example.refit

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.refit.databinding.ActivityMainBinding
import com.example.refit.presentation.common.BaseActivity
import com.example.refit.presentation.common.WindowUtil.setStatusBarColor
import com.example.refit.presentation.mypage.MyFeedFragment
import com.example.refit.presentation.mypage.MyInfoFragment
import com.example.refit.presentation.mypage.ScrapFragment
import com.example.refit.presentation.mypage.SettingFragment
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
                    R.id.nav_closet -> {
                        setStatusBarColor(R.color.green1)
                        View.VISIBLE
                    }
                    R.id.nav_community, R.id.nav_my_page  -> {
                        setStatusBarColor(R.color.default_dark)
                        View.VISIBLE
                    }
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

    fun changeFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()

        when(index) {
            1 -> {
                transaction.replace(R.id.layout, MyInfoFragment())
                transaction.commitAllowingStateLoss()
            }
            2 -> {
                transaction.replace(R.id.layout, MyFeedFragment())
                transaction.commitAllowingStateLoss()
            }
            3 -> {
                transaction.replace(R.id.layout, ScrapFragment())
                transaction.commitAllowingStateLoss()
            }
            4 -> {
                transaction.replace(R.id.layout, SettingFragment())
                transaction.commitAllowingStateLoss()
            }
        }
    }
}