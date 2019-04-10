package com.popm.miscash;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.popm.miscash.Conexiones.SqlServerC;
import com.popm.miscash.Usuario.UsuarioSQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Tranferencia extends Fragment {


    ImageButton tranferir;
    EditText correo,cantidad;
    String [] correos = new String [2];
    String monto;
    public Tranferencia() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                View view =inflater.inflate(R.layout.fragment_tranferencia, container, false);
                tranferir = (ImageButton) view.findViewById(R.id.taceptar);
                correo =  (EditText) view.findViewById(R.id.tCorreo);
                cantidad = (EditText) view.findViewById(R.id.tCantidad);

                tranferir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UsuarioSQL usuario = new UsuarioSQL(getContext());
                        correos[0] = usuario.correo();
                        correos[1]= correo.getText().toString();
                        monto = cantidad.getText().toString();
                        enviaCorreo(correos,monto);
                        correo.setText("");
                        cantidad.setText("");
                        Toast.makeText(getContext(), "Transferencia exitosa!",Toast.LENGTH_LONG).show();
                    }
                });
                return view;
    }


    public void enviaCorreo(String [] correo, String cantidad){
        SqlServerC conexion = new SqlServerC();

        String sqlE =  "UPDATE usuario set saldo-="+cantidad+" where correo= '"+correo[0]+"'";
        String sqlR =  "UPDATE usuario set saldo+=+"+cantidad+" where correo= '"+correo[1]+"'";

        try {
            PreparedStatement pstE = conexion.conexionBD().prepareStatement(sqlE);
            PreparedStatement pstR = conexion.conexionBD().prepareStatement(sqlR);
            pstE.execute();
            pstR.execute();
        } catch (SQLException e) {
            Log.e("ERORR",e.getMessage());
        }
    }


}
