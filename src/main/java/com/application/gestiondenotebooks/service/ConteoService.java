package com.application.gestiondenotebooks.service;

import com.application.gestiondenotebooks.enums.EstadoDevolucion;
import com.application.gestiondenotebooks.enums.TipoEquipo;
import com.application.gestiondenotebooks.repository.EquipoRepository;
import com.application.gestiondenotebooks.repository.PrestamoEquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConteoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private PrestamoEquipoRepository prestamoEquipoRepository;

    public Map<String, Long> getEquiposDisponiblesPorTipo() {
        // 1. Obtener el total de equipos por tipo
        Map<String, Long> totales = new HashMap<>();
        List<Object[]> totalEquipos = equipoRepository.countTotalEquiposPorTipo();
        for (Object[] result : totalEquipos) {
            // result[0] es un TipoEquipo (enum)
            TipoEquipo tipo = (TipoEquipo) result[0];
            Long count = (Long) result[1];
            totales.put(tipo.name(), count); // Usamos .name() para la clave String
        }

        // 2. Obtener la cantidad de equipos actualmente prestados (EN_PRESTAMO)
        List<Object[]> prestados = prestamoEquipoRepository.countByEstadoDevolucion(EstadoDevolucion.EN_PRESTAMO);

        // 3. Restar los prestados del total
        Map<String, Long> disponibles = new HashMap<>(totales);

        for (Object[] result : prestados) {
            // CORRECCIÓN CLAVE: Castear a TipoEquipo y luego obtener el nombre.
            String tipoEquipo = ((TipoEquipo) result[0]).name();
            Long prestadoCount = (Long) result[1];

            disponibles.computeIfPresent(tipoEquipo, (key, total) -> total - prestadoCount);
        }

        // Asegurar que todas las categorías (incluso si son 0) estén en el mapa
        disponibles.putIfAbsent(TipoEquipo.NOTEBOOK.name(), totales.getOrDefault(TipoEquipo.NOTEBOOK.name(), 0L));
        disponibles.putIfAbsent(TipoEquipo.CARGADOR.name(), totales.getOrDefault(TipoEquipo.CARGADOR.name(), 0L));
        disponibles.putIfAbsent(TipoEquipo.MOUSE.name(), totales.getOrDefault(TipoEquipo.MOUSE.name(), 0L));

        return disponibles;
    }
}