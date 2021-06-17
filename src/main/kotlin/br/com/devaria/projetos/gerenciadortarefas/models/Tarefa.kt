package br.com.devaria.projetos.gerenciadortarefas.models

import com.fasterxml.jackson.annotation.JsonBackReference
import java.time.LocalDate
import javax.persistence.*

@Entity
data class Tarefa (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var nome: String = "",
    var dataPrevistaConclusao : LocalDate = LocalDate.MIN,
    var dataConclusao : LocalDate? = null,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario")
    val usuario: Usuario? = null
)