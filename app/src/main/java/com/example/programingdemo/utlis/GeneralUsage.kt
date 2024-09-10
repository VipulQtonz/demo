package com.example.programingdemo.utlis

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.example.programingdemo.R
import com.example.programingdemo.data.CallLogItem
import com.example.programingdemo.data.ChatItem
import com.example.programingdemo.data.StatusItem


@Suppress("DEPRECATION")
class GeneralUsage(private val context: Context) {
    fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    companion object {
        @SuppressLint("ObsoleteSdkInt")
        fun checkVersion(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        }
    }

    object MyContract {
        const val AUTHORITY = "com.example.programingdemo.ContentProviderMy"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/mytable")

        object MyTable {
            const val TABLE_NAME = "mytable"
            const val _ID = "_id"
            const val COLUMN_NAME = "name"
        }
    }

    object DataProvider {
        fun getCallLogItems(): List<CallLogItem> {
            return listOf(
                CallLogItem(
                    profileImageResource = R.drawable.image_one,
                    displayName = "Vipul Jadav",
                    callDetails = "Today 11:30 AM",
                    callTypeActionIconResource = R.drawable.arrow_left_down_svg,
                    callTypeIconResource = R.drawable.call_svg
                ),
                CallLogItem(
                    profileImageResource = R.drawable.image_two,
                    displayName = "Ravi Patel",
                    callDetails = "Today 10:15 AM",
                    callTypeActionIconResource = R.drawable.arrow_left_down_svg,
                    callTypeIconResource = R.drawable.video_svg
                ),
                CallLogItem(
                    profileImageResource = R.drawable.image_three,
                    displayName = "Neha Shah",
                    callDetails = "Today 2:45 PM",
                    callTypeActionIconResource = R.drawable.arrow_right_up_svg,
                    callTypeIconResource = R.drawable.call_svg
                ),
                CallLogItem(
                    profileImageResource = R.drawable.image_four,
                    displayName = "Rahul Mehta",
                    callDetails = "Today 9:00 AM",
                    callTypeActionIconResource = R.drawable.arrow_right_up_svg,
                    callTypeIconResource = R.drawable.video_svg
                ),
                CallLogItem(
                    profileImageResource = R.drawable.image_five,
                    displayName = "Pooja Verma",
                    callDetails = "30 minutes ago",
                    callTypeActionIconResource = R.drawable.arrow_left_down_svg,
                    callTypeIconResource = R.drawable.call_svg
                ),
                CallLogItem(
                    profileImageResource = R.drawable.image_six,
                    displayName = "Amit Kumar",
                    callDetails = "3 days ago 6:20 PM",
                    callTypeActionIconResource = R.drawable.arrow_right_up_svg,
                    callTypeIconResource = R.drawable.video_svg
                ),
                CallLogItem(
                    profileImageResource = R.drawable.image_seven,
                    displayName = "Sneha Singh",
                    callDetails = "Today 8:15 AM",
                    callTypeActionIconResource = R.drawable.arrow_left_down_svg,
                    callTypeIconResource = R.drawable.call_svg
                ),
                CallLogItem(
                    profileImageResource = R.drawable.image_eight,
                    displayName = "Rohan Gupta",
                    callDetails = "Yesterday 7:30 PM",
                    callTypeActionIconResource = R.drawable.arrow_right_up_svg,
                    callTypeIconResource = R.drawable.video_svg
                ),
                CallLogItem(
                    profileImageResource = R.drawable.image_nine,
                    displayName = "Anjali Joshi",
                    callDetails = "4 days ago 1:15 PM",
                    callTypeActionIconResource = R.drawable.arrow_left_down_svg,
                    callTypeIconResource = R.drawable.call_svg
                ),
                CallLogItem(
                    profileImageResource = R.drawable.image_ten,
                    displayName = "Karan Patel",
                    callDetails = "Today 10:45 AM",
                    callTypeActionIconResource = R.drawable.arrow_right_up_svg,
                    callTypeIconResource = R.drawable.video_svg
                )
            )
        }

