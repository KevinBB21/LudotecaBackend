package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;

import java.sql.Date;
import java.util.List;

/**
 * @author ccsw
 *
 */
public interface LoanService {

    /**
     *
     * @param fechainic título del juego
     * @param idCliente PK del cliente
     * @param idGame PK del juego
     * @return {@link List} de {@link Loan}
     */
    List<Loan> find(Date fechainic, Long idGame, Long idCliente);

    /**
     * Guarda o modifica un juego, dependiendo de si el identificador está o no informado
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, LoanDto dto);

   /**
     * Método para borrar una {@link Category}
     *
     * @param id PK de la entidad
     */
     void delete(Long id) throws Exception;


}

