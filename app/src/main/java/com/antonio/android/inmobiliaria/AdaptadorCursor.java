package com.antonio.android.inmobiliaria;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Antonio on 01/12/2014.
 */
public class AdaptadorCursor extends CursorAdapter {
    private Context contexto;
    public AdaptadorCursor(Context context, Cursor c) {
        super(context, c, true);
        contexto = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup vg) {
        LayoutInflater i = LayoutInflater.from(vg.getContext());
        View v = i.inflate(R.layout.detalle, vg, false);
        return v;
    }

    @Override
    public void bindView(View v, Context co, Cursor c) {
        Inmueble in=new Inmueble();
        in= getRow(c);
        TextView tvL = (TextView) v.findViewById(R.id.textViewL);
        TextView tvD = (TextView) v.findViewById(R.id.textViewD);
        TextView tvT = (TextView) v.findViewById(R.id.textViewT);
        TextView tvP = (TextView) v.findViewById(R.id.textViewP);
        tvL.setText(in.getLocalidad());
        tvD.setText(in.getDireccion());
        tvT.setText(in.getTipo());
        tvP.setText(in.getPrecio()+" ");
    }
    public static Inmueble getRow(Cursor c) {
        Inmueble in = new Inmueble();
        in.setId(c.getInt(0));
        in.setLocalidad(c.getString(1));
        in.setDireccion(c.getString(2));
        in.setTipo(c.getString(3));
        in.setPrecio(Double.parseDouble(c.getString(4)));
        return in;
    }

}