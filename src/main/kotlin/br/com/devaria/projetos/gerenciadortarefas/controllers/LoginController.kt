package br.com.devaria.projetos.gerenciadortarefas.controllers

import br.com.devaria.projetos.gerenciadortarefas.dtos.ErroDTO
import br.com.devaria.projetos.gerenciadortarefas.dtos.LoginDTO
import br.com.devaria.projetos.gerenciadortarefas.dtos.LoginRespostaDTO
import br.com.devaria.projetos.gerenciadortarefas.extensions.md5
import br.com.devaria.projetos.gerenciadortarefas.extensions.toHex
import br.com.devaria.projetos.gerenciadortarefas.repositories.UsuarioRepository
import br.com.devaria.projetos.gerenciadortarefas.utils.JWTUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.RuntimeException

@RestController
@RequestMapping("api/login")
class LoginController(val usuarioRepository: UsuarioRepository) {

    @PostMapping
    fun efetuarLogin(@RequestBody dto : LoginDTO) : ResponseEntity<Any>{
        try{
            if(dto == null || dto.login.isNullOrBlank() || dto.login.isNullOrEmpty()
                || dto.senha.isNullOrEmpty() || dto.senha.isNullOrBlank()){
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(),
                    "Parâmetros de entrada inválidos"), HttpStatus.BAD_REQUEST)
            }

            var usuario = usuarioRepository.findByEmail(dto.login)
            if(usuario == null || usuario.senha != md5(dto.senha).toHex()){
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(),
                    "Usuário ou senha inválidos"), HttpStatus.BAD_REQUEST)
            }

            val token = JWTUtils().gerarToken(usuario.id.toString())

            val usuarioTeste = LoginRespostaDTO(usuario.nome, usuario.email, token)
            return ResponseEntity(usuarioTeste, HttpStatus.OK)
        }catch (e: Exception){
            return ResponseEntity(ErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possível efetuar o login, tente novamente"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}