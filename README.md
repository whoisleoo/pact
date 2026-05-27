## Estrutura do repositório

```
pact/
└── Backend/
    ├── pom.xml
    ├── db/
    │   ├── schema.sql
    │   └── seed.sql
    └── src/main/java/
        ├── model/
        │   ├── domain/
        │   └── repositories/
        ├── dao/
        ├── service/
        ├── controller/
        ├── exception/
        ├── thread/
        └── util/
```

### To-do Next - Happy path perfeito se não quebrar nada

Possiveis passos para continuar o projeto

1. Configurar banco (local): editar e executar [Backend/db/schema.sql](Backend/db/schema.sql) e, opcionalmente, [Backend/db/seed.sql](Backend/db/seed.sql).
2. DAOs (JDBC): implementar `dao/ProdutoDAO`, `dao/UsuarioDAO`, `dao/PedidoDAO`, `dao/PedidoItemDAO` com `PreparedStatement` e `try-with-resources`. Incluir `UPDATE ... WHERE estoque >= ?` para atualização condicional de estoque.
3. Services (regras): implementar `service/ProdutoService` e `service/PedidoService` com validações de negócio (checagem de estoque antes de persistir, composição de `Pedido` e `ProdutoPedido`). Testar com mocks de DAO.
4. Controller (console): montar menus no pacote `controller` que só deleguem ao `service` (sem usar `java.sql`). Implementar fluxos: cadastrar cliente, cadastrar produto, criar pedido, finalizar pedido.
5. Thread de processamento: implementar `thread/ProcessadorPedidos` que busca pedidos com status `FILA`, marca como `PROCESSAMENTO` de forma atômica e finaliza após simulação (`Thread.sleep`). Abrir/fechar conexão por ciclo.
6. Relatórios SQL: criar ao menos dois relatórios gerenciais (ex.: vendas por produto, total por cliente) usando consultas agregadas/agrupamentos no DAO e endpoints no controller para exibir.
7. Testes e documentação: adicionar testes unitários para `service` e `dao` (usar base de dados de teste/local), gerar Diagrama de Classes e Documento de Requisitos.
8. Entrega: preparar `README.md` final com instruções de compilação, configuração do MySQL e execução do projeto.


### Backend

| Pacote | Função                                                                                                                                                                                                                               |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `model.domain` | Value Objects e enums do domínio (`Email`, `Senha`, `Nome`, `Descricao`, `Preco`, `Quantidade`, `Autenticacao`, `DetalhesProduto`, `Categoria`, `StatusPedido`). Classes imutáveis sem setters — objetos nascem válidos pelo construtor.                                                   |
| `model.repositories` | Entidades persistidas (`Usuario`, `Produto`, `Pedido`, `ProdutoPedido`) — `record`s imutáveis que compõem os VOs do `model.domain`. |
| `dao` | Acesso ao banco de dados via JDBC. Cada classe é responsável por um único tipo de entidade (`UsuarioDAO`, `ProdutoDAO`, `PedidoDAO`). Usa exclusivamente `PreparedStatement` e `try-with-resources`. Nenhuma lógica de negócio aqui. |
| `service` | Regras de negócio da aplicação. Orquestra os DAOs, valida dados e coordena a transação de criação de pedidos com verificação de estoque.                                                                                             |
| `controller` | Camada de menus do console. Delega ao `service` correspondente. Não contém lógica de negócio nem acesso direto ao banco (proibido importar `java.sql`).                                                                                                  |
| `exception` | Exceções customizadas do domínio (`EstoqueInsuficienteException`, `ClienteNaoEncontradoException`, `ProdutoNaoEncontradoException`, `EmailInvalidoException`).                                                                       |
| `thread` | `ProcessadorPedidos` — thread de background que busca pedidos com status `FILA`, processa e atualiza para `ENTREGUE`. Gerencia sua própria conexão JDBC por ciclo, isolada da conexão principal.                                   |
| `util` | Classes utilitárias sem vínculo com o domínio. Contém a `ConnectionFactory`, responsável por fornecer a conexão com o banco a partir das variáveis de ambiente do `.env`.                           |

