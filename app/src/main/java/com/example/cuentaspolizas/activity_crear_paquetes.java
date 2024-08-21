package com.example.cuentaspolizas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class activity_crear_paquetes extends AppCompatActivity implements View.OnClickListener {
    private static final int MINPRODUCTS = 3;
    Button btnGrabarP, btnLimpiarP, btnEliminarFila, btnAgregarFila, btnModificarPaquetes;
    EditText txtPaqueteID;
    ArrayAdapter<String> productsAdapter;
    ArrayList<packagesRowTable> rows;
    BaseDeDatos conexion;
    SQLiteDatabase BD;
    String sql = "";
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_paquetes);

        tryConnection();

        rows = new ArrayList<>(3);

        fillArrayForSpinner();
        fillInitialTable();

        txtPaqueteID = findViewById(R.id.txtProducto);
        // Botones
        btnAgregarFila = findViewById(R.id.btnAgregarFila);
        btnEliminarFila = findViewById(R.id.btnEliminarFila);
        btnLimpiarP = findViewById(R.id.btnLimpiarP);
        btnGrabarP = findViewById(R.id.btnGrabarP);

        btnModificarPaquetes = findViewById(R.id.btnModificarPaquetes);

        btnAgregarFila.setOnClickListener(this);
        btnEliminarFila.setOnClickListener(this);
        btnLimpiarP.setOnClickListener(this);
        btnGrabarP.setOnClickListener(this);
        btnModificarPaquetes.setOnClickListener(this);
    }

    private void fillInitialTable() {
        tableLayout = findViewById(R.id.tableLayout);
        TableRow headerRow = new TableRow(this);
        headerRow.setGravity(Gravity.CENTER);
        // Header
        for (int i = 0; i < 3; i++) {
            TextView headerTextView = getHeaderTextView(i, i == 0 ? 2f : 1f);
            headerRow.addView(headerTextView);
        }
        tableLayout.addView(headerRow);
        // Rows
        for (int i = 0; i < MINPRODUCTS; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);
            tableRow.setPadding(20, 20, 20, 0);

            Spinner spinner = createSpinner();
            tableRow.addView(spinner);

            EditText units = createEditText(2);
            tableRow.addView(units);

            EditText discount = createEditText(3);
            tableRow.addView(discount);

            tableLayout.addView(tableRow);

            rows.add(new packagesRowTable(spinner, units, discount));
        }
    }

    private void fillArrayForSpinner() {
        Cursor c = BD.rawQuery("SELECT NOMBRE FROM PRODUCTOS WHERE ESTATUS = 'A' ORDER BY NOMBRE", null);
        ArrayList<String> productos = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                productos.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        productsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productos);
        productsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productos.add(0, "Seleccionar");
    }

    private Spinner createSpinner() {
        Spinner spinner = new Spinner(this);
        spinner.setAdapter(productsAdapter);
        spinner.setGravity(Gravity.CENTER);
        spinner.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
        return spinner;
    }

    private EditText createEditText(int lengthFilter) {
        EditText editText = new EditText(this);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(lengthFilter)});
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setGravity(Gravity.CENTER);
        editText.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        return editText;
    }

    @NonNull
    private TextView getHeaderTextView(int i, float peso) {
        TextView headerTextView = new TextView(this);
        headerTextView.setText(i == 0 ? "PRODUCTO" : (i == 1 ? "UNIDADES" : "DESCUENTO"));
        headerTextView.setGravity(Gravity.CENTER);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0, // Width
                ViewGroup.LayoutParams.WRAP_CONTENT, // Altura
                peso // Establecer el peso del diseño en 1
        );

        headerTextView.setLayoutParams(params);
        return headerTextView;
    }

    private void addRow() {
        TableRow tableRow = new TableRow(this);
        tableRow.setGravity(Gravity.CENTER);
        tableRow.setPadding(20, 20, 20, 0);

        Spinner spinner = createSpinner();
        tableRow.addView(spinner);

        EditText units = createEditText(2);
        tableRow.addView(units);

        EditText discount = createEditText(3);
        tableRow.addView(discount);

        tableLayout.addView(tableRow);

        rows.add(new packagesRowTable(spinner, units, discount));
        return;
    }

    private void deleteRow() {
        // Verificar si hay filas para eliminar
        if (rows.size() > MINPRODUCTS) {
            int lastIndex = tableLayout.getChildCount() - 1; // Índice de la última fila
            tableLayout.removeViewAt(lastIndex);
            rows.remove(rows.size() - 1);

        } else {
            // Mostrar un mensaje si no hay filas para eliminar
            Rutinas.mensajeDialog("Minimo 3 productos", this);
        }
    }

    @Override
    public void onClick(View v) {
        sql = "";
        if (v == btnLimpiarP) {
            Limpiar();
            return;
        }
        if (v == btnAgregarFila) {
            addRow();
            return;
        }
        if (v == btnEliminarFila) {
            deleteRow();
            return;
        }
        if (v == btnGrabarP) {
            try {
                if (!validaciones()) {
                    return;
                }

                // insertar en la base de datos
                insertBD();

                Rutinas.mensajeDialog("Paquete " + txtPaqueteID.getText().toString() + " guardado", this);
//                Limpiar();
            } catch (Exception e) {
                //por si truena
                Rutinas.mensajeDialog(e.toString(), this);
            }
        }
        if (v == btnModificarPaquetes) {
//            Cursor c = BD.rawQuery("SELECT IDPAQUETE FROM PAQUETES ", null);
//            if (c.getCount() == 0) {
//                Rutinas.mensajeDialog("No hay paquetes", this);
//                return;
//            }
            Intent intent = new Intent(this, rud_paquetes.class);
            startActivity(intent);
        }
    }

    private boolean validaciones() {
        // ID no vacio
        if (TextUtils.isEmpty(txtPaqueteID.getText().toString())) {
            Rutinas.mensajeDialog("Se requiere el ID del paquete", this);
            return false;
        }

        // validar que todos los productos han sido seleccionados
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getSpinner().getSelectedItem().toString().equals("Seleccionar")) {
                Rutinas.mensajeDialog("Seleccione los productos", this);
                return false;
            }
        }

        // validar que el paquete no exista
        Cursor c = BD.rawQuery("SELECT IDPAQUETE " +
                "FROM PAQUETES " +
                "WHERE IDPAQUETE = " + txtPaqueteID.getText().toString(), null);
        if (c.getCount() > 0) {
            Rutinas.mensajeDialog("El paquete + " + txtPaqueteID.getText().toString() + " ya existe", this);
            return false;
        }
        c.close();

        // al menos 3 productos
        if (rows.size() < MINPRODUCTS) {
            Rutinas.mensajeDialog("Inserte al menos 3 productos", this);
            return false;
        }

