package eventlysa.evently.controller;

import eventlysa.evently.entity.Entrada;
import eventlysa.evently.service.EntradaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/entradas")
public class EntradaController {

    @Autowired
    private EntradaService entradaService;

    @GetMapping
    public List<Entrada> getAllEntradas() {
        return entradaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrada> getEntradaById(@PathVariable Long id) {
        Optional<Entrada> entrada = entradaService.findById(id);
        return entrada.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Entrada createEntrada(@RequestBody Entrada entrada) {
        return entradaService.save(entrada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrada> updateEntrada(@PathVariable Long id, @RequestBody Entrada entrada) {
        try {
            Entrada updatedEntrada = entradaService.update(id, entrada);
            return ResponseEntity.ok(updatedEntrada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrada(@PathVariable Long id) {
        entradaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
