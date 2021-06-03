package br.com.devaria.projetos.gerenciadortarefas.dtos

data class LoginRespostaDTO(val nome: String, val email: String, val token : String = "")