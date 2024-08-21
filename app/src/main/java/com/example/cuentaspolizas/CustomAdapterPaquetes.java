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


public class CustomAdapterPaquetes extends RecyclerView.Adapter<CustomAdapterPaquetes.ViewHolder> {

    private ArrayList<Paquete> localDataSet;
    private static TextView lblIdPaquete, lblIdProducto, lblUnidades, lblDescuento;
    private static ConstraintLayout layout;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            lblIdPaquete = view.findViewById(R.id.idProducto);
            lblIdProducto = view.findViewById(R.id.lblNombre);
            lblUnidades = view.findViewById(R.id.lblImporte);
            lblDescuento = view.findViewById(R.id.lblDescuento);
            layout = view.findViewById(R.id.myLayout);

        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public CustomAdapterPaquetes(ArrayList<Paquete> dataSet) {
        localDataSet = dataSet;
        System.out.println("Total de datos " + localDataSet.size());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_custom_adapter_paquetes, null, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        lblIdPaquete.setText(String.valueOf(localDataSet.get(position).getIdPaquete()));
        lblIdProducto.setText(String.valueOf(localDataSet.get(position).getIdProducto()));
        lblUnidades.setText(String.valueOf(localDataSet.get(position).getUnidades()));
        lblDescuento.setText(String.valueOf(localDataSet.get(position).getDescuento()));

        try {
            GradientDrawable background = (GradientDrawable) layout.getBackground();
                background.setColor(Color.parseColor("#C8E6C9"));
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