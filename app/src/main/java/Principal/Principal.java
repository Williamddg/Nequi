package Principal;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nequi.R;

import java.util.Random;

public class Principal extends AppCompatActivity {

    // Variables de cuenta
    private String telefonoRecibido;
    private final String telefonoAsociado = "3112781234";
    private int dinerocuenta = 50000;
    private int tarjeta = 40000;
    private int colchon = 10000;
    private boolean oculto = false;

    // Views de la plata
    private TextView txtDinero, txtTotal, txtTuPlata;
    private ImageView imgOcultar, imgPerfil, imgNotificaciones, imgAyuda, imgEditFavoritos;

    // Views de clave dinámica
    private LinearLayout layoutClaveDinamica;
    private TextView tvContador, tvNumeros;
    private ImageView imgClavedin, imgCopiarClave;

    // Variables de clave dinámica
    private CountDownTimer countDownTimer;
    private String claveActual = "";
    private int tiempoRestante = 60; // guardamos el tiempo real

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias vistas de dinero
        txtDinero = findViewById(R.id.txtdinero);
        txtTotal = findViewById(R.id.txttotal);
        txtTuPlata = findViewById(R.id.txttu_plata);

        imgOcultar = findViewById(R.id.imgocultar);
        imgPerfil = findViewById(R.id.imgtu_perfil);
        imgNotificaciones = findViewById(R.id.imgnotificaciones);
        imgAyuda = findViewById(R.id.imgayuda);
        imgEditFavoritos = findViewById(R.id.imgeditfavoritos);

        // Referencias clave dinámica
        layoutClaveDinamica = findViewById(R.id.layoutClaveDinamica);
        tvContador = findViewById(R.id.tvContador);
        tvNumeros = findViewById(R.id.tvNumeros);
        imgClavedin = findViewById(R.id.imgclavedin);
        imgCopiarClave = findViewById(R.id.imgCopiarClave);

        // Recibir el teléfono del intent
        Intent intent = getIntent();
        telefonoRecibido = intent.getStringExtra("telefono");

        // Validar si el teléfono coincide
        if (telefonoRecibido != null && telefonoRecibido.equals(telefonoAsociado)) {
            mostrarValores();
        }

        // Acción ocultar/mostrar dinero
        imgOcultar.setOnClickListener(v -> {
            if (oculto) {
                mostrarValores();
                oculto = false;
            } else {
                txtDinero.setText("*****");
                txtTotal.setText("Total $ *****");
                oculto = true;
            }
        });

        // Acción mostrar/ocultar clave dinámica
        imgClavedin.setOnClickListener(v -> {
            if (layoutClaveDinamica.getVisibility() == View.GONE) {
                layoutClaveDinamica.setVisibility(View.VISIBLE);
                // mostrar la clave y el tiempo actual (no reiniciar nada)
                tvNumeros.setText(claveActual);
                tvContador.setText(String.valueOf(tiempoRestante));
            } else {
                layoutClaveDinamica.setVisibility(View.GONE);
            }
        });

        // Copiar clave al portapapeles
        imgCopiarClave.setOnClickListener(v -> {
            String numero = tvNumeros.getText().toString();
            if (!numero.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Clave dinámica", numero);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(this, "Clave copiada: " + numero, Toast.LENGTH_SHORT).show();
            }
        });

        // Ocultar el layout al hacer click en cualquier parte de él (excepto copiar)
        layoutClaveDinamica.setOnClickListener(v -> {
            layoutClaveDinamica.setVisibility(View.GONE);
        });

        // Navegaciones
        imgPerfil.setOnClickListener(v -> startActivity(new Intent(Principal.this, Tu_perfil.class)));
        imgNotificaciones.setOnClickListener(v -> startActivity(new Intent(Principal.this, Notificaciones.class)));
        imgAyuda.setOnClickListener(v -> startActivity(new Intent(Principal.this, Servicios.Ayuda.class)));
        imgEditFavoritos.setOnClickListener(v -> startActivity(new Intent(Principal.this, Favoritos.class)));
        txtTuPlata.setOnClickListener(v -> startActivity(new Intent(Principal.this, Tu_plata.class)));
        txtTotal.setOnClickListener(v -> startActivity(new Intent(Principal.this, Tu_plata.class)));

        // 🔹 Iniciar clave dinámica (corre siempre en segundo plano)
        generarClaveNueva();
        iniciarContador();
    }

    // Método para mostrar valores con formato
    private void mostrarValores() {
        txtDinero.setText("$ " + dinerocuenta);
        int total = dinerocuenta + tarjeta + colchon;
        txtTotal.setText("Total $ " + total);
    }

    // 🔹 Generar clave aleatoria de 6 dígitos
    private void generarClaveNueva() {
        Random random = new Random();
        int numero = 100000 + random.nextInt(900000);
        claveActual = String.valueOf(numero);
        tvNumeros.setText(claveActual);
    }

    // 🔹 Iniciar contador de 60s
    private void iniciarContador() {
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante = (int) (millisUntilFinished / 1000);
                // solo actualiza el TextView si el layout está visible
                if (layoutClaveDinamica.getVisibility() == View.VISIBLE) {
                    tvContador.setText(String.valueOf(tiempoRestante));
                }
            }

            @Override
            public void onFinish() {
                generarClaveNueva();
                iniciarContador();   // reinicia el ciclo automáticamente
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
