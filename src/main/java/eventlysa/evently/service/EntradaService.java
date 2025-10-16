package eventlysa.evently.service;

import eventlysa.evently.entity.Entrada;
import java.util.List;
import java.util.Optional;

public interface EntradaService {
    List<Entrada> findAll();
    Optional<Entrada> findById(Long id);
    Entrada save(Entrada entrada);
    Entrada update(Long id, Entrada entrada);
    void deleteById(Long id);
}
