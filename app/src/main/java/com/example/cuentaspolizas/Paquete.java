package com.example.cuentaspolizas;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Paquete implements Parcelable {

    private int idPaquete, idProducto, unidades;
    private double descuento;

    public Paquete(int idPaquete, int idProducto, int unidades, double descuento) {
        this.idPaquete = idPaquete;
        this.idProducto = idProducto;
        this.unidades = unidades;
        this.descuento = descuento;
    }

    protected Paquete(Parcel in) {
        idPaquete = in.readInt();
        idProducto = in.readInt();
        unidades = in.readInt();
        descuento = in.readDouble();
    }

    public static final Creator<Paquete> CREATOR = new Creator<Paquete>() {
        @Override
        public Paquete createFromParcel(Parcel in) {
            return new Paquete(in);
        }

        @Override
        public Paquete[] newArray(int size) {
            return new Paquete[size];
        }
    };

    public int getIdPaquete() {
        return idPaquete;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getUnidades() {
        return unidades;
    }

    public double getDescuento() {
        return descuento;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idPaquete);
        dest.writeInt(idProducto);
        dest.writeInt(unidades);
        dest.writeDouble(descuento);
    }
}
