USE `di_foda`;

-- Limpar dados existentes para evitar erros de chave primária duplicada ao reexecutar o seed
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `produto_pedido`;
TRUNCATE TABLE `pedido`;
TRUNCATE TABLE `produto`;
TRUNCATE TABLE `usuario`;
SET FOREIGN_KEY_CHECKS = 1;

-- Inserir Usuários (Senhas com mínimo de 8 caracteres contendo letras e números conforme validação da classe Senha)
-- Usuário 1 (Cliente): senha "cliente123"
INSERT INTO `usuario` (`id_usuario`, `nome`, `e_mail`, `senha`, `created_at`) VALUES
(1, 'Gabriel Cliente', 'cliente@pact.com', 'cliente123', NOW());

-- Usuário 2 (Vendedor): senha "vendedor123"
INSERT INTO `usuario` (`id_usuario`, `nome`, `e_mail`, `senha`, `created_at`) VALUES
(2, 'Lucas Vendedor', 'vendedor@pact.com', 'vendedor123', NOW());

-- Inserir Produtos (Categorias: DEV, MARKETING, VIDEOS, DESIGN)
INSERT INTO `produto` (`id_produto`, `nome`, `preco`, `ativo`, `descricao`, `categoria`, `id_vendedor`, `created_at`, `estoque`) VALUES
(1, 'Curso de Java', 99.90, 1, 'Aprenda Java do basico ao avancado', 'DEV', 2, NOW(), 50),
(2, 'Mentoria de Marketing', 500.00, 1, 'Mentoria de marketing digital para SaaS', 'MARKETING', 2, NOW(), 10),
(3, 'Edicao de Video', 150.00, 1, 'Edicao profissional para canais do Youtube', 'VIDEOS', 2, NOW(), 25),
(4, 'Identidade Visual', 350.00, 1, 'Criacao de logotipo e paleta de cores', 'DESIGN', 2, NOW(), 5);

-- Inserir Pedido 1 (FINALIZADO) - Simula um pedido já concluído anteriormente
INSERT INTO `pedido` (`id_pedido`, `status`, `id_cliente`, `created_at`) VALUES
(1, 'FINALIZADO', 1, NOW() - INTERVAL 1 DAY);

-- Itens do Pedido 1
INSERT INTO `produto_pedido` (`id_produto_pedido`, `id_pedido`, `id_produto`, `quantidade`, `preco_unitario`) VALUES
(1, 1, 1, 2, 99.90),
(2, 1, 3, 1, 150.00);

-- Inserir Pedido 2 (FILA) - Pronto para ser consumido e processado pela thread em segundo plano
INSERT INTO `pedido` (`id_pedido`, `status`, `id_cliente`, `created_at`) VALUES
(2, 'FILA', 1, NOW());

-- Itens do Pedido 2
INSERT INTO `produto_pedido` (`id_produto_pedido`, `id_pedido`, `id_produto`, `quantidade`, `preco_unitario`) VALUES
(3, 2, 2, 1, 500.00);

-- Inserir Pedido 3 (ABERTO) - Pedido ativo em andamento pelo cliente
INSERT INTO `pedido` (`id_pedido`, `status`, `id_cliente`, `created_at`) VALUES
(3, 'ABERTO', 1, NOW());

-- Itens do Pedido 3
INSERT INTO `produto_pedido` (`id_produto_pedido`, `id_pedido`, `id_produto`, `quantidade`, `preco_unitario`) VALUES
(4, 3, 4, 1, 350.00);
