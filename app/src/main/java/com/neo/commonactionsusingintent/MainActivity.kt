package com.neo.commonactionsusingintent

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.actions.NoteIntents
import com.pluralsight.commonintents.todo
import kotlinx.android.synthetic.main.layout_message_input.*
import kotlinx.android.synthetic.main.layout_user_feed.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonReminder.setOnClickListener {
            onButtonReminderClick()
        }

        buttonAttach.setOnClickListener {
            onButtonAttachClick()
        }

        imageViewAvatar.setOnClickListener {
            openProfile()
        }

        textViewName.setOnClickListener {
            openProfile()
        }

        buttonSave.setOnClickListener {
            saveMessageAsNote()
        }
    }

    private fun saveMessageAsNote() {
        // note intent, of type plain text, "we just use text/* as a wildcard for any text"
        val intent = Intent(NoteIntents.ACTION_CREATE_NOTE).apply {
            putExtra(NoteIntents.EXTRA_NAME, "Message Subject")
            putExtra(NoteIntents.EXTRA_TEXT, "Message text")
            type = "text/plain"
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }

    }

    private fun openProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun onButtonAttachClick() {
        todo("Attach Picture")
    }

    private fun onButtonReminderClick() {
        // creates an intent to open apps that can set alarm and set alarm to details passed
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "write a post")
            putExtra(AlarmClock.EXTRA_HOUR, 14)
            putExtra(AlarmClock.EXTRA_MINUTES, 40)
            putExtra(AlarmClock.EXTRA_DAYS, arrayListOf(
                Calendar.SUNDAY, Calendar.MONDAY,
                Calendar.TUESDAY, Calendar.WEDNESDAY,
                Calendar.THURSDAY, Calendar.FRIDAY,
                Calendar.SATURDAY
            ))
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }

    }
}
