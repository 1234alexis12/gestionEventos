package eventlysa.evently.repository;

import eventlysa.evently.entity.Aforo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AforoRepository extends JpaRepository<Aforo, Long> {
    // Métodos personalizados según necesidad
}
