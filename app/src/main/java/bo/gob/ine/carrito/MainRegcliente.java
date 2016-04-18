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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class MainRegcliente extends AppCompatActivity {
    EditText nom,ape,doc,usu,con;
    Button guardar, modificar;
    String nom1,ape1,doc1,usu1,con1;
    Spinner tip;
    private TableLayout tabla;
    private TableRow fila;
    TableRow.LayoutParams layoutFila;
    private SQLiteDatabase db;
    private Context context;
    String[] opciones;
    int idmod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainregcliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=this;
        nom=(EditText)findViewById(R.id.etnom);
        ape=(EditText)findViewById(R.id.etape);
        doc=(EditText)findViewById(R.id.etdoc);
        usu=(EditText)findViewById(R.id.etusu);
        con=(EditText)findViewById(R.id.etcon);
        tip=(Spinner)findViewById(R.id.sptip);

        opciones=new String[]{"Seleccionar","Administrador","Cliente"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opciones);
        tip.setAdapter(adaptador);
        guardar=(Button)findViewById(R.id.btnguardar);
        modificar=(Button)findViewById(R.id.btnmodificar);

        tabla=(TableLayout)findViewById(R.id.tabla);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
        guardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int tipo = tip.getSelectedItemPosition();
                nom1 = nom.getText().toString().trim();
                ape1 = ape.getText().toString().trim();
                doc1 = doc.getText().toString().trim();
                usu1 = usu.getText().toString().trim();
                con1 = con.getText().toString().trim();

                Base_datos base = new Base_datos(context);
                String[] variables = {"nombres", "apellidos", "nrodocumento", "usuario", "contrasena", "rol"};
                String[] valores = {nom1, ape1, doc1, usu1, con1, String.valueOf(tipo)};
                base.insert("cliente",variables,valores);
                base.close();
                Toast.makeText(getApplicationContext(), "Registro agregado.", Toast.LENGTH_SHORT).show();
                reiniciarActividad();
            }
        });
        modificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int tipo = tip.getSelectedItemPosition();
                nom1 = nom.getText().toString().trim();
                ape1 = ape.getText().toString().trim();
                doc1 = doc.getText().toString().trim();
                usu1 = usu.getText().toString().trim();
                con1 = con.getText().toString().trim();

                Base_datos base = new Base_datos(context);
                ContentValues values = new ContentValues();
                values.put("nombres", nom1);
                values.put("apellidos", ape1);
                values.put("nrodocumento", doc1);
                values.put("usuario", usu1);
                values.put("contrasena", con1);
                values.put("rol", tipo);
                String[] args=new String[]{""+idmod};
                db.update("cliente", values, "_id =?", args);
                base.close();
                Toast.makeText(context, "Registro actualizado.", Toast.LENGTH_SHORT).show();
                reiniciarActividad();
            }
        });

        agregarFilas("Nombre","Apellidos","Documento","Rol","0");
        Base_datos base = new Base_datos(context);
        db=base.getWritableDatabase();
        Cursor clientes_existentes=db.rawQuery("SELECT * FROM cliente", null);
        if(clientes_existentes.moveToFirst())
        {
            do{
                agregarFilas(clientes_existentes.getString(1),clientes_existentes.getString(2),clientes_existentes.getString(3),clientes_existentes.getString(6),clientes_existentes.getString(0));
            }while(clientes_existentes.moveToNext());
        }

    }

    private void agregarFilas(String nomb,String apel,String docu, String role, String id)
    {
        fila=new TableRow(this);
        fila.setLayoutParams(layoutFila);

        TextView nombreclie=new TextView(this);
        TextView apeclie=new TextView(this);
        TextView docclie=new TextView(this);
        TextView rolclie=new TextView(this);

        nombreclie.setText(nomb);
        apeclie.setText(apel);
        docclie.setText(docu);
        rolclie.setText(role);

        if(id.compareTo("0")!=0){
            nombreclie.setBackgroundResource(R.drawable.celda_cuerpo);
            apeclie.setBackgroundResource(R.drawable.celda_cuerpo);
            docclie.setBackgroundResource(R.drawable.celda_cuerpo);
            docclie.setGravity(Gravity.CENTER);
            rolclie.setBackgroundResource(R.drawable.celda_cuerpo);
            rolclie.setGravity(Gravity.CENTER);
        }else{
            nombreclie.setBackgroundResource(R.drawable.celda_cabecera);
            nombreclie.setGravity(Gravity.CENTER);
            apeclie.setBackgroundResource(R.drawable.celda_cabecera);
            docclie.setBackgroundResource(R.drawable.celda_cabecera);
            rolclie.setBackgroundResource(R.drawable.celda_cabecera);
        }


        nombreclie.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 5));
        apeclie.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT, 5));
        docclie.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT, 4));
        rolclie.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT, 4));

        fila.addView(nombreclie);
        fila.addView(apeclie);
        fila.addView(docclie);
        fila.addView(rolclie);


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
                    Cursor cliente_mod=db.rawQuery("SELECT * FROM cliente where _id='"+view.getId()+"'", null);
                    if(cliente_mod.moveToFirst())
                    {
                        nom.setText(cliente_mod.getString(1));
                        ape.setText(cliente_mod.getString(2));
                        doc.setText(cliente_mod.getString(3));
                        usu.setText(cliente_mod.getString(4));
                        con.setText(cliente_mod.getString(5));
                        tip.setSelection(Integer.valueOf(cliente_mod.getString(6)));

                        guardar.setVisibility(View.GONE);
                        modificar.setVisibility(View.VISIBLE);
                        idmod=view.getId();
                    }                }
            });

            eliminar.setId(Integer.parseInt(id));
            eliminar.setAdjustViewBounds(true);
            eliminar.setBackgroundResource(R.drawable.celda_cuerpo);
            eliminar.setImageResource(R.drawable.eliminar);
            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] args=new String[]{""+view.getId()};
                    db.delete("cliente", "_id = ?", args);
                    Toast.makeText(getApplicationContext(),"Registro eliminado.",Toast.LENGTH_SHORT).show();
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
    private void reiniciarActividad() {
        Intent a=new Intent(context,MainRegcliente.class);
        finish();
        startActivity(a);
    }
}
