package com.popm.miscash;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.popm.miscash.Conexiones.SqlServerC;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;



public class Registro extends Fragment {

    private EditText nombre_ed, apellido_p_ed, apellido_m_ed, fecha_nac_ed,correo_ed,telefono_ed,pass_ed;

    public Registro() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        nombre_ed = view.findViewById(R.id.registro_Nombre);
        apellido_p_ed = view.findViewById(R.id.registro_apellidoP);
        apellido_m_ed = view.findViewById(R.id.registro_apellidoM);
        fecha_nac_ed = view.findViewById(R.id.registro_fnacimiento);
        correo_ed = view.findViewById(R.id.registro_correo);
        telefono_ed = view.findViewById(R.id.registro_telefono);
        pass_ed = view.findViewById(R.id.registro_pass);
        ImageButton registro = view.findViewById(R.id.registrar);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });


        return view;
    }



    public void registrarUsuario(){
        SqlServerC conexion = new SqlServerC();
        try{

            //PreparedStatement pst = conexionBD().prepareStatement("INSERT INTO USUARIO (nombre, apellido_p, apellido_m, fecha_nac, correo, sexo, saldo, rfc, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pst = conexion.conexionBD().prepareStatement("INSERT INTO USUARIO" +
                                                                        "(nombre, apellido_p,apellido_m,fecha_nac,correo,sexo,telefono,pass) " +
                                                                        "VALUES (?, ?, ?, ?, ?, ?, ?,?)");

            pst.setString(1 , datosPersonales(nombre_ed));
            pst.setString(2 , datosPersonales(apellido_p_ed));
            pst.setString(3 , datosPersonales(apellido_m_ed) );
            pst.setInt(4 , datoFecha(fecha_nac_ed) );
            pst.setString(5 , datoCorreo(correo_ed));
            pst.setString(6 , "I");
            pst.setString(7 , datoTelefono(telefono_ed));
            pst.setString(8,pass_ed.getText().toString());
            pst.executeUpdate();
            addFragment(new Login(),true,"two");
            Toast.makeText(getContext(),"Inicia sesion",Toast.LENGTH_LONG).show();

        }catch (SQLException e) {

            Toast.makeText(getContext(),"ERROR",Toast.LENGTH_LONG).show();
            Log.e("ERROR", e.getMessage());
        }

    }


    public String datosPersonales (EditText campo){

        String data = campo.getText().toString().toUpperCase();

        if (data == ""){
            Toast.makeText(getContext(), "Hay un campo vacio!", Toast.LENGTH_LONG).show();
            return "#1";
        }

        if(Pattern.matches("[A-Za-zäÄëËïÏöÖüÜáéíóúáéíóúÁÉÍÓÚÂÊÎÔÛâêîôûàèìòùÀÈÌÒÙ.-]+",data.trim())){
            return data;
        }else{
            return "#1";
        }
    }


    public int datoFecha (EditText campo){

        String dato = campo.getText().toString().trim();



        if( Pattern.matches("[0-9]{4}",dato) ){

            return Integer.valueOf(dato);

        }

        return 0000;

    }


    public String datoTelefono (EditText campo){

        String telefono = "";

        telefono = campo.getText().toString().trim();

        if (telefono == ""){
            return "#1";
        }


        if( Pattern.matches("[0-9]{10}",telefono) ){
            return telefono;
        }

        return telefono;
    }

    public String datoCorreo(EditText campo){
        String correo = campo.getText().toString();

        if(correo == ""){
            return "#1";
        }

        if( Pattern.matches("[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+",correo) ){
            return correo;
        }else return "#1";
    }

    public String validaDatos (String dato){

        String [] datos = dato.split(" ");

        if (datos.length>1)
            return datos[0];
        else
            return dato;
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
