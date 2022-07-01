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
import com.antique.events.data.model.SuccessResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.antique.events.data.model.User
import com.antique.events.data.services.ApiRequester
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
import java.util.Arrays

class RegisterActivity : AppCompatActivity(){

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java);
        }
    }

    private lateinit var auth: FirebaseAuth;

    private lateinit var userReference: DatabaseReference

    private lateinit var loadingDialog: Dialog;

    private var mSelectedType: String = "student";
    private var mSelectedGender: String = "Male";
    private var mSelectedYearLevel: String = "1st";
    private var mSelectedDepartment: String = "College of Computer Studies(CCS)";
    private var mSelectedCourse: String = "BS Computer Science";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

        auth = FirebaseAuth.getInstance();
        userReference = Firebase.database.reference.child("users");


        /*reg_birthdate.setOnClickListener {
            val calendarMinDate = Calendar.getInstance()
            var minDate: MonthAdapter.CalendarDay? = MonthAdapter.CalendarDay(
                calendarMinDate.get(Calendar.YEAR),
                calendarMinDate.get(Calendar.MONTH), calendarMinDate.get(Calendar.DAY_OF_MONTH)
            )

            val calendarMaxDate = Calendar.getInstance()
            calendarMaxDate[Calendar.YEAR] = calendarMinDate.get(Calendar.YEAR) - 12
            var maxDate: MonthAdapter.CalendarDay? = MonthAdapter.CalendarDay(
                calendarMaxDate.get(Calendar.YEAR),
                calendarMaxDate.get(Calendar.MONTH), calendarMaxDate.get(Calendar.DAY_OF_MONTH)
            )

            val calendar = Calendar.getInstance()
            val calendarDatePickerDialogFragment = CalendarDatePickerDialogFragment()
                .setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val datePicked = DateUtil.formatDate(calendar.time, DateUtil.GOOD_FORMAT_DATE);
                    reg_birthdate.setText(datePicked);
                }
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setDateRange(null, maxDate)
                .setDoneText("Ok")
                .setCancelText("Cancel")
                .setThemeLight()
                .setPreselectedDate(calendarMaxDate.get(Calendar.YEAR),calendarMaxDate.get(Calendar.MONTH), calendarMaxDate.get(Calendar.DAY_OF_MONTH))

            calendarDatePickerDialogFragment.isCancelable = false
            calendarDatePickerDialogFragment.show(supportFragmentManager, "birtdate")
        }*/

        reg_button.setOnClickListener {
            // check whether the email or password is empty
            if(reg_email_address.text!!.isEmpty() || reg_password.text!!.isEmpty()){
                // then it will show the message
                Toast.makeText(this@RegisterActivity, "Please make sure that email or password is not empty", Toast.LENGTH_LONG).show();
            }else {
                // it will try to register the user
                this.registerUser();
            }
        }

        password_unmask.setOnClickListener {
            if(reg_password.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
                password_unmask.setImageResource(R.drawable.ic_visible);
                reg_password.transformationMethod = HideReturnsTransformationMethod.getInstance();
            }
            else{
                password_unmask.setImageResource(R.drawable.ic_invisible);
                reg_password.transformationMethod = PasswordTransformationMethod.getInstance();
            }
        }

        confirm_password_unmask.setOnClickListener {
            if(reg_confirm_password.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
                confirm_password_unmask.setImageResource(R.drawable.ic_visible);
                reg_confirm_password.transformationMethod = HideReturnsTransformationMethod.getInstance();
            }
            else{
                confirm_password_unmask.setImageResource(R.drawable.ic_invisible);
                reg_confirm_password.transformationMethod = PasswordTransformationMethod.getInstance();
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

        val types = resources.getStringArray(R.array.type)
        adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, types)
        reg_type.adapter = adapter
        reg_type.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                mSelectedType = types[position];
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
            android.R.layout.simple_spinner_item, courses
        )

        val departments = resources.getStringArray(R.array.department)
        adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, departments)
        reg_department.adapter = adapter
        reg_department.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                mSelectedDepartment = departments[position];

                val newcourses = if(departments[position] == "College of Computer Studies(CCS)") {
                    listOf<String>("BS Computer Science", "BS Information Technology", "BS Information System")
                } else if(departments[position] == "College of Teachers Education (CTE)") {
                    listOf<String>("B Elementary Education", "B Secondary Education")
                } else if(departments[position] == "College of Fisheries (COF)") {
                    listOf<String>("BS Fisheries and Aquatic Resources", "BLGA")
                } else{
                    listOf<String>("BS HRM", "BS HM", "BS Entrepreneurship")
                }

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
            loadingDialog = DialogLoader.createProgressDialog(this@RegisterActivity)!!;
            loadingDialog.show();

            val user = StudentRegisterParams(
                fname = reg_firstname.text.toString(),
                lname =  reg_lastname.text.toString(),
                mname = reg_middlename.text.toString(),
                yearLevel = mSelectedYearLevel,
                gender = mSelectedGender,
                course = mSelectedCourse,
                department = mSelectedDepartment,
                email = reg_email_address.text.toString(),
                password = reg_password.text.toString(),
                type = mSelectedType
            )

            ApiRequester.studentRegister(user).enqueue(object : Callback<SuccessResponse?> {
                override fun onResponse(
                    call: Call<SuccessResponse?>,
                    response: Response<SuccessResponse?>
                ) {
                    Toast.makeText(this@RegisterActivity, "Register successful", Toast.LENGTH_LONG).show();
                   startActivity(LoginActivity.getIntent(this@RegisterActivity));
                }

                override fun onFailure(call: Call<SuccessResponse?>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Error register, please try again later.", Toast.LENGTH_LONG).show();
                }
            })

        }else{
            Toast.makeText(this@RegisterActivity, "Confirmation password is not the same. Please try again.", Toast.LENGTH_LONG).show();
        }
    }
}