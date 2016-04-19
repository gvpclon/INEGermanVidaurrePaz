package bo.gob.ine.carrito;

import android.app.Dialog;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import bo.gob.ine.carrito.R;

public class MainReporte extends AppCompatActivity {
    private TableLayout tabla;
    private TableRow fila;
    TableRow.LayoutParams layoutFila;
    private SQLiteDatabase db;
    private Context context;
    String email1;
    TextView fecha,lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainreporte);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                final Dialog dialog = new Dialog(MainReporte.this);
                dialog.setContentView(R.layout.activity_email);
                dialog.setTitle("Correo electrónico a enviar listado:");
                final TextView email=(TextView) dialog.findViewById(R.id.etmail);
                Button enviar=(Button) dialog.findViewById(R.id.btnenviar);
                Button cancelar=(Button) dialog.findViewById(R.id.btncancelare);
                enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(email.getText().toString().length() == 0)
                        { email.setError("Ingrese su correo."); return;} else{email.setError(null);
                            if(email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){ email.setError("Correo incorrecto."); return; }else{ email.setError(null);}}

                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString()});
                        i.putExtra(Intent.EXTRA_SUBJECT,"Listado de pedido.");
                        i.putExtra(Intent.EXTRA_TEXT,"Enviado por android");
                        startActivity(Intent.createChooser(i, "Seleccione la aplicacion de mail."));
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        fecha=(TextView)findViewById(R.id.tvfecha);
        lista=(TextView)findViewById(R.id.tvlistado);
        tabla=(TableLayout)findViewById(R.id.tabla);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
        agregarFilas("Producto","Cantidad","Total","0");
        Base_datos base = new Base_datos(context);
        fecha.setText(fecha());
        db=base.getWritableDatabase();
        Cursor pedido=db.rawQuery("SELECT pedido._id, producto.producto, pedido.cant_pedido as cantidad, (pedido.cant_pedido * producto.precio) AS total " +
                "FROM producto INNER JOIN pedido ON (producto._id = pedido.id_producto) " +
                "INNER JOIN cliente ON (pedido.id_cliente = cliente._id) " +
                "WHERE cliente.usuario = '"+usuario()+"' AND  pedido.fechapedido = '"+fecha()+"' " +
                "GROUP BY producto.producto,pedido.cant_pedido,producto.precio,pedido._id", null);
        if(pedido.moveToFirst())
        {
            String listado="";
            int i=0;
            do{
                i=1+1;
                listado=listado+"Producto: "+pedido.getString(1)+"  Cantidad:"+pedido.getString(2)+"  Total:"+ pedido.getString(3)+"\n";
                agregarFilas(pedido.getString(1), pedido.getString(2), pedido.getString(3), pedido.getString(0));
            }while(pedido.moveToNext());
            lista.setText(listado);
        }
    }

    public String fecha(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechareg = sdf.format(new java.util.Date());
        return fechareg;
    }
    public String usuario(){
        Mainlogin Mainlogin=new Mainlogin();
        String incoming_name =Mainlogin.name;//I can not use like that ?
        return incoming_name;
    }
    private void agregarFilas(String produ,  String cant,String prec, String id)
    {
        fila=new TableRow(this);
        fila.setLayoutParams(layoutFila);

        TextView nombrepro=new TextView(this);
        TextView cantidadpro=new TextView(this);
        TextView preciopro=new TextView(this);

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
        cantidadpro.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 3));
        preciopro.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 3));

        fila.addView(nombrepro);
        fila.addView(cantidadpro);
        fila.addView(preciopro);


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
                public void onClick(final View view) {
                    final Dialog dialog = new Dialog(MainReporte.this);
                    dialog.setContentView(R.layout.activity_cantpedidom);
                    dialog.setTitle("Nueva cantidad solicitada:");
                    final TextView tvpro1=(TextView) dialog.findViewById(R.id.tvpro);
                    final TextView tvcan1=(TextView) dialog.findViewById(R.id.tvcan);
                    final TextView tvcos1=(TextView) dialog.findViewById(R.id.tvcos);
                    final TextView tvped1=(TextView) dialog.findViewById(R.id.tvped);
                    final EditText etcan=(EditText) dialog.findViewById(R.id.etcanu);

                    Button aceptar1=(Button) dialog.findViewById(R.id.modi);
                    Button cancelar=(Button) dialog.findViewById(R.id.btncancelarl);
                    Base_datos base=new Base_datos(context);
                    final String[] rescon = base.conseguirpedido(String.valueOf(view.getId()));
                    tvpro1.setText(rescon[0].toString());
                    tvcan1.setText(rescon[1].toString());
                    tvcos1.setText(rescon[2].toString());
                    tvped1.setText(rescon[4].toString());

                    aceptar1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] valores1 = tvcos1.getText().toString().split(":");
                            if (etcan.getText().toString().length() == 0) {
                                etcan.setError("Ingrese la cantidad de unidades.");
                                return;
                            } else {
                                etcan.setError(null);
                            }

                            if (Integer.parseInt(etcan.getText().toString()) > Integer.parseInt(valores1[1].toString().trim())) {
                                etcan.setError("La cantidad ingresada es superior a la existente.");
                                return;
                            } else {
                                etcan.setError(null);
                            }
                            String[] valores2 = tvped1.getText().toString().split(":");

                            int valores=Integer.parseInt(etcan.getText().toString())+Integer.parseInt(valores2[1].toString().trim());
                            Base_datos base = new Base_datos(context);
                            ContentValues values = new ContentValues();
                            values.put("cant_pedido", String.valueOf(valores));
                            String[] args=new String[]{""+String.valueOf(view.getId())};
                            db.update("pedido", values, "_id =?", args);
                            base.close();
                            Toast.makeText(context, "Registro actualizado.", Toast.LENGTH_SHORT).show();
                            reiniciarActividad();

                        }

                    });
                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            etcan.setText("");
                        }
                    });
                    dialog.show();

                }
            });

            eliminar.setId(Integer.parseInt(id));
            eliminar.setAdjustViewBounds(true);
            eliminar.setBackgroundResource(R.drawable.celda_cuerpo);
            eliminar.setImageResource(R.drawable.eliminarc);
            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] args=new String[]{""+view.getId()};
                    db.delete("pedido", "_id = ?", args);
                    Toast.makeText(context, "Registro eliminado.", Toast.LENGTH_SHORT).show();
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
        Intent a=new Intent(context,MainReporte.class);
        finish();
        startActivity(a);
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
