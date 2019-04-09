package com.popm.miscash.Compras.Tickets;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popm.miscash.DetalleTicket;
import com.popm.miscash.R;

import java.util.List;

public class DticketRv extends RecyclerView.Adapter<DticketRv.ticketViewHolder> {

    List<DTicket> data;


    public DticketRv(List<DTicket> data) {
        this.data = data;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ticketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ticket_item, viewGroup, false);
        ticketViewHolder tv = new ticketViewHolder(view);
        return tv;
    }

    @Override
    public void onBindViewHolder(final ticketViewHolder ticketViewHolder, final int i) {

        ticketViewHolder.nombre.setText(data.get(i).getNombre());
        ticketViewHolder.cantidad.setText("Cantidad: "+String.valueOf(data.get(i).getCantidad()));
        ticketViewHolder.precio.setText("Precio: "+String.valueOf(data.get(i).getPrecio()));
        ticketViewHolder.subtotal.setText("SubTotal: "+String.valueOf(data.get(i).getSubtotal()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ticketViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, precio,cantidad, subtotal;

        public ticketViewHolder(View view) {
            super(view);

            nombre = view.findViewById(R.id.ticket_nom);
            precio = view.findViewById(R.id.ticket_precio);
            cantidad = view.findViewById(R.id.ticket_cantidad);
            subtotal = view.findViewById(R.id.ticket_subtotal);
        }

    }
}
