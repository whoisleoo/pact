# COMO USAR O CLI

Aos participantes do projeto, eu desenvolvi um método prático de criação de menus para o projeto.
Todos os arquivos podem ser encontrados dentro da pasta `Backend/src/main/java/cli/`.

---

## Estrutura de arquivos

```
cli/
├── App.java              Ponto de entrada da aplicação
├── core/
│   ├── Menu.java         Interface que todo menu deve implementar
│   ├── MenuRunner.java   Motor que executa os menus em loop
│   ├── Screen.java       Métodos para exibir elementos visuais
│   ├── Input.java        Métodos para ler entradas do usuário
│   └── Ansi.java         Constantes e helpers de cor/estilo ANSI
└── menus/
    ├── MainMenu.java      Menu principal (exemplo com banner ASCII)
    └── TesteMenu.java     Submenu de exemplo
```

---

## Como criar um novo menu

Todo menu é uma classe Java que implementa a interface `Menu`.

```java
package cli.menus;

import cli.core.Input;
import cli.core.Menu;
import cli.core.MenuRunner;
import cli.core.Screen;

public class MeuMenu implements Menu {
    private final MenuRunner runner;

    public MeuMenu(MenuRunner runner) {
        this.runner = runner;
    }

    @Override
    public String title() {
        return "Título do Menu";
    }

    @Override
    public String[] options() {
        return new String[]{"Opção 1", "Opção 2", "Opção 3"};
    }

    @Override
    public void execute(int escolha, Input input) {
        switch (escolha) {
            case 1 -> Screen.success("Opção 1 executada!");
            case 2 -> Screen.warning("Em desenvolvimento...");
            case 3 -> Screen.error("Algo deu errado.");
        }
    }
}
```

A opção `(0) Voltar` é gerada automaticamente, não precisa adicionar.

---

## Interface Menu — métodos obrigatórios

### `title()`
Retorna o texto exibido no cabeçalho do menu.

```java
@Override
public String title() {
    return "Gestão de Pedidos";
}
```

### `options()`
Retorna as opções do menu. O índice no array corresponde ao número da opção (começando em 1).

```java
@Override
public String[] options() {
    return new String[]{"Criar pedido", "Listar pedidos", "Cancelar pedido"};
}
```

### `execute(int escolha, Input input)`
Executado após o usuário escolher uma opção. O parâmetro `escolha` é o número digitado (1, 2, 3...).

```java
@Override
public void execute(int escolha, Input input) {
    switch (escolha) {
        case 1 -> criarPedido(input);
        case 2 -> listarPedidos();
        case 3 -> cancelarPedido(input);
    }
}
```

### `banner()` (opcional)
Retorna linhas de arte ASCII exibidas acima do cabeçalho. Retornar `null` omite o banner.

```java
@Override
public String[] banner() {
    return new String[]{
        "  ____  ",
        " |    | ",
        " |____| ",
    };
}
```

---

## Navegando entre menus

Para abrir um submenu a partir de `execute()`, use `runner.execute(new OutroMenu(runner))`.

```java
@Override
public void execute(int escolha, Input input) {
    switch (escolha) {
        case 1 -> runner.execute(new PedidosMenu(runner));
        case 2 -> runner.execute(new ClientesMenu(runner));
    }
}
```

O `MenuRunner` cuida do loop, da limpeza de tela e do retorno automático ao menu anterior quando o usuário digitar `0`.

---

## Screen — exibindo elementos na tela

A classe `Screen` fornece métodos estáticos para todos os elementos visuais.

### Mensagens de feedback

```java
Screen.success("Pedido criado com sucesso!");   // caixa verde com ✓
Screen.error("Não foi possível conectar.");      // caixa vermelha com ×
Screen.warning("Funcionalidade em breve.");      // caixa amarela com ⓘ
```

### Limpeza de tela

```java
Screen.clearScreen();  // limpa o terminal antes de renderizar o menu
```

O `MenuRunner` já chama `clearScreen()` automaticamente antes de cada menu. Use manualmente apenas se precisar limpar dentro de um `execute()`.

### Exibição direta

```java
System.out.println("Texto simples");
System.out.println(Ansi.bold("Texto em negrito"));
```

---

