package com.neo.commonactionsusingintent

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import androidx.appcompat.app.AppCompatActivity
import com.pluralsight.commonintents.todo
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        buttonCalendar.setOnClickListener {
            onClickCalendar()
        }

        buttonMaps.setOnClickListener {
            onClickMaps()
        }

        buttonTaxi.setOnClickListener {
            onClickTaxi()
        }

        buttonSearch.setOnClickListener {
            omClickSearch()
        }

        buttonListen.setOnClickListener {
            onClickListen()
        }

        buttonWebsite.setOnClickListener {
            onClickWeb()
        }

        buttonCall.setOnClickListener {
            onClickCall()
        }

        buttonEmail.setOnClickListener {
            onClickEmail()
        }

        buttonContact.setOnClickListener {
            onClickContact()
        }
    }

    private fun onClickContact() {
        todo("Save as Contact")
    }

    private fun onClickEmail() {
        todo("Send Email")
    }

    private fun onClickCall() {
        todo("Call Friend")
    }

    private fun onClickWeb() {
        // intent to open a webpage
        val webPage = Uri.parse("http://livescores.com")
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }

    private fun onClickListen() {
        todo("Play Music")
    }

    private fun omClickSearch() {
        // intent for carrying out webSearch
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra(SearchManager.QUERY, "Elon Musk")
        }

        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }

    private fun onClickTaxi() {
        todo("Call a Taxi")
    }

    private fun onClickMaps() {
        val address = "182 N. Union Ave, Farmington, UT 84025"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("geo:0,0?q=$address")
        }

        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }

    private fun onClickCalendar() {
        // gets the needed date
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER)
        calendar.set(Calendar.DAY_OF_MONTH, 21)
        val dateLong = calendar.timeInMillis        // gets the current time when method ran

        // intent for event creation in calendar
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI               // specify that the action insert is for a Calendar event
            putExtra(CalendarContract.Events.TITLE, "Test birthday")
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, dateLong)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, dateLong)
            putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
            putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY")      // sets the repeat freq i.e yearly here
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }

    }
}
