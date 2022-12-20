package com.rhomairamaduetritasugiarto.myapplication

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.webviewtopdf.PdfView


class MainActivity : AppCompatActivity() {
    private val PERMISSION_STORAGE_CODE = 1000
    private lateinit var webView :WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webView)
        val data = "Your data which you want to load"

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(data, "text/html; charset=utf-8", "UTF-8");
        webView.webViewClient = WebViewClient()
        startService(Intent(this, TimerService::class.java))

    }

    private fun initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val a = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
            if (a  ) {
                val permission = arrayOf<String>(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
                requestPermissions(permission, PERMISSION_STORAGE_CODE)
            } else {
                writeData()
            }
        } else {
            writeData()
        }
    }

    private fun writeData() {
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/")
        val fileName = "Test.pdf"

        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()
        PdfView.createWebPrintJob(
            this@MainActivity,
            webView,
            directory,
            fileName,
            object : PdfView.Callback {
                override fun success(path: String) {
                    progressDialog.dismiss()
                    PdfView.openPdfFile(
                        this@MainActivity, getString(R.string.app_name),
                        "Do you want to open the pdf file?$fileName", path
                    )
                }

                override fun failure() {
                    progressDialog.dismiss()
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check permission is Granting
        if (requestCode == PERMISSION_STORAGE_CODE
            && Environment.isExternalStorageManager()){
            writeData()
        }
    }

    fun convert(view: View) {
        initPermission()
    }

}