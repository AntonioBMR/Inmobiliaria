package com.antonio.android.inmobiliaria;

import android.provider.BaseColumns;

/**
 * Created by Antonio on 01/12/2014.
 */
public class Contrato {

    private Contrato(){

    }

    public static abstract class TablaInmuebles implements BaseColumns {
        public static final String TABLA = "inmuebles";
        public static final String DIRECCION = "direccion";
        public static final String TIPO = "tipo";
        public static final String PRECIO = "precio";
    }
}