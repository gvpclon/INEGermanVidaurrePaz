package bo.gob.ine.carrito;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class MainRegproducto extends AppCompatActivity {
    EditText nomp,pre,can;
    Button guardar,modificar;
    String nomp1,pre1,can1;
    private TableLayout tabla;
    private TableRow fila;
    TableRow.LayoutParams layoutFila;
    private SQLiteDatabase db;
    private Context context;
    int idmod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainregproducto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;
        nomp=(EditText)findViewById(R.id.etnomp);
        pre=(EditText)findViewById(R.id.etpre);
        can=(EditText)findViewById(R.id.etcan);
        guardar=(Button)findViewById(R.id.btnguardar);
        modificar=(Button)findViewById(R.id.btnmodificar);
        tabla=(TableLayout)findViewById(R.id.tabla);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
        nomp1 = nomp.getText().toString().trim();
        pre1 = pre.getText().toString().trim();
        can1 = can.getText().toString().trim();
        guardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nomp1 = nomp.getText().toString().trim();
                pre1 = pre.getText().toString().trim();
                can1 = can.getText().toString().trim();

                Base_datos base = new Base_datos(context);
                String[] variables = {"producto", "precio", "cantidad"};
                String[] valores = {nomp1, pre1, can1};
                base.insert("producto", variables, valores);
                base.close();
                Toast.makeText(context, "Registro agregado.", Toast.LENGTH_SHORT).show();
                reiniciarActividad();
            }
        });
        modificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nomp1 = nomp.getText().toString().trim();
                pre1 = pre.getText().toString().trim();
                can1 = can.getText().toString().trim();

                Base_datos base = new Base_datos(context);
                ContentValues values = new ContentValues();
                values.put("producto", nomp1);
                values.put("precio", pre1);
                values.put("cantidad", can1);
                String[] args=new String[]{""+idmod};
                db.update("producto", values, "_id =?", args);
                base.close();
                Toast.makeText(context, "Registro actualizado.", Toast.LENGTH_SHORT).show();
                reiniciarActividad();
            }
        });


        agregarFilas("Producto","Precio","Stock","0");
        Base_datos base = new Base_datos(context);
        db=base.getWritableDatabase();
        Cursor clientes_existentes=db.rawQuery("SELECT * FROM producto", null);
        if(clientes_existentes.moveToFirst())
        {
            do{
                agregarFilas(clientes_existentes.getString(1),clientes_existentes.getString(2),clientes_existentes.getString(3),clientes_existentes.getString(0));
            }while(clientes_existentes.moveToNext());
        }


    }
    private int validar(){
        int valor=0;
        if(nomp1.length()==0){ valor+=1; nomp.setError("Ingrese el nombre del producto."); }else{ nomp.setError(null);}
        if(pre1.length()==0){ valor+=1; pre.setError("Ingrese el rpecio."); }else{ pre.setError(null);}
        if(can1.length()==0){ valor+=1; can.setError("Ingrese la cantidad."); }else{ can.setError(null);}
        return valor;
    }

    private void reiniciarActividad() {
        Intent a=new Intent(context,MainRegproducto.class);
        finish();
        startActivity(a);
    }
    private void agregarFilas(String produ, String prec, final String cant, String id)
    {
        fila=new TableRow(this);
        fila.setLayoutParams(layoutFila);

        TextView nombrepro=new TextView(this);
        final TextView preciopro=new TextView(this);
        TextView cantidadpro=new TextView(this);

        nombrepro.setText(produ);
        preciopro.setText(prec);
        cantidadpro.setText(cant);
        if(id.compareTo("0")!=0){
            nombrepro.setBackgroundResource(R.drawable.celda_cuerpo);
            preciopro.setBackgroundResource(R.drawable.celda_cuerpo);
            cantidadpro.setGravity(Gravity.CENTER);
            cantidadpro.setBackgroundResource(R.drawable.celda_cuerpo);
            preciopro.setGravity(Gravity.CENTER);
        }else{
            nombrepro.setBackgroundResource(R.drawable.celda_cabecera);
            nombrepro.setGravity(Gravity.CENTER);
            preciopro.setBackgroundResource(R.drawable.celda_cabecera);
            cantidadpro.setBackgroundResource(R.drawable.celda_cabecera);
        }


        nombrepro.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 9));
        preciopro.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT, 3));
        cantidadpro.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 3));

        fila.addView(nombrepro);
        fila.addView(preciopro);
        fila.addView(cantidadpro);


        if(id.compareTo("0")!=0)
        {
            ImageView editar=new ImageView(this);
            ImageView eliminar=new ImageView(this);

            editar.setId(Integer.parseInt(id));
            editar.setAdjustViewBounds(true);
            editar.setBackgroundResource(R.drawable.celda_cuerpo);
            editar.setImageResource(R.drawable.modificar);
            editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Base_datos base=new Base_datos(context);
                    db=base.getWritableDatabase();
                    Cursor producto_mod=db.rawQuery("SELECT * FROM producto where _id='"+view.getId()+"'", null);
                    if(producto_mod.moveToFirst())
                    {
                        llenarcampos(producto_mod.getString(1),producto_mod.getString(2),producto_mod.getString(3),view.getId());
                    }

                    //reiniciarActividad();


                }
            });

            eliminar.setId(Integer.parseInt(id));
            eliminar.setAdjustViewBounds(true);
            eliminar.setBackgroundResource(R.drawable.celda_cuerpo);
            eliminar.setImageResource(R.drawable.eliminar);
            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] args=new String[]{""+view.getId()};
                    db.delete("producto", "_id = ?", args);
                    Toast.makeText(context,"Registro eliminado.",Toast.LENGTH_SHORT).show();
                    reiniciarActividad();
                }
            });


            editar.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT, 2));
            eliminar.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT, 2));

            fila.addView(editar);
            fila.addView(eliminar);
        }
        else
        {
            TextView vacio = new TextView(this);
            vacio.setText("Acción");
            vacio.setBackgroundResource(R.drawable.celda_cabecera);
            vacio.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 4));
            vacio.setGravity(Gravity.CENTER);
            fila.addView(vacio);
        }

        tabla.addView(fila);
    }
    private void llenarcampos(String npro,String nprec,String ncant,int id){
        nomp.setText(npro);
        pre.setText(nprec);
        can.setText(ncant);
        guardar.setVisibility(View.GONE);
        modificar.setVisibility(View.VISIBLE);
        idmod=id;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salir:
                Intent a= new Intent(context,Mainlogin.class);
                finish();
                startActivity(a);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
