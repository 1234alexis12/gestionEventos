package eventlysa.evently.service;

import eventlysa.evently.entity.Aforo;
import java.util.List;
import java.util.Optional;

public interface AforoService {
    List<Aforo> findAll();
    Optional<Aforo> findById(Long id);
    Aforo save(Aforo aforo);
    Aforo update(Long id, Aforo aforo);
    void deleteById(Long id);
}
