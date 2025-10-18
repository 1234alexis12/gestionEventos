package eventlysa.evently.controller;

import eventlysa.evently.entity.Entrada;
import eventlysa.evently.entity.Evento;              // <-- LÍNEA AÑADIDA
import eventlysa.evently.repository.EntradaRepository;
import eventlysa.evently.repository.EventoRepository; // <-- LÍNEA AÑADIDA
import eventlysa.evently.service.EntradaService;
import eventlysa.evently.util.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.io.IOException;
import com.google.zxing.WriterException;
import java.math.BigDecimal;                         // <-- LÍNEA AÑADIDA

@Controller
@RequestMapping("/entradas")
public class EntradaController {

    @Autowired
    private EntradaRepository entradaRepository;

    @Autowired
    private EntradaService entradaService;

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @Autowired // Asegúrate de que EventoRepository esté inyectado
    private EventoRepository eventoRepository;

    @GetMapping
    public String listarEntradas(Model model) {
        model.addAttribute("entradas", entradaRepository.findAll());
        return "entradas";
    }

    @PostMapping("/comprar")
    public String procesarCompra(@RequestParam Long eventoId,
                                 @RequestParam(defaultValue = "10") Long usuarioId, // Asegúrate que ID 10 exista
                                 @RequestParam(defaultValue = "1") int cantidad,
                                 RedirectAttributes redirectAttributes) {
        try {
            List<Entrada> entradasCompradas = entradaService.comprarEntradas(eventoId, usuarioId, cantidad);
            List<String> qrImagesBase64 = new ArrayList<>();
            for (Entrada entrada : entradasCompradas) {
                qrImagesBase64.add(qrCodeGenerator.generateQRCodeImageBase64(entrada.getCodigoQr(), 250, 250));
            }
            redirectAttributes.addFlashAttribute("entradasCompradas", entradasCompradas);
            redirectAttributes.addFlashAttribute("qrImagesBase64", qrImagesBase64);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Compra realizada con éxito!");
            return "redirect:/entradas/confirmacion";
        } catch (RuntimeException | WriterException | IOException e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al comprar entradas: " + e.getMessage());
            return "redirect:/eventos";
        }
    }

    @GetMapping("/confirmacion")
    public String mostrarConfirmacion(Model model) {
        return "compra_confirmada";
    }

    @GetMapping("/confirmar-compra")
    public String mostrarConfirmacionPrevia(@RequestParam Long eventoId, Model model) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado para confirmar compra: " + eventoId));
        BigDecimal precioUnitario = new BigDecimal("75.00");
        int cantidad = 1;
        BigDecimal total = precioUnitario.multiply(new BigDecimal(cantidad));
        model.addAttribute("evento", evento);
        model.addAttribute("cantidad", cantidad);
        model.addAttribute("precioUnitario", precioUnitario);
        model.addAttribute("total", total);
        return "confirmar_compra";
    }
}