## Input — lendo dados do usuário

A classe `Input` é recebida como parâmetro no método `execute()`. Use seus métodos para coletar dados.

### Texto obrigatório

```java
String nome = input.readText("Nome do cliente");
```

Exibe um prompt estilizado. Rejeita entradas vazias automaticamente.

### Número inteiro

```java
int quantidade = input.readInt("Quantidade");
```

Rejeita entradas não numéricas automaticamente.

### Número decimal

```java
double preco = input.readDouble("Preço");
```

Aceita tanto ponto quanto vírgula como separador decimal.

### Escolha de opção numerada

```java
int opcao = input.readOption(max);  // usado internamente pelo MenuRunner
```

Valida que a entrada está entre 0 e `max`.

### Pausa

```java
input.pause();  // exibe "Pressione Enter para continuar..."
```

O `MenuRunner` já chama `pause()` automaticamente após cada `execute()`.

---

## Ansi — cores e estilos

A classe `Ansi` oferece constantes e métodos para estilizar texto diretamente.

### Métodos de wrapping (recomendado)

```java
System.out.println(Ansi.bold("Texto em negrito"));
System.out.println(Ansi.dim("Texto apagado"));
System.out.println(Ansi.red("Texto vermelho"));
System.out.println(Ansi.green("Texto verde"));
System.out.println(Ansi.yellow("Texto amarelo"));
System.out.println(Ansi.pastel("Texto rosa pastel"));
System.out.println(Ansi.soft("Texto branco suave"));
```

### Cor RGB customizada

```java
String cor = Ansi.rgb(100, 200, 255);
System.out.println(cor + "Texto azul claro" + Ansi.RESET);
```

Sempre feche com `Ansi.RESET` ao usar constantes diretamente.

### Constantes disponíveis

| Constante         | Efeito                         |
|-------------------|--------------------------------|
| `Ansi.RESET`      | Remove todos os estilos        |
| `Ansi.BOLD`       | Negrito                        |
| `Ansi.DIM`        | Texto apagado/suave            |
| `Ansi.BLINK`      | Piscante                       |
| `Ansi.RED`        | Vermelho (erros)               |
| `Ansi.GREEN`      | Verde (sucesso)                |
| `Ansi.YELLOW`     | Amarelo (avisos)               |
| `Ansi.RED_PASTEL` | Rosa pastel (headers)          |
| `Ansi.WHITE_PASTEL` | Branco suave               |

---

## Exemplo completo

Abaixo, um menu de cadastro de produto com coleta de dados reais:

```java
package cli.menus;

import cli.core.Input;
import cli.core.Menu;
import cli.core.MenuRunner;
import cli.core.Screen;

public class ProdutosMenu implements Menu {
    private final MenuRunner runner;

    public ProdutosMenu(MenuRunner runner) {
        this.runner = runner;
    }

    @Override
    public String title() {
        return "Produtos";
    }

    @Override
    public String[] options() {
        return new String[]{"Cadastrar produto", "Listar produtos"};
    }

    @Override
    public void execute(int escolha, Input input) {
        switch (escolha) {
            case 1 -> cadastrarProduto(input);
            case 2 -> Screen.warning("Em desenvolvimento...");
        }
    }

    private void cadastrarProduto(Input input) {
        String nome   = input.readText("Nome do produto");
        int quantidade = input.readInt("Quantidade em estoque");
        double preco  = input.readDouble("Preço unitário");

        // lógica de persistência aqui...

        Screen.success("Produto \"" + nome + "\" cadastrado com sucesso!");
    }
}
```

Para registrar esse menu no `MainMenu`, adicione um case em `execute()`:

```java
case 1 -> runner.execute(new ProdutosMenu(runner));
```

---

## Fluxo geral do sistema

```
App.java
  └── MenuRunner.execute(MainMenu)
        ├── limpa tela
        ├── exibe banner (se houver)
        ├── exibe header com title()
        ├── exibe opções de options()
        ├── lê escolha do usuário
        ├── chama execute(escolha, input)
        │     └── pode abrir submenus via runner.execute(...)
        └── pausa → repete o loop
```

A opção `0` em qualquer menu retorna ao menu anterior automaticamente. No `MainMenu`, encerra o programa.
