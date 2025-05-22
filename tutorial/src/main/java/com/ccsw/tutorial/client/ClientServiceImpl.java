package com.ccsw.tutorial.client;


import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.exception.DuplicateClientNameException;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;


 @Override
    public Client get(Long id) {

        return this.clientRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Client> findAll() {

          return (List<Client>) this.clientRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
public void save(Long id, ClientDto dto) {
    if (dto.getName() == null || dto.getName().trim().isEmpty()) {
        throw new IllegalArgumentException("El nombre del cliente no puede estar vacío ni contener solo espacios.");
    }

    boolean exists = clientRepository.existsByName(dto.getName());
    if (exists) {
        throw new DuplicateClientNameException("Ya existe un cliente con ese nombre.");
    }

    Client client;
    if (id == null) {
        client = new Client();
    } else {
        client = this.clientRepository.findById(id).orElse(null);
    }

    client.setName(dto.getName());
    this.clientRepository.save(client);
}

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

          if(this.clientRepository.findById(id).orElse(null) == null){
             throw new Exception("Not exists");
          }

          this.clientRepository.deleteById(id);
    }

}