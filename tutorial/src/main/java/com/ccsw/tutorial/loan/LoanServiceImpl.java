package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.author.AuthorService;
import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.game.GameSpecification;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
    AuthorService authorService;

    @Autowired
    ClientService clientService;

    @Autowired
    GameService gameService;


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Loan> find(Date fechainic, Long idClient, Long idGame) {

       LoanSpecification fechaSpec = new LoanSpecification(new SearchCriteria("fechainic", ":", fechainic));
        LoanSpecification gameSpec = new LoanSpecification(new SearchCriteria("game.id", ":", idGame));
        LoanSpecification clientSpec = new LoanSpecification(new SearchCriteria("client.id", ":", idClient));

        Specification<Loan> spec = Specification.where(fechaSpec).and(gameSpec).and(clientSpec);

        return this.loanRepository.findAll(spec);
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