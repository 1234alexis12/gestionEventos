package eventlysa.evently.controller;

import eventlysa.evently.entity.Aforo; // Importa tu Entidad Aforo
import eventlysa.evently.repository.AforoRepository; // Importa tu Repositorio Aforo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controlador para gestionar las vistas relacionadas con el Aforo.
 * Usa @Controller para devolver plantillas de Thymeleaf.
 */
@Controller
@RequestMapping("/aforo") // Todas las rutas de este controlador empezarán con /aforo
public class AforoController {

    // Inyecta el repositorio para acceder a la base de datos
    @Autowired
    private AforoRepository aforoRepository;

    /**
     * Maneja las solicitudes GET a /aforo.
     * Busca todos los registros de aforo y los pasa a la plantilla "aforo.html".
     *
     * @param model El modelo de Spring para pasar datos a la vista.
     * @return El nombre de la plantilla de Thymeleaf ("aforo").
     */
    @GetMapping
    public String mostrarControlDeAforo(Model model) {
        
        // 1. Obtenemos todos los registros de aforo
        // Gracias a JPA, estos objetos 'Aforo' también contendrán 
        // la información del 'Evento' asociado.
        List<Aforo> listaAforos = aforoRepository.findAll();
        
        // 2. Agregamos la lista al modelo con el nombre "aforos"
        // Este nombre ${aforos} será usado en el HTML
        model.addAttribute("aforos", listaAforos);
        
        // 3. Devolvemos el nombre del archivo "aforo.html"
        return "aforo";
    }
}
