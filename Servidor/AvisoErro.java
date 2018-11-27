public class AvisoErro
{
    protected String erro;
    public AvisoErro(String e) throws Exception
    {
        if (e == null || e.equals(""))
            throw new Exception("Erro errado");
        this.erro = e;
    }
    public String toString()
    {
        return this.erro;
    }
    public int hashCode()
    {
        int ret = 932;
        ret = ret * 3 + erro.hashCode();
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
        AvisoErro a = (AvisoErro)obj;
        if (!this.erro.equals(a.erro))
            return false;
        return true;
    }
}