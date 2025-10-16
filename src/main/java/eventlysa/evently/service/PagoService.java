package eventlysa.evently.service;

import eventlysa.evently.entity.Pago;
import java.util.List;
import java.util.Optional;

public interface PagoService {
    List<Pago> findAll();
    Optional<Pago> findById(Long id);
    Pago save(Pago pago);
    Pago update(Long id, Pago pago);
    void deleteById(Long id);
}
