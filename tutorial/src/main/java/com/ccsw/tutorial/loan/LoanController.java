package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ccsw
 *
 */
@Tag(name = "Loan", description = "API of Loan")
@RequestMapping(value = "/loan")
@RestController
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    ModelMapper mapper;

    
     /**
     * Método para recuperar un listado paginado de {@link Loan}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link LoanDto}
     */
    @Operation(summary = "Find Page", description = "Method that returns a page of Loans")
@RequestMapping(path = "", method = RequestMethod.POST)
public Page<LoanDto> findPage(@RequestBody LoanSearchDto dto,
                              @RequestParam(value = "gameId", required = false) Long idGame,
                              @RequestParam(value = "clientId", required = false) Long idClient,
                              @RequestParam(value = "date", required = false) String dateString) {
                              

    // Log para verificar los valores recibidos
    System.out.println("gameId: " + idGame + ", clientId: " + idClient + ", date: " + dateString);
    
    

    // Llamar al servicio con los filtros proporcionados
    Page<Loan> page = this.loanService.findPage(idGame, idClient, dateString, dto);

    // Convertir la página de entidades Loan a LoanDto
    return new PageImpl<>(
        page.getContent().stream().map(e -> mapper.map(e, LoanDto.class)).collect(Collectors.toList()),
        page.getPageable(),
        page.getTotalElements()
    );
}
    
    
    /**
     * Método para recuperar una lista de {@link loan}
     *
     * @param fecha fecha inicial
     * @param idGame PK del game
     * @param idClient PK del cliente
     * @return {@link List} de {@link GameDto}
     */
    @Operation(summary = "Find", description = "Method that return a filtered list of Loans")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<LoanDto> find(@RequestParam(value = "fecha", required = false) Date fecha,
                               @RequestParam(value = "idGame", required = false) Long idGame,
                               @RequestParam(value = "idClient", required = false) Long idClient) {

        List<Loan> loans = loanService.findAll();

        return loans.stream().map(e -> mapper.map(e, LoanDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para crear o actualizar un {@link Game}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Loan")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody LoanDto dto) {

        loanService.save(id, dto);

    }

   /**
     * Método para borrar una {@link loan}
     *
     * @param id PK de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a Loan")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.loanService.delete(id);
    }

}