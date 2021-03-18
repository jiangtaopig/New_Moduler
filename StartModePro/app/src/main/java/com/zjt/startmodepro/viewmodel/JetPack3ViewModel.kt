package com.zjt.startmodepro.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zjt.startmodepro.bean.Student

class JetPack3ViewModel : ViewModel() {

    var mStudent = MutableLiveData<Student>()

    fun getStudentInfo(){
        mStudent.value = Student("zjt", 23)
    }


}