package com.example.cuentaspolizas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CustomAdapterConsulta3 extends RecyclerView.Adapter<CustomAdapterConsulta3.ViewHolder> {

    private final ArrayList<Consulta3> localDataSet;
    private static TextView idProducto, lblNombre, lblImporte;
    private static ConstraintLayout layout;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            idProducto = (TextView) view.findViewById(R.id.idProducto);
            lblNombre = (TextView) view.findViewById(R.id.lblNombre);
            lblImporte = (TextView) view.findViewById(R.id.lblImporte);

        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public CustomAdapterConsulta3(ArrayList<Consulta3> dataSet) {
        localDataSet = dataSet;
        System.out.println("Total de datos " + localDataSet.size());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_custom_adapter_consulta3, null, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        idProducto.setText(localDataSet.get(position).getIdProducto());
        lblNombre.setText(localDataSet.get(position).getNombre());
        lblImporte.setText(String.valueOf(localDataSet.get(position).getImporteTotal()));


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}