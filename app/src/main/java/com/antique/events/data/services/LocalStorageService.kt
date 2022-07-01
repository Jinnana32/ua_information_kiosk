package com.antique.events.data.services

import com.antique.events.data.model.UserData
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs

class LocalStorageService {

    companion object {

        val USER_KEY = "USER_KEY";

        private val gson = Gson();

        fun setUser(user: UserData) {
            val userString = gson.toJson(user);
            Prefs.putString(USER_KEY, userString);
        }

        fun removeUser() {
            Prefs.remove(USER_KEY);
        }

        fun getUser() : UserData {
            val userString =  Prefs.getString(USER_KEY, "");
            val user : UserData = gson.fromJson(userString, UserData::class.java);
            return user;
        }

    }

}