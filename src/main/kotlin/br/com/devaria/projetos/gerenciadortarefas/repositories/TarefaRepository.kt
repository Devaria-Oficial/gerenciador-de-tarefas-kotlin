package br.com.devaria.projetos.gerenciadortarefas.repositories

import br.com.devaria.projetos.gerenciadortarefas.models.Tarefa
import br.com.devaria.projetos.gerenciadortarefas.models.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TarefaRepository : JpaRepository<Tarefa, Long>{

    @Query("SELECT t FROM Tarefa t " +
            " WHERE t.usuario.id  = :idUsuario " +
            "   AND (:periodoDe IS NULL OR t.dataPrevistaConclusao >= :periodoDe) " +
            "   AND (:periodoAte IS NULL OR t.dataPrevistaConclusao <= :periodoAte) " +
            "   AND (:status = 0 OR (:status = 1 AND t.dataConclusao IS NULL) " +
            "           OR (:status = 2 AND t.dataConclusao IS NOT NULL)) ")
    fun findByUsuarioWithFilter(idUsuario: Long, periodoDe : LocalDate?, periodoAte : LocalDate?, status : Int) : List<Tarefa>?
}