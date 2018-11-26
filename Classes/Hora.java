import java.util.*;
public class Hora extends Thread implements Cloneable
{
    protected String hora = "00";
    protected String minutos = "00";
    protected String segundos = "00";
    protected boolean viva = true;
    public Hora()
    {
        Calendar c = Calendar.getInstance();
        this.hora = c.get(Calendar.HOUR_OF_DAY) + "";
        this.minutos = c.get(Calendar.MINUTE) + "";
        this.segundos = c.get(Calendar.SECOND) + "";
        if (this.hora.length() < 2)
            this.hora = "0" + this.hora;
        if (this.minutos.length() < 2)
            this.minutos = "0" + this.minutos;
        if (this.segundos.length() < 2)
            this.segundos = "0" + this.segundos;
    }
    private void setHoraAtual(){
        Calendar c = Calendar.getInstance();
        this.hora = c.get(Calendar.HOUR_OF_DAY) + "";
        this.minutos = c.get(Calendar.MINUTE) + "";
        this.segundos = c.get(Calendar.SECOND) + "";
        if (this.hora.length() < 2)
            this.hora = "0" + this.hora;
        if (this.minutos.length() < 2)
            this.minutos = "0" + this.minutos;
        if (this.segundos.length() < 2)
            this.segundos = "0" + this.segundos;
    }
    public String getHora()
    {
        return this.hora;
    }
    public String getMinutos()
    {
        return this.minutos;
    }
    public String getSegundos()
    {
        return this.segundos;
    }
    public String toString()
    {
        return "[" + this.hora + ":" + this.minutos + ":" + this.segundos + "]";
    }
    public int hashCode()
    {
        int ret = 43;
        ret = ret * 3 + this.hora.hashCode();
        ret = ret * 5 + this.minutos.hashCode();
        ret = ret * 7 + this.segundos.hashCode();
        return ret;
    }
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        Hora h = (Hora)obj;
        if (!this.hora.equals(h.hora))
            return false;
        if (!this.minutos.equals(h.minutos))
            return false;
        if (!this.segundos.equals(h.segundos))
            return false;
        return true;
    }
    public Hora(Hora outra) throws Exception
    {
        if (outra == null)
            throw new Exception("Hora inválida");
        this.hora = outra.hora;
        this.minutos = outra.minutos;
        this.segundos = outra.segundos;
    }
    public Hora clone()
    {
        Hora ret = null;
        try {ret = new Hora(this);}
        catch(Exception e) {}
        return ret;
    }
    public void run()
    {
        while(this.viva)
            this.setHoraAtual();
    }
    public void morra()
    {
        this.viva = false;
    }
}