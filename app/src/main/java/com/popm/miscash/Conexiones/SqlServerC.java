package com.popm.miscash.Conexiones;

import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlServerC {

/*
    String ip = "//200.38.35.62:1433";
    String user = "popmobile2";
    String password = "R3c4rg4f4c1l2.17#";

*/
    String ip;
    String db;
    String user;

    String password;
    Connection conexion ;

    public SqlServerC (){
        ip = "//192.168.100.5/XLR8";
        //ip = "//192.168.1.47/XLR8";
        db = "XLR8";
        user = "nahumr";
        password = "Rodriguez1$";
        conexion = null;
    }

    public Connection conexionBD(){

        try{

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            conexion = DriverManager.getConnection(""
                    + "jdbc:jtds:sqlserver:"+ip+";"
                    + "user="+user+";password="+password+";");

            Log.i("DATA-","CONEXION EXITOSA");
        }catch (Exception e){
            Log.e("DATA-ERROR",e.getMessage());
        }

        return conexion;
    }


    public void cierraConexion(){
        try {
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}