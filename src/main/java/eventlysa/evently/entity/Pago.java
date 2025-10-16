package eventlysa.evently.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago", indexes = {
    @Index(name = "idx_pago_referencia", columnList = "referencia_transaccion")
})
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;
    
    @NotNull
    private BigDecimal monto;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private MetodoPago metodoPago;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoPago estadoPago;
    
    @Column(name = "referencia_transaccion", length = 200)
    private String referenciaTransaccion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @OneToOne
    @JoinColumn(name = "id_entrada", unique = true, nullable = false)
    private Entrada entrada;
    
    // Constructores
    public Pago() {}
    
    public Pago(BigDecimal monto, MetodoPago metodoPago, EstadoPago estadoPago, 
                Usuario usuario, Entrada entrada) {
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estadoPago = estadoPago;
        this.usuario = usuario;
        this.entrada = entrada;
        this.fechaPago = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
    
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
    
    public EstadoPago getEstadoPago() { return estadoPago; }
    public void setEstadoPago(EstadoPago estadoPago) { this.estadoPago = estadoPago; }
    
    public String getReferenciaTransaccion() { return referenciaTransaccion; }
    public void setReferenciaTransaccion(String referenciaTransaccion) { this.referenciaTransaccion = referenciaTransaccion; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Entrada getEntrada() { return entrada; }
    public void setEntrada(Entrada entrada) { this.entrada = entrada; }
}