package com.antonio.android.inmobiliaria;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Fragmento2 extends Fragment {
    private View v;
    private ImageView iv;
    public Fragmento2() {
    }

    public void setTexto(String texto,String texto1,String texto2){
        TextView tv=(TextView)v.findViewById(R.id.tvDF2);
        tv.setText(texto);
        TextView tv1=(TextView)v.findViewById(R.id.tvTF2);
        tv1.setText(texto1);
        TextView tv2=(TextView)v.findViewById(R.id.tvPF2);
        tv2.setText(texto2);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.fragment_2,container,false);
        iv =(ImageView) v.findViewById(R.id.imageView1);
        return v;
    }


}





