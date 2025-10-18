package eventlysa.evently.service;

import eventlysa.evently.entity.Entrada;
import java.util.List;

public interface EntradaService {
    // Podrías necesitar otros métodos aquí después
    
    /**
     * Procesa la compra simulada de entradas para un evento por un usuario.
     * Verifica aforo, crea Entradas con QR, crea Pagos y actualiza conteos.
     * * @param eventoId ID del evento a comprar.
     * @param usuarioId ID del usuario que compra.
     * @param cantidad Número de entradas a comprar.
     * @return La lista de Entradas creadas.
     * @throws RuntimeException Si no hay suficiente aforo o ocurre otro error.
     */
    List<Entrada> comprarEntradas(Long eventoId, Long usuarioId, int cantidad);
}