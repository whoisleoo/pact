## Estrutura do repositório

```
pact/
├── Backend/
│   ├── db/
│   │   ├── schema.sql
│   │   └── seed.sql
│   └── src/main/java/
│       ├── model/
│       ├── dao/
│       ├── service/
│       ├── controller/
│       ├── exception/
│       ├── thread/
│       └── util/
└── Front/
    ├── pages/
    ├── scripts/
    └── styles/
```

### To-do Next - Happy path perfeito se não quebrar nada

Possiveis passos para continuar o projeto

1. Configurar banco (local): editar e executar [Backend/db/schema.sql](Backend/db/schema.sql) e, opcionalmente, [Backend/db/seed.sql](Backend/db/seed.sql).
2. DAOs (JDBC): implementar `dao/ProdutoDAO`, `dao/UsuarioDAO`, `dao/PedidoDAO`, `dao/PedidoItemDAO` com `PreparedStatement` e `try-with-resources`. Incluir `UPDATE ... WHERE estoque >= ?` para atualização condicional de estoque.
3. Services (regras): implementar `service/ProdutoService` e `service/PedidoService` com validações de negócio (checagem de estoque antes de persistir, composição de `Pedido` e `PedidoItem`). Testar com mocks de DAO.
4. Controller (console): montar menus no pacote `controller` que só deleguem ao `service` (sem usar `java.sql`). Implementar fluxos: cadastrar cliente, cadastrar produto, criar pedido, finalizar pedido.
5. Thread de processamento: implementar `thread/ProcessadorPedidos` que busca pedidos com status `FILA`, marca como `PROCESSANDO` de forma atômica e finaliza após simulação (`Thread.sleep`). Abrir/fechar conexão por ciclo.
6. Relatórios SQL: criar ao menos dois relatórios gerenciais (ex.: vendas por produto, total por cliente) usando consultas agregadas/agrupamentos no DAO e endpoints no controller para exibir.
7. Testes e documentação: adicionar testes unitários para `service` e `dao` (usar base de dados de teste/local), gerar Diagrama de Classes e Documento de Requisitos.
8. Entrega: preparar `README.md` final com instruções de compilação, configuração do MySQL e execução do projeto.


### Backend

| Pacote | Função                                                                                                                                                                                                                               |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `model` | Entidades do domínio (`Cliente`, `Produto`, `Pedido`, `ItemPedido`) e enums (`Categoria`, `StatusPedido`). Classes imutáveis sem setters — objetos nascem válidos pelo construtor.                                                   |
| `dao` | Acesso ao banco de dados via JDBC. Cada classe é responsável por um único tipo de entidade (`ClienteDAO`, `ProdutoDAO`, `PedidoDAO`). Usa exclusivamente `PreparedStatement` e `try-with-resources`. Nenhuma lógica de negócio aqui. |
| `service` | Regras de negócio da aplicação. Orquestra os DAOs, valida dados e coordena a transação de criação de pedidos com verificação de estoque.                                                                                             |
| `controller` | Recebe as requisições HTTP do frontend e delega ao service correspondente. Não contém lógica de negócio nem acesso direto ao banco.                                                                                                  |
| `exception` | Exceções customizadas do domínio (`EstoqueInsuficienteException`, `ClienteNaoEncontradoException`, `ProdutoNaoEncontradoException`, `EmailInvalidoException`).                                                                       |
| `thread` | `ProcessadorPedidos` — thread de background que busca pedidos com status `FILA`, processa e atualiza para `FINALIZADO`. Gerencia sua própria conexão JDBC por ciclo, isolada da conexão principal.                                   |
| `util` | Classes utilitárias sem vínculo com o domínio. Contém a `ConnectionFactory`, responsável por fornecer a conexão com o banco a partir das variáveis de ambiente do `.env` (teoricamente vai ser fornecida).                           |

### Backend/db

| Arquivo | Função |
|---|---|
| `schema.sql` | DDL completo — criação do banco, tabelas, relacionamentos e constraints. Execute este arquivo antes de rodar o projeto. |
| `seed.sql` | Dados de teste para popular o banco em ambiente de desenvolvimento. Opcional. |

### Backend/Model

| Arquivo | Função |
|---|---|
| `Autenticacao.java` | Combina `Email` e `Senha` como um agregado de autenticação para o usuário. |
| `Categoria.java` | Enum com categorias de produto (atualmente: DEV, MARKETING, VIDEOS, DESIGN). Recomenda-se alinhar com requisitos caso necessário. |
| `Descricao.java` | VO que valida descrição não nula e não vazia. |
| `DetalhesProduto.java` | `record` agregando `Nome`, `Descricao` e `Preco` (informações de produto imutáveis). |
| `Email.java` | VO que valida email no construtor; atualmente verifica `null`/`blank` e um formato simples (contém `@` e `.`). Recomenda-se normalizar com `trim()` e usar regex mais robusta. |
| `Senha.java` | VO que valida não nulo/não vazio; atualmente armazena a senha em texto (rever armazenamento/hashing para produção). |
| `Nome.java` | VO que valida não nulo/não vazio. |
| `Preco.java` | VO que armazena `BigDecimal` e valida não-negatividade (rever para exigir `> 0` se o requisito pedir preço positivo). |
| `Quantidade.java` | VO que valida quantidade não-negativa. |
| `FilaPedido.java` | Enum de estados do pedido (atualmente: FILA, PROCESSAMENTO, ENTREGUE). Recomenda-se alinhar com o enunciado (`ABERTO, FILA, PROCESSANDO, FINALIZADO`). |

**Records (persistência / DTOs)**

| Arquivo | Função |
|---|---|
| `Usuario.java` | `record` representando usuário persistido; possui construtor auxiliar que define `createdAt`. |
| `Produto.java` | `record` representando produto persistido; contém `DetalhesProduto`, `isAtivo`, `categoria`, `idVendedor` e `createdAt`. Observação: falta campo de `estoque` para validação de pedidos. |
| `ProdutoPedido.java` | Representa o item de pedido (preço unitário e quantidade). Recomenda-se renomear para `PedidoItem` e padronizar campos (`id`, `pedidoId`, `produtoId`, `precoUnitario`, `quantidade`). |
| `Pedido.java` | `record` com `id`, `status` (enum), `idCliente`, `idVendedor`. Recomenda-se adicionar `createdAt`/`updatedAt` para auditoria. |

Observações gerais

- Os VOs seguem a ideia de objetos imutáveis nascendo válidos no construtor (boa prática alinhada ao enunciado).
- Falta integração com DAOs/`dao/` que façam as operações JDBC com `try-with-resources` e `PreparedStatement`.
- Para operações de pedido, a aplicação deve garantir atualizações condicionais no banco para evitar estoque negativo (por exemplo `UPDATE produto SET estoque = estoque - ? WHERE id = ? AND estoque >= ?`).

Use este arquivo como referência rápida ao implementar DAOs e serviços que consumirão esses modelos.


### Frontend

| Pasta | Função |
|---|---|
| `pages` | Páginas HTML da aplicação. |
| `scripts` | Arquivos JavaScript. `main.js` contém a lógica de chamadas à API. `tailwind-config.js` configuração do Tailwind CSS. |
| `styles` | Folhas de estilo CSS globais da aplicação. |
 
---