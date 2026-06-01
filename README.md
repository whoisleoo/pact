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
### Ja feito -
> Model, DAO, Schema, Parte da ui

### To-do Next — sequência de implementação

A ordem importa: cada passo depende do anterior. Não pule etapas SENAO MORTE 

#### 1. Exceções customizadas — pacote `exception/`

Criar as classes que serão lançadas pelos services. Todas estendem `RuntimeException` (ou uma exception base do projeto):

- `EstoqueInsuficienteException` — lançada quando o pedido pede mais do que tem em estoque.
- `ClienteNaoEncontradoException` / `UsuarioNaoEncontradoException`.
- `ProdutoNaoEncontradoException`.
- `EmailInvalidoException` (opcional — o VO `Email` já lança `IllegalArgumentException`).
- `PedidoNaoEncontradoException`.

Cada uma com construtor recebendo mensagem e (opcionalmente) o ID/valor que causou o erro. Manter o pacote enxuto — só exceções, sem lógica.

#### 2. `util/ConnectionFactory`

Classe utilitária única, responsável por fornecer `Connection` JDBC.

- O enunciado diz que o professor fornece uma classe utilitária básica — usar a dele quando chegar. Até lá, escrever um stub:
  - Lê credenciais do `.env` (URL, usuário, senha) ou de constantes.
  - Método estático `Connection getConnection() throws SQLException`.
  - `Class.forName("com.mysql.cj.jdbc.Driver")` no static block (opcional em Java moderno, mas seguro).
- **Importante**: a thread de processamento abre/fecha sua própria conexão por ciclo, então a `ConnectionFactory` precisa devolver conexões novas a cada chamada (não singleton).

#### 3. Services — pacote `service/`

Camada de regras de negócio. Recebe DAOs por construtor (DI manual).

- `UsuarioService` — cadastro com validação (email único, etc.).
- `ProdutoService` — cadastro e listagem.
- `PedidoService` — o mais complexo. Fluxo de `criarPedido`:
  1. Buscar produtos de cada item.
  2. Iniciar transação (`conn.setAutoCommit(false)`).
  3. Para cada item: tentar baixar estoque com o `UPDATE` condicional.
  4. Se algum falhar → `rollback` + lança `EstoqueInsuficienteException`.
  5. Se todos passarem → inserir `pedido` (status `ABERTO`) + `produto_pedido[]` + `commit`.
- Método `finalizarPedido(idPedido)` muda status para `FILA`.

#### 4. Controllers (console) — pacote `controller/`

Camada de menus. **Proibido importar `java.sql`** — só chama `service`.

- `Main` com loop de menu principal: cadastrar cliente, cadastrar produto, criar pedido, finalizar pedido, listar tudo, relatórios, sair.
- Cada submenu coleta input via `Scanner`, constrói os VOs (`new Email(...)`, `new Senha(...)`), captura `IllegalArgumentException` e exceções customizadas, exibe mensagem amigável.
- **Importante**: o menu não pode bloquear a thread de processamento — ela roda em paralelo.

#### 5. Thread de processamento — `thread/ProcessadorPedidos` - PAIA

`Runnable` ou `Thread`. Loop infinito (com `Thread.sleep` entre ciclos):

1. Abrir conexão própria via `ConnectionFactory.getConnection()`.
2. Buscar pedidos com status `FILA` (`SELECT ... LIMIT 1`).
3. Marcar como `PROCESSAMENTO` com o UPDATE condicional atômico do passo 4.
4. Se conseguiu (linhas afetadas == 1), simular processamento (`Thread.sleep(3000)`).
5. Atualizar status para `ENTREGUE` (ou `FINALIZADO`).
6. Fechar conexão.
7. Voltar pro passo 1.

Iniciar a thread no `Main` antes do loop de menu: `new Thread(new ProcessadorPedidos()).start()`.

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

O projeto usa Maven, note-se que ele VAI ter que ser alterado com o passar do tempo pra funfar com o CLI. FAVOR PIA LEO ADICIONAR O POM DELE AQUI. O [Backend/pom.xml](Backend/pom.xml) deve (deveria) definir:

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
