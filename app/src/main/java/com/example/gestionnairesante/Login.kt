package com.example.gestionnairesante

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class Login : AppCompatActivity() {
    private lateinit var executor: Executor
    private lateinit var biometricprompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /****************************/
        /* Declaration des éléments */
        /****************************/
        // Ici choix de pas faire de binding

        val username: EditText = findViewById(R.id.username)
        val password: EditText = findViewById(R.id.password)
        val register: Button = findViewById(R.id.button_register)

        register.setOnClickListener() {
            if (username.text.toString() == "camille@gmail.com" && password.text.toString() == "camille") {
                val intent = Intent(applicationContext, MainActivity::class.java)
                //Toast.makeText(applicationContext, "Authentification réussie", Toast.LENGTH_LONG).show()
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Mauvais login et password !!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //
        // Premiere tentative de biométrie
        //
        executor = ContextCompat.getMainExecutor(this)
        biometricprompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(
                        errorCode,
                        errString
                    )
                    Toast.makeText(
                        applicationContext,
                        "Erreur d'authentification $errString",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    //Toast.makeText(applicationContext,"Authentification réussie", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext,
                        "Authentification échouée, veuillez entrer votre login/password",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login par Biometrie")
            .setSubtitle("Maintenir le doigt sur la zone d'empreinte)")
            .setNegativeButtonText("appuyer sur la zone")
            .build()
        biometricprompt.authenticate(promptInfo)
    }
}