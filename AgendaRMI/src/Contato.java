

import java.io.Serializable;


@SuppressWarnings("serial")
public class Contato implements Serializable {
    private String nome, email;
    private String dtAniversario;
    private int id = 0;

    public String getNome() {
            return nome;
    }

    public void setNome(String nome) {
            this.nome = nome;
    }

    public String getEmail() {
            return email;
    }

    public void setEmail(String email) {
            this.email = email;
    }

    public String getDtAniversario() {
            return dtAniversario;
    }

    public void setDtAniversario(String dtAniversario) {
            this.dtAniversario = dtAniversario;
    }

    public int getId() {
            return id;
    }

    public void setId(int id) {
            this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Contato other = (Contato) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }
}
