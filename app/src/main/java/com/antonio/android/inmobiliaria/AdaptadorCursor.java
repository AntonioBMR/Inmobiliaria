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
    private Cursor c;
    public AdaptadorCursor(Context context, Cursor c) {
        super(context, c, true);
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
        in= GestorInmuebles.getRow(c);
        TextView tvD = (TextView) v.findViewById(R.id.textViewD);
        TextView tvT = (TextView) v.findViewById(R.id.textViewT);
        TextView tvP = (TextView) v.findViewById(R.id.textViewP);
        tvD.setText(in.getDireccion());
        tvT.setText(in.getTipo());
        tvP.setText(in.getPrecio()+" ");
    }
}