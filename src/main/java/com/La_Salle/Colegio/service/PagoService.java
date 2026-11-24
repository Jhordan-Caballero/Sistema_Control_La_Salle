package com.La_Salle.Colegio.service;

import com.La_Salle.Colegio.dao.CuotaDao;
import com.La_Salle.Colegio.dao.PagoDao;
import com.La_Salle.Colegio.model.Cuota;
import com.La_Salle.Colegio.model.Pago;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final CuotaDao cuotaDao;
    private final PagoDao pagoDao;

    @Transactional
    public Pago registrar(Long cuotaId, BigDecimal monto, String medioPago, String nroOperacion,
                          boolean confirmarExtemporaneo, String usuario) {
        Cuota cuota = cuotaDao.findById(cuotaId)
                .orElseThrow(() -> new IllegalArgumentException("La cuota no existe"));

        if (monto == null || monto.signum() <= 0)
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        if (monto.compareTo(cuota.getSaldo()) > 0)
            throw new IllegalArgumentException("El monto supera el saldo de la cuota (S/ " + cuota.getSaldo() + ")");

        // Pago extemporáneo: el tesorero debe confirmarlo
        boolean extemporaneo = cuota.getFechaVencimiento().isBefore(LocalDate.now());
        if (extemporaneo && !confirmarExtemporaneo)
            throw new IllegalArgumentException("Pago extemporáneo: envíe confirmarExtemporaneo=true para registrarlo");

        // Pago parcial: el saldo queda pendiente
        cuota.setSaldo(cuota.getSaldo().subtract(monto));
        cuota.setEstado(cuota.getSaldo().signum() == 0 ? "PAGADA" : "PARCIAL");
        cuotaDao.save(cuota);

        Pago p = new Pago();
        p.setCuota(cuota);
        p.setFechaPago(LocalDateTime.now());
        p.setMonto(monto);
        p.setMedioPago(medioPago);
        p.setNroOperacion(nroOperacion);
        p.setExtemporaneo(extemporaneo);
        p.setUsuario(usuario);
        return pagoDao.save(p);
    }
}