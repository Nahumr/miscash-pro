package com.popm.miscash;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popm.miscash.Conexiones.SqlServerC;
import com.popm.miscash.PromocionesP.Promocion;
import com.popm.miscash.PromocionesP.Rv_promociones;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class Promociones extends Fragment {

    RecyclerView recycle;

    public Promociones() {
        // Required empty public constructor
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_promociones, container, false);
        recycle= view.findViewById(R.id.Precycle);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recycle.setLayoutManager(llm);
        recycle.setHasFixedSize(true);
        Rv_promociones adapter = new Rv_promociones(Productos(),getContext());
        recycle.setAdapter(adapter);
        return view;
    }

    public List<Promocion> Productos (){

        List<Promocion> list = new ArrayList<Promocion>();
        SqlServerC conexion = new SqlServerC();

        try {

            Statement statement = conexion.conexionBD().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT nombre,descripcion,precio FROM recompensas");

            while (resultSet.next()){
                list.add(new Promocion(
                        resultSet.getString("nombre"),
                        String.valueOf(resultSet.getFloat("precio")),
                        resultSet.getString("descripcion")
                ));

            }

        }catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
