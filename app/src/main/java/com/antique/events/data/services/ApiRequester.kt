package com.antique.events.data.services

import com.antique.events.data.model.GetActivitiesResponse
import com.antique.events.data.model.GetDocumentResponse
import com.antique.events.data.model.RegisterOrActivityResponse
import com.antique.events.data.model.RegisterOrJoinActivityParams
import com.antique.events.data.model.StudentLoginParams
import com.antique.events.data.model.StudentLoginResponse
import com.antique.events.data.model.StudentRegisterParams
import com.antique.events.data.model.StudentUpdateParams
import com.antique.events.data.model.StudentUpdateResponse
import com.antique.events.data.model.SuccessResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRequester {
    companion object {
        const val BASE_URL = "https://information-kiosk-api.uc.r.appspot.com/"

        private fun makeApiInterface() : ApiInterface {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(ApiInterface::class.java)
        }

        fun studentLogin(studentLoginParams: StudentLoginParams) : Call<StudentLoginResponse> {
            return makeApiInterface().studentLogin(studentLoginParams);
        }

        fun studentRegister(studentRegisterParams: StudentRegisterParams): Call<SuccessResponse> {
            return makeApiInterface().studentRegister(studentRegisterParams);
        }

        fun studentUpdate(studentUpdateParams: StudentUpdateParams, studentId: String): Call<StudentUpdateResponse> {
            return makeApiInterface().studentUpdate(studentUpdateParams, studentId);
        }


        fun getActivities() : Call<GetActivitiesResponse> {
            return makeApiInterface().getActivities();
        }


        fun registerActivity(registerActivityParams: RegisterOrJoinActivityParams, activityId: String): Call<RegisterOrActivityResponse> {
            return makeApiInterface().registerActivity(registerActivityParams, activityId);
        }


        fun joinActivity(joinActivityParams: RegisterOrJoinActivityParams, activityId: String) : Call<RegisterOrActivityResponse> {
            return makeApiInterface().joinActivity(joinActivityParams, activityId);
        }

        fun getDocuments( studentId: String) : Call<GetDocumentResponse> {
            return makeApiInterface().getDocuments(studentId);
        }
    }
}