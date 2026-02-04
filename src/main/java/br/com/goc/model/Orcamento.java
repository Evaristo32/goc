package br.com.goc.model;


import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orcamentos")
public class Orcamento implements Serializable {
    private static final long serialVersionUID = 1856425408984809859L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orcamentoID; //Primary Key

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "validade", nullable = false)
    private LocalDate validade;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "id")
    private Cliente cliente;

    @OneToMany
    @JoinColumn(name = "id")
    private List<ItensOrcamento> itensOrcamentos;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItensOrcamento> getItensOrcamentos() {
        return itensOrcamentos;
    }

    public void setItensOrcamentos(List<ItensOrcamento> itensOrcamentos) {
        this.itensOrcamentos = itensOrcamentos;
    }

    public Orcamento(){

    }

    public Orcamento(LocalDate data, LocalDate validade, String status, BigDecimal total) {
        //Foreign Key

        this.data = data;
        this.validade = validade;
        this.status = status;
        this.total = total;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getOrcamentoID() {
        return orcamentoID;
    }

    public void setOrcamentoID(Long orcamentoID) {
        this.orcamentoID = orcamentoID;
    }

}