### Backend/db

| Arquivo | Função |
|---|---|
| `schema.sql` | DDL completo — criação do banco, tabelas, relacionamentos e constraints. Execute este arquivo antes de rodar o projeto. |
| `seed.sql` | Dados de teste para popular o banco em ambiente de desenvolvimento. Opcional. |

### Backend/model/domain

| Arquivo | Função |
|---|---|
| `Autenticacao.java` | Agregado de autenticação (`Email` + `Senha`). Expõe `getEmail()` e `autenticar(Senha tentativa)` — a senha nunca é exposta diretamente. |
| `Categoria.java` | Enum com categorias de produto (atualmente: `DEV`, `MARKETING`, `VIDEOS`, `DESIGN`). |
| `Descricao.java` | VO que valida descrição não nula e não vazia. |
| `DetalhesProduto.java` | `record` agregando `Nome`, `Descricao` e `Preco` (informações imutáveis do produto). |
| `Email.java` | VO que valida email no construtor via regex RFC-aproximada (verifica formato, comprimento e domínio). |
| `Nome.java` | VO que valida não nulo/não vazio. |
| `Preco.java` | VO sobre `BigDecimal` que exige valor estritamente positivo (`> 0`). |
| `Quantidade.java` | VO que valida quantidade não-negativa. |
| `Senha.java` | VO que valida senha (mínimo 8 caracteres, ao menos uma letra e um dígito). Implementa `equals`/`hashCode` para autenticação por valor. `toString()` retorna `"***"` para evitar vazamento em logs. |
| `StatusPedido.java` | Enum de estados do pedido: `ABERTO`, `FILA`, `PROCESSAMENTO`, `ENTREGUE`. |

### Backend/model/repositories (entidades persistidas)

| Arquivo | Função |
|---|---|
| `Usuario.java` | `record` representando usuário persistido (`Long id`, `Nome`, `Autenticacao`, `LocalDateTime createdAt`). Construtor auxiliar define `createdAt` para `now()` quando criado em memória. |
| `Produto.java` | `record` com `id`, `DetalhesProduto`, `isAtivo`, `categoria`, `idVendedor`, `createdAt` e `quantidadeEstoque`. Defaults: `isAtivo = true`, `createdAt = now()`. |
| `ProdutoPedido.java` | `record` representando item de pedido (`idProduto`, `idPedido`, `idProdutoPedido`, `precoUnitario`, `quantidade`). |
| `Pedido.java` | `record` com `id`, `idCliente`, `idVendedor`, `List<ProdutoPedido> itens` e `StatusPedido status`. Construtor compacto: status default `ABERTO`, garante que `itens` não seja nulo nem vazio, e usa `List.copyOf` para imutabilidade defensiva. |

Observações gerais

- Os VOs seguem a ideia de objetos imutáveis nascendo válidos no construtor (alinhado ao enunciado e a Object Calisthenics).
- `Senha` nunca expõe o valor cru — autenticação é feita por comparação de VO via `equals`.
- Falta integração com DAOs/`dao/` que façam as operações JDBC com `try-with-resources` e `PreparedStatement`.
- Para operações de pedido, a aplicação deve garantir atualizações condicionais no banco para evitar estoque negativo (por exemplo `UPDATE produto SET estoque = estoque - ? WHERE id = ? AND estoque >= ?`).

Use este arquivo como referência rápida ao implementar DAOs e serviços que consumirão esses modelos.

### Build (Maven)

O projeto usa Maven, note-se que ele VAI ter que ser alterado com o passar do tempo pra funfar com o CLI. O [Backend/pom.xml](Backend/pom.xml) define:

- Java 21 como source/target
- Encoding UTF-8
- Dependência do driver MySQL JDBC (`mysql-connector-j`)

Compilar:
```bash
cd Backend
mvn compile
```

Rodar (após existir uma classe `Main`):
```bash
mvn exec:java -Dexec.mainClass="Main"
```
