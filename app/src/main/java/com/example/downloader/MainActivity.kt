package com.example.downloader

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    lateinit var editText: EditText
    lateinit var download: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.url)
        download = findViewById(R.id.download_btn)

        download.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED)
                {
                    requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),11)
                }
                else
                {
                    startDownloadURL()
                }
            }
            else
            {
                // system is less than Marshmallow , runtime permissions not required
                startDownloadURL()
            }
        }
    }

    private fun startDownloadURL() {
        val url = editText.text.toString()

        // To start download process , we need to prepare DownloadManager.Request
        // which contain all info to request a new download
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The file is downloading now ...")
        request.setMimeType("image/jpeg")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "image.jpeg")

        // Once the request is set up , now we have initiate the download
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // enqueue method return a unique long ID which acts as an identifier for the download
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==11 && grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            startDownloadURL()
        }
        else
        {
            Toast.makeText(this,"Permission Denied!",Toast.LENGTH_LONG).show()
        }
    }
}