package controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @GetMapping("/{id}")
    public ResponseEntity<String> getUsuario(@PathVariable String id) {
        return ResponseEntity.ok("Usuário encontrado: " + id);
    }

    @PostMapping("/terminal")
    public ResponseEntity<String> executeTerminal(@RequestBody String comando) {
        String resultado = processarTerminal(comando);
        return ResponseEntity.ok(resultado);
    }

    private String processarTerminal(String comando) {
        // Placeholder para futura integração com outra pasta/módulo de terminal
        return "Comando recebido: " + comando;
    }
}
