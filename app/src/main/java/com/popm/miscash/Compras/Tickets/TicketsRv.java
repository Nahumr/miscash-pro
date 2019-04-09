package com.popm.miscash.Compras.Tickets;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.popm.miscash.DetalleTicket;
import com.popm.miscash.R;

import java.util.List;

public class TicketsRv extends RecyclerView.Adapter<TicketsRv.ticketViewHolder> {

    List <Ticket> data;
    FragmentManager fragmentManager;

    public TicketsRv(List<Ticket> data, FragmentManager fragmentManager) {
        this.data = data;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ticketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ticket,viewGroup,false);
        ticketViewHolder tv = new ticketViewHolder(view);
        return tv;
    }

    @Override
    public void onBindViewHolder(final ticketViewHolder ticketViewHolder, final int i) {

        ticketViewHolder.fecha.setText("Fecha de compra: "+data.get(i).getFecha());
        ticketViewHolder.numeroC.setText("Codigo: "+data.get(i).getId_ticket());
        ticketViewHolder.tienda.setText("Tienda:"+data.get(i).getTienda());
        ticketViewHolder.total.setText("Total: "+String.valueOf(data.get(i).getTotal()));
        ticketViewHolder.vuelto.setText("Te devolveremos: "+ String.valueOf(data.get(i).getVuelto()));

        if (data.get(i).getStatus().equals("S")){
            ticketViewHolder.status.setText("PAGADO-Presenta tu codigo al tendero");
        }else  if (data.get(i).getStatus().equals("E")){
            ticketViewHolder.status.setText("Esperando autorizacion");
        }else {
            ticketViewHolder.status.setText("Algo anda mal");
        }

        ticketViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DetalleTicket ticket = new DetalleTicket();
                Bundle args = new Bundle();
                args.putString("TICKET", data.get(i).getId_ticket());
                ticket.setArguments(args);

                addFragment(ticket,false,"two");

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ticketViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView numeroC, tienda, fecha,total, vuelto, status;

        public ticketViewHolder(View view){
            super (view);

            numeroC = view.findViewById(R.id.rci_ncompra);
            tienda = view.findViewById(R.id.rci_tienda);
            fecha = view.findViewById(R.id.rci_fecha);
            total = view.findViewById(R.id.rci_total);
            cardView= view.findViewById(R.id.resumen_item);
            vuelto = view.findViewById(R.id.rci_vuelto);
            status = view.findViewById(R.id.rci_status);
        }

    }


    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.content_frame, fragment, tag);
        ft.commitAllowingStateLoss();
    }



}
