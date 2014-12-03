package com.antonio.android.inmobiliaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;


public class Principal extends Activity {
    private ArrayList<Inmueble> inmuebles;
    private ArrayList<File> fotos;
    private GestorInmuebles gi;
    private AdaptadorCursor ac;
    private Cursor cursor;
    private final int ACTIVIDAD2=1;
    private ListView lv;
    int imgActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        inmuebles = new ArrayList<Inmueble>();
        gi=new GestorInmuebles(this);
        gi.open();
        inmuebles= (ArrayList<Inmueble>) gi.select();
        cursor= gi.getCursor();
        lv=(ListView)findViewById(R.id.listView);
        ac=new AdaptadorCursor(this, cursor);
        lv.setAdapter(ac);
        imgActual = 0;
        final Fragmento2 fdos=(Fragmento2)getFragmentManager().findFragmentById(R.id.fragment_2);
        final boolean horizontal;
        if(fdos!=null && fdos.isInLayout()){
            horizontal=true;
        }else{horizontal=false;}
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String[] opc = new String[]{"Borrar", "Modificar", "Hacer Foto"};
                final int posicion = position;
                AlertDialog opciones = new AlertDialog.Builder(
                        Principal.this)
                        .setTitle("Opciones")
                        .setItems(opc,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int selected) {
                                        if (selected == 0) {
                                            borrar(posicion);
                                        } else if (selected == 1) {
                                            editar(posicion);
                                        }else if (selected == 2) {
                                            hacerFoto(posicion);
                                        }
                                    }
                                }).create();
                opciones.show();
                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Inmueble im=(Inmueble) lv.getItemAtPosition(position);
                if(horizontal){
                    fdos.setTexto(inmuebles.get(position).getDireccion(),inmuebles.get(position).getTipo(),inmuebles.get(position).getPrecio()+"€");
                    ImageView iv= (ImageView)findViewById(R.id.imageView1);
                    fotos=new ArrayList<File>();
                    Button siguiente=(Button)findViewById(R.id.bSiguiente);
                    Button atras=(Button)findViewById(R.id.bAtras);
                    String nombre=inmuebles.get(position).getId()+"";
                    String ruta = Environment.getExternalStorageDirectory() +"/fotosInmobiliaria/";
                    System.out.println(ruta+" ruta");
                    imgActual=0;
                    File carpeta = new  File(ruta);
                    File[] listaFotos = carpeta.listFiles();
                    for (int i = 0; i < listaFotos.length; i++) {
                        String idIn="";
                        idIn=listaFotos[i].getName().split("_")[0];
                        if(idIn.equals(nombre)){
                            fotos.add(listaFotos[i]);
                        }
                    }
                    if(!fotos.isEmpty()){
                        if (fotos.size() == 1) {
                            iv.setImageURI(Uri.fromFile(fotos.get(0)));
                            atras.setVisibility(View.INVISIBLE);
                            siguiente.setVisibility(View.INVISIBLE);
                        } else if (fotos.size() > 1) {
                            iv.setVisibility(View.VISIBLE);
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
                    }else{
                        iv.setImageResource(R.drawable.nofoto);
                        System.out.println("NO existe file");
                        atras.setVisibility(View.INVISIBLE);
                        siguiente.setVisibility(View.INVISIBLE);
                    }
                }else {
                    Intent i=new Intent(Principal.this,Secundaria.class);
                    i.putExtra("id", im.getId());
                    i.putExtra("dir", im.getDireccion());
                    i.putExtra("tip",im.getTipo());
                    i.putExtra("pr",im.getPrecio()+"€");
                    startActivityForResult(i,ACTIVIDAD2);
                }
            }
        });
        Adaptador ad=new Adaptador(this,R.layout.detalle,inmuebles);
        lv.setAdapter(ad);
        registerForContextMenu(lv);

    }

    private boolean editar(final int index) {

        //cargamos jugador
        Inmueble i=new Inmueble();
        i=inmuebles.get(index);
        //cargamos vista
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo, null);
        final EditText et1, et2,et3;
        String pr,dir,tip;
        pr=i.getPrecio()+"";
        dir=i.getDireccion();
        tip=i.getTipo();
        et1 = (EditText) vista.findViewById(R.id.etDA);
        et2 = (EditText) vista.findViewById(R.id.etTA);
        et3 = (EditText) vista.findViewById(R.id.etPA);

        et1.setText(dir);
        et2.setText(tip);
        et3.setText(pr);


        //dialogo
        final AlertDialog d = new AlertDialog.Builder(this)
                .setView(vista)
                .setTitle("Modificar inmueble")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if(et1.getText().toString().length() > 0
                                && et3.getText().length() > 0&& et2.getText().length()>0 ) {
                            Double pr = 0.0;
                            Inmueble i = inmuebles.get(index);

                            try {
                                pr = Double.parseDouble(et3.getText().toString());
                            } catch (Exception e) {
                            }
                            i.setPrecio(pr);
                            i.setDireccion(et1.getText().toString());
                            i.setTipo(et2.getText().toString());
                            gi.update(i);
                            inmuebles.set(index,i);
                            Adaptador ad = new Adaptador(Principal.this, R.layout.detalle, inmuebles);
                            ad.notifyDataSetChanged();

                            ListView lv = (ListView) findViewById(R.id.listView);
                            lv.setAdapter(ad);
                            tostada("El inmueble de "+i.getDireccion()+" ha sido modificado");
                            d.dismiss();
                        }
                        // Filtramos que nos este vacios
                        if(et2.getText().toString().length() == 0 ){
                            tostada("¡Introduzca tipo!");
                        }
                        if(et3.getText().toString().length() == 0 ){
                            tostada("¡Introduzca precio!");
                        }
                        if(et1.getText().toString().length() == 0 ){
                            tostada("¡Introduzca direccion!");
                        }
                    }
                });
            }
        });
        d.show();
        lv=(ListView) findViewById(R.id.listView);
        Adaptador ad=new Adaptador(this,R.layout.detalle,inmuebles);
        lv.setAdapter(ad);
        return true;
    }

    public boolean borrar(final int pos){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Principal.this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿ Desea borrar el inmueble seleccionado ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                gi.delete(inmuebles.get(pos));
                inmuebles.remove(pos);
                Adaptador ad = new Adaptador(Principal.this, R.layout.detalle, inmuebles);
                ad.notifyDataSetChanged();
                ListView lv = (ListView) findViewById(R.id.listView);
                lv.setAdapter(ad);
                tostada("Inmueble borrado");
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                finish();
            }
        });
        dialogo1.show();
        return true;
    }

    private boolean agregar() {

        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo, null);
        final EditText et1, et2,et3;
        et1 = (EditText) vista.findViewById(R.id.etDA);
        et2 = (EditText) vista.findViewById(R.id.etTA);
        et3 = (EditText) vista.findViewById(R.id.etPA);
        et1.setHint("Introduzca direccion");
        et2.setHint("Introduzca tipo");
        et3.setHint("Introduzca precio");

        //dialogo
        final AlertDialog d = new AlertDialog.Builder(this)
                .setView(vista)
                .setTitle("Añadir inmueble")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //filtros

                        if(et1.getText().toString().length() > 0
                                && et3.getText().length() > 0&& et2.getText().length()>0) {
                            Double pr = 0.0;

                            Inmueble i = new Inmueble();
                            try {
                                pr = Double.parseDouble(et3.getText().toString());
                            } catch (Exception e) {
                            }
                            i.setPrecio(pr);
                            i.setDireccion(et1.getText().toString());
                            i.setTipo(et2.getText().toString());
//                      añadimos jugados y mostramos
                            gi.insert(i);
                            inmuebles.add(i);
                            Adaptador ad = new Adaptador(Principal.this, R.layout.detalle, inmuebles);
                            ad.notifyDataSetChanged();

                            ListView lv = (ListView) findViewById(R.id.listView);
                            lv.setAdapter(ad);
                            tostada("El inmueble de "+i.getDireccion()+" ha sido añadido");
                            d.dismiss();
                        }
                        // Filtramos que nos este vacios
                        if(et2.getText().toString().length() == 0 ){
                            tostada("¡Introduzca tipo!");
                        }
                        if(et3.getText().toString().length() == 0 ){
                            tostada("¡Precio incorrecto!");
                        }
                        if(et1.getText().toString().length() == 0 ){
                            tostada("¡Introduzca direccion!");
                        }
                    }
                });
            }
        });
        d.show();
        lv=(ListView) findViewById(R.id.listView);
        Adaptador ad=new Adaptador(this,R.layout.detalle,inmuebles);
        lv.setAdapter(ad);
        return true;

    }

    public boolean isRepetido(Inmueble i){
        for(int j=0;j<inmuebles.size();j++){
            if(inmuebles.get(j).compareTo(i)==0){
                return true;
            }
        }
        return false;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_principal, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.anadir) {
            agregar();
            return true;
        }
        if (id == R.id.ordenaP) {
            Collections.sort(inmuebles, new OrdenaPrecios());
            Adaptador ad = new Adaptador(Principal.this, R.layout.detalle, inmuebles);
            ListView lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(ad);
            return true;

        }
        if (id == R.id.ordenaT) {
            Collections.sort(inmuebles, new OrdenaTipos());
            Adaptador ad = new Adaptador(Principal.this, R.layout.detalle, inmuebles);
            ListView lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(ad);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    public Toast tostada(String t) {
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        t + "", Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        lv=(ListView)findViewById(R.id.listView);
        outState.putParcelableArrayList("Inmuebles", inmuebles);

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        inmuebles= savedInstanceState.getParcelableArrayList("Inmuebles");
        Adaptador ad = new Adaptador(this, R.layout.detalle, inmuebles);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(ad);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == 2&&resultCode == Activity.RESULT_OK ){
            inmuebles=data.getParcelableArrayListExtra("inmuebles");
            Adaptador ad = new Adaptador(Principal.this, R.layout.detalle, inmuebles);
            ad.notifyDataSetChanged();
            lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(ad);
        }
        if (requestCode == 3 && resultCode == RESULT_OK) {

        }
    }
    private boolean hacerFoto(final int index){
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String fecha = df.format(date);
        int id=inmuebles.get(index).getId();
        String nombre=id+"_"+fecha;
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File imagesFolder = new File(
                Environment.getExternalStorageDirectory(), "fotosInmobiliaria");
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, nombre+".jpg");
        Uri uriSavedImage = Uri.fromFile(image);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(cameraIntent, ACTIVIDAD2);
        return true;
    }

}

