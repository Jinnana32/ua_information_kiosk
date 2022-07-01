import android.content.Context
import android.os.Environment
import android.telephony.mbms.DownloadRequest
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import java.io.File
import java.util.Locale


object Utils {

    fun createNewFolderAndGetPath(context: Context) : String{
        //val path = context.getExternalFilesDir(null)
        val path = Environment.getExternalStorageDirectory()
        val folder = File(path, "ua_downloads")
        if(!folder.exists()) {
            folder.mkdirs()
        }
        return folder.absolutePath
    }

    fun getUploadPath() : String {
        return "https://information-kiosk-api.uc.r.appspot.com/uploads/";
    }

    fun downloadFile(context: Context, fileName: String) : com.downloader.request.DownloadRequest {
        val dirPath = getRootDirPath(context)
        val url = getUploadPath() + fileName
        return PRDownloader.download(url, dirPath, fileName)
            .build();
            /*
            * .setOnStartOrResumeListener { }
            .setOnPauseListener { }
            .setOnProgressListener {
                Log.i("Android debug", it.currentBytes.toString())
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        this@LoginActivity,
                        "Download completed",
                        Toast.LENGTH_LONG
                    ).show()
                }
                override fun onError(error: com.downloader.Error?) {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        this@LoginActivity,
                        "Download error",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("Android debug", error.toString())
                }
            })*/
    }

    fun getRootDirPath(context: Context): String {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.getApplicationContext(),
                null
            )[0]
            file.getAbsolutePath()
        } else {
            context.getApplicationContext().getFilesDir().getAbsolutePath()
        }
    }

    fun getProgressDisplayLine(currentBytes: Long, totalBytes: Long): String {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes)
    }

    private fun getBytesToMBString(bytes: Long): String {
        return java.lang.String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
    }
}