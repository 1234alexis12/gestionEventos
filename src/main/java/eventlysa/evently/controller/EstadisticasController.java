package eventlysa.evently.controller;

import eventlysa.evently.entity.EstadoEntrada;
import eventlysa.evently.entity.EstadoPago;
import eventlysa.evently.repository.EntradaRepository;
import eventlysa.evently.repository.EventoRepository;
import eventlysa.evently.repository.PagoRepository;
import eventlysa.evently.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticasController {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EntradaRepository entradaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PagoRepository pagoRepository;

    /**
     * Muestra el dashboard de estadísticas generales.
     */
    @GetMapping
    public String mostrarEstadisticas(Model model) {
        
        // 1. Total de Eventos
        long totalEventos = eventoRepository.count();

        // 2. Entradas Vendidas (usando el método que añadimos)
        long entradasVendidas = entradaRepository.countByEstado(EstadoEntrada.VENDIDA);

        // 3. Usuarios Registrados
        long usuariosRegistrados = usuarioRepository.count();

        // 4. Ingresos Totales (usando la consulta que añadimos)
        BigDecimal ingresos = pagoRepository.sumMontoByEstadoPago(EstadoPago.COMPLETADO);
        
        // Maneja el caso de que no haya ventas (la suma daría null)
        BigDecimal ingresosTotales = Optional.ofNullable(ingresos).orElse(BigDecimal.ZERO);

        // Pasamos los datos a la vista
        model.addAttribute("totalEventos", totalEventos);
        model.addAttribute("entradasVendidas", entradasVendidas);
        model.addAttribute("usuariosRegistrados", usuariosRegistrados);
        model.addAttribute("ingresosTotales", ingresosTotales);

        // Devolvemos el nombre de la plantilla
        return "estadisticas";
    }
}