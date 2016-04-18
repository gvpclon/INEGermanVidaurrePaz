package bo.gob.ine.carrito;

/**
 * Created by gvidaurre on 23/02/2016.
 */
public class Login {
    String usuario = null;
    String idcli = null;



    public Login(String usuario, String idcli) {
        super();
        this.usuario = usuario;
        this.idcli = idcli;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIdcli() {
        return idcli;
    }

    public void setIdcli(String idcli) {
        this.idcli = idcli;
    }


}
