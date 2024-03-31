package com.example.freshmind.UI.Welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Notes_DataFiles

class WelcomeViewModel() : ViewModel() {



    // Optionally, you can expose a function to refresh the data
    fun refreshCheckedNotes() {
        // You may need to call dbHelper.getCheckedNotes() again if it's not automatically refreshed
        // For example, if you're using Room, LiveData automatically updates the data when it changes in the database
    }
}
