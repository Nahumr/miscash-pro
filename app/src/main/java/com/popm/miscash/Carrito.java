package com.popm.miscash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.popm.miscash.Carritoc.CarritoItem;
import com.popm.miscash.Carritoc.RvCarrito;
import com.popm.miscash.Compras.Admin_SQLite;
import com.popm.miscash.Conexiones.SqlServerC;
import com.popm.miscash.Usuario.Usuario;
import com.popm.miscash.Usuario.UsuarioSQL;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Carrito extends Fragment {

    RecyclerView recycle;
    Admin_SQLite helper;
    ImageButton resumen;
    UsuarioSQL usuarioSQL;
    public Carrito() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_carrito, container, false);
        helper= new Admin_SQLite(getContext());
        recycle=view.findViewById(R.id.recycle_item);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recycle.setLayoutManager(llm);
        recycle.setHasFixedSize(true);
        RvCarrito adapter = new RvCarrito(helper.datoItems(),getContext());
        recycle.setAdapter(adapter);
        resumen = view.findViewById(R.id.finalizar);
        usuarioSQL = new UsuarioSQL(getContext());

        resumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final float total =helper.granTotal();
                final int lon = helper.registros();
                final LinkedList<Float> totales = helper.totales();
                final LinkedList<Float> vueltos = helper.vueltos();
                final LinkedList<Integer> tienda = helper.tiendas();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                if (helper.registros()>0){

                    if (usuarioSQL.campos()==1){

                        builder.setTitle("¿Estas seguro de tu compra?");
                        builder.setMessage("El total de tu compra es de: "+String.valueOf(total));

                        builder.setCancelable(false);
                        builder.setPositiveButton("Efectivo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                for (int i=0;i<lon;i++){
                                    finalizaCompra(totales.get(i),vueltos.get(i),tienda.get(i),"E");
                                }
                                helper.drop();
                                Toast.makeText(getContext(), "Presentate en la tienda con tu codigo", Toast.LENGTH_SHORT).show();
                                addFragment(new ResumenTickets(),true,"TICKETS");
                            }
                        });

                        builder.setNegativeButton("Saldo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (realizaPago(total)){
                                    for (int i=0;i<lon;i++){
                                        finalizaCompra(totales.get(i),vueltos.get(i),tienda.get(i),"S");
                                    }
                                    helper.drop();
                                    addFragment(new ResumenTickets(),true,"TICKETS");
                                    Toast.makeText(getContext(), "Presenta en la tienda tu codigo", Toast.LENGTH_SHORT).show();
                                }else {
                                    Snackbar.make(v,"No cuentas con saldo suficiente",Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });

                        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }else {
                        builder.setTitle("Es necesario iniciar sesión");
                        builder.setMessage("Por favor inicia sesion");

                        builder.setCancelable(false);
                        builder.setPositiveButton("Iniciar sesion", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Login login = new Login();
                                Bundle args = new Bundle();
                                args.putString("PRODC", "CARRITO");
                                login.setArguments(args);
                                addFragment(login,false,"three");
                            }
                        });

                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                    }
                }else{
                    builder.setTitle("Tu carrito esta vacio");

                    builder.setCancelable(false);
                    builder.setPositiveButton("Buscar una tienda", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addFragment(new Mapa_tiendas(),true,"Tienda");
                        }
                    });

                    builder.setNegativeButton("Seguir viendo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }


                builder.show();

            }
        });
        return view;
    }



    void finalizaCompra (float total,float vueltoT,int id_tienda,String tipo_compra){

        List <CarritoItem> data = helper.datoItems();
        Random random = new Random();
        int id = random.nextInt(9999);

        registraTicket(id,total,vueltoT,usuarioSQL.correo(),id_tienda,tipo_compra);
        registraCompra(data,id);
    }

    void registraCompra(List <CarritoItem> data, int id){
        PreparedStatement pst;
        SqlServerC conexion = new SqlServerC();
        for (CarritoItem item: data){

            try{
                //PreparedStatement pst = conexionBD().prepareStatement("INSERT INTO USUARIO (nombre, apellido_p, apellido_m, fecha_nac, correo, sexo, saldo, rfc, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                pst = conexion.conexionBD().prepareStatement("INSERT INTO COMPRA VALUES (?, ?, ?)");
                pst.setInt(1,id);
                pst.setString(2,item.getProducto_cb());
                pst.setInt(3,item.getProducto_cantidad());
                pst.executeUpdate();

            }catch (SQLException e) {

                Log.e("ERROR", e.getMessage());
            }
        }
    }

    void registraTicket (int id, float total, float vueltoT,String usuario, int tienda,String tipo_compra){

        SqlServerC conexion = new SqlServerC();

        try {
            /*PreparedStatement pst = conexion.conexionBD().prepareStatement("INSERT INTO TICKET " +
                    "(id_ticket,usuario,tienda,total) values(?,?,?,?)");*/
            PreparedStatement pst = conexion.conexionBD().prepareStatement("INSERT INTO TICKET (id_ticket,usuario,tienda,total,vuelto,tipop) VALUES (?, ?, ?, ?,?,?)");
            pst.setInt(1,id);
            pst.setString(2,usuario);
            pst.setInt(3,tienda);
            pst.setFloat(4,total);
            pst.setFloat(5,vueltoT);
            pst.setString(6,tipo_compra);
            pst.executeUpdate();
            pst.close();

        } catch (SQLException e) {

            Log.e("ERROR", e.getMessage());
        }
    }


    void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.content_frame, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    boolean realizaPago (float total){
        float saldo=0;
        SqlServerC conexion = new SqlServerC();
        Statement statement = null;
        try {
            statement = conexion.conexionBD().createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select usuario.saldo as saldo" +
                            " from usuario where usuario.correo = '"+usuarioSQL.correo()+"';");

            while (resultSet.next()){
                saldo=resultSet.getFloat("saldo");
            }

            Log.i("USUARIO",usuarioSQL.correo());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (saldo>=total){

            try {
                PreparedStatement pst= conexion.conexionBD().prepareStatement("update usuario set saldo-= ? " +
                        "where correo= '"+ usuarioSQL.correo()+"';"
                        );
                pst.setFloat(1,total);
                pst.executeUpdate();
                pst.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }



}
