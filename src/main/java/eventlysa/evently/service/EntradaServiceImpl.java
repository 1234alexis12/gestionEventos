package eventlysa.evently.service;

import eventlysa.evently.entity.Entrada;
import eventlysa.evently.repository.EntradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EntradaServiceImpl implements EntradaService {

    @Autowired
    private EntradaRepository entradaRepository;

    @Override
    public List<Entrada> findAll() {
        return entradaRepository.findAll();
    }

    @Override
    public Optional<Entrada> findById(Long id) {
        return entradaRepository.findById(id);
    }

    @Override
    public Entrada save(Entrada entrada) {
        return entradaRepository.save(entrada);
    }

    @Override
    public Entrada update(Long id, Entrada entrada) {
        if (entradaRepository.existsById(id)) {
            entrada.setId(id);
            return entradaRepository.save(entrada);
        }
        throw new RuntimeException("Entrada no encontrado con id: " + id);
    }

    @Override
    public void deleteById(Long id) {
        entradaRepository.deleteById(id);
    }
}
