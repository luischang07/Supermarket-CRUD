package com.example.cuentaspolizas;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CustomAdapterProductos extends RecyclerView.Adapter<CustomAdapterProductos.ViewHolder> {

    private ArrayList<Product> localDataSet;
    private static TextView IDProductBD, nameBD, UnitPriceBD, unitsInStock, statusBD;
    private static ConstraintLayout layout;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            IDProductBD = view.findViewById(R.id.idProducto);
            nameBD = view.findViewById(R.id.lblNombre);
            UnitPriceBD = view.findViewById(R.id.lblImporte);
            unitsInStock = view.findViewById(R.id.lblDescuento);
            layout = view.findViewById(R.id.myLayout);
            statusBD = view.findViewById(R.id.lblPrecio);

        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public CustomAdapterProductos(ArrayList<Product> dataSet) {
        localDataSet = dataSet;
        System.out.println("Total de datos " + localDataSet.size());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_custom_adapter, null, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

            IDProductBD.setText(String.valueOf(localDataSet.get(position).getIDProduct()));
            nameBD.setText(localDataSet.get(position).getName());
            UnitPriceBD.setText(String.valueOf(localDataSet.get(position).getUnitPrice()));
            unitsInStock.setText(String.valueOf(localDataSet.get(position).getUnitsInStock()));
            statusBD.setText(String.valueOf(localDataSet.get(position).getStatus()));
        try {
            //Cambiar color de fondo
            GradientDrawable background = (GradientDrawable) layout.getBackground();
            if (localDataSet.get(position).getStatus() == 'A') {
                background.setColor(Color.parseColor("#339ED6"));
            } else {
                background.setColor(Color.parseColor("#DF779C"));
            }
        } catch (Exception e) {
            Rutinas.mensajeDialog("Error en el adapter: " + e.getMessage(), layout.getContext());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}