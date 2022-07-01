package com.antique.events

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.antique.events.data.model.StudentRegisterParams
import com.antique.events.data.model.StudentUpdateParams
import com.antique.events.data.model.StudentUpdateResponse
import com.antique.events.data.model.SuccessResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.antique.events.data.model.User
import com.antique.events.data.services.ApiRequester
import com.antique.events.data.services.LocalStorageService
import com.antique.events.ui.utils.helper.DialogLoader
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.password_unmask
import kotlinx.android.synthetic.main.activity_register.reg_button
import kotlinx.android.synthetic.main.activity_register.reg_confirm_password
import kotlinx.android.synthetic.main.activity_register.reg_email_address
import kotlinx.android.synthetic.main.activity_register.reg_password
import kotlinx.android.synthetic.main.activity_register.toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserProfileActivity : AppCompatActivity(){

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java);
        }
    }

    private lateinit var loadingDialog: Dialog;

    private var mSelectedGender: String = "Male";
    private var mSelectedYearLevel: String = "1st";
    private var mSelectedDepartment: String = "College of Computer Studies(CCS)";
    private var mSelectedCourse: String = "BS Computer Science";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        val toolbar: Toolbar = toolbar as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar !== null) {
            supportActionBar!!.setTitle("Register");
            supportActionBar!!.setDisplayHomeAsUpEnabled(true);
            supportActionBar!!.setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener {
                finish();
            }
        }



        reg_button.setOnClickListener {
            // check whether the email or password is empty
            if(reg_email_address.text!!.isEmpty()){
                // then it will show the message
                Toast.makeText(this@UpdateUserProfileActivity, "Please make sure that email or password is not empty", Toast.LENGTH_LONG).show();
            }else {
                // it will try to register the user
                this.registerUser();
            }
        }

        val genders = resources.getStringArray(R.array.gender)
        var adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, genders)
        reg_gender.adapter = adapter
        reg_gender.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                mSelectedGender = genders[position];
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}

        }

        val yearLevels = resources.getStringArray(R.array.year_level)
        adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, yearLevels)
        reg_year_level.adapter = adapter
        reg_year_level.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                mSelectedYearLevel = yearLevels[position];
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        var courses = listOf<String>("BS HRM", "BS HM", "BS Entrepreneurship")
        var courses_adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, courses)

        val departments = resources.getStringArray(R.array.department)
        adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, departments)
        reg_department.adapter = adapter
        reg_department.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                mSelectedDepartment = departments[position];

                courses_adapter.clear()
                courses = if(mSelectedDepartment == "College of Computer Studies(CCS)") {
                    listOf<String>("BS Computer Science", "BS Information Technology", "BS Information System")
                } else if(mSelectedDepartment == "College of Teachers Education (CTE)") {
                    listOf<String>("B Elementary Education", "B Secondary Education")
                } else if(mSelectedDepartment == "College of Fisheries (COF)") {
                    listOf<String>("BS Fisheries and Aquatic Resources", "BLGA")
                } else{
                    listOf<String>("BS HRM", "BS HM", "BS Entrepreneurship")
                }

                courses.forEach {
                    courses_adapter.add(it);
                }

                courses_adapter.notifyDataSetChanged()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        reg_course.adapter = courses_adapter
        reg_course.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                mSelectedCourse = courses[position];
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    private fun registerUser() {
        val confirmPassword = reg_confirm_password.text.toString();
        val regPassword = reg_password.text.toString();
        if(confirmPassword == regPassword){
            loadingDialog = DialogLoader.createProgressDialog(this@UpdateUserProfileActivity)!!;
            loadingDialog.show();

            val xUser = LocalStorageService.getUser();
            val user = StudentUpdateParams(
                fname = reg_firstname.text.toString(),
                lname =  reg_lastname.text.toString(),
                mname = reg_middlename.text.toString(),
                yearLevel = mSelectedYearLevel,
                gender = mSelectedGender,
                course = mSelectedCourse,
                department = mSelectedDepartment,
                email = reg_email_address.text.toString(),
            )

            ApiRequester.studentUpdate(user, xUser._id).enqueue(object : Callback<StudentUpdateResponse?> {
                override fun onResponse(
                    call: Call<StudentUpdateResponse?>,
                    response: Response<StudentUpdateResponse?>
                ) {
                    Toast.makeText(this@UpdateUserProfileActivity, "Updated Succesfully", Toast.LENGTH_LONG).show();
                }

                override fun onFailure(call: Call<StudentUpdateResponse?>, t: Throwable) {
                    Toast.makeText(this@UpdateUserProfileActivity, "Error register, please try again later.", Toast.LENGTH_LONG).show();
                }
            })

        }
    }
}