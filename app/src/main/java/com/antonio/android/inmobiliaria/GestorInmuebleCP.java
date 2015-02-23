package com.antonio.android.inmobiliaria;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Antonio on 29/01/2015.
 */
public class GestorInmuebleCP {
    Context contexto;
    ContentResolver cr;
    Uri uri = Contrato.TablaInmuebles.CONTENT_URI;
    public GestorInmuebleCP(Context context){
        contexto = context;
        cr = contexto.getContentResolver();
    }
    public Cursor getCursor() {
        Cursor cursor = cr.query(uri, null, null, null, null);
        return cursor;
    }
    public Cursor getCursorTipo() {
        Cursor cursor = cr.query(uri, null, null, null, Contrato.TablaInmuebles.TIPO);
        return cursor;
    }
    public Cursor getCursorPre() {
        Cursor cursor = cr.query(uri, null, null, null, Contrato.TablaInmuebles.PRECIO);
        return cursor;
    }

    public void insertar (Inmueble in){
        Uri uri=Contrato.TablaInmuebles.CONTENT_URI;
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmuebles.LOCALIDAD, in.getLocalidad());
        valores.put(Contrato.TablaInmuebles.DIRECCION, in.getDireccion());
        valores.put(Contrato.TablaInmuebles.TIPO, in.getTipo());
        valores.put(Contrato.TablaInmuebles.PRECIO, in.getPrecio());
        valores.put(Contrato.TablaInmuebles.SUBIDO, in.getSubido());
        Uri urielemento= cr.insert(uri, valores);
        Cursor cursor=cr.query(
                urielemento,null,null,null,null);

    }
    public int update(Inmueble i) {
        Uri uri= Contrato.TablaInmuebles.CONTENT_URI;
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmuebles.LOCALIDAD, i.getLocalidad());
        valores.put(Contrato.TablaInmuebles.DIRECCION, i.getDireccion());
        valores.put(Contrato.TablaInmuebles.TIPO, i.getTipo());
        valores.put(Contrato.TablaInmuebles.PRECIO, i.getPrecio());
        valores.put(Contrato.TablaInmuebles.SUBIDO, i.getSubido());
        String where = Contrato.TablaInmuebles._ID + " = ?";
        String[] args = { i.getId() + "" };
        int in = cr.update(uri, valores, where, args);

        return in;
    }
    public void eliminar(int id){
        Uri uri= Contrato.TablaInmuebles.CONTENT_URI;
        String where= Contrato.TablaInmuebles._ID+"=?";
        String[] args= new String[]{id+""};
        int i=cr.delete(uri,where,args);
    }

    public void select() {
        Uri uri = Contrato.TablaInmuebles.CONTENT_URI;
        String[] proyeccion = null;
        String condicion = null;
        String[] parametros = null;
        String orden = null;
        Cursor cursor= cr.query(
                uri,
                proyeccion,
                condicion,
                parametros,
                orden);

    }
    public ArrayList<Inmueble> pasaArrayl() {
       // getCursor().close();
        Uri uri = Contrato.TablaInmuebles.CONTENT_URI;
        String[] proyeccion = null;
        String condicion = null;
        String[] parametros = null;
        String orden = null;
        Cursor cursor= cr.query(
                uri,
                proyeccion,
                condicion,
                parametros,
                orden);
        ArrayList<Inmueble>al=new ArrayList<Inmueble>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Inmueble i=new Inmueble();
            i.setId(cursor.getInt(0));
            i.setLocalidad(cursor.getString(1));
            i.setDireccion(cursor.getString(2));
            i.setTipo(cursor.getString(3));
            i.setPrecio(Double.parseDouble(cursor.getString(4)));
            i.setSubido(cursor.getString(5));
            al.add(i);
            System.out.println("inmuebles "+i.toString());
        }
        cursor.close();
        return al;

    }

    public static Inmueble getRow(Cursor c) {
        Inmueble objeto = new Inmueble();
        System.out.println("cursor ");
        objeto.setId(c.getInt(0));
        objeto.setLocalidad(c.getString(1));
        objeto.setDireccion(c.getString(2));
        objeto.setTipo(c.getString(3));
        objeto.setPrecio(Double.parseDouble(c.getString(4)));
        objeto.setSubido(c.getString(5));
        //objeto.setSubido(c.getString(6));
        return objeto;
    }
}
