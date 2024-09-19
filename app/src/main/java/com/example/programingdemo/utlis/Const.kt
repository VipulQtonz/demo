package com.example.programingdemo.utlis

import android.Manifest
import android.net.Uri

object Const {
    const val GENERAL_NOTIFICATION_CHANNEL_ID = "CHA1"
    const val GENERAL_NOTIFICATION_CHANNEL_NAME = "General Notification"
    const val GENERAL_NOTIFICATION_CHANNEL_DESCRIPTION =
        "This is a description of general notification"

    const val FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID = "CHA2"
    const val FOREGROUND_SERVICE_CHANNEL_NAME = "Foreground services"
    const val FOREGROUND_SERVICE_CHANNEL_DESCRIPTION =
        "This is a description of foreground services"

    const val SHARED_PREF_USER_DETAILS = "UserDetails"

    // for storing sharePreferences data
    const val FIRSTNAME = "FirstName"
    const val LASTNAME = "LastName"
    const val EMAIL = "Email"
    const val MOBILE_NUMBER = "MobileNumber"
    const val GENDER = "Gender"
    const val HOBBIES = "Hobbies"
    const val ADDRESS = "Address"
    const val CITY = "City"
    const val STATE = "State"
    const val IMAGE = "Image"
    const val IMAGE_URI = "ImageUri"
    const val IS_OPEN = "isOpen"

    // uri
    const val QTONZ_URI = "https://qtonz.com/"

    // for gender
    const val MALE = "Male"
    const val FEMALE = "Female"
    const val OTHER = "OTHER"


    // for service
    const val SERVICE_ACTION_STOP = "STOP_SERVICE"
    const val FOREGROUND_SERVICE_TITLE = "BackGround Music"
    const val FOREGROUND_SERVICE_DISCRIPTION = "if you want to stop music then click to stop button"


    // for notification action
    const val NOTIFICATION_ACTION_STOP = "stop"
    const val NOTIFICATION_ACTION_NEXT = "next"

    const val INTENT_CHANNEL_ID = 12


    //intent data
    const val SENDER_NAME = "SenderName"
    const val MESSAGE = "Message"

    //ailplane mode status
    const val AIR_PLANE_MODE = "state"
    const val BATTERY_LEVEL = "Battery Level"
    const val PERCENTAGE = "%"

    //action
    const val CALL_ENDED = "Call ended"
    const val CALL_IN_PROGRESS = "Call in progress"
    const val INCOMING_CALL = "Incoming call"


    //log tag
    const val RECEIVER_STATIC_BROADCAST = "ReceiverStaticBroadcast"


    //for sqlite
    const val DATABASE_NAME = "mydatabase.db"
    const val DATABASE_VERSION = 1

    const val TABLE_NAME = "mytable"
    const val TABLE_NAME_HASH = "mytable/#"
    const val COLUMN_ID = "_id"
    const val COLUMN_NAME = "name"

    const val MY_TABLE = 1
    const val MY_TABLE_ID = 2

    const val UPDATE = "Update"
    const val SAVE = "Save"

    //fragment lifecycle

    const val FRAGMENT_GREEN = "FragmentGreen"

    const val CAMERAX_APP = "CameraXApp"
    const val MESSAGE_COUNT_PLUS = "99+"


    //for camera
    const val REQUEST_CODE_PERMISSIONS = 10
    val REQUIRED_PERMISSIONS =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    //whats app  chat data
    const val CHAT_ITEM = "chatItem"

    //for permission
    const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 100
    const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 101

    const val MYA_PP_IMAGES = "MyAppImages"
    const val DATA = "data"


    const val EXAMPLE_IMAGE_NAME = "example_image.jpg"

    const val REQUEST_CODE_READ_CONTACTS = 100
    const val REQUEST_CODE_WRITE_CONTACTS = 2

    const val CONTACT_NAME = "CONTACT_NAME"
    const val CONTACT_ID = "CONTACT_ID"

    const val USER_DATABASE = "user_database"

    //for realtime database
    const val USER_DATA = "UserData"
    const val DATE_OF_BIRTH = "dateOfBirth"
    const val EMAIL_NEW = "email"
    const val ADDRESS_NEW = "address"
    const val NAME = "name"


    const val RECENT = "Recent"
    const val IMAGE_PATH = "imagePath"
    const val FOLDER_PATH = "folder_path"


    //room tables
    const val RECENT_PHOTOS = "recentPhotos"
    const val USERS = "users"


    const val AUTHORITY = "com.example.programingdemo.ContentProviderMy"
    val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/mytable")


    const val IMAGE_LIST = "image_list"
    const val IMAGE_POSITION = "image_position"


    //for write external storage
    const val REQUEST_CODE_WRITE_STORAGE = 1001
    const val REQUEST_CODE_MANAGE_STORAGE = 1002


    const val POSITION="position"


    // for firebase firestore
    const val USER_DATABASE_="userDatabase"


    //for firebase storage
    const val DELETE="Delete"
    const val CANCEL="Cancel"
    const val DOWNLOAD="Download"
    const val IMAGE_FOLDER_PATH="images/"
    const val SELECTION_IMAGE="image/*"

    const val JPG_EXTENSION=".jpg"

    const val ID="id"
    const val PLASH_SCREEN_TIME="PlashScreenTime"
    const val TAG="TAG"



}