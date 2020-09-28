package com.neo.commonactionsusingintent

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.AlarmClock
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.gms.actions.NoteIntents
import com.pluralsight.commonintents.todo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_message_input.*
import kotlinx.android.synthetic.main.layout_user_feed.*
import java.io.File
import java.util.*


// const
const val REQUEST_IMAGE_CAPTURE = 2
const val REQUEST_SELECT_CONTACT = 1
const val REQUEST_IMAGE_GET = 3
const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private var currentPhotoPath = ""
    private var uriSavedImage: Uri? = null

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

        select_contact.setOnClickListener {
            selectContact()
        }

        select_image_file.setOnClickListener {
            selectImageFile()
        }

        play_file.setOnClickListener {
            playFile()
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
        // creation of content uri of the image file
        uriSavedImage = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile())
        Log.d(TAG, "onButtonAttachClick: Storing image in $uriSavedImage")

        // intent to start camera and ret the uri of the image taken
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage)
        }
        if(intent.resolveActivity(packageManager) != null){
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }



    // fun that gen the file name
    private fun createImageFile(): File {
        // create an image file name
        val timeStamp: String = java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        return File.createTempFile(
            "JPEG_${timeStamp}_",  // prefix
            ".jpg", // suffix
            storageDir  // directory
        ).apply {
            // saves file path for use for with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    private fun onButtonReminderClick() {
        // creates an intent to open apps that can set alarm and set alarm to details passed
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "write a post")
            putExtra(AlarmClock.EXTRA_HOUR, 14)
            putExtra(AlarmClock.EXTRA_MINUTES, 40)
            putExtra(
                AlarmClock.EXTRA_DAYS, arrayListOf(
                    Calendar.SUNDAY, Calendar.MONDAY,
                    Calendar.TUESDAY, Calendar.WEDNESDAY,
                    Calendar.THURSDAY, Calendar.FRIDAY,
                    Calendar.SATURDAY
                )
            )
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }

    }

    private fun selectContact() {
        // intent to select a contact from contact list and ret the contact phone number back
        val intent = Intent(Intent.ACTION_PICK).apply {
            // mime type of intent is for phone
            type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT)
        }
    }

    private fun selectImageFile() {
        // selects image file from phone
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"

            // allows to select multiple images
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        if(intent.resolveActivity(packageManager) != null){
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
    }

    private fun playFile(){
        // uri of audio file, which is online here
        val mp3 = Uri.parse("https://www.naijavibes.com/wp-content/uploads/2020/09/Davido-FEM.mp3")

        val intent = Intent(Intent.ACTION_VIEW).apply {
            type = "audio/mp3"
            data = mp3
        }
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == Activity.RESULT_OK) {
            val contactUri: Uri = data?.data ?: return

            // gets the phone number
            val projection: Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

            contentResolver.query(contactUri, projection, null, null, null).use {cursor ->
                // if cursor ret is valid, get the phone Number
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        val numberIndex = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val number = cursor?.getString(numberIndex)

                        // do something with the phone number of selected contact returned
                        Log.d(TAG, "onActivityResult: Data: $number")
                    }
                }
            }
        } else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            // gets the image bitmap from file Uri
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriSavedImage)

            imageViewAttachmentPreview.visibility = View.VISIBLE
            imageViewAttachmentPreview.setImageBitmap(bitmap)
        }else if(requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK){
            // used to get uri of images when we choose more than one image file
//            data?.clipData()

            val fullPhotoUri = data?.data ?: error("Missing data")
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fullPhotoUri)
            imageViewAttachmentPreview.visibility = View.VISIBLE
            imageViewAttachmentPreview.setImageBitmap(bitmap)

        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
