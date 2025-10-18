package eventlysa.evently.service;

import eventlysa.evently.entity.*;
import eventlysa.evently.repository.*;
import eventlysa.evently.util.QRCodeGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Importa Optional si no está

@Service
public class EntradaServiceImpl implements EntradaService {

    @Autowired
    private EntradaRepository entradaRepository;
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private AforoRepository aforoRepository;
    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @Override
    @Transactional
    public List<Entrada> comprarEntradas(Long eventoId, Long usuarioId, int cantidad) {

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + eventoId));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
        
        // --- USA EL MÉTODO CORRECTO AQUÍ ---
        // Usamos el método que acabamos de añadir al repositorio
        Aforo aforo = aforoRepository.findByEventoId(eventoId)
                .orElseThrow(() -> new RuntimeException("Aforo no configurado para el evento ID: " + eventoId));
        // --- FIN DE LA CORRECCIÓN ---

        long entradasYaVendidas = entradaRepository.countByEventoIdAndEstado(eventoId, EstadoEntrada.VENDIDA);

        if (aforo.getCapacidadMaxima() < (entradasYaVendidas + cantidad)) {
            throw new RuntimeException("No hay suficiente aforo disponible para " + cantidad + " entradas.");
        }

        List<Entrada> entradasCreadas = new ArrayList<>();

        // --- CORRECCIÓN DEL PRECIO ---
        // Como el precio está en Entrada, no en Evento, usamos un valor fijo para la simulación.
        // TODO: Implementar una mejor lógica para obtener el precio real del evento (ej. añadir campo a Evento)
        BigDecimal precioUnitario = new BigDecimal("75.00"); // Precio fijo de ejemplo
        // --- FIN DE LA CORRECCIÓN ---


        for (int i = 0; i < cantidad; i++) {
            Entrada nuevaEntrada = new Entrada();
            nuevaEntrada.setEvento(evento);
            nuevaEntrada.setUsuario(usuario);
            nuevaEntrada.setPrecio(precioUnitario); // Usamos el precio definido arriba
            nuevaEntrada.setFechaCompra(LocalDateTime.now());
            nuevaEntrada.setEstado(EstadoEntrada.VENDIDA);

            String codigoQrTexto = qrCodeGenerator.generateUniqueQRCodeText(eventoId, usuarioId);
            nuevaEntrada.setCodigoQr(codigoQrTexto);

            Entrada entradaGuardada = entradaRepository.save(nuevaEntrada);

            Pago nuevoPago = new Pago();
            nuevoPago.setEntrada(entradaGuardada);
            nuevoPago.setUsuario(usuario);
            nuevoPago.setMonto(precioUnitario); // Usa el mismo precio
            nuevoPago.setFechaPago(LocalDateTime.now());
            nuevoPago.setMetodoPago(MetodoPago.STRIPE);
            nuevoPago.setEstadoPago(EstadoPago.COMPLETADO);
            nuevoPago.setReferenciaTransaccion("SIMULATED_TXN_" + System.currentTimeMillis());

            pagoRepository.save(nuevoPago);

            entradasCreadas.add(entradaGuardada);
        }

        return entradasCreadas;
    }
}
