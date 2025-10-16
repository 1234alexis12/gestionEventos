package eventlysa.evently.repository;

import eventlysa.evently.entity.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long> {
    // Métodos personalizados según necesidad
}
