package com.antonio.android.inmobiliaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
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
import java.util.Date;
import java.util.GregorianCalendar;


public class Principal extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    /*************                                           ************************//////
    /************************************variables***********************************//////
    /*************                                          ************************//////
    private ArrayList<Inmueble> inmuebles;
    private ArrayList<File> fotos;
    private AdaptadorCursor ac;
    private final int ACTIVIDAD2=1;
    private ListView lv;
    private Loader loader;
    private GestorInmuebleCP gi;
    private int imgActual;
    /*************                                           ************************//////
    /************************************onCreate()***********************************//////
    /*************                                          ************************//////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String user = sharedPref.getString("usuario", "AntonioBMR");
        getLoaderManager().initLoader(0, null, this);
        inmuebles = new ArrayList<Inmueble>();
        gi = new GestorInmuebleCP(this);
        lv=(ListView)findViewById(R.id.listView);
        ac=new AdaptadorCursor(this, null);
        lv.setAdapter(ac);
        loader = getLoaderManager().initLoader(0, null, this);
        imgActual = 0;
        final Fragmento2 fdos=(Fragmento2)getFragmentManager().findFragmentById(R.id.fragment_2);
        final boolean horizontal;
        if(fdos!=null && fdos.isInLayout()){
            horizontal=true;
        }else{
            horizontal=false;}
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = gi.getCursor();
                cursor.moveToPosition(position);
                Inmueble im= GestorInmuebleCP.getRow(cursor);
                cursor.close();
                if(horizontal){
                    fdos.setTexto(im.getLocalidad(),im.getDireccion(),im.getTipo(),im.getPrecio()+"€");
                    ImageView iv= (ImageView)findViewById(R.id.imageView1);
                    iv.setVisibility(View.VISIBLE);
                    fotos=new ArrayList<File>();
                    Button siguiente=(Button)findViewById(R.id.bSiguiente);
                    Button atras=(Button)findViewById(R.id.bAtras);
                    String nombre=im.getId()+"";
                    String ruta = Environment.getExternalStorageDirectory() +"/fotosInmobiliaria/";
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
                        iv.setVisibility(View.VISIBLE);
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
                        iv.setVisibility(View.VISIBLE);
                        iv.setImageResource(R.drawable.nofoto);
                        atras.setVisibility(View.INVISIBLE);
                        siguiente.setVisibility(View.INVISIBLE);
                    }
                }else {
                    Intent i=new Intent(Principal.this,Secundaria.class);
                    i.putExtra("id", im.getId());
                    i.putExtra("loc", im.getLocalidad());
                    i.putExtra("dir", im.getDireccion());
                    i.putExtra("tip", im.getTipo());
                    i.putExtra("pr",im.getPrecio()+"€");
                    startActivityForResult(i,ACTIVIDAD2);
                }
            }
        });
        registerForContextMenu(lv);
        ac=new AdaptadorCursor(this,null);
        lv.setAdapter(ac);
        loader.onContentChanged();
    }

    /*************                                                         ************************//////
    /************************************METODOS CRUD INTERFACE***********************************//////
    /*************                                                   ************************//////
    private boolean editar(final Inmueble i) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo, null);
        final EditText et,et1, et2,et3;
        String loc,pr,dir,tip;
        loc=i.getLocalidad();
        pr=i.getPrecio()+"";
        dir=i.getDireccion();
        tip=i.getTipo();
        et = (EditText) vista.findViewById(R.id.etLA);
        et1 = (EditText) vista.findViewById(R.id.etDA);
        et2 = (EditText) vista.findViewById(R.id.etTA);
        et3 = (EditText) vista.findViewById(R.id.etPA);
        et.setText(loc);
        et1.setText(dir);
        et2.setText(tip);
        et3.setText(pr);
        final AlertDialog d = new AlertDialog.Builder(this)
                .setView(vista)
                .setTitle(R.string.ModificarIn)
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

                        if(et.getText().toString().length() > 0&&et1.getText().toString().length() > 0
                                && et3.getText().length() > 0&& et2.getText().length()>0 ) {
                            Double pr = 0.0;
                            try {
                                pr = Double.parseDouble(et3.getText().toString());
                            } catch (Exception e) {
                            }
                            i.setPrecio(pr);
                            i.setLocalidad(et.getText().toString());
                            i.setDireccion(et1.getText().toString());
                            i.setTipo(et2.getText().toString());
                            gi.update(i);
                            tostada(R.string.elimb+i.getDireccion()+R.string.hasdm);
                            d.dismiss();
                        }
                        // Filtramos que nos este vacios
                        if(et2.getText().toString().length() == 0 ){
                            tostada(getResources().getString(R.string.intT));
                        }
                        if(et3.getText().toString().length() == 0 ){
                            tostada(getResources().getString(R.string.intP));
                        }
                        if(et1.getText().toString().length() == 0 ){
                            tostada(getResources().getString(R.string.intD));
                        }
                        if(et.getText().toString().length() == 0 ){
                            tostada(getResources().getString(R.string.intL));
                        }
                    }
                });
            }
        });
        d.show();
        lv=(ListView) findViewById(R.id.listView);
        ac=new AdaptadorCursor(this,null);
        lv.setAdapter(ac);
        loader.onContentChanged();
        return true;
    }

    private boolean borrar(final int idI){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Principal.this);
        dialogo1.setTitle(getResources().getString(R.string.importante));
        dialogo1.setMessage(getResources().getString(R.string.desBorrar));
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(getResources().getString(R.string.conf), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                borraFotos(idI);
                gi.eliminar(idI);
                final Fragmento2 fdos=(Fragmento2)getFragmentManager().findFragmentById(R.id.fragment_2);
                ImageView iv= (ImageView)findViewById(R.id.imageView1);
                final boolean horizontal;
                if(fdos!=null && fdos.isInLayout()){
                    horizontal=true;
                    fdos.setTexto("","","","");
                    iv.setVisibility(View.INVISIBLE);
                }
                loader.onContentChanged();
                ac = new AdaptadorCursor(Principal.this, null);
                ac.notifyDataSetChanged();
                ListView lv = (ListView) findViewById(R.id.listView);
                lv.setAdapter(ac);
                loader.onContentChanged();

                tostada(getResources().getString(R.string.elimb)+getResources().getString(R.string.hasB));
            }
        });
        dialogo1.setNegativeButton(getResources().getString(R.string.canc), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.cancel();
            }
        });
        dialogo1.show();
        return true;
    }

    private boolean agregar() {

        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo, null);
        final EditText et,et1, et2,et3;
        et = (EditText) vista.findViewById(R.id.etLA);
        et1 = (EditText) vista.findViewById(R.id.etDA);
        et2 = (EditText) vista.findViewById(R.id.etTA);
        et3 = (EditText) vista.findViewById(R.id.etPA);
        et.setHint(getResources().getString(R.string.intL));
        et1.setHint(getResources().getString(R.string.intD));
        et2.setHint(getResources().getString(R.string.intT));
        et3.setHint(getResources().getString(R.string.intP));

        //dialogo
        final AlertDialog d = new AlertDialog.Builder(this)
                .setView(vista)
                .setTitle(getResources().getString(R.string.agregarI))
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
                        if(et1.getText().toString().length() > 0&& et1.getText().toString().length() > 0
                                && et3.getText().length() > 0&& et2.getText().length()>0) {
                            Double pr = 0.0;

                            Inmueble i = new Inmueble();
                            try {
                                pr = Double.parseDouble(et3.getText().toString());
                            } catch (Exception e) {
                            }
                            i.setPrecio(pr);
                            i.setLocalidad(et.getText().toString());
                            i.setDireccion(et1.getText().toString());
                            i.setTipo(et2.getText().toString());
//                      añadimos jugados y mostramos
                            gi.insertar(i);
                            gi.select();
                            ac = new AdaptadorCursor(Principal.this,null);
                            ac.notifyDataSetChanged();

                            ListView lv = (ListView) findViewById(R.id.listView);
                            lv.setAdapter(ac);
                            loader.onContentChanged();

                            tostada(getResources().getString(R.string.elimb)+i.getDireccion()+getResources().getString(R.string.hasA));
                            d.dismiss();
                        }
                        // Filtramos que nos este vacios
                        if(et2.getText().toString().length() == 0 ){
                            tostada(getResources().getString(R.string.intT));
                        }
                        if(et3.getText().toString().length() == 0 ){
                            tostada(getResources().getString(R.string.intP));
                        }
                        if(et1.getText().toString().length() == 0 ){
                            tostada(getResources().getString(R.string.intD));
                        }if(et.getText().toString().length() == 0 ){
                            tostada(getResources().getString(R.string.intL));
                        }
                    }
                });
            }
        });
        d.show();
        lv=(ListView) findViewById(R.id.listView);
        ac=new AdaptadorCursor(this,null);
        lv.setAdapter(ac);
        loader.onContentChanged();

        return true;

    }

    /*************                                                         ************************//////
    /************************************METODOS MENUs ACTIVIDAD***********************************//////
    /*************                                                   ************************//////
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextual, menu);

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index= info.position;
        Cursor cursor = gi.getCursor();
        cursor.moveToPosition(index);
        Inmueble inmueble = GestorInmuebleCP.getRow(cursor);
        cursor.close();
        if (id == R.id.borrar) {
            borrar(inmueble.getId());

               return true;
        }else if (id == R.id.editar) {
            editar(inmueble);
            final Fragmento2 fdos=(Fragmento2)getFragmentManager().findFragmentById(R.id.fragment_2);
            ImageView iv=(ImageView)findViewById(R.id.imageView1);
            final boolean horizontal;
            if(fdos!=null && fdos.isInLayout()){
                horizontal=true;
                fdos.setTexto("","","","");
                iv.setVisibility(View.INVISIBLE);

            }
            loader.onContentChanged();
            return true;
        }
        else if (id == R.id.foto) {
            hacerFoto(inmueble);
            final Fragmento2 fdos=(Fragmento2)getFragmentManager().findFragmentById(R.id.fragment_2);
            ImageView iv=(ImageView)findViewById(R.id.imageView1);
            final boolean horizontal;
            if(fdos!=null && fdos.isInLayout()){
                horizontal=true;
                fdos.setTexto("","","","");
                iv.setVisibility(View.INVISIBLE);
            }
            loader.onContentChanged();
            return true;
        }
        else if (id == R.id.Bfoto) {
            borraFotos(inmueble.getId());
            final Fragmento2 fdos=(Fragmento2)getFragmentManager().findFragmentById(R.id.fragment_2);
            ImageView iv=(ImageView)findViewById(R.id.imageView1);
            final boolean horizontal;
            if(fdos!=null && fdos.isInLayout()){
                horizontal=true;
                fdos.setTexto("","","","");
                iv.setVisibility(View.INVISIBLE);

            }
            loader.onContentChanged();

            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.anadir) {
            agregar();
            return true;
        }
        if (id == R.id.usuario) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getResources().getString(R.string.usuario));
            LayoutInflater inflater = LayoutInflater.from(this);
            final View v = inflater.inflate(R.layout.dialogo_usuario, null);
            alert.setView(v);
            alert.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            EditText ett = (EditText) v.findViewById(R.id.et_usuario);
                            String txt = ett.getText().toString();
                            txt=txt.trim();
                            if(txt.length()>0) {
                                SharedPreferences sharedPref = Principal.this.getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("usuario", txt);
                                editor.commit();
                            }
                        }
                    });
            alert.setNegativeButton(android.R.string.cancel,null);
            alert.show();
        }
        if (id == R.id.ordenaP) {
            Cursor cursor=gi.getCursorPre();
            //Collections.sort(inmuebles, new OrdenaPrecios());
            ac = new AdaptadorCursor(Principal.this,cursor);
            ListView lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(ac);
            loader.onContentChanged();
            return true;
        }
        if (id == R.id.ordenaT) {
            Cursor cursor=gi.getCursorPre();
           // Collections.sort(inmuebles, new OrdenaTipos());
            ac = new AdaptadorCursor(Principal.this, cursor);
            ListView lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(ac);
            loader.onContentChanged();
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

/*************                                                         ************************//////
    /*********************************METODOS AUXILIARES**********************************//////
    /*************                                                   ************************//////
    public Toast tostada(String t) {
        Toast toast =
                Toast.makeText(getApplicationContext(),
                        t + "", Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }
    /*
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
        ac= new AdaptadorCursor(this,null);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(ac);
        loader.onContentChanged();

    }
    */
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == 2&&resultCode == Activity.RESULT_OK ){
            inmuebles=data.getParcelableArrayListExtra("inmuebles");
            ac = new AdaptadorCursor(Principal.this,null);
            ac.notifyDataSetChanged();
            lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(ac);
        }
        if (requestCode == 3 && resultCode == RESULT_OK) {

        }
    }*/
    private boolean hacerFoto(final Inmueble index){
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String fecha = df.format(date);
        int id=(index).getId();
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

    public void borraFotos(final int id){
        String ruta = Environment.getExternalStorageDirectory() +"/fotosInmobiliaria/";
        File carpeta = new  File(ruta);
        String[] listaFotos = carpeta.list();
        for (int i = 0; i < listaFotos.length; i++) {
            String idFoto=listaFotos[i];
            String idF=listaFotos[i].split("_")[0];
            if(idF.equals(id+"")){
                File f= new File(ruta,idFoto);
                f.delete();
            }
        }
    }
    /*************                                                         ************************//////
    /*********************************METODOS LOADER**********************************//////
    /*************                                                   ************************//////
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this,
                Contrato.TablaInmuebles.CONTENT_URI, null, null, null, Contrato.TablaInmuebles._ID);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ac.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ac.swapCursor(null);
    }
}

