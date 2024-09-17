package com.example.usercatalog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    val users: MutableLiveData<MutableList<User>> by lazy {
        MutableLiveData<MutableList<User>>(mutableListOf())
    }
}
