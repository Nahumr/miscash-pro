package com.popm.miscash;


import android.os.Bundle;
import android.app.FragmentManager;
import android.support.annotation.Nullable;
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
import com.popm.miscash.Usuario.Usuario;
import com.popm.miscash.Usuario.UsuarioSQL;

import java.sql.ResultSet;
import java.sql.Statement;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    EditText correo,pass;
    ImageButton login;
    Usuario usuario;
    String procedencia;
    public Login() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments()!=null){
            procedencia = getArguments().getString("PRODC");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        correo = view.findViewById(R.id.login_correo);
        pass = view.findViewById(R.id.login_pass);
        login= view.findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                String pmail = correo.getText().toString();
                String ppass = pass.getText().toString();


                if (ppass!=null && ppass!="" && pmail!=null && pmail!=""){
                    if (login(pmail,ppass)){

                        UsuarioSQL usuarioH = new UsuarioSQL(getContext());
                        int campos =usuarioH.campos();
                        Log.i("CAMPOS",String.valueOf(campos));
                        if (campos==0){
                            usuarioH.agregar(usuario);
                            Log.i("DATAU",String.valueOf(usuarioH.campos()));
                            Toast.makeText(getContext(),"Bienvenido",Toast.LENGTH_LONG).show();
                            if (procedencia.equals("CARRITO")){
                                addFragment(new Carrito(),false,"LOGIN-C");
                            }else if (procedencia.equals("LOGIN")){
                                addFragment(new Mapa_tiendas(),false,"LOGIN-M");
                            }else if (procedencia.equals("TICKETS")){
                                addFragment(new ResumenTickets(),false,"LOGIN-T");
                            }else if (procedencia.equals("PERFIL")){
                                addFragment(new Perfil(),false,"LOGIN-P");
                            }else if (procedencia.equals("CALIFICA")){
                                addFragment(new CalificaTienda(),false,"LOGIN-CT");
                            }else if (procedencia.equals("TIENDA")){
                                addFragment(new Mapa_tiendas(),false,"LOGIN-MT");
                            }else {
                                addFragment(new Mapa_tiendas(),false,"LOGIN-M");
                            }


                        }else if (campos!=0){
                            Toast.makeText(getContext(),"Error,Cierra tu sesion actual",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getContext(),"Algo anda mal!",Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(getContext(),"Algo anda mal!",Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }


    public boolean login (String Correo,String pass){

        String ppass="";

        SqlServerC conexion = new SqlServerC();
        try {

            Statement statement = conexion.conexionBD().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT USUARIO.pass,USUARIO.nombre as nombre, USUARIO.apellido_p as apellido" +
                                                                " FROM USUARIO WHERE USUARIO.correo = '"+Correo+"'");
            while (resultSet.next()){
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setApellido(resultSet.getString("apellido"));
                usuario.setCorreo(Correo);
                ppass= resultSet.getString("PASS");

            }

        }catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        if (pass.equals(ppass)){
            return true;
        }

        return false;
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
