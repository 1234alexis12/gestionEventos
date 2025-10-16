package eventlysa.evently.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "aforo", uniqueConstraints = {
    @UniqueConstraint(name = "uk_aforo_evento", columnNames = {"id_evento"})
})
public class Aforo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Min(0)
    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;
    
    @Min(0)
    @Column(name = "asistencia_actual", nullable = false)
    private Integer asistenciaActual = 0;
    
    @OneToOne
    @JoinColumn(name = "id_evento", nullable = false, unique = true)
    private Evento evento;
    
    @Version
    private Long version;
    
    // Constructores
    public Aforo() {}
    
    public Aforo(Integer capacidadMaxima, Evento evento) {
        this.capacidadMaxima = capacidadMaxima;
        this.evento = evento;
        this.asistenciaActual = 0;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(Integer capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    
    public Integer getAsistenciaActual() { return asistenciaActual; }
    public void setAsistenciaActual(Integer asistenciaActual) { this.asistenciaActual = asistenciaActual; }
    
    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    // Método de negocio para incrementar asistencia
    public boolean incrementarAsistencia() {
        if (asistenciaActual < capacidadMaxima) {
            asistenciaActual++;
            return true;
        }
        return false;
    }
}