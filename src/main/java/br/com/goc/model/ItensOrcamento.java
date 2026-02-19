package br.com.goc.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "itensorcamento")
public class ItensOrcamento implements Serializable {
    private static final long serialVersionUID = 1856425408984809859L;

    @Id
    @SequenceGenerator(name = "seq_itens_orcamento", sequenceName = "seq_itens_orcamento", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_itens_orcamento")
    @Column(name = "itemid")
    private Long itemId;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "produtoid")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "orcamentoid")
    private Orcamento orcamento;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
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
