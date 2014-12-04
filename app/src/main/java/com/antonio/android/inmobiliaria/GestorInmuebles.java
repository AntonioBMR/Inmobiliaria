package com.antonio.android.inmobiliaria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 26/11/2014.
 */
public class GestorInmuebles {
    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorInmuebles(Context c) {
        abd = new Ayudante(c);
    }

    public void open() {
        bd = abd.getWritableDatabase();
    }

    public void openRead() {
        bd = abd.getReadableDatabase();
    }

    public void close() {
        abd.close();
    }

    public long insert(Inmueble i) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmuebles.DIRECCION, i.getDireccion());
        valores.put(Contrato.TablaInmuebles.TIPO, i.getTipo() );
        valores.put(Contrato.TablaInmuebles.PRECIO, i.getPrecio());
        long id = bd.insert(Contrato.TablaInmuebles.TABLA, null, valores);
        return id;
    }
    public int delete(Inmueble i) {
        String condicion = Contrato.TablaInmuebles._ID + " = ?";
        String[] argumentos = { i.getId() + "" };
        int cuenta = bd.delete(
                Contrato.TablaInmuebles.TABLA, condicion, argumentos);
        return cuenta;
    }
    public int update(Inmueble i) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmuebles.DIRECCION, i.getDireccion());
        valores.put(Contrato.TablaInmuebles.TIPO, i.getTipo());
        valores.put(Contrato.TablaInmuebles.PRECIO, i.getPrecio());
        String condicion = Contrato.TablaInmuebles._ID + " = ?";
        String[] argumentos = { i.getId() + "" };
        int cuenta = bd.update(Contrato.TablaInmuebles.TABLA, valores,
                condicion, argumentos);
        return cuenta;
    }

    public List<Inmueble> select() {
        return select(null,null,null);
    }

    public List<Inmueble> select(String condicion, String[] parametros, String ord) {
        List<Inmueble> alj = new ArrayList<Inmueble>();
        Cursor cursor = bd.query(Contrato.TablaInmuebles.TABLA, null,
                condicion, parametros, null, null, ord);
        cursor.moveToFirst();
        Inmueble in;
        while (!cursor.isAfterLast()) {
            in = getRow(cursor);
            alj.add(in);
            cursor.moveToNext();
        }
        cursor.close();
        return alj;
    }

    public static Inmueble getRow(Cursor c) {
        Inmueble i = new Inmueble();
        i.setId(c.getInt(0));
        i.setDireccion(c.getString(1));
        i.setTipo(c.getString(2));
        double p=0;
        try{
            p= Double.parseDouble(c.getString(3));
        }catch(Exception e){}
        i.setPrecio(p);
        return i;
    }

    public Inmueble getRow(long id){
        List<Inmueble> alj= select(Contrato.TablaInmuebles._ID + " = ?",new String[]{ id+"" },null );
        if (alj.isEmpty()) {
            return alj.get(0);
        }
        return null;
    }

    public Cursor getCursor(String condicion, String[] parametros, String orderby) {
        Cursor cursor = bd.query(
                Contrato.TablaInmuebles.TABLA, null, condicion, parametros, null, null,
                orderby);
        return cursor;
    }

    public Cursor getCursor() {
        Cursor cursor = bd.query(
                Contrato.TablaInmuebles.TABLA, null, null, null, null, null,
                null);
        return cursor;
    }


}