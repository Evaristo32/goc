package br.com.goc.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity // PESQUISA PARA QUE SERVE ESSA ANOTACAO NO JAVA?
@Table(name = "cliente") // PESQUISA PARA QUE SERVE ESSA ANOTACAO NO JAVA?
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1856425408984809859L;

    @Id // PESQUISA PARA QUE SERVE ESSA ANOTACAO NO JAVA?
    @SequenceGenerator(name = "seq_cliente", sequenceName = "seq_cliente", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cliente") // PESQUISA PARA QUE SERVE ESSA ANOTACAO NO JAVA?
    @Column(name = "clienteid")
    private Long clienteId;

    @Column(nullable = false, length = 150) // PESQUISA PARA QUE SERVE ESSA ANOTACAO NO JAVA?
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 255)
    private String endereco;


    public Cliente() {
    }


    public Cliente(String nome, String email, String endereco) {
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
    }


    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
