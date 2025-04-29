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
    
        // Validar si la fecha no es nula antes de agregar el filtro de fechas
        if (dateString != null) {
            try {
                LocalDate date = LocalDate.parse(dateString); // Convertir String a LocalDate
                LoanSpecification dateSpec = new LoanSpecification(new SearchCriteria("fechainic", ":", date));
                spec = spec.and(dateSpec);
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

        Loan loan;

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.loanRepository.findById(id).orElse(null);
        }

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