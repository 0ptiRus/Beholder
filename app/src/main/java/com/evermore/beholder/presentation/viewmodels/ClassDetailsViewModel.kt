package com.evermore.beholder.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evermore.beholder.presentation.classes.ClassData
import com.google.gson.Gson

class ClassDetailsViewModel : ViewModel() {

    private val _classData = MutableLiveData<ClassData>()
    val classData: LiveData<ClassData> = _classData

    fun loadClassData(classJson: String) {
        val gson = Gson()
        val data = gson.fromJson(classJson, ClassData::class.java)
        _classData.value = data
    }
}
