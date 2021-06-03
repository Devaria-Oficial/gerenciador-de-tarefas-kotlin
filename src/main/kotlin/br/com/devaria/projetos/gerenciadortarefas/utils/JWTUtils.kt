package br.com.devaria.projetos.gerenciadortarefas.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component

@Component
class JWTUtils {

    private val chaveSeguranca = "MinhaChaveDeSegurancaSuperSecretaKotlinNaoCompartilhar"

    fun gerarToken(idUsuario : String) : String{
        return Jwts.builder()
            .setSubject(idUsuario)
            .signWith(SignatureAlgorithm.HS512, chaveSeguranca.toByteArray())
            .compact()
    }

    fun isTokenValido(token: String) : Boolean {
        val claims = getClaimsToken(token)

        if(claims != null){
            val idUsuario = claims.subject
            if(!idUsuario.isNullOrEmpty() && !idUsuario.isNullOrBlank()){
                return true
            }
        }

        return false
    }

    private fun getClaimsToken(token: String) = try{
        Jwts.parser().setSigningKey(chaveSeguranca.toByteArray()).parseClaimsJws(token).body
    }catch (exception : Exception){
        null
    }

    fun getUsuarioId(token : String) : String?{
        val claims = getClaimsToken(token)
        return claims?.subject
    }
}