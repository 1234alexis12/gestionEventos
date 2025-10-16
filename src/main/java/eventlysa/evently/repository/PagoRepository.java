package eventlysa.evently.repository;

import eventlysa.evently.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    // Métodos personalizados según necesidad
}
