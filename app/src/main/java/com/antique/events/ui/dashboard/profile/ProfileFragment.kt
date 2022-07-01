package com.antique.events.ui.dashboard.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.antique.events.R
import com.antique.events.UpdateUserProfileActivity
import com.antique.events.data.services.LocalStorageService
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var loadingDialog: Dialog;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_update_user_profile.setOnClickListener {
            startActivity(UpdateUserProfileActivity.getIntent(requireContext()));
        }

        initStudentView();
    }

    private fun initStudentView() {
        val user = LocalStorageService.getUser();
        layout_patient_view.visibility = View.VISIBLE;
        student_name.text = "${user.fname} ${user.mname} ${user.lname}"
        student_email.text = user.email
        student_year.text = user.yearLevel
        student_department.text = user.department
        student_gender.text = user.gender
        student_course.text = user.course
    }

}