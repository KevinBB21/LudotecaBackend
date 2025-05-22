package com.ccsw.tutorial.loan;


import com.ccsw.tutorial.loan.model.Loan;

import java.time.LocalDate;
import java.util.List;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author ccsw
 *
 */
public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

    @Override
    @EntityGraph(attributePaths = {"client", "game"})
    List<Loan> findAll(Specification<Loan> spec);


    @Query("SELECT l FROM Loan l WHERE l.game.id = :gameId AND " +
    "(l.fechainic <= :fechafin AND l.fechafin >= :fechainic)")
    List<Loan> findOverlappingLoans(@Param("gameId") Long gameId,
                                @Param("fechainic") LocalDate fechainic,
                                @Param("fechafin") LocalDate fechafin);

    @Query("SELECT l FROM Loan l WHERE l.client.id = :clientId AND " +
    "(l.fechainic <= :fechafin AND l.fechafin >= :fechainic)")
    List<Loan> findClientLoansInRange(@Param("clientId") Long clientId,
                                  @Param("fechainic") LocalDate fechainic,
                                  @Param("fechafin") LocalDate fechafin);
}

