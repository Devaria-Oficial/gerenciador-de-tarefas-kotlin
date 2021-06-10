package br.com.devaria.projetos.gerenciadortarefas.controllers

import br.com.devaria.projetos.gerenciadortarefas.dtos.ErroDTO
import br.com.devaria.projetos.gerenciadortarefas.dtos.SuccessoDTO
import br.com.devaria.projetos.gerenciadortarefas.extensions.md5
import br.com.devaria.projetos.gerenciadortarefas.extensions.toHex
import br.com.devaria.projetos.gerenciadortarefas.models.Usuario
import br.com.devaria.projetos.gerenciadortarefas.repositories.UsuarioRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/usuario")
class UsuarioController(val usuarioRepository: UsuarioRepository) {

    @PostMapping
    fun criarUsuario(@RequestBody usuario: Usuario) : ResponseEntity<Any>{
        try{
            val erros = mutableListOf<String>()

            if(usuario == null){
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(), "parâmetros de entrada não enviado"),
                    HttpStatus.BAD_REQUEST)
            }

            if(usuario.nome.isNullOrEmpty() || usuario.nome.isNullOrBlank() || usuario.nome.length < 2){
                erros.add("Nome inválido")
            }

            if(usuario.email.isNullOrEmpty() || usuario.email.isNullOrBlank() || usuario.email.length < 5){
                erros.add("Email inválido")
            }

            if(usuario.senha.isNullOrEmpty() || usuario.senha.isNullOrBlank() || usuario.senha.length < 4){
                erros.add("Senha inválida")
            }

            if(usuarioRepository.findByEmail(usuario.email) != null){
                erros.add("Já existe usuário com o email informado")
            }

            if(erros.size > 0){
                return ResponseEntity(ErroDTO(status = HttpStatus.BAD_REQUEST.value(), erros = erros), HttpStatus.BAD_REQUEST)
            }

            usuario.senha = md5(usuario.senha).toHex()
            usuarioRepository.save(usuario)

            return ResponseEntity(SuccessoDTO("Usuário criado com sucesso"), HttpStatus.OK)
        }catch (excecao : Exception){
            return ResponseEntity(
                ErroDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possível cadastrar o usuário, tente novamente"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}