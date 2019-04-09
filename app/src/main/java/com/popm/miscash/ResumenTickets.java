package com.popm.miscash;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popm.miscash.Compras.Tickets.Ticket;
import com.popm.miscash.Compras.Tickets.TicketsRv;
import com.popm.miscash.Conexiones.SqlServerC;
import com.popm.miscash.Productos.Producto;
import com.popm.miscash.Productos.RecyclerAdapter;
import com.popm.miscash.Usuario.UsuarioSQL;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class ResumenTickets extends Fragment {

    RecyclerView recycle;
    UsuarioSQL usuarioSQL;

    public ResumenTickets() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_resumen_tickets, container, false);
        recycle= view.findViewById(R.id.rvResumen);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recycle.setLayoutManager(llm);
        recycle.setHasFixedSize(true);
        usuarioSQL = new UsuarioSQL(getContext());
        TicketsRv adapter = new TicketsRv(data(),getFragmentManager());
        recycle.setAdapter(adapter);

        if (usuarioSQL.campos()!=1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Parece que no has iniciado sesion");
            builder.setMessage("¿Deseas iniciar sesion?");
            builder.setCancelable(true);

            builder.setPositiveButton("Iniciar sesion", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Login login = new Login();
                    Bundle args = new Bundle();
                    args.putString("PRODC", "TICKETS");
                    login.setArguments(args);
                    addFragment(login,false,"three");
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();
        }

        return view;
    }

    private List<Ticket> data (){
        List<Ticket> data = new ArrayList<>();
        SqlServerC conexion = new SqlServerC();
        try {

            Statement statement = conexion.conexionBD().createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select ticket.id_ticket,tienda.nombre,ticket.fecha,ticket.total,ticket.vuelto, ticket.tipop" +
                            " from ticket inner join tienda on ticket.tienda = tienda.id_tienda where ticket.statusT = 'X'" +
                            " and usuario = '"+usuarioSQL.correo()+"';");

            while (resultSet.next()){
                data.add(new Ticket(
                        String.valueOf(resultSet.getInt(1)),
                        resultSet.getString(2),
                        resultSet.getDate(3),
                        resultSet.getFloat(4),
                        resultSet.getFloat(5),
                        resultSet.getString(6)
                ));

            }

        }catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.content_frame, fragment, tag);
        ft.commitAllowingStateLoss();
    }
}
