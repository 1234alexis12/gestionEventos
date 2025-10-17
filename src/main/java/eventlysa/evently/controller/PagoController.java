package eventlysa.evently.controller;

//Asegúrate de que estas importaciones están presentes al inicio del archivo
import eventlysa.evently.entity.Pago;
import eventlysa.evently.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

//Asegúrate de que la clase tenga @Controller
@Controller
@RequestMapping("/pagos") // Agrupamos todas las rutas de pagos
public class PagoController {

 // Inyectamos el repositorio que ya tienes
 @Autowired
 private PagoRepository pagoRepository;

 /**
  * Este método maneja las solicitudes GET a /pagos.
  * Busca todos los pagos en la BD y los pasa a la plantilla "pagos.html".
  *
  * @param model El modelo para enviar datos a Thymeleaf.
  * @return El nombre de la plantilla: "pagos".
  */
 @GetMapping
 public String listarPagos(Model model) {
     
     // 1. Obtenemos todos los pagos de la base de datos
     List<Pago> listaDePagos = pagoRepository.findAll();
     
     // 2. Agregamos la lista al modelo con el nombre "pagos"
     // Este nombre ${pagos} será usado en el HTML
     model.addAttribute("pagos", listaDePagos);
     
     // 3. Devolvemos el nombre del archivo "pagos.html"
     return "pagos";
 }

 // (Aquí puedes agregar más métodos, como /pagos/{id} en el futuro)
}