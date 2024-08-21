package com.example.cuentaspolizas;

import java.io.Serializable;

public class Consulta3 implements Serializable {

//    String query = "SELECT P.IDPRODUCTO, P.NOMBRE, SUM(V.UNIDADES * V.PRECIO) AS IMPORTE_TOTAL " +
    int IDPRODUCTO;
    String NOMBRE;
    double IMPORTE_TOTAL;

    public Consulta3(int IDPRODUCTO, String NOMBRE, double IMPORTE_TOTAL) {
        this.IDPRODUCTO = IDPRODUCTO;
        this.NOMBRE = NOMBRE;
        this.IMPORTE_TOTAL = IMPORTE_TOTAL;
    }

    public int getIdProducto() {
        return IDPRODUCTO;
    }

    public String getNombre() {
        return NOMBRE;
    }

    public double getImporteTotal() {
        return IMPORTE_TOTAL;
    }
}
