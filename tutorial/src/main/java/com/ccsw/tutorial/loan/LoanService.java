package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

/**
 * @author ccsw
 *
 */
public interface LoanService {

    List<Loan> findAll();


    Page<Loan> findPage(Long gameId, Long clientId, LocalDate date, LoanSearchDto dto);


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

