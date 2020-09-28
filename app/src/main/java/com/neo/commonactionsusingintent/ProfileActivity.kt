package com.neo.commonactionsusingintent

import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.actions.ReserveIntents
import com.pluralsight.commonintents.todo
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*
import java.util.jar.Manifest

const val MY_PERMISSION_REQUEST_CALL = 2

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
        val intent = Intent(Intent.ACTION_INSERT).apply {
            type = ContactsContract.Contacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.NAME, "neo")
            putExtra(ContactsContract.Intents.Insert.EMAIL, "goldenboy@gmail.com")
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun onClickEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("goldenboy@gmail.com"))
        }
    }

    private fun onClickCall() {
//        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
//        != PackageManager.PERMISSION_GRANTED){
//            // if permission not granted request permission else perform call
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(android.Manifest.permission.CALL_PHONE),
//                MY_PERMISSION_REQUEST_CALL
//            )
//        } else{
//            performCall()
//        }

        // intent to dial number
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:+234045656565")
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }


    // fun callback when permission is granted or refused after perm dialog pops up
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_REQUEST_CALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    performCall()
                }
            }
        }
    }

    private fun performCall() {
        // intent to make call
    }

    private fun onClickWeb() {
        // intent to open a webpage
        val webPage = Uri.parse("http://livescores.com")
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun onClickListen() {
        // search for an artiste and song
        val artist = "Roddy Rich"
        val song = "The Box"

        val intent = Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH).apply {
            // media focus for searching for an artist
//            putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE)
            putExtra(MediaStore.EXTRA_MEDIA_FOCUS, "vnd.android.cursor.item/audio")
            putExtra(MediaStore.EXTRA_MEDIA_ARTIST, artist)
            putExtra(MediaStore.EXTRA_MEDIA_TITLE, song)
            putExtra(SearchManager.QUERY, "$artist $song")       // for query
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun omClickSearch() {
        // intent for carrying out webSearch
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra(SearchManager.QUERY, "Elon Musk")
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun onClickTaxi() {
        // intent to start taxi reservation
        val intent = Intent(ReserveIntents.ACTION_RESERVE_TAXI_RESERVATION)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun onClickMaps() {
        val address = "182 N. Union Ave, Farmington, UT 84025"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("geo:0,0?q=$address")
        }

        if (intent.resolveActivity(packageManager) != null) {
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
            data =
                CalendarContract.Events.CONTENT_URI               // specify that the action insert is for a Calendar event
            putExtra(CalendarContract.Events.TITLE, "Test birthday")
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, dateLong)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, dateLong)
            putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
            putExtra(
                CalendarContract.Events.RRULE,
                "FREQ=YEARLY"
            )      // sets the repeat freq i.e yearly here
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }

    }
}
