package eventlysa.evently.controller;

import eventlysa.evently.entity.Entrada;
import eventlysa.evently.repository.EntradaRepository;
import eventlysa.evently.service.EntradaService; // Importa el servicio
import eventlysa.evently.util.QRCodeGenerator; // Importa el generador
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // Para recibir datos del form
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // Para recibir parámetros
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Para pasar datos tras redirección

import java.util.ArrayList;
import java.util.Base64; // Para codificar imagen QR
import java.util.List;
import java.io.IOException; // Para excepciones de QR
import com.google.zxing.WriterException; // Para excepciones de QR

@Controller
@RequestMapping("/entradas")
public class EntradaController {

    @Autowired
    private EntradaRepository entradaRepository;

    @Autowired
    private EntradaService entradaService; // Inyecta el servicio

    @Autowired
    private QRCodeGenerator qrCodeGenerator; // Inyecta el generador

    @GetMapping
    public String listarEntradas(Model model) {
        model.addAttribute("entradas", entradaRepository.findAll());
        return "entradas";
    }

    // --- NUEVO MÉTODO PARA PROCESAR LA COMPRA ---
    /**
     * Endpoint POST para simular la compra de entradas.
     * Recibe el ID del evento, ID de usuario (simulado) y cantidad.
     * Llama al servicio para procesar la compra.
     * Redirige a la página de confirmación con los códigos QR.
     */
    @PostMapping("/comprar")
    public String procesarCompra(@RequestParam Long eventoId,
                                 @RequestParam(defaultValue = "3") Long usuarioId, // Simula usuario logueado (ID 3 = Juan Pérez)
                                 @RequestParam(defaultValue = "1") int cantidad,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Llama al servicio para realizar la compra
            List<Entrada> entradasCompradas = entradaService.comprarEntradas(eventoId, usuarioId, cantidad);
            
            // Genera las imágenes QR en Base64 para pasarlas a la vista
            List<String> qrImagesBase64 = new ArrayList<>();
            for (Entrada entrada : entradasCompradas) {
                qrImagesBase64.add(qrCodeGenerator.generateQRCodeImageBase64(entrada.getCodigoQr(), 250, 250));
            }

            // Pasa las imágenes y las entradas a la página de confirmación usando RedirectAttributes
            redirectAttributes.addFlashAttribute("entradasCompradas", entradasCompradas);
            redirectAttributes.addFlashAttribute("qrImagesBase64", qrImagesBase64);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Compra realizada con éxito!");

            return "redirect:/entradas/confirmacion"; // Redirige al GET de confirmación

        } catch (RuntimeException | WriterException | IOException e) {
            // Si hay error (ej. aforo), redirige de vuelta con mensaje de error
            redirectAttributes.addFlashAttribute("mensajeError", "Error al comprar entradas: " + e.getMessage());
            // Redirige a la página del evento o a una página de error genérica
            return "redirect:/eventos"; // O cambia a la página apropiada
        }
    }

    // --- NUEVO MÉTODO PARA MOSTRAR LA CONFIRMACIÓN ---
    /**
     * Endpoint GET para mostrar la página de confirmación de compra.
     * Recibe los datos pasados por RedirectAttributes desde el POST /comprar.
     */
    @GetMapping("/confirmacion")
    public String mostrarConfirmacion(Model model) {
        // Thymeleaf automáticamente recoge los Flash Attributes ("entradasCompradas", "qrImagesBase64", etc.)
        // y los añade al modelo si existen.
        // Si no existen (ej. si se accede directamente a /confirmacion), las variables serán null.
        return "compra_confirmada"; // Nombre del nuevo archivo HTML
    }
}