//              validar que no haya productos repetidos
        for (int i = 0; i < rows.size(); i++) {
            for (int j = i + 1; j < rows.size(); j++) {
                if (rows.get(i).getSpinner().getSelectedItem().toString().equals(rows.get(j).getSpinner().getSelectedItem().toString())) {
                    Rutinas.mensajeDialog("No puede haber productos repetidos", this);
                    return false;
                }
            }
        }

        // validar que no haya productos con unidades o descuento vacios
        for (int i = 0; i < rows.size(); i++) {
            if (TextUtils.isEmpty(rows.get(i).getUnits().getText().toString()) || TextUtils.isEmpty(rows.get(i).getDiscount().getText().toString())) {
                Rutinas.mensajeDialog("No puede haber productos con unidades o descuento vacios", this);
                return false;
            }
        }

        // validar que las unidades totales sean mayores a 10
        int totalUnits = 0;
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getUnits().getText().toString().equals("0")) {
                Rutinas.mensajeDialog("Minimo una unidad por paquete", this);
                return false;
            }
            totalUnits += Integer.parseInt(rows.get(i).getUnits().getText().toString());
        }
        if (totalUnits < 10) {
            Rutinas.mensajeDialog("Las unidades totales deben ser mayores a 10", this);
            return false;
        }

        // validar que haya stock suficiente
        for (int i = 0; i < rows.size(); i++) {
            Cursor c2 = BD.rawQuery("SELECT UNIDADES " +
                    "FROM PRODUCTOS " +
                    "WHERE NOMBRE = '" + rows.get(i).getSpinner().getSelectedItem().toString()
                    + "'", null);
            if (c2.moveToFirst()) {
                if (Integer.parseInt(rows.get(i).getUnits().getText().toString()) > c2.getInt(0)) {
                    Rutinas.mensajeDialog("No hay stock suficiente de " +
                            rows.get(i).getSpinner().getSelectedItem().toString(), this);
                    return false;
                }
            }
            c2.close();
        }
        return true;
    }

    private void insertBD() {
        sql = "INSERT INTO PAQUETES VALUES ('" + txtPaqueteID.getText().toString() + "', ";
        BD.beginTransaction();
        try {
            for (int i = 0; i < rows.size(); i++) {
                insertarRegistroEnPaquetes(sql,
                        rows.get(i).getSpinner().getSelectedItem().toString(), // IDPRODUCTO
                        rows.get(i).getUnits().getText().toString(), // UNIDADES
                        rows.get(i).getDiscount().getText().toString()); // DESCUENTO
            }
            BD.setTransactionSuccessful();
        } catch (Exception e) {
            Rutinas.mensajeDialog(e.getMessage(), this);
        } finally {
            BD.endTransaction();
        }
    }

    private void insertarRegistroEnPaquetes(String sql, String producto, String unidades, String descuento) {
        sql += "(SELECT IDPRODUCTO FROM PRODUCTOS WHERE NOMBRE = '" + producto + "'), "
                + unidades + ", " + descuento + ")";
        BD.execSQL(sql);
    }

    private void Limpiar() {
        txtPaqueteID.setText("");
        for (int i = 0; i < rows.size(); i++) {
            rows.get(i).getSpinner().setSelection(0);
            rows.get(i).getUnits().setText("");
            rows.get(i).getDiscount().setText("");
        }

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
}