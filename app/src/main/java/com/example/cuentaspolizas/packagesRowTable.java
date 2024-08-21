package com.example.cuentaspolizas;

import android.widget.EditText;
import android.widget.Spinner;

public class packagesRowTable {
    public Spinner spinner;
    public EditText units;
    public EditText discount;

    public packagesRowTable(Spinner spinner, EditText units, EditText discount) {
        this.spinner = spinner;
        this.units = units;
        this.discount = discount;
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public void setSpinner(Spinner spinner) {
        this.spinner = spinner;
    }

    public EditText getUnits() {
        return units;
    }

    public void setUnits(EditText units) {
        this.units = units;
    }

    public EditText getDiscount() {
        return discount;
    }

    public void setDiscount(EditText discount) {
        this.discount = discount;
    }
}