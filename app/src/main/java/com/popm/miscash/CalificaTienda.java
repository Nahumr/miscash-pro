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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popm.miscash.Compras.Tickets.DTicket;
import com.popm.miscash.Conexiones.SqlServerC;
import com.popm.miscash.Tiendas.Compra;
import com.popm.miscash.Tiendas.Tienda;
import com.popm.miscash.Tiendas.TiendasRv;
import com.popm.miscash.Usuario.UsuarioSQL;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class CalificaTienda extends Fragment {


    RecyclerView recycle;

    public CalificaTienda() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_califica_tienda, container, false);
        UsuarioSQL usuarioSQL = new UsuarioSQL(getContext());

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
                    args.putString("PRODC", "CALIFICA");
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
        }else{
            recycle= view.findViewById(R.id.rv_tiendas);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            recycle.setLayoutManager(llm);
            recycle.setHasFixedSize(true);
            TiendasRv tiendas = new TiendasRv(data(),getContext());
            recycle.setAdapter(tiendas);
        }


        return view;
    }


    List<Compra> data (){
        List <Compra> tiendas = new ArrayList<>();
        UsuarioSQL usuario = new UsuarioSQL(getContext());
        SqlServerC helper = new SqlServerC();

        try {

            Statement statement = helper.conexionBD().createStatement();
            ResultSet resultSet = statement.executeQuery("select tienda.id_tienda,ticket.id_ticket,tienda.calif,tienda.nombre, ticket.total " +
                                                                "from tienda inner join ticket on tienda.id_tienda=ticket.tienda " +
                                                                "inner join usuario on ticket.usuario = usuario.correo " +
                                                                "where ticket.usuario='"+usuario.correo()+"' and ticket.statusT='Y'" +
                                                                "and ticket.statusU='X';");

            while (resultSet.next()){

                tiendas.add(new Compra(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getFloat(3),
                        resultSet.getString(4),
                        String.valueOf(resultSet.getFloat(5))
                ));
            }

        }catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return tiendas;
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
