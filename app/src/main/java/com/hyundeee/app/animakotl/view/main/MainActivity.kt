package com.hyundeee.app.animakotl.view.main

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.desmond.squarecamera.CameraActivity
import com.desmond.squarecamera.ImageUtility
import com.hyundeee.app.animakotl.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val REQUEST_CAMERA = 0
    private val REQUEST_CAMERA_PERMISSION = 1
    var mSize: Point? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val display = windowManager.defaultDisplay
        mSize = Point()
        display.getSize(mSize)

    }


    /*// Check for camera permission in MashMallow
    fun requestForCameraPermission(view: View) {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(this@MainActivity, permission) !== PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, permission)) {
                // Show permission rationale
            } else {
                // Handle the result in Activity#onRequestPermissionResult(int, String[], int[])
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), REQUEST_CAMERA_PERMISSION)
            }
        } else {
            // Start CameraActivity
        }
    }

    // Start CameraActivity
    var startCustomCameraIntent = Intent(this, CameraActivity::class.java)

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
    }
    //startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA)

    // Receive Uri of saved square photo
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != Activity.RESULT_OK) return

        if (requestCode == REQUEST_CAMERA) {
            val photoUri = data.data
        }
        super.onActivityResult(requestCode, resultCode, data)
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != Activity.RESULT_OK) return

        if (requestCode == REQUEST_CAMERA) {
            val photoUri = data.data
            // Get the bitmap in according to the width of the device
            val bitmap = ImageUtility.decodeSampledBitmapFromPath(photoUri.path, mSize!!.x, mSize!!.x)
            (findViewById<View>(R.id.image) as ImageView).setImageBitmap(bitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun requestForCameraPermission(view: View) {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(this@MainActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, permission)) {
                showPermissionRationaleDialog("Test", permission)
            } else {
                requestForPermission(permission)
            }
        } else {
            launch()
        }
    }

    private fun showPermissionRationaleDialog(message: String, permission: String) {
        AlertDialog.Builder(this@MainActivity)
                .setMessage(message)
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which -> this@MainActivity.requestForPermission(permission) })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> })
                .create()
                .show()
    }

    private fun requestForPermission(permission: String) {
        toast("here")
        ActivityCompat.requestPermissions(this@MainActivity,  arrayOf<String>(permission), REQUEST_CAMERA_PERMISSION)
    }

    private fun launch() {
        val startCustomCameraIntent = Intent(this, CameraActivity::class.java)
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                val numOfRequest = grantResults.size
                val isGranted = numOfRequest == 1 && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1]
                if (isGranted) {
                    launch()
                }
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    fun toast(message: String) = Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
}
