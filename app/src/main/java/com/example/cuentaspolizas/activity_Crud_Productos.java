package com.example.cuentaspolizas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class activity_Crud_Productos extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Product> productList;
    Button btnGrabar, btnBorrar, btnModificar, btnConsultarProducto, btnLimpiar, btnRecuperar, btnBack;
    EditText txtIdProducto, txtNombreProducto, txtPrecioUnitario, txtUnidades;
    BaseDeDatos conexion;
    SQLiteDatabase BD;
    SwitchMaterial swModificar;

    String sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_productos);

        btnGrabar = findViewById(R.id.btnGrabar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnModificar = findViewById(R.id.btnModificar);
        btnConsultarProducto = findViewById(R.id.btnConsultarProducto);
        btnLimpiar = findViewById(R.id.btnLimpiarPaquete);
        btnRecuperar = findViewById(R.id.btnRecuperar);
        btnBack = findViewById(R.id.btnBack);

        txtIdProducto = findViewById(R.id.txtIDProducto);
        txtNombreProducto = findViewById(R.id.txtProducto);
        txtPrecioUnitario = findViewById(R.id.txtPrecioUnitario);
        txtUnidades = findViewById(R.id.txtUnidades);

        swModificar = findViewById(R.id.swModificar);

        tryConnection();

        btnGrabar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnConsultarProducto.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        btnRecuperar.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        swModificar.setOnClickListener(this);
    }

    private void tryConnection() {
        conexion = new BaseDeDatos(this, "SuperMarket", null, BaseDeDatos.version);
        BD = conexion.getWritableDatabase();
        if (BD == null) {
            Rutinas.mensajeDialog("La BD no se ha establecido para lectura y escritura", this);
            return;
        }
        Rutinas.mensajeToast("conexion exitosa", this);
    }

    @Override
    public void onClick(View v) {
        sql = "";
        if (v == btnLimpiar) {
            Limpiar();
            return;
        }
        if (v == swModificar) {
            if (swModificar.isChecked()) {
                txtIdProducto.setText("");
                txtIdProducto.setEnabled(true);
                txtIdProducto.requestFocus();
                txtNombreProducto.setEnabled(true);
                txtPrecioUnitario.setEnabled(true);
                txtUnidades.setEnabled(true);
                btnGrabar.setEnabled(false);
                btnModificar.setEnabled(true);
                btnBorrar.setEnabled(true);
                btnRecuperar.setEnabled(true);
                return;
            }
            txtIdProducto.setText("*");
            txtIdProducto.setEnabled(false);
            btnBorrar.setEnabled(false);
            btnGrabar.setEnabled(true);
            btnModificar.setEnabled(false);
            btnRecuperar.setEnabled(false);
            return;
        }

        if (btnBack == v) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return;
        }
        if (v == btnGrabar) {
            if (validarDatosFaltantes())
                return;
            if (productExists(txtNombreProducto.getText().toString())) {
                Rutinas.mensajeDialog("El producto " + txtNombreProducto.getText().toString() + " ya existe", this);
                txtIdProducto.requestFocus();
                return;
            }
            try {
                // Insertar el producto
                sql = "INSERT INTO PRODUCTOS (NOMBRE, PRECIOUNITARIO, UNIDADES)" +
                        "VALUES ('"+txtNombreProducto.getText().toString() +"', "
                        + txtPrecioUnitario.getText().toString() + ", "
                        + txtUnidades.getText().toString() + ")";
                BD.execSQL(sql);
                //obtener el id insertado
                sql = "SELECT MAX(IDPRODUCTO) FROM PRODUCTOS";
                Cursor cursor = BD.rawQuery(sql, null);
                cursor.moveToFirst();
                txtIdProducto.setText(cursor.getString(0));
                Rutinas.mensajeToast("Producto " + txtIdProducto.getText().toString() + " grabado correctamente", this);

                disable();

                cursor.close();
                return;
            } catch (Exception e) {
                Rutinas.mensajeDialog(e.getMessage(), this);
            }
        }
        if (v == btnBorrar) {
            if (txtIdProducto.getText().toString().isEmpty()) {
                Rutinas.mensajeDialog("ID del producto obligatorio", this);
                txtIdProducto.requestFocus();
                return;
            }

            if (!productExists(Integer.parseInt(txtIdProducto.getText().toString()))) {
                Rutinas.mensajeDialog("El Producto esta inactivo o no existe ", this);
                txtIdProducto.requestFocus();
                return;
            }

            borrar(Integer.parseInt(txtIdProducto.getText().toString()));
            return;
        }
        if (v == btnModificar) {
            if (txtIdProducto.getText().toString().isEmpty()) {
                Rutinas.mensajeDialog("Falta el ID del producto", this);
                txtIdProducto.requestFocus();
                return;
            }

            if (!productExists(Integer.parseInt(txtIdProducto.getText().toString()))) {
                Rutinas.mensajeDialog("El producto esta inactivo o no existe ", this);
                txtIdProducto.requestFocus();
                return;
            }

            sql = "UPDATE PRODUCTOS SET   ";
            if (!txtNombreProducto.getText().toString().isEmpty())
                sql += "NOMBRE = '" + txtNombreProducto.getText().toString() + "' , ";
            if (!txtPrecioUnitario.getText().toString().isEmpty())
                sql += "PRECIOUNITARIO = " + txtPrecioUnitario.getText().toString() + " , ";
            if (!txtUnidades.getText().toString().isEmpty())
                sql += "UNIDADES = " + txtUnidades.getText().toString() + " , ";
            sql = sql.substring(0, sql.length() - 2);
            System.out.println(sql);
            sql += "WHERE IDPRODUCTO = " + txtIdProducto.getText().toString();

            try {
                BD.execSQL(sql);
                Rutinas.mensajeToast("Producto modificado correctamente ", this);
                Limpiar();
            } catch (Exception e) {
                Rutinas.mensajeDialog(e.getMessage(), this);

            }
        }
        if (v == btnRecuperar) {
            if (txtIdProducto.getText().toString().isEmpty()) {
                Rutinas.mensajeDialog("Falta el ID del producto", this);
                txtIdProducto.requestFocus();
                return;
            }

//          Buscar el producto
            sql = "SELECT IDPRODUCTO FROM PRODUCTOS WHERE IDPRODUCTO = " + txtIdProducto.getText().toString() + " AND ESTATUS = 'A'";
            Cursor cursor = BD.rawQuery(sql, null);
            if (cursor.getCount() == 0) {
                Rutinas.mensajeDialog("El producto " + txtIdProducto.getText().toString() + " esta inactivo o no existe", this);
                txtIdProducto.requestFocus();
                cursor.close();
                return;
            }

            sql = "SELECT * FROM PRODUCTOS WHERE IDPRODUCTO = " + txtIdProducto.getText().toString();
            cursor = BD.rawQuery(sql, null);
            cursor.moveToFirst();
            txtNombreProducto.setText(cursor.getString(1));
            txtPrecioUnitario.setText(cursor.getString(2));
            txtUnidades.setText(cursor.getString(3));
            cursor.close();
            Rutinas.mensajeToast("Producto recuperado", this);
            return;
        }
        if (v == btnConsultarProducto) {
            productList = new ArrayList<>();
            Cursor cursor = BD.rawQuery("SELECT * FROM PRODUCTOS order by IDPRODUCTO", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                productList.add(new Product(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getInt(3), cursor.getString(4).charAt(0)));
                cursor.moveToNext();
            }
            cursor.close();
            if (productList.size() == 0) {
                Rutinas.mensajeDialog("No hay productos registrados", this);
                return;
            }
            Rutinas.mensajeToast("Se encontraron " + productList.size() + " registros", this);
            Intent intent = new Intent(this, ReciclerView.class);
            intent.putExtra("productList", productList);
            intent.putExtra("source", "Productos");
            startActivity(intent);
        }
    }

    private void disable() {
        txtIdProducto.setEnabled(false);
        txtNombreProducto.setEnabled(false);
        txtPrecioUnitario.setEnabled(false);
        txtUnidades.setEnabled(false);

        btnGrabar.setEnabled(false);
    }

    private boolean productExists(int IDProducto) {
        sql = "SELECT IDPRODUCTO FROM PRODUCTOS WHERE IDPRODUCTO = " + IDProducto + " AND ESTATUS = 'A'";
        Cursor cursor = BD.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
    private boolean productExists(String nombre) {
        sql = "SELECT NOMBRE FROM PRODUCTOS WHERE NOMBRE = '" + nombre + "'";
        Cursor cursor = BD.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean validarDatosFaltantes() {
        if (txtNombreProducto.getText().toString().isEmpty()) {
            Rutinas.mensajeDialog("Falta el nombre del producto", this);
            txtNombreProducto.requestFocus();
            return true;
        }
        if (txtPrecioUnitario.getText().toString().isEmpty()) {
            Rutinas.mensajeDialog("Falta el precio del producto", this);
            txtPrecioUnitario.requestFocus();
            return true;
        }
        if (txtUnidades.getText().toString().isEmpty()) {
            Rutinas.mensajeDialog("Falta el numero de unidades", this);
            txtUnidades.requestFocus();
            return true;
        }
        return false;
    }

    private void borrar(int IDProduct) {
        sql = "UPDATE PRODUCTOS SET ESTATUS = 'B' WHERE IDPRODUCTO = " + IDProduct;
        try {
            BD.execSQL(sql);
            Rutinas.mensajeToast("Producto borrado correctamente", this);
        } catch (Exception e) {
            Rutinas.mensajeDialog("Error al borrar", this);
        }
    }

    private void Limpiar() {
        if (swModificar.isChecked()) {
            btnGrabar.setEnabled(false);
            txtIdProducto.setText("");
            txtIdProducto.setEnabled(true);
            txtIdProducto.requestFocus();
        } else {
            btnGrabar.setEnabled(true);
            txtIdProducto.setText("*");
        }
        txtNombreProducto.setText("");
        txtPrecioUnitario.setText("");
        txtUnidades.setText("");
        txtNombreProducto.setEnabled(true);
        txtPrecioUnitario.setEnabled(true);
        txtUnidades.setEnabled(true);
    }
}