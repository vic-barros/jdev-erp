package br.com.jdeverp.pro.contexto;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import br.com.jdeverp.pro.app.JDevERPPROApplication;

@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc(addFilters = false) // Desativa filtros (ex: Spring Security)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(classes = JDevERPPROApplication.class)
//@Transactional
public class TestContextoSpring {

    @Test
    @Order(1) // <- Define a ordem de execução do teste
    public void testeInicial() {
        System.out.println("Teste Funcionando");
    }
}