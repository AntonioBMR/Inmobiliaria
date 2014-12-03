package com.antonio.android.inmobiliaria;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class Secundaria extends Activity {
    private ArrayList<File> fotos;
    int imgActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secundaria);
        String dir, tip, pr;
        int id;
        fotos = new ArrayList<File>();
        dir = getIntent().getStringExtra("dir");
        tip = getIntent().getStringExtra("tip");
        pr = getIntent().getStringExtra("pr");
        id = getIntent().getExtras().getInt("id");
        TextView tv = (TextView) findViewById(R.id.tvDF2);
        TextView tv1 = (TextView) findViewById(R.id.tvTF2);
        TextView tv2 = (TextView) findViewById(R.id.tvPF2);
        final Fragmento2 fdos = (Fragmento2) getFragmentManager().findFragmentById(R.id.fragment_2);
        ImageView iv = (ImageView) findViewById(R.id.imageView1);
        Button siguiente = (Button) findViewById(R.id.bSiguiente);
        Button atras = (Button) findViewById(R.id.bAtras);
        String nombre = id + "";
        String ruta = Environment.getExternalStorageDirectory() + "/fotosInmobiliaria/";
        System.out.println(ruta + " ruta");
        imgActual = 0;
        File carpeta = new File(ruta);
        File[] listaFotos = carpeta.listFiles();
        for (int i = 0; i < listaFotos.length; i++) {
            String idIn = "";
            idIn = listaFotos[i].getName().split("_")[0];
            if (idIn.equals(nombre)) {
                fotos.add(listaFotos[i]);
            }
        }
        if (!fotos.isEmpty()) {
            if (fotos.size() == 1) {
                iv.setImageURI(Uri.fromFile(fotos.get(0)));
                atras.setVisibility(View.INVISIBLE);
                siguiente.setVisibility(View.INVISIBLE);
            } else if (fotos.size() > 1) {

                iv.setImageURI(Uri.fromFile(fotos.get(0)));
                atras.setVisibility(View.VISIBLE);
                siguiente.setVisibility(View.VISIBLE);
                atras.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView iv = (ImageView) findViewById(R.id.imageView1);
                        int imgFin = fotos.size() - 1;
                        if (imgActual + 1 <= imgFin) {
                            iv.setImageURI(Uri.fromFile(fotos.get(imgActual + 1)));
                            imgActual++;
                        } else {
                            iv.setImageURI(Uri.fromFile(fotos.get(0)));
                            imgActual = 0;
                        }

                    }
                });
                siguiente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView iv = (ImageView) findViewById(R.id.imageView1);
                        int imgFin = fotos.size() - 1;
                        if (imgActual + 1 <= imgFin) {
                            iv.setImageURI(Uri.fromFile(fotos.get(imgActual + 1)));
                            imgActual++;
                        } else {
                            iv.setImageURI(Uri.fromFile(fotos.get(0)));
                            imgActual = 0;
                        }
                    }

                });
            }
        }else {
                System.out.println("NO existe file");
                iv.setImageResource(R.drawable.nofoto);
                atras.setVisibility(View.INVISIBLE);
                siguiente.setVisibility(View.INVISIBLE);
            }
            tv.setText(dir);
            tv1.setText(tip);
            tv2.setText(pr);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_secundaria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onSaveInstanceState(Bundle b){
        super.onSaveInstanceState(b);
        finish();
    }
    public Toast tostada(String t) {
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        t + "", Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }
}
