package com.udayy.thedownloader.ui.download

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.udayy.thedownloader.R
import com.udayy.thedownloader.databinding.ActivityDownloadBinding
import com.udayy.thedownloader.ui.login.afterTextChanged


class DownloadActivity : AppCompatActivity() {

    private lateinit var downloadViewModel: DownloadViewModel
    private lateinit var binding: ActivityDownloadBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var url: EditText

    private var downloadId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        url = binding.url
        val download = binding.download

        downloadViewModel = ViewModelProvider(this)[DownloadViewModel::class.java]

        download.setOnClickListener {
            if (haveStoragePermission()) {
                getDestinationUri()
            }
        }

        // Check for valid url to enable/disable download button
        downloadViewModel.downloadFormState.observe(this, Observer {
            val downloadFormState = it ?: return@Observer

            // disable download button unless url is valid
            download.isEnabled = downloadFormState.isDataValid

            if (downloadFormState.urlError != null) {
                url.error = getString(downloadFormState.urlError)
            }
        })

        url.apply {
            afterTextChanged {
                downloadViewModel.downloadDataChanged(url.text.toString())
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        download.performClick()
                }
                false
            }
        }

        // Broadcast Receiver that listens to download completes
        val br = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    Toast.makeText(
                        context,
                        getString(R.string.download_complete),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // register broadcast receiver to take action when download completes
        registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    val uri = data?.data
                    uri?.let { startDownload(it) }
                }
            }
    }

    private fun startDownload(destinationDirPath: Uri) {
        Toast.makeText(
            this,
            getString(R.string.download_start),
            Toast.LENGTH_SHORT
        ).show()

        // Pass entered url to downloadManager
        val urlText = url.text.toString()
        val request =
            DownloadManager.Request(Uri.parse(urlText))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(destinationDirPath)
        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        // Save download ID for later use
        downloadId = dm.enqueue(request)
    }

    private fun haveStoragePermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //you have the permission now.
            getDestinationUri()
        }
    }

    private fun getDestinationUri() {
        // Choose destination folder to save downloaded file
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        resultLauncher.launch(intent)
    }
}