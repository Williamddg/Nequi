package Servicios;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nequi.*;
import com.example.nequi.R;

public class Ayuda extends AppCompatActivity {

    private TextView btnCasos, btnEvaluar, textView7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ayuda);

        // Referencias a las vistas
        btnCasos = findViewById(R.id.btncasos);
        btnEvaluar = findViewById(R.id.btnevaluar);
        textView7 = findViewById(R.id.textView7);

        // 🔹 btncasos → lleva al MainActivity
        btnCasos.setOnClickListener(v -> {
            Intent intent = new Intent(Ayuda.this, MainActivity.class);
            startActivity(intent);
        });

        // 🔹 btnevaluar → lleva a Servicios.Evaluar
        btnEvaluar.setOnClickListener(v -> {
            Intent intent = new Intent(Ayuda.this, Evaluar.class);
            startActivity(intent);
        });

        // 🔹 textView7 → también lleva al MainActivity
        textView7.setOnClickListener(v -> {
            Intent intent = new Intent(Ayuda.this, MainActivity.class);
            startActivity(intent);
        });

        // Ajuste de insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
