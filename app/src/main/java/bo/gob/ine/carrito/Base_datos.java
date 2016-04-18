package bo.gob.ine.carrito;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;


public class Base_datos extends SQLiteOpenHelper
{
    public static final String NOMBREBD = "carrito.sqlite";
    public static final int VERSION = 1;
    //Versión de la base de datos
    public Base_datos(Context context)
    {
        super(context, NOMBREBD, null, VERSION);

    }

    //Método utilizado cuando se crea la base de datos.
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table producto (_id integer primary key autoincrement not null, producto text, precio integer,cantidad integer);");
        db.execSQL("create table cliente (_id integer primary key autoincrement not null, nombres varchar, apellidos varchar, nrodocumento varchar,usuario varchar, contrasena varchar,rol varchar);");
        db.execSQL("create table pedido (_id integer primary key autoincrement not null, id_producto integer, id_cliente integer,cant_pedido integer,fechapedido date);");
        db.execSQL("insert into  producto (producto,precio,cantidad) values('PAN','10','15');");
        db.execSQL("insert into  producto (producto,precio,cantidad) values('LLAJUA','6','5');");
        db.execSQL("insert into  producto (producto,precio,cantidad) values('REFRESCO','11','6');");
        db.execSQL("insert into  producto (producto,precio,cantidad) values('GALLETA','12','8');");
        db.execSQL("insert into  producto (producto,precio,cantidad) values('PAPAS FRITAS','15','2');");
        db.execSQL("insert into  producto (producto,precio,cantidad) values('JAMON','25','16');");
        db.execSQL("insert into  producto (producto,precio,cantidad) values('CHUPETE','8','10');");
        db.execSQL("insert into  producto (producto,precio,cantidad) values('ADEREZO','10','4');");



        Log.d("Todos los tablas: ", "Se crearon las tablas");
    }

    //Método utilizado cuando se actualiza la base de datos
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
    public String[] conseguircontrasena(String usuario)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String[] valor;
        try {
            Cursor cursor = database.rawQuery("SELECT contrasena,_id FROM cliente WHERE usuario='" + usuario + "'", null);
            if(cursor.getCount()<1)
            {
                if(usuario.equals("administrador"))
                        valor= new String[]{"adm_1", ""};
                else valor= new String[]{"nousuario", ""};
                cursor.close();
            }else{
                cursor.moveToFirst();
                valor= new String[]{cursor.getString(cursor.getColumnIndex("contrasena")), cursor.getString(cursor.getColumnIndex("_id"))};
                cursor.close();}
        }
        catch (SQLiteException e) {
            if (database.inTransaction())
                database.endTransaction();
            Log.e("Error", e.getMessage().toString() + "primer error");
            valor= new String[]{"nousuario", ""};
        }
        return valor;
    }
    public String conseguirtipo(String usuario)
    {
        String valor="";
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            Cursor cursor = database.rawQuery("SELECT rol FROM cliente WHERE usuario='"+usuario+"'", null);
            if(cursor.getCount()<1) // UserName Not Exist
            {
                valor="null";
                cursor.close();
            }
            cursor.moveToFirst();
            valor = cursor.getString(cursor.getColumnIndex("rol"));
            cursor.close();
        }
        catch (SQLiteException e) {
            if (database.inTransaction())
                database.endTransaction();
            Log.e("Error", e.getMessage().toString() + "error de cargo persona");
            valor="null";
        }
        return valor;
    }
    public void insert(String tabla, String[] variables,String[] valores)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues insertar = new ContentValues();
        for(int i=0;i<variables.length;i++)
        {
            insertar.put(variables[i].toString(), valores[i].toString());
        }
        database.insert(tabla,null, insertar);

    }
    public ArrayList<HashMap<String, String>> conseguirproductos() {
        ArrayList<HashMap<String, String>> Lista;
        Lista = new ArrayList<HashMap<String, String>>();
     /*   String selectQuery = "SELECT producto.producto, producto.precio,(producto.cantidad-pedido.cant_pedido) as cantidad,producto._id " +
                "FROM producto LEFT OUTER JOIN pedido ON (producto._id = pedido.id_producto) " +
                "WHERE pedido.cant_pedido < producto.cantidad ";*/
        String selectQuery1="SELECT producto.producto, producto.precio, producto.cantidad, producto._id "+
                "FROM producto LEFT OUTER JOIN pedido ON (producto._id = pedido.id_producto) " +
                "WHERE pedido.cant_pedido IS NULL";

        SQLiteDatabase database = this.getWritableDatabase();
        /*Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //columnas
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("_id", cursor.getString(3));
                map.put("producto", cursor.getString(0));
                map.put("precio", "Bs. "+cursor.getString(1)+" c/u");
                map.put("cantidad","Cantidad de unidades existentes: "+ cursor.getString(2));
                Lista.add(map);
            } while (cursor.moveToNext());
        }*/
        Cursor cursor1 = database.rawQuery(selectQuery1, null);
        if (cursor1.moveToFirst()) {
            do {
                //columnas
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("_id", cursor1.getString(3));
                map.put("producto", cursor1.getString(0));
                map.put("precio", "Bs. "+cursor1.getString(1)+" c/u");
                map.put("cantidad", "Cantidad existente: "+cursor1.getString(2));
                Lista.add(map);
            } while (cursor1.moveToNext());
        }
        return Lista;
    }

    public String[] conseguirpedido(String id)
    {

        String[] valor = new String[0];
        try {
            String selectQuery = "SELECT producto.producto, producto.precio,(producto.cantidad-pedido.cant_pedido) as cantidad,producto._id,pedido.cant_pedido " +
                    "FROM producto LEFT OUTER JOIN pedido ON (producto._id = pedido.id_producto) " +
                    "WHERE pedido.cant_pedido < producto.cantidad and producto._id='"+id+"' ";
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);
            if(cursor.moveToFirst()){
                valor= new String[]{cursor.getString(cursor.getColumnIndex("producto")),"Costo Bs. "+ cursor.getString(cursor.getColumnIndex("precio"))+" c/u","Cantidad existente: "+cursor.getString(cursor.getColumnIndex("cantidad")), cursor.getString(cursor.getColumnIndex("_id")),"Cantidad pedida: "+cursor.getString(cursor.getColumnIndex("cant_pedido"))};
                cursor.close();}
        }

        catch (SQLiteException e) {
            Log.e("Error", e.getMessage().toString() + "primer error");
            valor= new String[]{"nousuario", ""};
        }
        return valor;
    }
}