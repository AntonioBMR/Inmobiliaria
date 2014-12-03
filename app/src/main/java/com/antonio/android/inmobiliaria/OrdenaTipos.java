package com.antonio.android.inmobiliaria;

import java.util.Comparator;

/**
 * Created by Antonio on 26/11/2014.
 */
public class OrdenaTipos implements Comparator<Inmueble> {
    @Override
    public int compare(Inmueble j1, Inmueble j2) {
        if(j1.getTipo().compareTo((j2.getTipo()))>0){
            return 1;
        }
        if(j1.getTipo().compareTo((j2.getTipo()))<0)
            return -1;
        return 0;
    }
}