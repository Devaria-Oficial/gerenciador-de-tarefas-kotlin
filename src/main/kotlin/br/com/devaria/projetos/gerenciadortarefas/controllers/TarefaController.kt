package br.com.devaria.projetos.gerenciadortarefas.controllers

import br.com.devaria.projetos.gerenciadortarefas.dtos.ErroDTO
import br.com.devaria.projetos.gerenciadortarefas.dtos.SuccessoDTO
import br.com.devaria.projetos.gerenciadortarefas.models.Tarefa
import br.com.devaria.projetos.gerenciadortarefas.repositories.TarefaRepository
import br.com.devaria.projetos.gerenciadortarefas.repositories.UsuarioRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/api/tarefa")
class TarefaController(usuarioRepository: UsuarioRepository,
        val tarefaRepository: TarefaRepository) : BaseController(usuarioRepository) {

    @GetMapping
    fun listaTarefasUsuario(
            @RequestHeader("Authorization") authorization : String,
            @RequestParam periodoDe : Optional<String>,
            @RequestParam periodoAte : Optional<String>,
            @RequestParam status : Optional<Int>) : ResponseEntity<Any>{
        try{
            val usuario = lerToken(authorization)

            var periodoDeDt = if(periodoDe.isPresent && periodoDe.get().isNotEmpty()){
                LocalDate.parse(periodoDe.get())
            }else{
                null
            }

            var periodoAteDt = if(periodoAte.isPresent && periodoAte.get().isNotEmpty()){
                LocalDate.parse(periodoAte.get())
            }else{
                null
            }

            var statusInt = if(status.isPresent){
                status.get()
            }else{
                0
            }

            val resultado = tarefaRepository.findByUsuarioWithFilter(usuario.id, periodoDeDt, periodoAteDt, statusInt)

            return ResponseEntity(resultado, HttpStatus.OK)
        }catch (exception : Exception){
            return ResponseEntity(ErroDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "não foi possível listar atividades do usuário"
            ), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping
    fun adicionarTarefa(@RequestBody req: Tarefa, @RequestHeader("Authorization") authorization : String) : ResponseEntity<Any>{
        try{
            var usuario = lerToken(authorization)
            var erros = mutableListOf<String>()
            if(req == null){
                erros.add("Tarefa não encontrada")
            }else{
                if(req.nome.isNullOrBlank() || req.nome.isNullOrEmpty() || req.nome.length < 4){
                    erros.add("Nome inválido")
                }

                if(req.dataPrevistaConclusao.isBefore(LocalDate.now())){
                    erros.add("Data de previsão não pode ser menor que hoje")
                }
            }

            if(erros.size > 0){
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(), erros = erros),
                    HttpStatus.BAD_REQUEST)
            }

            var tarefa = Tarefa(
                nome = req.nome,
                dataPrevistaConclusao = req.dataPrevistaConclusao,
                usuario = usuario)

            tarefaRepository.save(tarefa)

            return ResponseEntity(SuccessoDTO("Tarefa adicionada com sucesso"), HttpStatus.OK)
        }catch (e : Exception){
            return ResponseEntity(
                ErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Não foi possível adicionar tarefa, tente novamente.")
            , HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    fun DeletarTarefa(@PathVariable id: Long,
                      @RequestHeader("Authorization") authorization : String) : ResponseEntity<Any>{
        try{
            var usuario = lerToken(authorization)
            val tarefa = tarefaRepository.findByIdOrNull(id)

            if(tarefa == null || tarefa.usuario?.id != usuario.id){
                return ResponseEntity(ErroDTO(HttpStatus.BAD_REQUEST.value(),
                    "Tarefa informada não existe"), HttpStatus.BAD_REQUEST)
            }

            tarefaRepository.delete(tarefa)

            return ResponseEntity(SuccessoDTO("Tarefa deletada com sucesso"),
                HttpStatus.OK)
        }catch (exception : Exception){
            return ResponseEntity(ErroDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Não foi possível deletar tarefa, tente novamente"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/{id}")
    fun atualizarTarefa(
        @PathVariable id: Long,
        @RequestBody updateModel:Tarefa,
        @RequestHeader authorization: String) : ResponseEntity<Any>{

        try{
            var usuario = lerToken(authorization)
            var tarefa = tarefaRepository.findByIdOrNull(id)

            var erros = mutableListOf<String>()
            if(usuario == null || tarefa == null){
                return ResponseEntity(ErroDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "Tarefa informada não existe"), HttpStatus.BAD_REQUEST)
            }

            if(updateModel == null){
                erros.add("Favor enviar os dados que deseja atualizar")
            }else{
                if(!updateModel.nome.isNullOrBlank() && !updateModel.nome.isNullOrEmpty()
                    && updateModel.nome.length < 4){
                    erros.add("Nome inválido")
                }

                if(updateModel.dataConclusao != null && updateModel.dataConclusao == LocalDate.MIN){
                    erros.add("Data de conclusão inválida")
                }
            }

            if(erros.size > 0){
                return  ResponseEntity(ErroDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    erros = erros), HttpStatus.BAD_REQUEST)
            }

            if(!updateModel.nome.isNullOrEmpty() && !updateModel.nome.isNullOrBlank()){
                tarefa.nome = updateModel.nome
            }

            if(!updateModel.dataPrevistaConclusao.isBefore(LocalDate.now())){
                tarefa.dataPrevistaConclusao = updateModel.dataPrevistaConclusao
            }

            if(updateModel.dataConclusao != null && updateModel.dataConclusao != LocalDate.MIN){
                tarefa.dataConclusao = updateModel.dataConclusao
            }

            tarefaRepository.save(tarefa)

            return ResponseEntity(SuccessoDTO("Tarefa atualizada com sucesso"),
                HttpStatus.OK)
        }catch (exception : Exception){
            return ResponseEntity(ErroDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Não foi possível atualizar tarefa, tente novamente"
            ), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}