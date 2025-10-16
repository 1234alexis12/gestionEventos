package eventlysa.evently.service;

import eventlysa.evently.entity.Aforo;
import eventlysa.evently.repository.AforoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AforoServiceImpl implements AforoService {

    @Autowired
    private AforoRepository aforoRepository;

    @Override
    public List<Aforo> findAll() {
        return aforoRepository.findAll();
    }

    @Override
    public Optional<Aforo> findById(Long id) {
        return aforoRepository.findById(id);
    }

    @Override
    public Aforo save(Aforo aforo) {
        return aforoRepository.save(aforo);
    }

    @Override
    public Aforo update(Long id, Aforo aforo) {
        if (aforoRepository.existsById(id)) {
            aforo.setId(id);
            return aforoRepository.save(aforo);
        }
        throw new RuntimeException("Aforo no encontrado con id: " + id);
    }

    @Override
    public void deleteById(Long id) {
        aforoRepository.deleteById(id);
    }
}
