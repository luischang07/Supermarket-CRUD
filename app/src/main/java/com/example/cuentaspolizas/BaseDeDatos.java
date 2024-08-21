package com.example.cuentaspolizas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDeDatos extends SQLiteOpenHelper {
    static int version = 1;
    String productos = "CREATE TABLE PRODUCTOS (" +
            "IDPRODUCTO INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "NOMBRE TEXT UNIQUE," +
            "PRECIOUNITARIO DECIMAL, " +
            "UNIDADES INTEGER, " +
            "ESTATUS CHAR(1) DEFAULT ('A')" +
            ")";
    //                Tabla paquetes de conjunto de productos
    String paquetes = "CREATE TABLE PAQUETES (" +
            "IDPAQUETE INTEGER," +
            "IDPRODUCTO INTEGER, " +
            "UNIDADES INTEGER, " +
            "DESCUENTO INTEGER CHECK(DESCUENTO BETWEEN 0 AND 100), " +
            "PRIMARY KEY (IDPAQUETE, IDPRODUCTO) " +
            ");";
    //    FK CON PRODUCTOS Y PAQUETES
    String ventas = "CREATE TABLE VENTAS (" +
            "FOLIO INTEGER, " +
            "ID INTEGER, " +
            "PRODUCTO_OR_PAQUETEID INTEGER CHECK(PRODUCTO_OR_PAQUETEID IN (1, 2)), " +
            "UNIDADES INTEGER CHECK(UNIDADES > 0), " +
            "PRECIO DECIMAL CHECK(PRECIO > 0), " +
            "PRIMARY KEY (FOLIO, ID, PRODUCTO_OR_PAQUETEID))";

    public void createTriggers(SQLiteDatabase db) {
        // Trigger for products
        db.execSQL("CREATE TRIGGER VerificarStockProductos BEFORE INSERT ON VENTAS " +
                "FOR EACH ROW " +
                "BEGIN " +
                "   SELECT CASE " +
                "       WHEN NEW.PRODUCTO_OR_PAQUETEID = 1 THEN " +
                "           CASE WHEN EXISTS (SELECT 1 FROM PRODUCTOS " +
                "                             WHERE IDPRODUCTO = NEW.ID " +
                "                             AND UNIDADES < NEW.UNIDADES) " +
                "           THEN RAISE(ABORT, 'No hay suficientes unidades del producto') " +
                "           END " +
                "   END; " +
                "END;");
        // for update
        db.execSQL("CREATE TRIGGER VerificarStockProductosUpd BEFORE UPDATE OF UNIDADES ON VENTAS " +
                "FOR EACH ROW " +
                "BEGIN " +
                "   SELECT CASE " +
                "       WHEN NEW.PRODUCTO_OR_PAQUETEID = 1 THEN " +
                "           CASE WHEN EXISTS (SELECT 1 FROM PRODUCTOS " +
                "                             WHERE IDPRODUCTO = NEW.ID " +
                "                             AND UNIDADES < NEW.UNIDADES) " +
                "           THEN RAISE(ABORT, 'No hay suficientes unidades del producto') " +
                "           END " +
                "   END; " +
                "END;");

        db.execSQL("CREATE TRIGGER VerificarStockPaquetes BEFORE INSERT ON VENTAS " +
                "FOR EACH ROW " +
                "BEGIN " +
                "   SELECT CASE " +
                "       WHEN NEW.PRODUCTO_OR_PAQUETEID = 2 THEN " +
                "           CASE WHEN EXISTS (SELECT 1 FROM PAQUETES " +
                "                             JOIN PRODUCTOS ON PAQUETES.IDPRODUCTO = PRODUCTOS.IDPRODUCTO " +
                "                             WHERE PAQUETES.IDPAQUETE = NEW.ID " +
                "                             AND PRODUCTOS.UNIDADES < PAQUETES.UNIDADES * NEW.UNIDADES) " +
                "           THEN RAISE(ABORT, 'No hay suficiente stock para uno o más productos en el paquete') " +
                "           END " +
                "   END; " +
                "END;");
        // for update
        db.execSQL("CREATE TRIGGER VerificarStockPaquetesUpd BEFORE UPDATE OF UNIDADES ON VENTAS " +
                "FOR EACH ROW " +
                "BEGIN " +
                "   SELECT CASE " +
                "       WHEN NEW.PRODUCTO_OR_PAQUETEID = 2 THEN " +
                "           CASE WHEN EXISTS (SELECT 1 FROM PAQUETES " +
                "                             JOIN PRODUCTOS ON PAQUETES.IDPRODUCTO = PRODUCTOS.IDPRODUCTO " +
                "                             WHERE PAQUETES.IDPAQUETE = NEW.ID " +
                "                             AND PRODUCTOS.UNIDADES < PAQUETES.UNIDADES * NEW.UNIDADES) " +
                "           THEN RAISE(ABORT, 'No hay suficiente stock para uno o más productos en el paquete') " +
                "           END " +
                "   END; " +
                "END;");


        // Trigger for products
        db.execSQL("CREATE TRIGGER ReduceStockVentasProductos AFTER INSERT ON VENTAS " +
                "FOR EACH ROW BEGIN " +
                "UPDATE PRODUCTOS SET UNIDADES = UNIDADES - NEW.UNIDADES " +
                "WHERE IDPRODUCTO = NEW.ID AND NEW.PRODUCTO_OR_PAQUETEID = 1; " +
                "END;");


        // Trigger for packageS
        db.execSQL("CREATE TRIGGER ActualizarStockPaquetes AFTER INSERT ON VENTAS " +
                "FOR EACH ROW " +
                "BEGIN " +
                "       UPDATE PRODUCTOS SET UNIDADES = UNIDADES - " +
                "           (SELECT PAQUETES.UNIDADES * NEW.UNIDADES FROM PAQUETES " +
                "            WHERE PAQUETES.IDPRODUCTO = PRODUCTOS.IDPRODUCTO AND PAQUETES.IDPAQUETE = NEW.ID) " +
                "       WHERE IDPRODUCTO IN (SELECT IDPRODUCTO FROM PAQUETES WHERE IDPAQUETE = NEW.ID) " +
                "       AND NEW.PRODUCTO_OR_PAQUETEID = 2; " +
                "END;");
    }

    public BaseDeDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(productos);
        db.execSQL(paquetes);
        db.execSQL(ventas);
        createTriggers(db);

        System.out.println("constrtuctor");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PRODUCTOS");
        db.execSQL("DROP TABLE IF EXISTS paquetes");
        db.execSQL("DROP TABLE IF EXISTS ventas");
        db.execSQL("DROP TRIGGER IF EXISTS VerificarStockProductos");
        db.execSQL("DROP TRIGGER IF EXISTS VerificarStockProductosUpd");
        db.execSQL("DROP TRIGGER IF EXISTS VerificarStockPaquetes");
        db.execSQL("DROP TRIGGER IF EXISTS VerificarStockPaquetesUpd");
        db.execSQL("DROP TRIGGER IF EXISTS ReduceStockVentasProductos");
        db.execSQL("DROP TRIGGER IF EXISTS ActualizarStockPaquetes");



        db.execSQL(productos);
        db.execSQL(paquetes);
        db.execSQL(ventas);
        createTriggers(db);
        System.out.println("actualizacion");
    }
}

