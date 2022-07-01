package com.antique.events.data.services

import com.antique.events.data.model.GetActivitiesResponse
import com.antique.events.data.model.GetDocumentResponse
import com.antique.events.data.model.RegisterOrJoinActivityParams
import com.antique.events.data.model.RegisterOrActivityResponse
import com.antique.events.data.model.StudentLoginParams
import com.antique.events.data.model.StudentLoginResponse
import com.antique.events.data.model.StudentRegisterParams
import com.antique.events.data.model.StudentUpdateParams
import com.antique.events.data.model.StudentUpdateResponse
import com.antique.events.data.model.SuccessResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {

    @POST("/students/login")
    fun studentLogin(@Body studentLoginParams: StudentLoginParams): Call<StudentLoginResponse>

    @POST("/students/register")
    fun studentRegister(@Body studentRegisterParams: StudentRegisterParams): Call<SuccessResponse>

    @PUT("/students/{studentId}")
    fun studentUpdate(@Body studentUpdateParams: StudentUpdateParams, @Path("studentId") studentId: String): Call<StudentUpdateResponse>

    @GET("/activities")
    fun getActivities() : Call<GetActivitiesResponse>

    @POST("/register/{activityId}")
    fun registerActivity(@Body registerActivityParams: RegisterOrJoinActivityParams, @Path("activityId") activityId: String): Call<RegisterOrActivityResponse>

    @POST("/join/{activityId}")
    fun joinActivity(@Body joinActivityParams: RegisterOrJoinActivityParams, @Path("activityId") activityId: String) : Call<RegisterOrActivityResponse>

    @GET("/documents/student/{studentId}")
    fun getDocuments(@Path("studentId") studentId: String) : Call<GetDocumentResponse>
}