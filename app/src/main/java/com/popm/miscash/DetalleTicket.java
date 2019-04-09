package com.popm.miscash;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popm.miscash.Compras.Admin_SQLite;
import com.popm.miscash.Compras.Tickets.DTicket;
import com.popm.miscash.Compras.Tickets.DticketRv;
import com.popm.miscash.Conexiones.SqlServerC;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DetalleTicket extends Fragment {


    String ticket;
    RecyclerView rv;
    public DetalleTicket() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ticket = getArguments().getString("TICKET");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dticket, container, false);
        rv= view.findViewById(R.id.dticket_rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        DticketRv adapter = new DticketRv(data());
        rv.setAdapter(adapter);
        return view;
    }

    List<DTicket> data (){
        List <DTicket> productos = new ArrayList<>();

        SqlServerC helper = new SqlServerC();

        try {

            Statement statement = helper.conexionBD().createStatement();
            ResultSet resultSet = statement.executeQuery("select compra.cantidad as cantidad, productos.nombre as nombre, " +
                                                                "productos.precio_v as precio, productos.vuelto as vuelto " +
                                                                "from compra inner join productos  " +
                                                                "on COMPRA.codigo_b = PRODUCTOS.codigo_b  " +
                                                                "where compra.id_ticket="+ticket+";");

            while (resultSet.next()){

                    productos.add(new DTicket(

                       resultSet.getString("nombre"),
                       resultSet.getFloat("precio"),
                       resultSet.getInt("cantidad")
                    ));
            }

        }catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }


}
