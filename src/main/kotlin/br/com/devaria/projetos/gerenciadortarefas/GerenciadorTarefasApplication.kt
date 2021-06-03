package br.com.devaria.projetos.gerenciadortarefas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class GerenciadorTarefasApplication

fun main(args: Array<String>) {
	runApplication<GerenciadorTarefasApplication>(*args)
}
