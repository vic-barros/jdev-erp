--Desabilita restrições, trigger e contrains
SET session_replication_role = replica;

alter table plano drop constraint if exists plano_valor_mensal_check;


ALTER TABLE IF EXISTS public.plano
    ADD CONSTRAINT plano_valor_mensal_check 
    CHECK (
   		 (tipo_plano = 'FREE' and valor_mensal = 0)
   			 OR
    	 (tipo_plano <> 'FREE'
    		and valor_mensal >= 49::double precision 
    		AND valor_mensal <= 200::double precision)
    );
    
    
INSERT INTO public.plano(
	id, ativo, descricao, 
	limite_cliente, limite_usuario, nome,
	tipo_plano, valor_mensal)
	VALUES (1, true, 'Plano free para teste', 
	2, 4, 'Plano Free', 
	'FREE', 0);  

select nextval('seq_plano');	
	
INSERT INTO public.plano(
	id, ativo, descricao, 
	limite_cliente, limite_usuario, nome,
	tipo_plano, valor_mensal)
	VALUES (2, true, 'Plano Pro nível médio', 
	15, 30, 'Plano Pro', 
	'PRO', 50); 	
	
select nextval('seq_plano');	




INSERT INTO public.pessoa(
	id, ativo, bairro,
	cep, cidade, cnpj,
	complemento, cpf, data_cadastro,
	email, estado,
	logradouro, nome, nome_fantasia,
	observacao, pais, razao_social,
	telefone, tipo_pessoa, empresa_id, 
	insc_estadual)
	VALUES (
	     1, true, 'Jd Dias 1',
		'87025-758', 'Maringá', '26.934.453/0001-89',
		'perto do mercado Katayma', '059.486.784-85', '2026-07-15',
		'contato@jdevtreinamento.com.br', 'PR', 
		'Rua Pioneiro Antonio', 'Alex Fernando Egidio', 'Jdev Treinamento LTDA',
		'Nenhuma', 'Brasil', 'JDev Treinamento', 
		'44 9 8821-2355', 'PESSOA_JURIDICA', 1, 
		'878787-787');


select nextval('seq_pessoa');


	INSERT INTO public.empresa(
	id, bloqueio, logo_marca, 
	plano_ativo, total_cliente, 
	total_usuario,vigencia_plano, 
	pessoa_id, plano_id)
	VALUES (1, false, 'não existe', 
	        true, 10, 20, '2030-10-10', 1, 1);
			
select nextval('seq_empresa');		



--Habilita restrições, trigger e contrains
SET session_replication_role = origin;


-- Carga de dados para a tabela Categoria (30 registros)

INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Eletrodomésticos', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Informática', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Smartphones e Telefonia', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'TV e Áudio', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Móveis e Decoração', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Eletroportáteis', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Ferramentas e Jardim', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Automotivo e Acessórios', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Som Automotivo', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Cama, Mesa e Banho', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Esporte e Lazer', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Moda Masculina', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Moda Feminina', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Calçados', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Brinquedos e Jogos', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Bebê e Infantil', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Beleza e Perfumaria', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Cuidados Pessoais', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Alimentos e Bebidas', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Pet Shop', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Papelaria e Escritório', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Livros e Papelaria', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Games e Consoles', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Câmeras e Filmadoras', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Relógios e Joias', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Mala e Mochilas', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Iluminação', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Ar e Ventilação', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Segurança e Alarmes', 1);
INSERT INTO categoria (id, nome, empresa_id) VALUES (nextval('seq_categoria'), 'Instrumentos Musicais', 1);


CREATE EXTENSION IF NOT EXISTS unaccent;


INSERT INTO public.role(
	id, acesso)
	VALUES (1, 'ROLE_USER');

select nextval('seq_role');

INSERT INTO public.role(
	id, acesso)
	VALUES (2, 'ROLE_ADMIN');

select nextval('seq_role');

-- Correcoes para Teste do Usuario
ALTER TABLE cliente_funcionario
    ALTER COLUMN usuario_id DROP NOT NULL;
    
ALTER TABLE IF EXISTS usuario
	ALTER COLUMN refresh_token TYPE TEXT,
	ALTER COLUMN token_sessao TYPE TEXT;
	

-- Inserts para testes do usuario
-- Senha teste: lembre-se das suas senhas
	
	INSERT INTO public.cliente_funcionario(
	id, tipo_cliente_funcionario, empresa_id, pessoa_id, usuario_id)
	VALUES (1, 'FUNCIONARIO', 1, 1, null);

	select nextval('seq_cliente_funcionario');


	INSERT INTO public.usuario(
	id,
	bloqueio, 
	login, 
	refresh_token, 
	senha, 
	token_sessao, 
	cliente_funcionario_id, 
	empresa_id)
	VALUES (1, false, 'victoria2425laissa@gmail.com', '', '$2a$10$Ar5FDC4kS8U6qsoetREbDeGlWSoYjLAJZXNSPMsTu1XwNSFbPqGl2', '', 1, 1);

	update cliente_funcionario SET usuario_id = 1 where id =1;
	
	select nextval('seq_usuario');