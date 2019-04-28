package com.popm.miscash.PromocionesP;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.popm.miscash.Productos.RecyclerAdapter;
import com.popm.miscash.R;

import java.util.List;

public class Rv_promociones extends RecyclerView.Adapter<Rv_promociones.PersonViewHolder> implements AdapterView.OnItemSelectedListener {


    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView nombre;
        TextView precio;
        TextView descripcion;
        Spinner cantidad;
        ImageButton agregar;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.P_card_view);
            nombre = (TextView)itemView.findViewById(R.id.Pnombre);
            precio = (TextView)itemView.findViewById(R.id.Pprecio);
            descripcion = (TextView)itemView.findViewById(R.id.Pdescripcion);
            cantidad = (Spinner)itemView.findViewById(R.id.PCantidad);
            agregar = (ImageButton) itemView.findViewById(R.id.PAgregar);
        }
    }

    Context context;
    List<Promocion> productos;


    public Rv_promociones(List<Promocion> productos, Context context){
        this.productos = productos;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.promocion, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.cantidad_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personViewHolder.cantidad.setAdapter(adapter);
        personViewHolder.nombre.setText(productos.get(i).getNombre());
        personViewHolder.precio.setText(productos.get(i).precio);
        personViewHolder.descripcion.setText(productos.get(i).descripcion);

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}