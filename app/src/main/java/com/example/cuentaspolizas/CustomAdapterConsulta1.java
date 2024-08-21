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


public class CustomAdapterConsulta1 extends RecyclerView.Adapter<CustomAdapterConsulta1.ViewHolder> {

    private ArrayList<Integer> localDataSet;
    private static TextView IDIntegerBD;
    private static ConstraintLayout layout;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            IDIntegerBD = view.findViewById(R.id.producto);

        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public CustomAdapterConsulta1(ArrayList<Integer> dataSet) {
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

        IDIntegerBD.setText(String.valueOf(localDataSet.get(position)));
        try {
            //Cambiar color de fondo
            GradientDrawable background = (GradientDrawable) layout.getBackground();
            background.setColor(Color.parseColor("#339ED6"));
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