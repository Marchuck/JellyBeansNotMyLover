package test.api.packagee

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isDarkTheme()) {
            setTheme(R.style.Dark)
        }
        setContentView(R.layout.activity_main)

        imageView.setImageResource(R.drawable.ic_camera)
        textView.text = currentVersion()
        switchCompat.isChecked = isDarkTheme()
        switchCompat.setOnCheckedChangeListener { buttonView, isChecked ->
            setDarkThemeEnabled(isChecked)
            restartApp()
        }


        if ( isDarkTheme()) {
            textView.setBackgroundResource(R.drawable.bg_dark)
        } else {
            textView.setBackgroundResource(R.drawable.bg_light)
        }
    }

    private fun restartApp() {
        val baseContext = baseContext

        val mStartActivity = Intent(baseContext, MainActivity::class.java)
        val mPendingIntentId = 123456
        val mPendingIntent = PendingIntent.getActivity(
            baseContext, mPendingIntentId, mStartActivity,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val mgr = baseContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)
        System.exit(0)
    }

    private fun isDarkTheme(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean(javaClass.simpleName, false)
    }

    private fun setDarkThemeEnabled(enabled: Boolean): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(this)
            .edit()
            .putBoolean(javaClass.simpleName, enabled)
            .commit()
    }

    //Current Android version data
    private fun currentVersion(): String {
        val release = java.lang.Double.parseDouble(Build.VERSION.RELEASE.replace("(\\d+[.]\\d+)(.*)".toRegex(), "$1"))

        Log.d(javaClass.simpleName, "sdk int: ${Build.VERSION.SDK_INT}")

        var codeName = "Unsupported"//below Jelly bean OR above Oreo
        if (release >= 4.1 && release < 4.4)
            codeName = "Jelly Bean"
        else if (release < 5)
            codeName = "Kit Kat"
        else if (release < 6)
            codeName = "Lollipop"
        else if (release < 7)
            codeName = "Marshmallow"
        else if (release < 8)
            codeName = "Nougat"
        else if (release < 9) codeName = "Oreo"
        return codeName + " v" + release + ", API Level: " + Build.VERSION.SDK_INT
    }
}
