package eventlysa.evently.controller;

import eventlysa.evently.entity.Role;
import eventlysa.evently.entity.Usuario;
import eventlysa.evently.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/usuarios")
public class UsuarioWebController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Muestra la lista de todos los usuarios.
     * @param model El modelo para pasar datos a la vista.
     * @return El nombre de la vista de la lista de usuarios.
     */
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuarios";
    }

    /**
     * Muestra el formulario para crear un nuevo usuario.
     * @param model El modelo para pasar datos a la vista.
     * @return El nombre de la vista del formulario de usuario.
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioDeAlta(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Role.values()); // Pasa los roles a la vista
        return "usuario_form";
    }

    /**
     * Guarda un usuario (tanto nuevo como existente).
     * @param usuario El objeto Usuario vinculado desde el formulario.
     * @return Redirige a la lista de usuarios.
     */
    @PostMapping
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        if (usuario.getId() == null) {
            // Es un usuario nuevo, establecer fecha de registro y contraseña por defecto
            usuario.setFechaRegistro(LocalDateTime.now());
            // ¡IMPORTANTE! En una aplicación real, la contraseña debe ser "hasheada".
            // Esto es solo un ejemplo. Por ahora, asumimos que se recibe hasheada.
            if(usuario.getPasswordHash() == null || usuario.getPasswordHash().isEmpty()){
                usuario.setPasswordHash("default-hashed-password"); // Contraseña temporal
            }
        }
        usuarioService.save(usuario);
        return "redirect:/usuarios";
    }

    /**
     * Muestra el formulario para editar un usuario existente.
     * @param id El ID del usuario a editar.
     * @param model El modelo para pasar datos a la vista.
     * @return El nombre de la vista del formulario de usuario.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Usuario no válido:" + id));
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Role.values());
        return "usuario_form";
    }

    /**
     * Elimina un usuario por su ID.
     * @param id El ID del usuario a eliminar.
     * @return Redirige a la lista de usuarios.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return "redirect:/usuarios";
    }
    
}