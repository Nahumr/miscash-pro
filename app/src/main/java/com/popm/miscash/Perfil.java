package com.popm.miscash;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popm.miscash.Conexiones.SqlServerC;
import com.popm.miscash.Usuario.Usuario;
import com.popm.miscash.Usuario.UsuarioSQL;

import java.sql.ResultSet;
import java.sql.Statement;

public class Perfil extends Fragment {


    TextView nombre, saldo;
    ImageView foto;
    Usuario usuario;

    public Perfil(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.perfil, container, false);
        UsuarioSQL usuarioSQL =new UsuarioSQL(getContext());

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
                    args.putString("PRODC", "PERFIL");
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

        nombre = view.findViewById(R.id.perfil_nombre);
        saldo = view.findViewById(R.id.perfil_saldo);
        usuario = new Usuario();

        nombre.setText("Hola "+usuarioSQL.nombreApellido()+"!");
        saldo.setText("Tu saldo es de $"+String.valueOf(saldo(usuarioSQL.correo())));

        return view;
    }

    public float saldo (String correo){
        SqlServerC conexion = new SqlServerC();
        float total=0;
        try {

            Statement statement = conexion.conexionBD().createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select usuario.saldo as saldo" +
                            " from usuario where usuario.correo = '"+correo+"';");

            while (resultSet.next()){
                total=resultSet.getFloat("saldo");
            }
        }catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return total;
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

