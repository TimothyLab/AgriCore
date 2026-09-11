package agricore.projet.controller;

import agricore.projet.dto.utilisateur.request.ClientRequestDTO;
import agricore.projet.dto.utilisateur.response.ClientResponseDTO;
import agricore.projet.services.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {
	
	private final ClientService clientService;
	
	public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<ClientResponseDTO> findAll() {
        return clientService.getAll();
    }

    @GetMapping("/{id}")
    public ClientResponseDTO findById(@PathVariable Integer id) {
        return clientService.getById(id);
    }

    @PostMapping
    public ClientResponseDTO create(@RequestBody ClientRequestDTO request) {
        return clientService.create(request);
    }

   

    @PutMapping("/{id}")
    public ClientResponseDTO update(@PathVariable Integer id, @RequestBody ClientRequestDTO request) {
        return clientService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        clientService.deleteById(id);
    }
	

}
