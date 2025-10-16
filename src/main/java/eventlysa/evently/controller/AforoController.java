package eventlysa.evently.controller;

import eventlysa.evently.entity.Aforo;
import eventlysa.evently.service.AforoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/aforos")
public class AforoController {

    @Autowired
    private AforoService aforoService;

    @GetMapping
    public List<Aforo> getAllAforos() {
        return aforoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aforo> getAforoById(@PathVariable Long id) {
        Optional<Aforo> aforo = aforoService.findById(id);
        return aforo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Aforo createAforo(@RequestBody Aforo aforo) {
        return aforoService.save(aforo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aforo> updateAforo(@PathVariable Long id, @RequestBody Aforo aforo) {
        try {
            Aforo updatedAforo = aforoService.update(id, aforo);
            return ResponseEntity.ok(updatedAforo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAforo(@PathVariable Long id) {
        aforoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
