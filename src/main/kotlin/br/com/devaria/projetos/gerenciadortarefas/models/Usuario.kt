package br.com.devaria.projetos.gerenciadortarefas.models

data class Usuario (
    val id: Long,
    val nome: String,
    val email: String,
    val senha: String)