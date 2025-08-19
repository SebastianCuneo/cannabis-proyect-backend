package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Provider;
import uy.edu.ucu.inventario.repository.ProviderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing providers.
 */
@Service
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final AuditLogService auditLogService;

    public ProviderService(ProviderRepository providerRepository, AuditLogService auditLogService) {
        this.providerRepository = providerRepository;
        this.auditLogService = auditLogService;
    }

    public List<Provider> listAll() {
        return providerRepository.findAll();
    }

    public Optional<Provider> getById(Long id) {
        return providerRepository.findById(id);
    }

    public Provider save(Provider provider) {
        boolean isNew = (provider.getId() == null);

        // Validación de datos obligatorios
        if (provider.getName() == null || provider.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provider name is required.");
        }

        if (isNew && providerRepository.findByNameIgnoreCase(provider.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Provider with name '" + provider.getName() + "' already exists.");
        }

        if (isNew) {
        	
        	provider.setDate(LocalDate.now());
        }
        

        Provider saved = providerRepository.save(provider);

        auditLogService.saveLog(
            "Provider",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            "Provider name: " + saved.getName()
        );

        return saved;
	}

    public void delete(Long id) {
        if (!providerRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provider with id " + id + " not found");
        }

        providerRepository.deleteById(id);

        auditLogService.saveLog(
            "Provider",
            id,
            "DELETE",
            "Provider deleted with id: " + id
        );
    }
}