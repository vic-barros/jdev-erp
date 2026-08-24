package br.com.jdeverp.pro.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.jdeverp.pro.exception.MsgApiException;
import br.com.jdeverp.pro.model.Usuario;
import br.com.jdeverp.pro.service.JwtService;
import br.com.jdeverp.pro.service.UsuarioDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UsuarioDetailsService userService;

	/*
	 * HttpServeltRequest = vem todos os dados da requisição (dados da tela,
	 * integração ou API
	 */
	/* HttpServeltResponse = é a resposta que será dada ao usuário */
	/* FilterChain = é a classe do filtro do Spring */
	/* Tem que ser feito o tratamento de exceção */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			// Cabeçalho da requisição que vem do usuário, que é o token JWT
			String header = request.getHeader("Authorization");

			// Se não tem token o acesso pode ser público e será validado pelo Spring
			// Security, caso tenha token JWT, será validado pelo filtro JwtFilter
			if (header == null || !header.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}

			// Vai pegar a partir do 7º caractere, que é onde começa o token JWT, pois os 7
			// primeiros caracteres são "Bearer "
			String token = header.substring(7);

			if (token.isBlank()) {
				filterChain.doFilter(request, response);
				return;
			}

			// Token pode estar inválido, mas o user pode acessar a parte de login, por isso
			// tem que deixar continuar o processo para o Spring Security validar por baixo
			// dos panos
			if (!jwtService.validarToken(token)) {
				filterChain.doFilter(request, response);
				return;
			}

			// Se o token for válido, vai setar o contexto de segurança do Spring Security
			// com os dados do usuário
			// Vai extrair o login do usuário do token JWT e setar no contexto de segurança
			// do Spring Security

			String login = jwtService.extrairLogin(token);

			// Se tem login e ele não está autenticado vamos fazer a autenticação
			if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				// Busca usuário no banco
				UserDetails userDetails = userService.loadUserByUsername(login);

				// Cria o objeto para carregar o user
				UsuarioAutenticado principal = new UsuarioAutenticado((Usuario) userDetails);

				// Cria Objeto da autenticação
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal,
						token, principal.getAuthorities());

				// Adiciona essa autentição para a requisição
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Estabelece a autenticação do user para o nosso Spring Security
				SecurityContextHolder.getContext().setAuthentication(authentication);

			}

		} catch (Exception e) {
			e.printStackTrace();
			// Vai imprimir no console

			// limpa qualquer coisa que tenha sido setada no contexto de segurança
			// limpa qualquer dado de segurança que venha na requisição do usuário
			SecurityContextHolder.clearContext();

			throw new MsgApiException("Erro ao validar JWT do Usuário no sistema ");
		}

		// Passa a requisição para o próximo filtro da cadeia de filtros do Spring
		// Continua para o backend, caso não tenha token JWT, o Spring Security vai
		// validar se é um acesso público ou não
		filterChain.doFilter(request, response);

	}

}
