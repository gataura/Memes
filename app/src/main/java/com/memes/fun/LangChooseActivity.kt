package com.memes.`fun`

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.memes.`fun`.helper.find
import kotlinx.android.synthetic.main.activity_lang_choose.*

class LangChooseActivity : AppCompatActivity() {

    private lateinit var spanishButton: Button
    private lateinit var portuButton: Button
    private lateinit var indianButton: Button
    private lateinit var indonesianButton: Button
    private lateinit var engButton: Button 

    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lang_choose)

        spanishButton = find(R.id.spanish_button)
        portuButton = find(R.id.portu_button)
        indianButton = find(R.id.indian_button)
        indonesianButton = find(R.id.indonesian_button)
        engButton = find(R.id.eng_button)

        prefs = getSharedPreferences("com.memes.`fun`", Context.MODE_PRIVATE)

        spanishButton.setOnClickListener {
            prefs.edit().putString("language", "/memexico").apply()
            restartApp()
        }

        portuButton.setOnClickListener {
            prefs.edit().putString("language", "/PORTUGALCARALHO").apply()
            restartApp()
        }

        indianButton.setOnClickListener {
            prefs.edit().putString("language", "/IndianDankMemes").apply()
            restartApp()
        }

        indonesianButton.setOnClickListener {
            prefs.edit().putString("language", "/indonesia").apply()
            restartApp()
        }

        engButton.setOnClickListener {
            prefs.edit().putString("language", "").apply()
            restartApp()
        }

        ruButton.setOnClickListener {
            prefs.edit().putString("language", "/ANormalDayInRussia").apply()
            restartApp()
        }
    }

    fun restartApp() {
        val mStartActivity = Intent(this, MainActivity::class.java)
        startActivity(mStartActivity)
        finishAffinity()
    }
}
