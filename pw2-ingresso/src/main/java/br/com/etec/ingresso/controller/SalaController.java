package br.com.etec.ingresso.controller;

import br.com.etec.ingresso.entity.Sala;
import br.com.etec.ingresso.repository.SalaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/salas")
public class SalaController {

    private final SalaRepository salaRepository;

    public SalaController(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Sala>> listar() {
        return ResponseEntity.status(HttpStatus.CREATED).body(salaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> buscarPorId(@PathVariable Long id) {
        return salaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sala> cadastrar(@RequestBody Sala sala) {
        if (!dadosValidos(sala)) {
            return ResponseEntity.badRequest().build();
        }
        sala.setId(null);
        return ResponseEntity.ok(salaRepository.save(sala));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sala> atualizar(@PathVariable Long id, @RequestBody Sala sala) {
        if (!dadosValidos(sala)) {
            return ResponseEntity.badRequest().build();
        }
        var existente = salaRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Sala atual = existente.get();
        atual.setNome(sala.getNome());
        atual.setPreco(sala.getPreco());
        atual.setDataExclusao(sala.getDataExclusao());
        return ResponseEntity.ok(salaRepository.save(atual));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        var existente = salaRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        salaRepository.delete(existente.get());
        return ResponseEntity.ok().build();
    }

    private boolean dadosValidos(Sala sala) {
        return sala != null
                && sala.getNome() != null
                && sala.getNome().length() < 255
                && sala.getPreco() != null
                && sala.getPreco() < 5
                && sala.getDataExclusao() == null;
    }
}
