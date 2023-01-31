package com.meafocus.adarkworld

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import infix.imrankst1221.codecanyon.R
import com.meafocus.adarkworld.ui.home.HomeActivity
import com.meafocus.adarkworld.ui.splash.SplashActivity
import infix.imrankst1221.rocket.library.setting.AppDataInstance
import infix.imrankst1221.rocket.library.setting.ConfigureRocketWeb
import infix.imrankst1221.rocket.library.utility.Constants
import infix.imrankst1221.rocket.library.utility.PreferenceUtils
import infix.imrankst1221.rocket.library.utility.UtilMethods
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG: String = "---MainActivity"
    private lateinit var mContext: Context
    private lateinit var configureRocketWeb: ConfigureRocketWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContext = this

        initSetup()

        readBundle(intent.extras)
    }

    private fun readBundle(extras: Bundle?) {
        if(extras != null){
            AppDataInstance.notificationUrl = extras.getString(Constants.KEY_NOTIFICATION_URL).orEmpty()
            AppDataInstance.notificationUrlOpenType = extras.getString(Constants.KEY_NOTIFICATION_OPEN_TYPE).orEmpty()

            UtilMethods.printLog(TAG, AppDataInstance.notificationUrl)
            UtilMethods.printLog(TAG,  AppDataInstance.notificationUrlOpenType)
        }else{
            UtilMethods.printLog(TAG, "Bundle is empty!!")
        }

        try {
            val data = this.intent.data
            if (data != null && data.isHierarchical) {
                AppDataInstance.deepLinkUrl = this.intent.dataString.toString()
                UtilMethods.printLog(TAG, "Deep link clicked "+ AppDataInstance.deepLinkUrl)
            }
        }catch (ex: java.lang.Exception){
            UtilMethods.printLog(TAG, "$ex.message")
        }
    }

    private fun initSetup(){
        /** To enable firebase*/
        //FirebaseApp.initializeApp(this)
        //FirebaseMessaging.getInstance().isAutoInitEnabled = true

        initRocketWeb()
    }

    private fun initRocketWeb(){
        configureRocketWeb  = ConfigureRocketWeb(mContext)
        configureRocketWeb.readConfigureData("rocket_web.io", AppDataInstance.appConfig)

        if (configureRocketWeb.isConfigEmpty()){
            showErrorMessage()
        }else{
            goNextActivity()
        }

        if(configureRocketWeb.configureData!!.themeColor == Constants.THEME_PRIMARY) {
            PreferenceUtils.getInstance().editIntegerValue(Constants.KEY_COLOR_PRIMARY,
                R.color.colorPrimary
            )
            PreferenceUtils.getInstance().editIntegerValue(Constants.KEY_COLOR_PRIMARY_DARK,
                R.color.colorPrimaryDark
            )
        }

        val isSelectedRtl = TextUtils.getLayoutDirectionFromLocale(Locale
                .getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL
        PreferenceUtils.getInstance().editBoolenValue(Constants.KEY_RTL_ACTIVE, isSelectedRtl)
    }

    private fun showErrorMessage(){
        //UtilMethods.showLongToast(mContext, getString(R.string.massage_error_io))
        layout_error.visibility = View.VISIBLE
    }

    private fun goNextActivity(){
        if(PreferenceUtils.getInstance().getStringValue(Constants.KEY_API_ERROR_MESSAGE, "")!!.isNotEmpty()){
            showErrorMessage()
            UtilMethods.showLongToast(mContext, PreferenceUtils.getInstance().getStringValue(Constants.KEY_API_ERROR_MESSAGE, "")!!)
        }else {
            val intent: Intent
            if (PreferenceUtils.getInstance().getBooleanValue(Constants.KEY_SPLASH_SCREEN_ACTIVE, true)) {
                intent = Intent(this, SplashActivity::class.java)
            } else {
                intent = Intent(this, HomeActivity::class.java)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

}
