package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.client.model.Client;

import java.util.List;

/**
 * @author ccsw
 * 
 */
public interface ClientService {
    

        /**
     * Recupera un {@link Author} a través de su ID
     *
     * @param id PK de la entidad
     * @return {@link Author}
     */
    Client get(Long id);

    /**
     * Método para recuperar todos los clientes
     *
     * @return {@link List} de {@link Client}
     */
    List<Client> findAll();

    /**
     * Método para crear o actualizar un cliente
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, ClientDto dto);

    /**
     * Método para borrar un cliente
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

}