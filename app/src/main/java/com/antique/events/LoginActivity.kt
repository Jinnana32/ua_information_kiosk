package com.antique.events

import Utils
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.antique.events.data.model.StudentLoginParams
import com.antique.events.data.model.StudentLoginResponse
import com.antique.events.data.services.ApiRequester
import com.antique.events.data.services.FileDownloader
import com.antique.events.data.services.LocalStorageService
import com.antique.events.ui.dashboard.MainActivity
import com.antique.events.ui.utils.helper.DialogLoader
import com.antique.events.utils.UriPathUtils
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.google.gson.JsonSyntaxException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.login_button
import kotlinx.android.synthetic.main.activity_login.login_email
import kotlinx.android.synthetic.main.activity_login.login_password
import kotlinx.android.synthetic.main.activity_login.login_register_student
import kotlinx.android.synthetic.main.activity_login.password_unmask
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private lateinit var loadingDialog: Dialog
    private var disposable = Disposable.disposed()

    private val fileDownloader by lazy {
        FileDownloader(
            OkHttpClient.Builder().build()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadingDialog = DialogLoader.createProgressDialog(this@LoginActivity)!!

        val config = PRDownloaderConfig.newBuilder()
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()
        PRDownloader.initialize(applicationContext)

        try {
            LocalStorageService.getUser()
            startActivity(MainActivity.getIntent(this@LoginActivity))
        } catch (err: JsonSyntaxException){
            Log.e("Android Error", err.message.toString())
        } catch (err: IllegalStateException) {
            Log.e("Android Error", err.message.toString())
        } catch (err: NullPointerException) {
            Log.e("Android Error", err.message.toString())
        }

        login_register_student.setOnClickListener {
            startActivity(RegisterActivity.getIntent(this@LoginActivity));
        }

        password_unmask.setOnClickListener {
            if(login_password.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
                password_unmask.setImageResource(R.drawable.ic_visible)
                //Show Password
                login_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else{
                password_unmask.setImageResource(R.drawable.ic_invisible)
                //Hide Password
                login_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        login_button.setOnClickListener {

            // show the dialog after it was initialized
            loadingDialog.show()

            if (login_email.text!!.isEmpty()) {
                loadingDialog.dismiss()
                // if it is, then we show a pop up toast to the user saying that email or password is empty
                Toast.makeText(
                    this@LoginActivity,
                    "Please make sure that email is not empty",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val email = login_email.text.toString()
                val password = login_password.text.toString()
                val request = ApiRequester.studentLogin(StudentLoginParams(email = email, password = password))
                request.enqueue(object : Callback<StudentLoginResponse?> {
                    override fun onResponse(
                        call: Call<StudentLoginResponse?>,
                        response: Response<StudentLoginResponse?>
                    ) {
                        loadingDialog.dismiss()
                        if(response.code() == 200){
                            Log.d("Android debug", response.body().toString())
                            LocalStorageService.setUser(response.body()!!.data)
                            startActivity(MainActivity.getIntent(this@LoginActivity))
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Incorrect Email or Password. Please try again",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<StudentLoginResponse?>, t: Throwable) {
                        loadingDialog.dismiss()
                        Log.e("Android Error", t.message.toString())
                        Toast.makeText(
                            this@LoginActivity,
                            t.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        }
    }

}