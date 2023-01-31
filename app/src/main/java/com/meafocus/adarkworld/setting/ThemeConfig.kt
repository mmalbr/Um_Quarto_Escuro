package com.meafocus.adarkworld.setting

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import infix.imrankst1221.codecanyon.R
import com.meafocus.adarkworld.ui.home.HomeActivity
import infix.imrankst1221.rocket.library.utility.UtilMethods
import kotlinx.android.synthetic.main.activity_home.*

class ThemeConfig(){
    lateinit var mContext: Context
    lateinit var mActivity: HomeActivity

    constructor(context: Context, activity: HomeActivity) : this() {
        this.mContext = context
        this.mActivity = activity
    }

    fun initThemeColor(){
        mActivity.layout_toolbar.background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryColor()),
                        ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryDarkColor())))

        mActivity.navigation_view.getHeaderView(0).background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryColor()),
                        ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryDarkColor())))

        // radius for button
        val buttonGradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryColor()),
                        ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryDarkColor())))

        buttonGradientDrawable.cornerRadius = 20f
        mActivity.btn_try_again.background = buttonGradientDrawable
        mActivity.btn_error_home.background = buttonGradientDrawable
        mActivity.btn_error_try_again.background = buttonGradientDrawable

        // set other view color
        mActivity.loader_library.setColor(ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryColor()))
        mActivity.img_no_internet.setColorFilter(ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryColor()))
        mActivity.txt_no_internet_title.setTextColor(ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryDarkColor()))
        mActivity.txt_no_internet_detail.setTextColor(ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryColor()))
        mActivity.btn_ad_show.setColorFilter(ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryColor()))

        mActivity.swap_view.setColorSchemeColors(ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryColor()),
                ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryDarkColor()))
    }

    fun initThemeStyle(){
        UtilMethods.setLoaderStyle( mActivity.loader_background,
                mActivity.loader_default,
                mActivity.loader_library)
        UtilMethods.setNavigationBarStyle(  mActivity.layout_toolbar,
                mActivity.txt_toolbar_title)
        val isNavigationSlider = UtilMethods.setNavigationBarIcon(
                mActivity.img_left_menu,
                mActivity.img_right_menu,
                mActivity.img_left_menu1,
                mActivity.img_right_menu1,
                mActivity.layout_toolbar,
                R.drawable.ic_menu,
                R.drawable.ic_reload,
                R.drawable.ic_share_toolbar,
                R.drawable.ic_home_toolbar ,
                R.drawable.ic_exit_toolbar,
                R.drawable.ic_arrow_left,
                R.drawable.ic_arrow_right
        )

        if (isNavigationSlider){
            mActivity.drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            mActivity.navigation_view.setNavigationItemSelectedListener(mActivity)
            val toggle = ActionBarDrawerToggle(mActivity, mActivity.drawer_layout, null, R.string.open_drawer, R.string.close_drawer)
            mActivity.drawer_layout.addDrawerListener(toggle)
            toggle.syncState()
        }else{
            mActivity.drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }
}
