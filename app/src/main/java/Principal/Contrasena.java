package Principal;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nequi.R;

public class Contrasena extends AppCompatActivity {

    private TextView[] cajas;   // Los 4 cuadros blancos
    private int indice = 0;
    private StringBuilder claveIngresada = new StringBuilder(); // Guardamos los números

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contrasena);

        // Subrayar texto "¿Se te olvidó?"
        TextView tv = findViewById(R.id.textView4);
        tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Configurar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar las 4 cajas
        cajas = new TextView[]{
                findViewById(R.id.caja1),
                findViewById(R.id.caja2),
                findViewById(R.id.caja3),
                findViewById(R.id.caja4)
        };

        // IDs de botones numéricos
        int[] botones = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6,
                R.id.btn7, R.id.btn8, R.id.btn9
        };

        // Listener para números
        View.OnClickListener listenerNumero = v -> {
            if (indice < 4) {
                TextView boton = (TextView) v;
                String numero = boton.getText().toString();

                // Guardar número en claveIngresada
                claveIngresada.append(numero);

                // Mostrar asterisco en la caja correspondiente
                cajas[indice].setText("*");
                indice++;

                // ✅ Si ya tiene 4 dígitos, aquí puedes validar la clave
                if (indice == 4) {
                    validarClave(claveIngresada.toString());
                }
            }
        };

        // Asignar listeners a los botones
        for (int id : botones) {
            findViewById(id).setOnClickListener(listenerNumero);
        }

        // Botón borrar (el de % en la fila 4 col 3)
        findViewById(R.id.btnborrar).setOnClickListener(v -> {
            if (indice > 0) {
                indice--;
                cajas[indice].setText(""); // Quitar asterisco
                claveIngresada.deleteCharAt(claveIngresada.length() - 1); // Eliminar número
            }
        });
    }

    // Método para validar la clave ingresada
    private void validarClave(String clave) {
        String claveCorrecta = "1234"; // Ejemplo
        if (clave.equals(claveCorrecta)) {
            // ✅ Obtener número recibido del Main
            String telefono = getIntent().getStringExtra("numero");

            // Pasar a Principal con el número
            Intent intent = new Intent(Contrasena.this, Principal.class);
            intent.putExtra("telefono", telefono);
            startActivity(intent);

            finish(); // Para que no regrese a Contrasena con el botón atrás
        } else {
            // ❌ Clave incorrecta → limpiar campos
            Toast.makeText(this, "Contraseña incorrecta, intente nuevamente", Toast.LENGTH_SHORT).show();
            limpiarCajas();
        }
    }


    // Método para resetear
    private void limpiarCajas() {
        for (TextView caja : cajas) {
            caja.setText("");
        }
        claveIngresada.setLength(0);
        indice = 0;
    }
}
