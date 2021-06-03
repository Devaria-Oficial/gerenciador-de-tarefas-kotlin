package br.com.devaria.projetos.gerenciadortarefas.controllers

import br.com.devaria.projetos.gerenciadortarefas.models.Usuario
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/usuario")
class UsuarioController {

    @GetMapping
    fun obterUsuario() = Usuario(1, "Usuário Teste", "admin@admin.com", "")
}