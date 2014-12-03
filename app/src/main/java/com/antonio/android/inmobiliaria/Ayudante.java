package com.antonio.android.inmobiliaria;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Antonio on 01/12/2014.
 */
public class Ayudante  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "inmobiliaria.db";
    public static final int DATABASE_VERSION = 1;

    public Ayudante(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql="create table "+Contrato.TablaInmuebles.TABLA+
                " ("+ Contrato.TablaInmuebles._ID+
                " integer primary key autoincrement, "+
                Contrato.TablaInmuebles.DIRECCION+" text, "+
                Contrato.TablaInmuebles.TIPO+" text, "+
                Contrato.TablaInmuebles.PRECIO+" text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}