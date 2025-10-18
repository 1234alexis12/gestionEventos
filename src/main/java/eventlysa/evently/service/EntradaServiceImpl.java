package eventlysa.evently.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger log = LoggerFactory.getLogger(EntradaServiceImpl.class);
    
    @Override
    @Transactional
    public List<Entrada> comprarEntradas(Long eventoId, Long usuarioId, int cantidad) {
        // --- LOG INICIAL ---
        log.info("Iniciando compra: eventoId={}, usuarioId={}, cantidad={}", eventoId, usuarioId, cantidad);

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + eventoId));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
        Aforo aforo = aforoRepository.findByEventoId(eventoId)
                .orElseThrow(() -> new RuntimeException("Aforo no configurado para el evento ID: " + eventoId));

        log.info("Evento encontrado: '{}'. Aforo máx: {}", evento.getNombre(), aforo.getCapacidadMaxima());

        long entradasYaVendidas = entradaRepository.countByEventoIdAndEstado(eventoId, EstadoEntrada.VENDIDA);
        log.info("Entradas ya vendidas para este evento: {}", entradasYaVendidas);

        if (aforo.getCapacidadMaxima() < (entradasYaVendidas + cantidad)) {
            log.warn("Intento de compra fallido por falta de aforo para eventoId={}", eventoId); // --- LOG ERROR AFORO ---
            throw new RuntimeException("No hay suficiente aforo disponible para " + cantidad + " entradas.");
        }

        List<Entrada> entradasCreadas = new ArrayList<>();
        BigDecimal precioUnitario = new BigDecimal("75.00"); // Precio fijo de ejemplo

        // --- LOG ANTES DEL BUCLE ---
        log.info("Procediendo a crear {} entrada(s) para el evento '{}'", cantidad, evento.getNombre());

        for (int i = 0; i < cantidad; i++) {
            // --- LOG DENTRO DEL BUCLE ---
            log.info("Creando entrada #{} para eventoId={}", (i + 1), eventoId);

            Entrada nuevaEntrada = new Entrada();
            nuevaEntrada.setEvento(evento);
            nuevaEntrada.setUsuario(usuario);
            // ... (resto de la configuración de nuevaEntrada)
            nuevaEntrada.setPrecio(precioUnitario);
            nuevaEntrada.setFechaCompra(LocalDateTime.now());
            nuevaEntrada.setEstado(EstadoEntrada.VENDIDA);

            String codigoQrTexto = qrCodeGenerator.generateUniqueQRCodeText(eventoId, usuarioId);
            nuevaEntrada.setCodigoQr(codigoQrTexto);
            log.debug("Generado codigoQr: {}", codigoQrTexto); // Debug level para no llenar tanto

            Entrada entradaGuardada = entradaRepository.save(nuevaEntrada);

            Pago nuevoPago = new Pago();
            nuevoPago.setEntrada(entradaGuardada);
            nuevoPago.setUsuario(usuario);
            // ... (resto de la configuración de nuevoPago)
            nuevoPago.setMonto(precioUnitario);
            nuevoPago.setFechaPago(LocalDateTime.now());
            nuevoPago.setMetodoPago(MetodoPago.STRIPE);
            nuevoPago.setEstadoPago(EstadoPago.COMPLETADO);
            nuevoPago.setReferenciaTransaccion("SIMULATED_TXN_" + System.currentTimeMillis());

            pagoRepository.save(nuevoPago);

            entradasCreadas.add(entradaGuardada);
            log.info("Entrada #{} y Pago creados con éxito para eventoId={}", (i + 1), eventoId);
        }

        // --- LOG FINAL ---
        log.info("Compra finalizada con éxito para eventoId={}. {} entradas creadas.", eventoId, entradasCreadas.size());
        return entradasCreadas;
    }
}
