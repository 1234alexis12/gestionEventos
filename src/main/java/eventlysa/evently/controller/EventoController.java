package eventlysa.evently.controller;

import eventlysa.evently.entity.Evento;
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
        return "evento_form";
    }

    // GUARDAR (nuevo o edición)
    @PostMapping
    public String guardar(@ModelAttribute Evento evento) {
        if (evento.getId() == null) {
            evento.setCreadoEn(LocalDateTime.now());
        } else {
            evento.setActualizadoEn(LocalDateTime.now());
        }
        eventoService.save(evento);
        return "redirect:/eventos";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Evento evento = eventoService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado: " + id));
        model.addAttribute("evento", evento);
        return "evento_form";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        eventoService.deleteById(id);
        return "redirect:/eventos";
    }
}