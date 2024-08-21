package com.example.cuentaspolizas;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class rud_paquetes extends AppCompatActivity implements View.OnClickListener {
    private EditText txtIdPaquete, txtUnidadesPaquete, txtDescuento;
    private Spinner spProductoPaquete;
    private Button btnConsultarPaquete, btnModificarPaquete, btnEliminarPaquete, btnLimpiarPaquete;
    private BaseDeDatos conexion;
    private SQLiteDatabase BD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rud_paquetes);

        tryConnection();

        txtIdPaquete = (EditText) findViewById(R.id.txtIDPaquete);
        txtUnidadesPaquete = (EditText) findViewById(R.id.txtUnidadesPaquete);
        txtDescuento = (EditText) findViewById(R.id.txtDescuento);
        spProductoPaquete = (Spinner) findViewById(R.id.spProductoPaquete);
        btnConsultarPaquete = (Button) findViewById(R.id.btnConsultarPaquete);
        btnModificarPaquete = (Button) findViewById(R.id.btnModificarPaquete);
        btnEliminarPaquete = (Button) findViewById(R.id.btnEliminarPaquete);
        btnLimpiarPaquete = (Button) findViewById(R.id.btnLimpiarPaquete);

        fillSpinner();

        btnConsultarPaquete.setOnClickListener(this);
        btnModificarPaquete.setOnClickListener(this);
        btnEliminarPaquete.setOnClickListener(this);
        btnLimpiarPaquete.setOnClickListener(this);
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

    private void fillSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.add("Seleccione un producto");

        Cursor cursor = BD.rawQuery("SELECT * FROM productos ORDER BY IDPRODUCTO", null);
        if (cursor.moveToFirst()) {
            do {
                adapter.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        spProductoPaquete.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == btnModificarPaquete) {
            if (!validarDatos(false)) return;

            Cursor cursor = BD.rawQuery("SELECT * FROM paquetes WHERE IDPAQUETE = " + txtIdPaquete.getText().toString() + " AND IDPRODUCTO = " + spProductoPaquete.getSelectedItemPosition() + ";", null);
            if (!cursor.moveToFirst()) {
                Rutinas.mensajeDialog("No se encontró el paquete", this);
                return;
            }

            modificar();
            return;
        }
        if (v == btnEliminarPaquete) {
            if (!validarDatos(false))
                return;
            try {
                Cursor cursor = BD.rawQuery("SELECT * FROM paquetes WHERE IDPAQUETE = " + txtIdPaquete.getText().toString() + " AND IDPRODUCTO = " + spProductoPaquete.getSelectedItemPosition() + ";", null);
                if (!cursor.moveToFirst()) {
                    Rutinas.mensajeDialog("No se encontró el paquete", this);
                    return;
                }
                if (cursor.getInt(2) > 0) {
                    Rutinas.mensajeDialog("No se puede eliminar un paquete con ventas", this);
                    return;
                }

                BD.execSQL("DELETE FROM paquetes WHERE IDPAQUETE = " + txtIdPaquete.getText().toString() + " AND IDPRODUCTO = " + spProductoPaquete.getSelectedItemPosition() + ";");
                Rutinas.mensajeToast("Paquete eliminado", this);
            } catch (Exception e) {
                Rutinas.mensajeDialog(e.getMessage(), this);
            }
            return;
        }
        if (v == btnLimpiarPaquete) {
            limpiar();
            return;
        }
        if (v == btnConsultarPaquete) {
            ArrayList<Paquete> packageList = new ArrayList<>();
            Cursor cursor = BD.rawQuery("SELECT * FROM paquetes ORDER BY IDPAQUETE", null);
            if (cursor.moveToFirst()) {
                do {
                    packageList.add(new Paquete(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3)));
                } while (cursor.moveToNext());
            }
            cursor.close();
                Rutinas.mensajeToast("Paquetes Encontrados " + packageList.size(), this);
                Intent intent = new Intent(this, ReciclerView.class);
                intent.putExtra("packageList", packageList);
                intent.putExtra("source", "paquetes");
                startActivity(intent);

        }
    }

    private boolean validarDatos(boolean todos) {
        if (txtIdPaquete.getText().toString().isEmpty()) {
            Rutinas.mensajeDialog("Debe ingresar el ID del paquete", this);
            return false;
        }
        if (spProductoPaquete.getSelectedItemPosition() == 0) {
            Rutinas.mensajeDialog("Debe seleccionar un producto", this);
            return false;
        }
        if (todos) {
            if (txtUnidadesPaquete.getText().toString().isEmpty()) {
                Rutinas.mensajeDialog("Debe ingresar las unidades del paquete", this);
                return false;
            }
            if (txtDescuento.getText().toString().isEmpty()) {
                Rutinas.mensajeDialog("Debe ingresar el descuento del paquete", this);
                return false;
            }
        }
        return true;
    }

    private void modificar() {
        String sql = "UPDATE PRODUCTOS SET   ";
        if (!txtUnidadesPaquete.getText().toString().isEmpty()) {
            sql += "unidades = " + txtUnidadesPaquete.getText().toString() + ", ";
        }
        if (!txtDescuento.getText().toString().isEmpty()) {
            sql += "descuento = " + txtDescuento.getText().toString() + ", ";
        }
        sql = sql.substring(0, sql.length() - 2);
        sql += " WHERE IDPAQUETE = " + txtIdPaquete.getText().toString()
                + " AND IDPRODUCTO = " + spProductoPaquete.getSelectedItemPosition() + ";";
        try {
            BD.execSQL(sql);
            Rutinas.mensajeToast("Paquete modificado", this);
        } catch (Exception e) {
            Rutinas.mensajeDialog("No se encontró nada para modificar", this);
        }


    }

    private void limpiar() {
        txtIdPaquete.setText("");
        txtUnidadesPaquete.setText("");
        txtDescuento.setText("");
        spProductoPaquete.setSelection(0);
    }

}
