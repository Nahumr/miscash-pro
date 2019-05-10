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

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                if (helper.registros()>0){

                    if (usuarioSQL.campos()==1){

                        builder.setTitle("¿Estas seguro de tu compra?");
                        builder.setMessage("El total de tu compra es de: $"+String.valueOf(total));

                        builder.setCancelable(true);
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                ///////////////////////////////////////////////////////////////////////////77
                                final String[] Options = {"Efectivo", "Saldo","Tarjeta"};
                                AlertDialog.Builder window;
                                window = new AlertDialog.Builder(getContext());
                                window.setTitle("Selecciona un metodo de pago");
                                window.setItems(Options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0){
                                            finalizaCompra("E");
                                            transicion();
                                            Snackbar.make(v,"Paga en tienda",Snackbar.LENGTH_LONG).show();
                                        }else if(which == 1){
                                            if (saldoSuficiente(total)){
                                                finalizaCompra("S");
                                                transicion();
                                                Snackbar.make(v,"Pagado con saldo",Snackbar.LENGTH_LONG).show();
                                            }else{
                                                Snackbar.make(v,"No cuentas con saldo suficiente!",Snackbar.LENGTH_LONG).show();
                                            }
                                        }else if(which == 2){
                                            Snackbar.make(v,"En desarrollo....",Snackbar.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(getContext(), "Hmmm, No puedes pagar de esa forma", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                                window.show();
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(getContext(), ":(", Toast.LENGTH_LONG).show();
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

                    builder.setCancelable(true);
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

    void transicion (){
        helper.drop();
        ResumenTickets resumen = new ResumenTickets();
        addFragment(resumen,false,"CARRITO");
    }

    void finalizaCompra (String tipo_compra){

        SqlServerC conexion = new SqlServerC();
        LinkedList<Integer> tiendas = helper.tiendas();

        String correo = usuarioSQL.correo();
        for (int tienda : tiendas){
            Random random = new Random();
            int ticket = random.nextInt(9999);
            float total = helper.totalpagoTienda(tienda);
            float vuelto = helper.totalvueltoTienda(tienda);
            List <CarritoItem> productos = helper.datoItems(tienda);
            registroTicket(conexion,ticket,tienda,correo,tipo_compra,total,vuelto);
            for (CarritoItem item : productos ){
                    registroProducto(conexion,item,ticket);
            }
        }
        conexion.cierraConexion();
    }


    void registroProducto (SqlServerC conexion,CarritoItem item,int ticket){
        PreparedStatement pst;

        try{
            pst = conexion.conexionBD().prepareStatement("INSERT INTO COMPRA VALUES (?, ?, ?)");
            pst.setInt(1,ticket);
            pst.setString(2,item.getProducto_cb());
            pst.setInt(3,item.getProducto_cantidad());
            pst.executeUpdate();
        }catch (SQLException e) {
            Log.e("ERROR", e.getMessage());
        }
    }
    void registroTicket (SqlServerC conexion,int ticket,int tienda,String correo,String tipo_compra,float total, float vuelto){
        PreparedStatement pst;
        try{

            pst = conexion.conexionBD().prepareStatement("INSERT INTO TICKET (id_ticket,usuario,tienda,total,vuelto,tipop) VALUES (?, ?, ?, ?,?,?)");
            pst.setInt(1,ticket);
            pst.setString(2,correo);
            pst.setInt(3,tienda);
            pst.setFloat(4,total);
            pst.setFloat(5,vuelto);
            pst.setString(6,tipo_compra);
            pst.executeUpdate();
        }catch (SQLException e) {
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

    boolean saldoSuficiente (float total){
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
