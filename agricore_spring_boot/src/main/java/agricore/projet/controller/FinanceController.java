package agricore.projet.controller;

import agricore.projet.dto.compte.request.CompteRequestCreateDTO;
import agricore.projet.dto.compte.request.CompteRequestDTO;
import agricore.projet.dto.compte.request.TransfertRequestDTO;
import agricore.projet.dto.compte.response.CompteResponseDTO;
import agricore.projet.services.FinanceClient;
import agricore.projet.services.FinanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class FinanceController {

    private final FinanceService financeService;
    private final FinanceClient financeClient;

    public FinanceController(FinanceService financeService, FinanceClient financeClient) {
        this.financeService = financeService;
        this.financeClient = financeClient;
    }

    @GetMapping
    public List<CompteResponseDTO> getAll() {
        return financeService.getAll();
    }

    @GetMapping("/user/{id}")
    public CompteResponseDTO getByUserId(@PathVariable Integer id) {
        return financeClient.getByUserId(id);
    }

    @PostMapping("/virement")
    public void virement(@RequestBody TransfertRequestDTO dto) {
        financeClient.virement(dto);
    }

    @PostMapping
    public CompteResponseDTO create(@RequestBody CompteRequestCreateDTO compteRequestDTO) {
        return financeClient.create(compteRequestDTO);
    }

    @PutMapping("/{id}")
    public CompteResponseDTO update(@PathVariable Integer id, @RequestBody CompteRequestDTO compteRequestDTO) {
        return financeClient.update(id, compteRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        financeClient.delete(id);
    }


}
