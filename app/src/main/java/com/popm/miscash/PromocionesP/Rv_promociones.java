package com.popm.miscash.PromocionesP;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.popm.miscash.Conexiones.SqlServerC;
import com.popm.miscash.Productos.RecyclerAdapter;
import com.popm.miscash.R;
import com.popm.miscash.Usuario.UsuarioSQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Rv_promociones extends RecyclerView.Adapter<Rv_promociones.PersonViewHolder> {


    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView nombre;
        TextView precio;
        TextView descripcion;
        ImageButton agregar;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.P_card_view);
            nombre = (TextView)itemView.findViewById(R.id.Pnombre);
            precio = (TextView)itemView.findViewById(R.id.Pprecio);
            descripcion = (TextView)itemView.findViewById(R.id.Pdescripcion);
            agregar = (ImageButton) itemView.findViewById(R.id.PAgregar);
        }
    }

    Context context;
    List<Promocion> productos;
    UsuarioSQL usuarioSQL;

    public Rv_promociones(List<Promocion> productos, Context context){
        this.productos = productos;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.promocion, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {

        usuarioSQL= new UsuarioSQL(context);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.cantidad_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personViewHolder.nombre.setText(productos.get(i).getNombre());
        personViewHolder.precio.setText("Costo: $"+productos.get(i).precio);
        personViewHolder.descripcion.setText(productos.get(i).descripcion);
        personViewHolder.agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("¿Estas seguro de tu compra?");
                builder.setCancelable(true);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (realizaPago(Float.valueOf(productos.get(i).getPrecio()))){
                            Toast.makeText(context,"Gracias por tu compra!", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context,"No cuentas con saldo suficiente! :(!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    private boolean realizaPago (float total){
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