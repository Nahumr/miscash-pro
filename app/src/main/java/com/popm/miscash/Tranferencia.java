package com.popm.miscash;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class Tranferencia extends Fragment {


    ImageButton tranferir;
    EditText correo,cantidad;

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
                        Toast.makeText(getContext(), "Click",Toast.LENGTH_LONG).show();
                    }
                });
                return view;
    }


}
