package com.popm.miscash;

import android.content.Context;
import android.net.Uri;
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
import com.popm.miscash.Productos.Producto;
import com.popm.miscash.Productos.RecyclerAdapter;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ListaProductos extends Fragment {


    RecyclerView recycle;
    String tienda;
    public ListaProductos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            tienda = getArguments().getString("TIENDA");
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista_prodcutos, container, false);

        recycle= view.findViewById(R.id.recycle);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recycle.setLayoutManager(llm);
        recycle.setHasFixedSize(true);
        Productos();
        RecyclerAdapter adapter = new RecyclerAdapter(Productos(),getContext());
        recycle.setAdapter(adapter);
        return view;
    }


    public List<Producto> Productos (){

        List<Producto> list = new ArrayList<Producto>();
        SqlServerC conexion = new SqlServerC();

        try {

            Statement statement = conexion.conexionBD().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT PRODUCTOS.codigo_b as codigo_b,PRODUCTOS.nombre as nombre ,PRODUCTOS.precio_v as precio,PRODUCTOS.vuelto as vuelto,DISPONIBLIDAD.id_tienda as tienda  FROM PRODUCTOS inner join DISPONIBLIDAD on DISPONIBLIDAD.codigo_b = PRODUCTOS.codigo_b" +
                                                                " where DISPONIBLIDAD.id_tienda = "+tienda+";");

            while (resultSet.next()){
                list.add(new Producto(

                        resultSet.getString("codigo_b"),
                        resultSet.getString("nombre"),
                        resultSet.getFloat("precio"),
                        resultSet.getFloat("vuelto"),
                        resultSet.getInt("tienda")
                ));

            }

        }catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
