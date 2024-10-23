package com.app.atecapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var welcomeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializar el TextView de bienvenida
        welcomeTextView = findViewById(R.id.welcomeTextView)

        // Recuperar el rol del usuario de SharedPreferences
        val sharedPreferences = getSharedPreferences("ATECAppPrefs", MODE_PRIVATE)
        val userRole = sharedPreferences.getString("user_role", "0") // Default "0" si no hay rol

        // Mostrar el mensaje de bienvenida basado en el rol
        val welcomeMessage = when (userRole) {
            "1" -> "¡Bienvenido, Administrador!"
            "2" -> "¡Bienvenido, Profesor!"
            "3" -> "¡Bienvenido, Alumno!"
            else -> "¡Bienvenido!"
        }

        welcomeTextView.text = welcomeMessage // Establecer el mensaje en el TextView

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val buttonLogout = findViewById<Button>(R.id.buttonLogout)
        buttonLogout.setOnClickListener {
            // Aquí puedes implementar la lógica para cerrar sesión, por ejemplo:
            finish() // Cierra la actividad actual (MainActivity)
            // También puedes redirigir a la actividad de Login si es necesario
            // val intent = Intent(this, Login::class.java)
            // startActivity(intent)
        }

    }
}
