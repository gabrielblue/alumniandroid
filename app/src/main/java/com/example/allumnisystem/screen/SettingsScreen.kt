//package com.example.allumnisystem.screen
//
//import android.content.Context
//import android.content.SharedPreferences
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatDelegate
//import androidx.fragment.app.Fragment
//import com.example.allumnisystem.R
//import com.google.android.material.button.MaterialButtonToggleGroup
//
//class SettingsFragment : Fragment() {
//    private var themeToggleGroup: MaterialButtonToggleGroup? = null
//    private var sharedPreferences: SharedPreferences? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)
//
//        themeToggleGroup = view.findViewById(R.id.themeToggleGroup)
//        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//
//        // Load saved theme mode and update UI accordingly
//        val savedThemeMode =
//            sharedPreferences.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//        updateUIWithSavedTheme(savedThemeMode)
//
//        // Set up the listener for theme selection
//        themeToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
//            if (isChecked) {
//                when (checkedId) {
//                    R.id.lightModeButton -> setThemeMode(AppCompatDelegate.MODE_NIGHT_NO)
//                    R.id.darkModeButton -> setThemeMode(AppCompatDelegate.MODE_NIGHT_YES)
//                    R.id.recommendedModeButton -> setThemeMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//                }
//            }
//        }
//
//        return view
//    }
//
//    private fun setThemeMode(mode: Int) {
//        AppCompatDelegate.setDefaultNightMode(mode)
//
//        // Save the theme mode to SharedPreferences
//        val editor = sharedPreferences!!.edit()
//        editor.putInt(KEY_THEME_MODE, mode)
//        editor.apply()
//    }
//
//    private fun updateUIWithSavedTheme(mode: Int) {
//        when (mode) {
//            AppCompatDelegate.MODE_NIGHT_NO -> themeToggleGroup.check(R.id.lightModeButton)
//            AppCompatDelegate.MODE_NIGHT_YES -> themeToggleGroup.check(R.id.darkModeButton)
//            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> themeToggleGroup.check(R.id.recommendedModeButton)
//        }
//    }
//
//    companion object {
//        private const val PREFS_NAME = "ThemePreferences"
//        private const val KEY_THEME_MODE = "themeMode"
//    }
//}