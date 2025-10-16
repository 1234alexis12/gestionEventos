package eventlysa.evently.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "entrada", indexes = {
    @Index(name = "idx_entrada_codigo_qr", columnList = "codigo_qr")
})
public class Entrada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "codigo_qr", unique = true, nullable = false, length = 128)
    private String codigoQr;
    
    @NotNull
    private BigDecimal precio;
    
    @Column(name = "fecha_compra")
    private LocalDateTime fechaCompra;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoEntrada estado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @OneToOne(mappedBy = "entrada", fetch = FetchType.LAZY)
    private Pago pago;
    
    @Version
    private Long version;
    
    // Constructores
    public Entrada() {}
    
    public Entrada(String codigoQr, BigDecimal precio, EstadoEntrada estado, Evento evento, Usuario usuario) {
        this.codigoQr = codigoQr;
        this.precio = precio;
        this.estado = estado;
        this.evento = evento;
        this.usuario = usuario;
        this.fechaCompra = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCodigoQr() { return codigoQr; }
    public void setCodigoQr(String codigoQr) { this.codigoQr = codigoQr; }
    
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    
    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }
    
    public EstadoEntrada getEstado() { return estado; }
    public void setEstado(EstadoEntrada estado) { this.estado = estado; }
    
    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}