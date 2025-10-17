package eventlysa.evently.controller;

import eventlysa.evently.entity.Entrada; // Importa tu entidad
import eventlysa.evently.repository.EntradaRepository; // Importa tu repositorio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/entradas") // Agrupamos todas las rutas de Entradas
public class EntradaController {

    @Autowired
    private EntradaRepository entradaRepository;
    
    // (Aquí pueden ir tus otros métodos, como el de comprar o validar)

    /**
     * Muestra la lista completa de todas las entradas vendidas en el sistema.
     *
     * @param model El modelo para enviar datos a Thymeleaf.
     * @return El nombre de la plantilla: "entradas".
     */
    @GetMapping
    public String listarEntradas(Model model) {
        
        // 1. Obtenemos todas las entradas de la base de datos
        List<Entrada> listaEntradas = entradaRepository.findAll();
        
        // 2. Agregamos la lista al modelo con el nombre "entradas"
        model.addAttribute("entradas", listaEntradas);
        
        // 3. Devolvemos el nombre de la plantilla "entradas.html"
        return "entradas";
    }
}