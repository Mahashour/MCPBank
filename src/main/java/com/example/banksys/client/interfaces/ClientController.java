package com.example.banksys.client.interfaces;

import com.example.banksys.client.application.ClientService;
import com.example.banksys.client.interfaces.dto.ClientCreateRequest;
import com.example.banksys.client.interfaces.dto.ClientEmployeeResponse;
import com.example.banksys.client.interfaces.dto.ClientPublicResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientPublicResponse createClient(@Valid @RequestBody ClientCreateRequest request){
        return clientService.createClient(request);
    }

    @GetMapping("/{id}")
    public ClientPublicResponse getClient(@PathVariable UUID id){
        return clientService.getClientPublic(id);
    }

    @GetMapping("/{id}/full")
    public ClientEmployeeResponse getClientFull(@PathVariable UUID id){
        return clientService.getClientEmployee(id);
    }

    @GetMapping
    public List<ClientPublicResponse> getAllClients(){
        return clientService.getAllClients();
    }
}
