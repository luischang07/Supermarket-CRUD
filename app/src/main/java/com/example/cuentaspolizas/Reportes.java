package com.example.cuentaspolizas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Reportes extends AppCompatActivity implements View.OnClickListener {

    Button btnConsulta1, btnConsulta2, btnConsultaInd3, btnConsultaPaq3;
    BaseDeDatos baseDeDatos;
    SQLiteDatabase BD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reportes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tryConnection();

        btnConsulta1 = findViewById(R.id.btnConsulta1);
        btnConsulta2 = findViewById(R.id.btnConsulta2);
        btnConsultaInd3 = findViewById(R.id.btnConsultaInd3);
        btnConsultaPaq3 = findViewById(R.id.btnConsultaPaq3);

        btnConsulta1.setOnClickListener(this);
        btnConsulta2.setOnClickListener(this);
        btnConsultaInd3.setOnClickListener(this);
        btnConsultaPaq3.setOnClickListener(this);
    }

    private void tryConnection() {
        baseDeDatos = new BaseDeDatos(this, "CuentasPolizas", null, 1);
        BD = baseDeDatos.getWritableDatabase();
        if (BD == null) {
            BD = baseDeDatos.getReadableDatabase();
        }
        if (BD == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos");
        }
    }

    //Usar el recicler view para mostrar los datos
    @Override
    public void onClick(View v) {
        if (v == btnConsulta1) {
            ArrayList<Integer> productosNoVendidos = new ArrayList<>();
            Cursor cursor = getProductosPaquetesNoVendidos(BD);

            if (cursor.moveToFirst()) {
                System.out.println("Cursor: " + cursor.getCount());
                do {
                    productosNoVendidos.add(cursor.getInt(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
            Rutinas.mensajeToast("Productos no vendidos: " + productosNoVendidos.size(), this);
            Intent intent = new Intent(this, ReciclerView.class);
            intent.putExtra("productosNoVendidos", productosNoVendidos);
            intent.putExtra("source", "productosNoVendidos");

            startActivity(intent);
            return;
        }
        if (v == btnConsulta2) {
            ArrayList<Integer> paquetesConsulta = new ArrayList<>();
            Cursor cursor = getPaquetesRequierenMasDeTresProductosYDiezUnidades(BD);
            if (cursor.moveToFirst()) {
                do {
                    paquetesConsulta.add(cursor.getInt(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
            Rutinas.mensajeToast("Paquetes que requieren más de tres productos y diez unidades: " + paquetesConsulta.size(), this);
            Intent intent = new Intent(this, ReciclerView.class);
            intent.putExtra("paquetes", paquetesConsulta);
            intent.putExtra("source", "paquetesConsulta");

            startActivity(intent);
            return;
        }
        if (v == btnConsultaInd3) {
            ArrayList<Consulta3> productosIndividuales = new ArrayList<>();

            Cursor cursor = getImporteVentasProductosIndividuales(BD);
            if (cursor.moveToFirst()) {
                do {
                    productosIndividuales.add(new Consulta3(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2)));
                } while (cursor.moveToNext());
            }
            cursor.close();
            Rutinas.mensajeToast("Importe de ventas de productos individuales: " + productosIndividuales.size(), this);
            Intent intent = new Intent(this, ReciclerView.class);
            intent.putExtra("productosIndividuales", productosIndividuales);
            intent.putExtra("source", "productosIndividuales");

            startActivity(intent);
            return;

        }
        if (v == btnConsultaPaq3) {
            ArrayList<Consulta3> productosPaquetes = new ArrayList<>();
            Cursor cursor = getImporteVentasProductosEnPaquetes(BD);
            if (cursor.moveToFirst()) {
                do {
                    productosPaquetes.add(new Consulta3(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2)));
                } while (cursor.moveToNext());
            }
            cursor.close();
            Rutinas.mensajeToast("Importe de ventas de productos en paquetes: " + productosPaquetes.size(), this);
            Intent intent = new Intent(this, ReciclerView.class);
            intent.putExtra("productosPaquetes", productosPaquetes);
            intent.putExtra("source", "productosPaquetes");

            startActivity(intent);
        }
    }

    public Cursor getProductosPaquetesNoVendidos(SQLiteDatabase db) {
        String query = "SELECT IDPRODUCTO " +
                "FROM PRODUCTOS " +
                "WHERE IDPRODUCTO NOT IN (SELECT IDPRODUCTO FROM PAQUETES) " +
                "AND IDPRODUCTO NOT IN (SELECT ID FROM VENTAS WHERE PRODUCTO_OR_PAQUETEID = 1)";
        return db.rawQuery(query, null);
    }

    public Cursor getPaquetesRequierenMasDeTresProductosYDiezUnidades(SQLiteDatabase db) {
        String query = "SELECT IDPAQUETE " +
                "FROM PAQUETES " +
                "GROUP BY IDPAQUETE " +
                "HAVING COUNT(IDPRODUCTO) > 3 AND SUM(UNIDADES) > 10";
        return db.rawQuery(query, null);
    }

    public Cursor getImporteVentasProductosIndividuales(SQLiteDatabase db) {
        String query = "SELECT P.IDPRODUCTO, P.NOMBRE, SUM(V.UNIDADES * V.PRECIO) AS IMPORTE_TOTAL " +
                "FROM PRODUCTOS P " +
                "JOIN VENTAS V ON P.IDPRODUCTO = V.ID " +
                "WHERE V.PRODUCTO_OR_PAQUETEID = 1 " +
                "GROUP BY P.IDPRODUCTO, P.NOMBRE";
        return db.rawQuery(query, null);
    }

    public Cursor getImporteVentasProductosEnPaquetes(SQLiteDatabase db) {
        String query = "SELECT P.IDPRODUCTO, P.NOMBRE, SUM(V.UNIDADES * (P.PRECIOUNITARIO * (1 - (PAQ.DESCUENTO / 100.0)))) AS IMPORTE_TOTAL " +
                "FROM PRODUCTOS P " +
                "JOIN PAQUETES PAQ ON P.IDPRODUCTO = PAQ.IDPRODUCTO " +
                "JOIN VENTAS V ON PAQ.IDPAQUETE = V.ID " +
                "WHERE V.PRODUCTO_OR_PAQUETEID = 2 " +
                "GROUP BY P.IDPRODUCTO, P.NOMBRE";
        return db.rawQuery(query, null);
    }

    public Cursor getImporteVentas(SQLiteDatabase db, int productoOrPaqueteId) {
        String query = "SELECT P.IDPRODUCTO, P.NOMBRE, " +
                "CASE " +
                "WHEN V.PRODUCTO_OR_PAQUETEID = 1 THEN SUM(V.UNIDADES * V.PRECIO) " +
                "WHEN V.PRODUCTO_OR_PAQUETEID = 2 THEN SUM(V.UNIDADES * (P.PRECIOUNITARIO * (1 - (PAQ.DESCUENTO / 100.0)))) " +
                "END AS IMPORTE_TOTAL " +
                "FROM PRODUCTOS P " +
                "JOIN VENTAS V ON P.IDPRODUCTO = V.ID " +
                "LEFT JOIN PAQUETES PAQ ON P.IDPRODUCTO = PAQ.IDPRODUCTO " +
                "WHERE V.PRODUCTO_OR_PAQUETEID = ? " +
                "GROUP BY P.IDPRODUCTO, P.NOMBRE";
        return db.rawQuery(query, new String[]{String.valueOf(productoOrPaqueteId)});
    }

}