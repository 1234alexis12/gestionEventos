package eventlysa.evently.service;

import eventlysa.evently.entity.Evento;
import java.util.List;
import java.util.Optional;

public interface EventoService {
    List<Evento> findAll();
    Optional<Evento> findById(Long id);
    Evento save(Evento evento);
    Evento update(Long id, Evento evento);
    void deleteById(Long id);
}
