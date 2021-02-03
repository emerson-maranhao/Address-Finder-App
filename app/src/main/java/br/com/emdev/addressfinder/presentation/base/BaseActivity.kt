package br.com.emdev.addressfinder.presentation.base

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

open class BaseActivity : AppCompatActivity() {
    protected fun setupToolbar(toolbar: Toolbar, titleResId: Int, showToolbar: Boolean = false) {
        toolbar.title = getString(titleResId)
        setSupportActionBar(toolbar)
        if (showToolbar) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}