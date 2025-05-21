package com.ccsw.tutorial.loan;


import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    GameService gameService;

    @Override
    public List<Loan> findAll() {
        return (List<Loan>) this.loanRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Loan> findPage(Long gameId, Long clientId, String dateString, LoanSearchDto dto) {
    // Especificaciones para filtrar por gameId y clientId
    LoanSpecification idGameSpec = new LoanSpecification(new SearchCriteria("game.id", ":", gameId));
    LoanSpecification idClientSpec = new LoanSpecification(new SearchCriteria("client.id", ":", clientId));
    
    Specification<Loan> spec = Specification.where(idGameSpec).and(idClientSpec);
    
    // Validar si la fecha no es nula antes de agregar el filtro de rango de fechas
    if (dateString != null) {
        try {
            LocalDate date = LocalDate.parse(dateString); // Convertir String a LocalDate
            
            // Agregar filtro para verificar si la fecha está dentro del rango
            LoanSpecification dateRangeSpec = new LoanSpecification(new SearchCriteria("fechainic", "between", List.of(date, date)));
            spec = spec.and(dateRangeSpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'.");
        }
    }
    
    return this.loanRepository.findAll(spec, dto.getPageable().getPageable());
    }
    /**
     * {@inheritDoc}
     */
    @Override
public void save(Long id, LoanDto dto) {
    // Validar que la fecha de fin no sea anterior a la fecha de inicio
    if (dto.getFechainic().isAfter(dto.getFechafin())) {
        throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
    }

    // Validar que el periodo de préstamo no sea mayor a 14 días
    if (ChronoUnit.DAYS.between(dto.getFechainic(), dto.getFechafin()) > 14) {
        throw new IllegalArgumentException("El periodo de préstamo no puede exceder los 14 días.");
    }

    // Validar que el juego no esté prestado a otro cliente en el mismo rango de fechas
    List<Loan> overlappingLoans = loanRepository.findOverlappingLoans(
        dto.getGame().getId(),
        dto.getFechainic(),
        dto.getFechafin()
    );

    if (!overlappingLoans.isEmpty()) {
        throw new IllegalArgumentException("El juego ya está prestado a otro cliente durante las fechas seleccionadas.");
    }

    // Validar que el cliente no tenga más de 2 préstamos en el mismo rango de fechas
    List<Loan> clientLoans = loanRepository.findClientLoansInRange(
        dto.getClient().getId(),
        dto.getFechainic(),
        dto.getFechafin()
    );

    if (clientLoans.size() >= 2) {
        throw new IllegalArgumentException("El cliente no puede tener más de 2 préstamos durante las fechas seleccionadas.");
    }

    Loan loan = (id == null) ? new Loan() : this.loanRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("No existe un préstamo con el ID " + id + "."));

    BeanUtils.copyProperties(dto, loan, "id", "game", "client");
    loan.setClient(clientService.get(dto.getClient().getId()));
    loan.setGame(gameService.get(dto.getGame().getId()));

    this.loanRepository.save(loan);
}
       /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {

          if(this.loanRepository.findById(id).orElse(null) == null){
             throw new IllegalArgumentException("Not exists");
          }

          this.loanRepository.deleteById(id);
    }

    

}