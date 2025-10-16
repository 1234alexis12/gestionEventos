package eventlysa.evently.service;

import eventlysa.evently.entity.Usuario;
import eventlysa.evently.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        // Hashear la contraseña antes de guardar
        if (usuario.getPasswordHash() != null && !usuario.getPasswordHash().startsWith("$2a$")) {
            usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update(Long id, Usuario usuario) {
        if (usuarioRepository.existsById(id)) {
            usuario.setId(id);
            // Hashear la contraseña si se está actualizando
            if (usuario.getPasswordHash() != null && !usuario.getPasswordHash().startsWith("$2a$")) {
                usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
            }
            return usuarioRepository.save(usuario);
        }
        throw new RuntimeException("Usuario no encontrado con id: " + id);
    }

    @Override
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}