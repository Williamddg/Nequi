package com.example.nequi;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

import Principal.Contrasena;
import Servicios.Ayuda;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private Button btnEntrar;
    private TextView tvContador, tvNumeros;
    private ImageView imageView3, imageView4;

    // 📌 Número válido (puedes cambiarlo)
    private final String numeroRegistrado = "3112781234";

    // ⏱️ Contador
    private CountDownTimer countDownTimer;
    private int tiempoRestante = 60; // en segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editTextPhone = findViewById(R.id.editTextPhone);
        btnEntrar = findViewById(R.id.button);
        tvContador = findViewById(R.id.tvContador);
        tvNumeros = findViewById(R.id.tvNumeros);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);

        // 🔹 Iniciar con un número aleatorio
        generarClaveNueva();

        // 🔹 Iniciar el contador
        iniciarContador();

        // 👉 Ir a la activity Ayuda
        imageView3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Ayuda.class);
            startActivity(intent);
        });

        // 👉 Copiar clave al portapapeles
        imageView4.setOnClickListener(v -> {
            String numero = tvNumeros.getText().toString();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Clave dinámica", numero);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "Número copiado: " + numero, Toast.LENGTH_SHORT).show();
        });

        btnEntrar.setOnClickListener(v -> {
            String numeroIngresado = editTextPhone.getText().toString().trim();

            if (TextUtils.isEmpty(numeroIngresado)) {
                Toast.makeText(this, "Por favor ingresa tu número", Toast.LENGTH_SHORT).show();
                return;
            }

            if (numeroIngresado.equals(numeroRegistrado)) {
                // ✅ Número válido → pasar a contraseña
                Intent intent = new Intent(MainActivity.this, Contrasena.class);
                intent.putExtra("numero", numeroIngresado);
                startActivity(intent);
            } else {
                // ❌ Número incorrecto
                Toast.makeText(this, "Número no registrado", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 🔹 Generar clave aleatoria de 6 dígitos
    private void generarClaveNueva() {
        Random random = new Random();
        int numero = 100000 + random.nextInt(900000); // entre 100000 y 999999
        tvNumeros.setText(String.valueOf(numero));
    }

    // 🔹 Iniciar contador de 60s
    private void iniciarContador() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante = (int) (millisUntilFinished / 1000);
                tvContador.setText(String.valueOf(tiempoRestante));
            }

            @Override
            public void onFinish() {
                generarClaveNueva(); // nuevo número
                iniciarContador();   // reiniciar contador
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
