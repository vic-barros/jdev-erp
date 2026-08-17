package br.com.jdeverp.pro.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.exception.MsgApiException;
import br.com.jdeverp.pro.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	// chave secreta para aumentar a segurança do nosso token
	// Para não deixar explícita
	// Token carrega as informações de autenticação
	// Value é para pegar o valor do application.properties (valor que coloquei lá)

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private String expiration;

	// Retorna a chave secreta para assinar o token
	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.ISO_8859_1));
	}

	// Vamos pegar um token e pegar uma classe claim (uma informação que está dentro
	// do token)
	// Manipula o token com esse claims, recebe um token e faz a extração dos dados

	public Claims extrairClaims(String token) {
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	// recebe o usuario e através deste vai extrair o token
	// a senha não fica aqui por questão de segurança
	// a senha é interceptada pelo front e por trá pelo spring security
	public String gerarToken(Usuario usuario) {
		return Jwts.builder().subject(usuario.getLogin())
				.claim("usuarioId", usuario.getId())
				.claim("empresaId", usuario.getEmpresa().getId()).claim("login", usuario.getLogin())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getKey())
				.compact(); // Transforma em um Json
	}

	public Long extrairEmpresaId() {
		Claims claims = extrairClaims(getToken());
		return claims.get("empresaId", Long.class);

	}

	public Long extrairEmpresaId(String token) {
		Claims claims = extrairClaims(token);
		return claims.get("empresaId", Long.class);
	}

	public Long extrairUsuarioId() {
		Claims claims = extrairClaims(getToken());
		return claims.get("usuarioId", Long.class);
	}

	public String extrairLogin(String token) {
		return extrairClaims(token).getSubject();
	}

	// Método para Validar Token
	public boolean validarToken(String token) {
		try {

			if (token == null || token.isEmpty()) {
				return false;
			}

			Jwts.parser()
			.verifyWith(getKey())
			.build()
			.parseSignedClaims(token);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			throw new MsgApiException("Token de acesso do usuário é inválido");
		}
	}

	private String getToken() {
		return (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

		// Credencial é justamente o token
	}

}
