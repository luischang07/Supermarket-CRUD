package com.example.cuentaspolizas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/* Tópicos Avanzados de Programación
* Proyecto Moviles
* Horario: 09:00 - 10:00
* Profesor: DR. Clemente García Gerardo
* Alumno: Acosta Chang Luis Xavier
* Fecha: 08/12/2023
* */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnProductos, btnPaquetes, btnVentas, btnReportes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnProductos = findViewById(R.id.btnProductos);
        btnPaquetes = findViewById(R.id.btnPaquetes);
        btnVentas = findViewById(R.id.btnVentas);
        btnReportes = findViewById(R.id.btnReportes);

        btnProductos.setOnClickListener(this);
        btnPaquetes.setOnClickListener(this);
        btnVentas.setOnClickListener(this);
        btnReportes.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == btnProductos) {
            startActivity(new Intent(this, activity_Crud_Productos.class));
        }
        if (v == btnPaquetes) {
            startActivity(new Intent(this, activity_crear_paquetes.class));
        }
        if (v == btnVentas) {
            startActivity(new Intent(this, CrudVentas.class));
        }
        if (v == btnReportes) {
            startActivity(new Intent(this, Reportes.class));
        }
    }
}
