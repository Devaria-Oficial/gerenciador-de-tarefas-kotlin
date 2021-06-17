package br.com.devaria.projetos.gerenciadortarefas.controllers

import br.com.devaria.projetos.gerenciadortarefas.models.Usuario
import br.com.devaria.projetos.gerenciadortarefas.repositories.UsuarioRepository
import br.com.devaria.projetos.gerenciadortarefas.utils.JWTUtils
import org.springframework.data.repository.findByIdOrNull
import java.lang.IllegalArgumentException

open class BaseController(val usuarioRepository: UsuarioRepository) {

    fun lerToken(authorization: String) : Usuario {
        val token = authorization.substring(7)
        var userIdStr = JWTUtils().getUsuarioId(token)

        if(userIdStr == null || userIdStr.isNullOrEmpty() || userIdStr.isNullOrBlank()){
            throw IllegalArgumentException("Você não tem acesso a este recurso")
        }

        var usuario = usuarioRepository.findByIdOrNull(userIdStr.toLong())
        if(usuario == null){
            throw IllegalArgumentException("Usuário não encontrado")
        }

        return usuario
    }
}