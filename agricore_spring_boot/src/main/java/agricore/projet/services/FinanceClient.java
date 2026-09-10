package agricore.projet.services;

import agricore.projet.dto.compte.request.CompteRequestCreateDTO;
import agricore.projet.dto.compte.request.CompteRequestDTO;
import agricore.projet.dto.compte.request.TransfertRequestDTO;
import agricore.projet.dto.compte.response.CompteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "finance-service", url = "http://localhost:8081/api/comptes")
public interface FinanceClient {

    @PostMapping("/virement")
    void virement(@RequestBody TransfertRequestDTO dto);

    @PostMapping
    CompteResponseDTO create(@RequestBody CompteRequestCreateDTO compteRequestDTO);

    @GetMapping
    List<CompteResponseDTO> getAll();

    @GetMapping("/user/{id}")
    CompteResponseDTO getByUserId(@PathVariable Integer id);

    @PutMapping("/{id}")
    CompteResponseDTO update(@PathVariable Integer id, @RequestBody CompteRequestDTO compteRequestDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id);

}
