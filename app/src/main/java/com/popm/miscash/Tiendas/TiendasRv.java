package com.popm.miscash.Tiendas;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import com.popm.miscash.Conexiones.SqlServerC;
import com.popm.miscash.R;
import com.popm.miscash.Usuario.UsuarioSQL;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TiendasRv extends RecyclerView.Adapter<TiendasRv.TiendasVH> implements AdapterView.OnItemSelectedListener {

    List <Compra> data;
    Context context;

    public TiendasRv(List<Compra> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public TiendasVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tienda, viewGroup, false);
        TiendasVH tvh = new TiendasVH(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TiendasVH tiendasVH, final int i) {
        final UsuarioSQL usuario = new UsuarioSQL(context);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.calificaciones_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tiendasVH.calificacion.setAdapter(adapter);
        tiendasVH.nombre.setText(data.get(i).getNombre());
        tiendasVH.calif.setText("Calificación promedio: "+ String.valueOf(data.get(i).getCalif()));
        tiendasVH.calificacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if(selectedItem.equals("1"))
                {
                    califica(1,data.get(i).id_ticket);
                    update(i);
                    abonaVuelto(usuario.correo(),data.get(i).getMonto());
                    data.remove(i);
                    Snackbar.make(view,"Gracias por tu participacion",Snackbar.LENGTH_LONG).show();
                }else if(selectedItem.equals("2"))
                {
                    califica(2,data.get(i).id_ticket);
                    update(i);
                    abonaVuelto(usuario.correo(),data.get(i).getMonto());
                    data.remove(i);
                    Snackbar.make(view,"Gracias por tu participacion",Snackbar.LENGTH_LONG).show();
                }else if(selectedItem.equals("3"))
                {
                    califica(3,data.get(i).id_ticket);
                    update(i);
                    abonaVuelto(usuario.correo(),data.get(i).getMonto());
                    data.remove(i);
                    Snackbar.make(view,"Gracias por tu participacion",Snackbar.LENGTH_LONG).show();
                }else if(selectedItem.equals("4"))
                {
                    califica(4,data.get(i).id_ticket);
                    update(i);
                    abonaVuelto(usuario.correo(),data.get(i).getMonto());
                    data.remove(i);
                    Snackbar.make(view,"Gracias por tu participacion",Snackbar.LENGTH_LONG).show();
                } else if(selectedItem.equals("5"))
                {
                    califica(5,data.get(i).id_ticket);
                    update(i);
                    abonaVuelto(usuario.correo(),data.get(i).getMonto());
                    data.remove(i);
                    Snackbar.make(view,"Gracias por tu participacion",Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static class TiendasVH extends RecyclerView.ViewHolder{

        Spinner calificacion;
        TextView nombre, calif;
        public TiendasVH(@NonNull View itemView) {
            super(itemView);
            calificacion = itemView.findViewById(R.id.tienda_calificacion);
            nombre = itemView.findViewById(R.id.tienda_nombre);
            calif = itemView.findViewById(R.id.tienda_calif);
        }


    }


    public void califica (float calificacion,int id_ticket){

        SqlServerC conexion = new SqlServerC();
        try {
            PreparedStatement pst= conexion.conexionBD().prepareStatement("update ticket set calif= ?,statusU = 'Y'"+
                                                                                " where id_ticket = "+String.valueOf(id_ticket)+
                                                                                "and statusU = 'X' ");
            pst.setFloat(1,calificacion);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conexion.conexionBD();
    }

    public void abonaVuelto(String correo,String monto){
        SqlServerC conexion = new SqlServerC();
        try {
            PreparedStatement pst= conexion.conexionBD().prepareStatement("update usuario set saldo+= ? where correo ='"+correo+"';");
            pst.setFloat(1,Float.valueOf(monto));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conexion.conexionBD();
    }

    public void update(int i){
        this.notifyItemRemoved(i);
    }

}
