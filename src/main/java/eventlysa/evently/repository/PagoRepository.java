package eventlysa.evently.repository;

import eventlysa.evently.entity.EstadoPago; // Asegúrate de importar esto
import eventlysa.evently.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importa Query
import org.springframework.data.repository.query.Param; // Importa Param
import java.math.BigDecimal; // Importa BigDecimal
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByReferenciaTransaccion(String referencia);

    // --- AÑADE ESTAS LÍNEAS ---
    /**
     * Suma el 'monto' de todos los pagos que coincidan con un estado.
     * Usado para calcular los ingresos totales (solo pagos COMPLETADOS).
     *
     * @param estado El EstadoPago (COMPLETADO, PENDIENTE, FALLIDO)
     * @return La suma total como BigDecimal.
     */
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.estadoPago = :estado")
    BigDecimal sumMontoByEstadoPago(@Param("estado") EstadoPago estado);
}