        fun getStatusItems(): List<StatusItem> {
            return listOf(
                StatusItem(
                    profileImage = R.drawable.image_one,
                    displayName = "Pooja Sharma",
                    uploadedTime = "5 minutes ago"
                ),
                StatusItem(
                    profileImage = R.drawable.image_two,
                    displayName = "Raj Mehta",
                    uploadedTime = "10 minutes ago"
                ),
                StatusItem(
                    profileImage = R.drawable.image_three,
                    displayName = "Akash Desai",
                    uploadedTime = "15 minutes ago"
                ),
                StatusItem(
                    profileImage = R.drawable.image_four,
                    displayName = "Kunal Thakkar",
                    uploadedTime = "25 minutes ago"
                ),
                StatusItem(
                    profileImage = R.drawable.image_five,
                    displayName = "Dhara Shah",
                    uploadedTime = "30 minutes ago"
                ),
                StatusItem(
                    profileImage = R.drawable.image_six,
                    displayName = "Ankit Patel",
                    uploadedTime = "35 minutes ago"
                ),
                StatusItem(
                    profileImage = R.drawable.image_seven,
                    displayName = "Hiren Patel",
                    uploadedTime = "45 minutes ago"
                ),
                StatusItem(
                    profileImage = R.drawable.image_eight,
                    displayName = "Sneha Shah",
                    uploadedTime = "50 minutes ago"
                ),
                StatusItem(
                    profileImage = R.drawable.image_nine,
                    displayName = "Kruti Joshi",
                    uploadedTime = "55 minutes ago"
                ),
                StatusItem(
                    profileImage = R.drawable.image_ten,
                    displayName = "Vipul Jadav",
                    uploadedTime = "60 minutes ago"
                )
            )
        }

        fun getChatItems(): List<ChatItem> {
            return listOf(
                ChatItem(
                    profileImageResId = R.drawable.image_one,
                    displayName = "Vipul Jadav",
                    message = "Hey, how are you?",
                    messageTime = "09:15",
                    messageCount = 0,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_two,
                    displayName = "Ankit Patel",
                    message = "Let's catch up later.",
                    messageTime = "09:45",
                    messageCount = 0,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_three,
                    displayName = "Sneha Shah",
                    message = "Got the documents, thanks!",
                    messageTime = "10:05",
                    messageCount = 0,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_four,
                    displayName = "Raj Mehta",
                    message = "Meeting at 2 PM?",
                    messageTime = "11:30",
                    messageCount = 0,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_five,
                    displayName = "Pooja Sharma",
                    message = "Happy Birthday!",
                    messageTime = "12:50",
                    messageCount = 60,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_six,
                    displayName = "Akash Desai",
                    message = "Check the new update.",
                    messageTime = "13:20",
                    messageCount = 30,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_seven,
                    displayName = "Kruti Joshi",
                    message = "Can we reschedule the call?",
                    messageTime = "14:40",
                    messageCount = 8,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_eight,
                    displayName = "Hiren Patel",
                    message = "Good morning!",
                    messageTime = "15:05",
                    messageCount = 9,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_nine,
                    displayName = "Dhara Shah",
                    message = "See you soon.",
                    messageTime = "16:30",
                    messageCount = 80,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_ten,
                    displayName = "Kunal Thakkar",
                    message = "Thanks for the help!",
                    messageTime = "17:45",
                    messageCount = 20,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                // Additional items
                ChatItem(
                    profileImageResId = R.drawable.image_eleven,
                    displayName = "Neha Verma",
                    message = "What's the plan for today?",
                    messageTime = "18:15",
                    messageCount = 20,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_twelve,
                    displayName = "Manish Kumar",
                    message = "I will be late for the meeting.",
                    messageTime = "19:00",
                    messageCount = 30,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_thirteen,
                    displayName = "Ravi Gupta",
                    message = "Can you send me the report?",
                    messageTime = "19:45",
                    messageCount = 60,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_fourteen,
                    displayName = "Ritu Arora",
                    message = "Let's go for a walk in the evening.",
                    messageTime = "20:20",
                    messageCount = 4,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_fifteen,
                    displayName = "Shreya Roy",
                    message = "Lunch was great today!",
                    messageTime = "21:10",
                    messageCount = 2,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_sixteen,
                    displayName = "Vikas Singh",
                    message = "Call me when you're free.",
                    messageTime = "21:5",
                    messageReadRecepe = R.drawable.tick_double_svg,
                    messageCount = 10
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_seventeen,
                    displayName = "Arjun Nair",
                    message = "Got your email, will check it.",
                    messageTime = "22:30",
                    messageCount = 91,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.image_eighteen,
                    displayName = "Meera Iyer",
                    message = "Had a great day!",
                    messageTime = "23:05",
                    messageCount = 32,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.user_profile,
                    displayName = "Nikhil Rao",
                    message = "Can you help me with this?",
                    messageTime = "23:45",
                    messageCount = 28,
                    messageReadRecepe = R.drawable.tick_double_svg
                ),
                ChatItem(
                    profileImageResId = R.drawable.img1,
                    displayName = "Kriti Ahuja",
                    message = "Good night!",
                    messageTime = "00:30",
                    messageCount = 37,
                    messageReadRecepe = R.drawable.tick_double_svg
                )
            )
        }

        fun showToastShort(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun showToastLong(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}


