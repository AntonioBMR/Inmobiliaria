package com.antonio.android.inmobiliaria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by Antonio on 25/11/2014.
 */
public class Adaptador extends ArrayAdapter<Inmueble> {
    public static LayoutInflater i;
    public int resource;
    public List<Inmueble> lista;
    public Adaptador (Context context,int resource,List<Inmueble> objects){
        super(context,resource,objects);
        this.resource=resource;
        lista=objects;
        this.i=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int position, View v,ViewGroup vg){
        if(v==null){
            v=i.inflate(resource,null);
        }
        TextView tvD=(TextView) v.findViewById(R.id.textViewD);
        TextView tvT=(TextView) v.findViewById(R.id.textViewT);
        TextView tvP=(TextView) v.findViewById(R.id.textViewP);
        tvD.setText(lista.get(position).getDireccion());
        tvT.setText(lista.get(position).getTipo());
        DecimalFormat df=new DecimalFormat("#,###.00");
        tvP.setText(df.format(lista.get(position).getPrecio())+"€");
        return v;
    }
}
