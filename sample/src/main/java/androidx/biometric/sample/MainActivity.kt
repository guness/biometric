package androidx.biometric.sample

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Enable Fingerprint Api")
        .setSubtitle("Scan your fingerprint to confirm")
        .setDescription("You can login with your fingerprint now")
        .setNegativeButtonText("Cancel")
        .build()

    val biometricPrompt =
        BiometricPrompt(this, MainThreadExecutor(), object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                when (errorCode) {
                    BiometricConstants.ERROR_HW_NOT_PRESENT, BiometricConstants.ERROR_HW_UNAVAILABLE, BiometricConstants.ERROR_NO_BIOMETRICS -> {
                        showToast(errString.toString())
                    }
                    BiometricConstants.ERROR_USER_CANCELED, BiometricConstants.ERROR_NEGATIVE_BUTTON -> {
                        Log.d("BIOMETRIC", "User has cancelled fingerprint with code $errorCode with message $errString")
                    }
                    BiometricConstants.ERROR_LOCKOUT, BiometricConstants.ERROR_LOCKOUT_PERMANENT -> {
                        Log.e("BIOMETRIC", "Fingerprint is locked with code $errorCode with message $errString")
                    }

                    BiometricConstants.ERROR_CANCELED,
                    BiometricConstants.ERROR_NO_SPACE, BiometricConstants.ERROR_TIMEOUT, BiometricConstants.ERROR_UNABLE_TO_PROCESS,
                    BiometricConstants.ERROR_VENDOR -> {
                        Log.w("BIOMETRIC", "Authentication failed with code $errorCode with message $errString")
                    }
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                showToast("Success")
            }


            override fun onAuthenticationFailed() {
                showToast("Authentication failed")
                Log.e("BIOMETRIC", "Authentication failed")
            }

        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            biometricPrompt.authenticate(promptInfo)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

