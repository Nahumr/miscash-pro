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

    EditText [] campos = new EditText[7];
    String [] campos_String = new String [7];
    String [] campos_Nombres = {
            "Nombre",
            "Apellido Paterno",
            "Apellido Materno",
            "Año de nacimiento",
            "Correo Electronico",
            "Telefono",
            "Contraseña"
            };

    public Registro() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        campos[0] = view.findViewById(R.id.registro_Nombre);
        campos[1] = view.findViewById(R.id.registro_apellidoP);
        campos[2] = view.findViewById(R.id.registro_apellidoM);
        campos[3] = view.findViewById(R.id.registro_fnacimiento);
        campos[4] = view.findViewById(R.id.registro_correo);
        campos[5] = view.findViewById(R.id.registro_telefono);
        campos[6] = view.findViewById(R.id.registro_pass);
        ImageButton registro_button = view.findViewById(R.id.registrar);

        registro_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preCarga(v)) {
                    registrarUsuario();
                    Toast.makeText(getContext(),"Inicia sesion",Toast.LENGTH_LONG).show();
                    Login login = new Login();
                    Bundle args = new Bundle();
                    args.putString("PRODC", "TIENDA");
                    login.setArguments(args);
                    addFragment(login,false,"one");
                }
            }
        });


        return view;
    }


    public boolean preCarga (View view){
        boolean estatus = true;
        for (int i = 0 ; i<campos.length;i++){
            String campo = campos[i].getText().toString();
            if (campo.trim().length() == 0){
                estatus = false;
                Snackbar.make(view,campos_Nombres[i]+" no puede ser nulo",Snackbar.LENGTH_LONG).show();
            }

            if (i<3){
                campos_String[i] = campo;
            }

            if (i == 3){
                if (datoFecha(campo)){
                    campos_String [i] = campo;
                }else{
                    estatus = false;
                    Snackbar.make(view,campos_Nombres[i]+" invalido",Snackbar.LENGTH_LONG).show();
                }
            }

            if (i == 4){
                if (datoCorreo(campo)){
                    campos_String [i] = campo;
                }else{
                    estatus = false;
                    Snackbar.make(view,"Correo no valido",Snackbar.LENGTH_LONG).show();
                }
            }

            if (i == 5){
                if (datoTelefono(campo)){
                    campos_String[i] = campo;
                }else{
                    estatus = false;
                    Snackbar.make(view,"Telefono no valido",Snackbar.LENGTH_LONG).show();
                }
            }


            if (i == 6){
                campos_String [i] = campo;
            }
        }
        return estatus;
    }

    public boolean datoCorreo(String correo){
        if( Pattern.matches("[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+",correo) ){
            return true;
        }
        return false;
    }

    public boolean datoTelefono (String telefono){
        if( Pattern.matches("[0-9]{8}[0-9]*",telefono) ){
            return true;
        }
        return false;
    }

    public boolean datoFecha (String fecha){
        if( Pattern.matches("[0-9]{4}",fecha) ){
            return true;
        }
        return false;
    }

    public boolean datosPersonales (String  dato){
        if(Pattern.matches("[A-Za-zäÄëËïÏöÖüÜáéíóúáéíóúÁÉÍÓÚÂÊÎÔÛâêîôûàèìòùÀÈÌÒÙ.-]+",dato)){
            return true;
        }
        return false;
    }

    public void registrarUsuario(){
        SqlServerC conexion = new SqlServerC();
        try{

            PreparedStatement pst = conexion.conexionBD().prepareStatement("INSERT INTO USUARIO" +
                    "(nombre, apellido_p,apellido_m,fecha_nac,correo,sexo,telefono,pass) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?,?)");

            pst.setString(1 , campos_String[0]);
            pst.setString(2 , campos_String[1]);
            pst.setString(3 , campos_String[2]);
            pst.setInt(4 , Integer.valueOf(campos_String[3]));
            pst.setString(5 , campos_String[4]);
            pst.setString(6 , "I");
            pst.setString(7 , campos_String[5]);
            pst.setString(8,campos_String[6]);
            pst.executeUpdate();

        }catch (SQLException e) {

            Toast.makeText(getContext(),"ERROR",Toast.LENGTH_LONG).show();
            Log.e("ERROR", e.getMessage());
        }

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
