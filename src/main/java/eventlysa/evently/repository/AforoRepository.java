package eventlysa.evently.repository;

import eventlysa.evently.entity.Aforo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Asegúrate de importar Optional

public interface AforoRepository extends JpaRepository<Aforo, Long> {

    // --- AÑADE ESTA LÍNEA ---
    /**
     * Busca un registro de Aforo basado en el ID del Evento asociado.
     * Spring Data JPA generará la consulta automáticamente por el nombre del método.
     * @param eventoId El ID del Evento.
     * @return Un Optional que contiene el Aforo si se encuentra, o vacío si no.
     */
    Optional<Aforo> findByEventoId(Long eventoId);
    // --- FIN DE LA LÍNEA A AÑADIR ---

}