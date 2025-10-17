package eventlysa.evently.repository;

import eventlysa.evently.entity.Entrada;
import eventlysa.evently.entity.EstadoEntrada; // Asegúrate de que esta importación exista
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {

    Optional<Entrada> findByCodigoQr(String codigoQr);
    
    // --- LÍNEA CORREGIDA ---
    // Antes era: countByEventoAndEstado
    // Ahora es: countByEventoIdAndEstado
    /**
     * Cuenta las entradas para un ID de evento específico y un estado.
     * El nombre "EventoId" le dice a JPA que busque la propiedad "id" 
     * dentro de la entidad "evento".
     */
    long countByEventoIdAndEstado(Long eventoId, EstadoEntrada estado);
    // --- FIN DE LA CORRECCIÓN ---


    /**
     * Cuenta todas las entradas que coincidan con un estado específico.
     * (Usado para la página de Estadísticas)
     */
    long countByEstado(EstadoEntrada estado);
}