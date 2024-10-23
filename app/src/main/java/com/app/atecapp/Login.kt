package com.app.atecapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class Login : AppCompatActivity() {

    private lateinit var editTextCodigo: EditText
    private lateinit var editTextClave: EditText
    private lateinit var loginButton: Button
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar campos de texto y botón
        editTextCodigo = findViewById(R.id.editTextEmail)
        editTextClave = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.loginButton)

        // Inicializar Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this)

        // Configurar el click del botón
        loginButton.setOnClickListener {
            val codigo = editTextCodigo.text.toString().trim()
            val clave = editTextClave.text.toString().trim()

            // Verificar que no estén vacíos
            if (codigo.isNotEmpty() && clave.isNotEmpty()) {
                loginUser(codigo, clave)
            } else {
                Toast.makeText(this, "Por favor, ingresa el código y la clave", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun loginUser(codigo: String, clave: String) {
        val url = "http://10.0.2.2/apiAtec/login.php?codigo=$codigo&clave=$clave"

        // Imprimir la URL en los logs para depuración
        Log.d("LoginRequest", "URL de solicitud: $url")

        // Crear solicitud con StringRequest
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            try {
                // Imprimir la respuesta en los logs
                Log.d("LoginResponse", "Respuesta del servidor: $response")

                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getString("status")
                val message = jsonResponse.getString("message")

                if (status == "success") {
                    val rol = jsonResponse.getString("rol") // Obtener el rol del JSON
                    Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()

                    // Guardar el rol en SharedPreferences
                    val sharedPreferences = getSharedPreferences("ATECAppPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("user_role", rol)
                    editor.apply()

                    // Redirigir a MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Finalizar la actividad de inicio de sesión
                }
                else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    Log.d("LoginStatus", "Error: $message")
                }

            } catch (e: JSONException) {
                e.printStackTrace()
                Log.e("LoginError", "Error al procesar la respuesta JSON", e)
                Toast.makeText(this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
            }

        }, { error ->
            Log.e("LoginError", "Error en la solicitud: ${error.message}", error)
            Toast.makeText(this, "Error en la solicitud: ${error.message}", Toast.LENGTH_SHORT).show()
        })

        // Agregar la solicitud a la cola de Volley
        requestQueue.add(stringRequest)
    }
}
