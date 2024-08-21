package com.example.cuentaspolizas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CustomAdapterVentas extends RecyclerView.Adapter<CustomAdapterVentas.ViewHolder> {

    private final ArrayList<Ventas> localDataSet;
    private static TextView idProdPaq, lblFolio, lblID, lblProductoOrPaqueteID, lblUnidades, lblPrecio;
    private static ConstraintLayout layout;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            lblFolio = view.findViewById(R.id.idProducto);
            lblID = view.findViewById(R.id.lblNombre);
            lblProductoOrPaqueteID = view.findViewById(R.id.lblImporte);
            lblUnidades = view.findViewById(R.id.lblDescuento);
            layout = view.findViewById(R.id.myLayout);
            lblPrecio = view.findViewById(R.id.lblPrecio);

            idProdPaq = view.findViewById(R.id.nombre);
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public CustomAdapterVentas(ArrayList<Ventas> dataSet) {
        localDataSet = dataSet;
        System.out.println("Total de datos " + localDataSet.size());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_custom_adapter_ventas, null, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        lblFolio.setText(String.valueOf(localDataSet.get(position).getFolio()));
        lblID.setText(String.valueOf(localDataSet.get(position).getId()));
        lblProductoOrPaqueteID.setText(String.valueOf(localDataSet.get(position).getProducto_or_paqueteid()));
        lblUnidades.setText(String.valueOf(localDataSet.get(position).getUnidades()));
        lblPrecio.setText(String.valueOf(localDataSet.get(position).getPrecio()));

        if (localDataSet.get(position).getProducto_or_paqueteid() == 1) {
            idProdPaq.setText("ID Producto");
        } else {
            idProdPaq.setText("ID Paquete");
        }

        //Background color
        if (position % 2 == 0) {
            layout.setBackgroundColor(0xFFE0E0E0);
        } else {
            layout.setBackgroundColor(0xFFFFFFFF);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}