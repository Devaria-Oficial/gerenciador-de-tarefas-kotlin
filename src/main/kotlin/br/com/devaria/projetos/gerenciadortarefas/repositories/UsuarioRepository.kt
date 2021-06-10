package br.com.devaria.projetos.gerenciadortarefas.repositories

import br.com.devaria.projetos.gerenciadortarefas.models.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository : JpaRepository<Usuario, Long> {
    fun findByEmail(email: String) : Usuario?
}