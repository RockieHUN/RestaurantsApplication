package com.example.luigi.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.luigi.model.RegistrationUser
import com.example.luigi.room.User

class RegistrationViewModel() : ViewModel(){
    private var user : MutableLiveData<RegistrationUser> = MutableLiveData<RegistrationUser>()

    fun getUser() : RegistrationUser? {
        return this.user.value
    }

    fun setUser(user : RegistrationUser){
        this.user.value = user
    }
}