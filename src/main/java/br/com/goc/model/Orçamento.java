package br.com.goc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orçamento")
public class Orçamento {
    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrçamentoID")
    private Long id;


}
