package bo.gob.ine.carrito;


public class Item {
	protected int id;
	protected String titulo;
	protected String cantidad;
	protected String precio;

	//Creamos el constructor
	public Item(int id, String titulo, String cantidad, String precio) {
		this.id = id;
		this.titulo = titulo;
		this.cantidad = cantidad;
		this.precio = precio;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}
}



