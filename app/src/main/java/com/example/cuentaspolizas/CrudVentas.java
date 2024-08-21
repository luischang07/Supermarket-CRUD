package com.example.cuentaspolizas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CrudVentas extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText txtFolio, txtUnidades, txtPrecio;
    Button btnGrabarVenta, btnEliminar, btnModificarVenta, btnRecuperarVenta, btnLimpiar, btnConsultarVentas;
    Spinner spProductosPaquetes, spProdOPaq;
    BaseDeDatos conexion;
    SQLiteDatabase BD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crud_ventas);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout2), (v, insets) -> {
            WindowInsetsCompat insetsCompat = insets;
            Insets insets1 = insetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets1.left, insets1.top, insets1.right, insets1.bottom);
            return insetsCompat;
        });

        tryConnection();

        spProdOPaq = findViewById(R.id.spProductoPaquete);
        spProductosPaquetes = findViewById(R.id.spProductosPaquetes);
        txtFolio = findViewById(R.id.txtFolio);
        txtUnidades = findViewById(R.id.txtUnidades);
        txtPrecio = findViewById(R.id.txtPrecio);

        btnGrabarVenta = findViewById(R.id.btnConsultarPaquete);
        btnEliminar = findViewById(R.id.btnEliminarPaquete);
        btnModificarVenta = findViewById(R.id.btnModificarPaquete);
        btnRecuperarVenta = findViewById(R.id.btnRecuperarVenta);
        btnLimpiar = findViewById(R.id.btnLimpiarPaquete);
        btnConsultarVentas = findViewById(R.id.btnConsultarVentas);

        fillSpinnerProdOPaq();

        btnGrabarVenta.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        btnModificarVenta.setOnClickListener(this);
        btnRecuperarVenta.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        btnConsultarVentas.setOnClickListener(this);

        spProdOPaq.setOnItemSelectedListener(this);

    }

    private void tryConnection() {
        try {
            conexion = new BaseDeDatos(this, "SuperMarket", null, BaseDeDatos.version);
            if (conexion == null) {
                Rutinas.mensajeDialog("No se ha conectado a la BD", this);
                return;
            }
            BD = conexion.getWritableDatabase();
            if (BD == null) {
                Rutinas.mensajeDialog("La BD no se ha establecido para lectura y escritura", this);
                return;
            }
            Rutinas.mensajeToast("conexion exitosa", this);
        } catch (Exception e) {
            Rutinas.mensajeDialog(e.getMessage(), this);
        }
    }

    private void fillSpinnerProdOPaq() {
        ArrayAdapter<String> productsAdapter;
        productsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        productsAdapter.add("Seleccione producto o paquete");
        productsAdapter.add("Producto");
        productsAdapter.add("Paquete");
        spProdOPaq.setAdapter(productsAdapter);
    }

    private void fillSpinnerProductos() {
        ArrayAdapter<String> productsAdapter;
        productsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        productsAdapter.add("Seleccione");
        Cursor c = BD.rawQuery("SELECT NOMBRE FROM PRODUCTOS WHERE ESTATUS = 'A' ORDER BY IDPRODUCTO", null);

        if (c.moveToFirst()) {
            do {
                productsAdapter.add(c.getString(0));
            } while (c.moveToNext());
        }
        spProductosPaquetes.setAdapter(productsAdapter);
    }

    private void fillSpinnerPaquetes() {
        ArrayAdapter<String> productsAdapter;
        productsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        productsAdapter.add("Seleccione");
        Cursor c = BD.rawQuery("SELECT distinct IDPAQUETE FROM PAQUETES ORDER BY IDPAQUETE", null);
        if (c.moveToFirst()) {
            do {
                productsAdapter.add(c.getString(0));
            } while (c.moveToNext());
        }
        spProductosPaquetes.setAdapter(productsAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == btnGrabarVenta) {
            if (!validarDatos(true)) return;
            if (existeVenta()) {
                Rutinas.mensajeDialog("La venta ya existe", this);
                return;
            }

            if (spProdOPaq.getSelectedItemPosition() == 2){
                Cursor c = BD.rawQuery("SELECT count(IDPRODUCTO) FROM PAQUETES WHERE IDPAQUETE = " + spProductosPaquetes.getSelectedItemPosition(), null);
                if (c.moveToFirst()) {
                    if (c.getInt(0) < 3) {
                        Rutinas.mensajeDialog("El paquete debe tener al menos 3 productos", this);
                        return;
                    }
                }
            }

            try {
                BD.execSQL("INSERT INTO VENTAS VALUES(" + txtFolio.getText().toString() + ", " +
                        spProductosPaquetes.getSelectedItemPosition() + ", " + spProdOPaq.getSelectedItemPosition() + ", " +
                        txtUnidades.getText().toString() + ", " +
                        txtPrecio.getText().toString() + ")");
                Rutinas.mensajeToast("Venta grabada", this);
            } catch (Exception e) {
                String fullMessage = e.getMessage();
                String customMessage = fullMessage.substring(0, fullMessage.indexOf("(")).trim();
                Rutinas.mensajeDialog(customMessage, this);
            }
            return;
        }
        if (v == btnEliminar) {
            if (!validarDatos(false)) return;
            if (!existeVenta()) {
                Rutinas.mensajeDialog("La venta no existe", this);
                return;
            }

            try {
                EliminarVenta();
            } catch (SQLiteException e) {
                Rutinas.mensajeDialog(e.getMessage(), this);
            }
            Rutinas.mensajeToast("Venta con " + spProdOPaq.getSelectedItem() + ": " + spProductosPaquetes.getSelectedItem() + " eliminada", this);
            return;
        }
        if (v == btnModificarVenta) {
            if (!validarDatos(false)) return;
            if (!existeVenta()) {
                Rutinas.mensajeDialog("La venta no existe", this);
                return;
            }

            try {
                ModificarVenta();
                Rutinas.mensajeToast("Venta modificada", this);
            } catch (SQLiteException e) {
                String fullMessage = e.getMessage();
                String customMessage = fullMessage.substring(0, fullMessage.indexOf("(")).trim();
                Rutinas.mensajeDialog(customMessage, this);
            }
            return;
        }
        if (v == btnRecuperarVenta) {
            if (!validarDatos(false)) return;
            RecuperarVenta();
            return;
        }
        if (v == btnLimpiar) {
            Limpiar();
            return;
        }
        if (v == btnConsultarVentas) {
            ArrayList<Ventas> ventasList = new ArrayList<>();
            Cursor cursor = BD.rawQuery("SELECT * FROM VENTAS ORDER BY FOLIO", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ventasList.add(new Ventas(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4)));
                cursor.moveToNext();
            }
            cursor.close();
            Rutinas.mensajeToast("Se encontraron " + ventasList.size() + " ventas", this);
            Intent intent = new Intent(this, ReciclerView.class);
            intent.putExtra("sellList", ventasList);
            intent.putExtra("source", "Ventas");
            startActivity(intent);
        }
    }

    private boolean existeVenta() {
        Cursor c = BD.rawQuery("SELECT Folio FROM VENTAS WHERE FOLIO = " + txtFolio.getText().toString() + " AND ID = " +
                spProductosPaquetes.getSelectedItemPosition() + " AND PRODUCTO_OR_PAQUETEID = " +
                spProdOPaq.getSelectedItemPosition(), null);
        if (c.moveToFirst()) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    private boolean validarDatos(boolean todos) {
        if (txtFolio.getText().toString().isEmpty()) {
            Rutinas.mensajeDialog("Falta el folio", this);
            return false;
        }
        if (spProdOPaq.getSelectedItemPosition() == 0) {
            Rutinas.mensajeDialog("Seleccionar un tipo: producto o paquete", this);
            return false;
        }
        if (spProductosPaquetes.getSelectedItemPosition() == 0) {
            Rutinas.mensajeDialog("Falta seleccionar un producto o paquete", this);
            return false;
        }
        if (todos) {
            if (txtUnidades.getText().toString().isEmpty()) {
                Rutinas.mensajeDialog("Falta el número de unidades", this);
                return false;
            }
            if (txtPrecio.getText().toString().isEmpty()) {
                Rutinas.mensajeDialog("Falta el precio", this);
                return false;
            }
        }
        return true;
    }

    private void EliminarVenta() {
        BD.execSQL("DELETE FROM VENTAS WHERE FOLIO = " + txtFolio.getText().toString() + " AND ID = " +
                spProductosPaquetes.getSelectedItemPosition() + " AND PRODUCTO_OR_PAQUETEID = " +
                spProdOPaq.getSelectedItemPosition());
    }

    private void ModificarVenta() {
        String sql = "UPDATE VENTAS SET ";
        if (!txtUnidades.getText().toString().isEmpty()) {
            sql += "UNIDADES =" + txtUnidades.getText().toString() + ", ";
        }
        if (!txtPrecio.getText().toString().isEmpty()) {
            sql += "PRECIO =" + txtPrecio.getText().toString() + ", ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql += " WHERE FOLIO = " + txtFolio.getText().toString() + " AND ID = " +
                spProductosPaquetes.getSelectedItemPosition() + " AND PRODUCTO_OR_PAQUETEID = " +
                spProdOPaq.getSelectedItemPosition();
        BD.execSQL(sql);

    }

    private void RecuperarVenta() {
        Cursor c = BD.rawQuery("SELECT * FROM VENTAS WHERE FOLIO = " + txtFolio.getText().toString() + " AND ID = " +
                spProductosPaquetes.getSelectedItemPosition() + " AND PRODUCTO_OR_PAQUETEID = " +
                spProdOPaq.getSelectedItemPosition(), null);
        if (c.moveToFirst()) {
            txtUnidades.setText(c.getString(3));
            txtPrecio.setText(c.getString(4));
        } else {
            Rutinas.mensajeDialog("No se encontró la venta", this);
        }
    }

    private void Limpiar() {
        txtFolio.setText("");
        txtUnidades.setText("");
        txtPrecio.setText("");
        spProductosPaquetes.setSelection(0);
        spProdOPaq.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 1) {
            fillSpinnerProductos();
        } else if (position == 2) {
            fillSpinnerPaquetes();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        spProductosPaquetes.setAdapter(null);
    }
}