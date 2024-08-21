package com.example.cuentaspolizas;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Ventas implements Parcelable {
    int folio;
    int id;
    int producto_or_paqueteid;
    int unidades;
    double precio;

    public Ventas(int folio, int id, int producto_or_paqueteid, int unidades, double precio) {
        this.folio = folio;
        this.id = id;
        this.producto_or_paqueteid = producto_or_paqueteid;
        this.unidades = unidades;
        this.precio = precio;
    }

    protected Ventas(Parcel in) {
        folio = in.readInt();
        id = in.readInt();
        producto_or_paqueteid = in.readInt();
        unidades = in.readInt();
        precio = in.readDouble();
    }

    public static final Creator<Ventas> CREATOR = new Creator<Ventas>() {
        @Override
        public Ventas createFromParcel(Parcel in) {
            return new Ventas(in);
        }

        @Override
        public Ventas[] newArray(int size) {
            return new Ventas[size];
        }
    };

    public int getFolio() {
        return folio;
    }

    public int getId() {
        return id;
    }

    public int getProducto_or_paqueteid() {
        return producto_or_paqueteid;
    }

    public int getUnidades() {
        return unidades;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(folio);
        dest.writeInt(id);
        dest.writeInt(producto_or_paqueteid);
        dest.writeInt(unidades);
        dest.writeDouble(precio);
    }
}
