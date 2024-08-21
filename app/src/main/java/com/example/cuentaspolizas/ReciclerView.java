package com.example.cuentaspolizas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ReciclerView extends AppCompatActivity {
    TextView editTextVentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recicler_view);

        editTextVentas = findViewById(R.id.Paquetes);
        try {
            if (getIntent().getExtras().getString("source").equals("Productos")) {
                setTitle("productos");
                editTextVentas.setText("Productos");

                ArrayList<Product> productList;
                productList = (ArrayList<Product>) getIntent().getSerializableExtra("productList");

                if (productList.size() == 0) {
                    Rutinas.mensajeDialog("No hay datos para mostrar", this);
                }
                RecyclerView recyclerView = findViewById(R.id.ReciclerView1);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                CustomAdapterProductos adapter = new CustomAdapterProductos(productList);

                recyclerView.setAdapter(adapter);
            } else if (getIntent().getExtras().getString("source").equals("Ventas")) {
                setTitle("ventas");
                editTextVentas.setText("Ventas");
                TextView textView = findViewById(R.id.textView);
                textView.setVisibility(TextView.INVISIBLE);
                TextView textView2 = findViewById(R.id.textView2);
                textView2.setVisibility(TextView.INVISIBLE);

                ArrayList<Ventas> sellList;
                sellList = (ArrayList<Ventas>) getIntent().getSerializableExtra("sellList");
                if (sellList.size() == 0) {
                    Rutinas.mensajeDialog("No hay datos para mostrar", this);
                }
                RecyclerView recyclerView = findViewById(R.id.ReciclerView1);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                CustomAdapterVentas adapter = new CustomAdapterVentas(sellList);
                recyclerView.setAdapter(adapter);
            } else if (getIntent().getExtras().getString("source").equals("paquetes")) {
                setTitle("paquetes");
                editTextVentas.setText("Paquetes");
                TextView textView = findViewById(R.id.textView);
                textView.setVisibility(TextView.INVISIBLE);
                TextView textView2 = findViewById(R.id.textView2);
                textView2.setVisibility(TextView.INVISIBLE);

                ArrayList<Paquete> paqueteList;
                paqueteList = (ArrayList<Paquete>) getIntent().getSerializableExtra("packageList");
                if (paqueteList.size() == 0) {
                    Rutinas.mensajeDialog("No hay datos para mostrar", this);
                    return;
                }
                RecyclerView recyclerView = findViewById(R.id.ReciclerView1);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                CustomAdapterPaquetes adapter = new CustomAdapterPaquetes(paqueteList);
                recyclerView.setAdapter(adapter);
//      --------------------------------------------------------------------------------------------
            } else if (getIntent().getExtras().getString("source").equals("productosNoVendidos")) {
                setTitle("productos no vendidos");
                editTextVentas.setText("Productos no vendidos");
                TextView textView = findViewById(R.id.textView);
                textView.setVisibility(TextView.INVISIBLE);
                TextView textView2 = findViewById(R.id.textView2);
                textView2.setVisibility(TextView.INVISIBLE);


                ArrayList<Integer> productosNoVendidos;
                productosNoVendidos = (ArrayList<Integer>) getIntent().getSerializableExtra("productosNoVendidos");
                if (productosNoVendidos.size() == 0) {
                    Rutinas.mensajeDialog("No hay datos para mostrar", this);
                }
                RecyclerView recyclerView = findViewById(R.id.ReciclerView1);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                CustomAdapterConsulta1 adapter = new CustomAdapterConsulta1(productosNoVendidos);
                recyclerView.setAdapter(adapter);
            } else if (getIntent().getExtras().getString("source").equals("paquetesConsulta")) {
                setTitle("paquetes consulta");
                editTextVentas.setText("Paquetes consulta");
                TextView textView = findViewById(R.id.textView);

                ArrayList<Integer> paquetesConsulta;
                paquetesConsulta = (ArrayList<Integer>) getIntent().getSerializableExtra("paquetes");
                if (paquetesConsulta.size() == 0) {
                    Rutinas.mensajeDialog("No hay datos para mostrar", this);
                }
                RecyclerView recyclerView = findViewById(R.id.ReciclerView1);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                CustomAdapterConsulta1 adapter = new CustomAdapterConsulta1(paquetesConsulta);
                recyclerView.setAdapter(adapter);
            } else if (getIntent().getExtras().getString("source").equals("productosIndividuales")) {
                setTitle("productos individuales");
                editTextVentas.setText("Productos individuales");
                TextView textView = findViewById(R.id.textView);
                textView.setVisibility(TextView.INVISIBLE);
                TextView textView2 = findViewById(R.id.textView2);
                textView2.setVisibility(TextView.INVISIBLE);

                ArrayList<Consulta3> productosIndividuales;
                productosIndividuales = (ArrayList<Consulta3>) getIntent().getSerializableExtra("productosIndividuales");
                if (productosIndividuales.size() == 0) {
                    Rutinas.mensajeDialog("No hay datos para mostrar", this);
                }
                RecyclerView recyclerView = findViewById(R.id.ReciclerView1);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                CustomAdapterConsulta3 adapter = new CustomAdapterConsulta3(productosIndividuales);
                recyclerView.setAdapter(adapter);
            } else {
                setTitle("Productos paquete");
                editTextVentas.setText("Productos en Paquetes");
                TextView textView = findViewById(R.id.textView);
                textView.setVisibility(TextView.INVISIBLE);
                TextView textView2 = findViewById(R.id.textView2);
                textView2.setVisibility(TextView.INVISIBLE);

                ArrayList<Consulta3> productosPaquete;
                productosPaquete = (ArrayList<Consulta3>) getIntent().getSerializableExtra("productosPaquetes");
                if (productosPaquete.size() == 0) {
                    Rutinas.mensajeDialog("No hay datos para mostrar", this);
                }
                RecyclerView recyclerView = findViewById(R.id.ReciclerView1);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                CustomAdapterConsulta3 adapter = new CustomAdapterConsulta3(productosPaquete);
                recyclerView.setAdapter(adapter);
            }
        } catch (Exception e) {
            Rutinas.mensajeDialog("No hay datos para mostrar "+ e.getMessage(), this);
        }

    }
}