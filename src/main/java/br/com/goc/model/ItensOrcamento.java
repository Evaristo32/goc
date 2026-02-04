package br.com.goc.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "itensOrcamento")
public class ItensOrcamento implements Serializable {
    private static final long serialVersionUID = 1856425408984809859L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "id")
    private Produto produto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public ItensOrcamento() {

    }

    public ItensOrcamento(Integer quantidade, BigDecimal preco) {
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;

    }
}