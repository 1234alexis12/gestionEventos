package eventlysa.evently.controller;

import eventlysa.evently.entity.Aforo; // <-- Importa Aforo
import eventlysa.evently.entity.Evento;
import eventlysa.evently.repository.AforoRepository; // <-- Importa AforoRepository
import eventlysa.evently.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // --- INYECTA EL REPOSITORIO DE AFORO ---
    @Autowired
    private AforoRepository aforoRepository;
    // --- FIN DE LA INYECCIÓN ---

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("eventos", eventoService.findAll());
        return "eventos";
    }

    // FORMULARIO NUEVO
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("evento", new Evento());
        // TODO: Añadir campo para capacidad en evento_form.html si quieres que sea configurable
        return "evento_form";
    }

    // GUARDAR (nuevo o edición) - MODIFICADO
    @PostMapping
    public String guardar(@ModelAttribute Evento evento) {
        boolean isNew = evento.getId() == null; // Verifica si es nuevo ANTES de guardar

        if (isNew) {
            evento.setCreadoEn(LocalDateTime.now());
        } else {
            evento.setActualizadoEn(LocalDateTime.now());
            // Opcional: Si editas, podrías querer mantener el 'creadoEn' original
            // Evento existingEvento = eventoService.findById(evento.getId()).orElse(null);
            // if (existingEvento != null) evento.setCreadoEn(existingEvento.getCreadoEn());
        }

        // Guarda el evento (obtiene un ID si es nuevo)
        Evento eventoGuardado = eventoService.save(evento);

        // --- LÓGICA PARA CREAR AFORO SI ES NUEVO ---
        if (isNew) {
            Aforo nuevoAforo = new Aforo();
            nuevoAforo.setEvento(eventoGuardado); // Asocia el evento recién guardado
            
            // TODO: Obtener capacidad del formulario o usar un valor por defecto
            nuevoAforo.setCapacidadMaxima(100); // Capacidad por defecto de 100
            
            nuevoAforo.setAsistenciaActual(0); // Siempre empieza en 0
            
            aforoRepository.save(nuevoAforo); // Guarda el nuevo registro de aforo
        }
        // --- FIN LÓGICA AFORO ---

        return "redirect:/eventos";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Evento evento = eventoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado: " + id));
        model.addAttribute("evento", evento);
        // TODO: Si quieres editar la capacidad, necesitarías buscar el Aforo y añadirlo al modelo también
        return "evento_form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        // Al eliminar el Evento, el Aforo se borrará en cascada
        // gracias a `ON DELETE CASCADE` en tu SQL y `orphanRemoval=true` en la entidad Evento si lo tuvieras.
        // Si no, tendrías que borrar el Aforo manualmente aquí antes.
        eventoService.deleteById(id);
        return "redirect:/eventos";
    }
}