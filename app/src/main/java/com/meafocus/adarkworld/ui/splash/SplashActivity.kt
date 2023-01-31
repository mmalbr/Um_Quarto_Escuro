package com.meafocus.adarkworld.ui.splash

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import android.view.View
import infix.imrankst1221.codecanyon.R
import com.meafocus.adarkworld.ui.home.HomeActivity
import infix.imrankst1221.rocket.library.setting.ThemeBaseActivity
import infix.imrankst1221.rocket.library.utility.PreferenceUtils
import infix.imrankst1221.rocket.library.utility.Constants
import infix.imrankst1221.rocket.library.utility.UtilMethods
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : ThemeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mContext = this

        initView()

        gotoNextView()
    }

    private fun initView(){
        txe_splash_quote.text = PreferenceUtils.getStringValue(Constants.KEY_SPLASH_QUOTE, "")
        txt_splash_footer.text = PreferenceUtils.getStringValue(Constants.KEY_SPLASH_FOOTER, "")

        val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(ContextCompat.getColor(mContext, UtilMethods.getThemePrimaryColor()),
                        ContextCompat.getColor(mContext,UtilMethods.getThemePrimaryDarkColor())))
        gradientDrawable.cornerRadius = 0f
        view_background.background = gradientDrawable

        if (PreferenceUtils.getInstance().getBooleanValue(Constants.KEY_FULL_SCREEN_ACTIVE, false)){
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

    }

    private fun gotoNextView(){
        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }, 3000)
    }

}
