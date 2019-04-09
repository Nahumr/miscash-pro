package com.popm.miscash;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.popm.miscash.Conexiones.SqlServerC;
import com.popm.miscash.Tiendas.ManejadorE.InfoTienda;
import com.popm.miscash.Tiendas.Tienda;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Mapa_tiendas extends Fragment {


    Double ubicacionX, ubicacionY;
    String [] uX = new String [2];
    String [] uY = new String [2];
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public Mapa_tiendas() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mapa_tiendas, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.clear();
                mMap.setInfoWindowAdapter(new InfoTienda(getContext()));

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);

                mMap.getUiSettings().setZoomControlsEnabled(true);

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(final Marker marker) {
                        String[] items={"Ver productos","Cancelar"};
                        AlertDialog.Builder itemDilog = new AlertDialog.Builder(getContext());
                        itemDilog.setTitle("");
                        itemDilog.setCancelable(false);
                        itemDilog.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which){
                                    case 0:{
                                        Integer id_tienda= (Integer) marker.getTag();
                                        ListaProductos productos = new ListaProductos();
                                        Bundle args = new Bundle();
                                        args.putString("TIENDA",String.valueOf(id_tienda));
                                        productos.setArguments(args);

                                        addFragment(productos,true,"two");
                                    }break;
                                    case 1:{

                                    }break;
                                }

                            }
                        });
                        itemDilog.show();
                    }
                });

                //ubicacion();
                /*uX[0] = String.valueOf(ubicacionX-0.5);
                uX[1] = String.valueOf(ubicacionX+0.5);
                uY[0] = String.valueOf(ubicacionY-0.5);
                uY[1] = String.valueOf(ubicacionY+0.5);

                uX[0] = uX[0].substring(0,uX[0].indexOf(46)).concat((uX[0].substring(uX[0].indexOf(46),uX[0].indexOf(46)+6)));
                uX[1] = uX[1].substring(0,uX[1].indexOf(46)).concat((uX[1].substring(uX[1].indexOf(46),uX[1].indexOf(46)+6)));
                uY[0] = uY[0].substring(0,uY[0].indexOf(46)).concat((uY[0].substring(uY[0].indexOf(46),uY[0].indexOf(46)+6)));
                uY[1] = uY[1].substring(0,uY[1].indexOf(46)).concat((uY[1].substring(uY[1].indexOf(46),uY[1].indexOf(46)+6)));*/

                agregarTiendas(mMap);

            }

        });

        return rootView;
    }

    private void agregarTiendas(GoogleMap mMap) {
        for (Tienda tienda : Tiendas()) {
            LatLng ubicacion = new LatLng(tienda.getLatitud_x(), tienda.getLatitud_y());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(ubicacion)
                    .title(tienda.getNombre())
                    .snippet("Calificacion: "+String.valueOf(tienda.getCalif()))
            );

            marker.setTag(tienda.getId_tienda());
        }
    }

    public List<Tienda> Tiendas() {
        List<Tienda> tiendas = new ArrayList<>();
        SqlServerC conexion = new SqlServerC();

        String query = "SELECT * FROM TIENDA ";/*+
                        "where (tienda.latitud_x between "+ uX[0] +" and "+ uX[1]+") and " +
                        "(tienda.latitud_y between " +uY[0]+" and " + uY[1]+" );";*/
        Log.i("QUERY",query);
        try {

            Statement statement = conexion.conexionBD().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                tiendas.add(new Tienda(
                        resultSet.getInt("id_tienda"),
                        resultSet.getInt("calif"),
                        resultSet.getString("nombre"),
                        resultSet.getFloat("latitud_x"),
                        resultSet.getFloat("latitud_y")
                ));

            }

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return tiendas;
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


    void actualizarUbicacion(Location location) {

        if (location != null) {
            ubicacionX = location.getLatitude();
            ubicacionY = location.getLongitude();
        }
    }


    void ubicacion() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locationListener);
    }



    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
                actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Necesitamos tu autorización")
                        .setMessage("¿Nos otorgas el saber tu ubicación?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }



}