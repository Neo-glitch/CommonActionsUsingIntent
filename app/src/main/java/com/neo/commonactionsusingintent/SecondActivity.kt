package com.neo.commonactionsusingintent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.actions.NoteIntents

class SecondActivity : AppCompatActivity() {
    private val TAG: String = "SecondActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val action = intent?.action
        val type = intent?.type

        if(action == NoteIntents.ACTION_CREATE_NOTE && type == "text/plain"){
            Log.d(TAG, "onCreate: text is: " + intent?.getStringExtra(NoteIntents.EXTRA_TEXT))
        }
    }
}