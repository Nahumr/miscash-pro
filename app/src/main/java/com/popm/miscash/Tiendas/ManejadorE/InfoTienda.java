package com.popm.miscash.Tiendas.ManejadorE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.popm.miscash.R;
import com.popm.miscash.Tiendas.Tienda;

public class InfoTienda implements GoogleMap.InfoWindowAdapter {


    static final String TAG = "CustomInfoWindowAdapter";
    View inflater;
    TextView nombre,calificacion;
    Context context;

    public InfoTienda(Context context) {
        this.context=context;
        inflater = LayoutInflater.from(context).inflate(R.layout.infowindow_layout,null);
    }


    private void renderWindow(final Marker marker, View view ){
        nombre = view.findViewById(R.id.infot_nombre);
        calificacion = view.findViewById(R.id.infot_calificacion);
        nombre.setText(marker.getTitle());
        calificacion.setText(marker.getSnippet());
    }

    @Override
    public View getInfoWindow(Marker marker) {

        renderWindow(marker,inflater);
        return inflater;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindow(marker,inflater);
        return inflater;
    }
}
