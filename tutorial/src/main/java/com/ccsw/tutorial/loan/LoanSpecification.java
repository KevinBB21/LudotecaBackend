package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.loan.model.Loan;
import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;


public class LoanSpecification implements Specification<Loan> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;

    public LoanSpecification(SearchCriteria criteria) {

        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            Path<String> path = getPath(root);
            if (path.getJavaType() == String.class) {
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(path, criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("between") && criteria.getValue() != null) {
            // Verificar si el valor es una lista de fechas
            @SuppressWarnings("unchecked")
            List<LocalDate> dates = (List<LocalDate>) criteria.getValue();
            if (dates.size() == 2) {
                return builder.and(
                    builder.greaterThanOrEqualTo(root.get("fechainic"), dates.get(0)),
                    builder.lessThanOrEqualTo(root.get("fechafin"), dates.get(1))
                );
            } else {
                throw new IllegalArgumentException("Invalid date range. Expected exactly 2 dates.");
            }
        }
        return null;
}

    private Path<String> getPath(Root<Loan> root) {
        String key = criteria.getKey();
        String[] split = key.split("[.]", 0);

        Path<String> expression = root.get(split[0]);
        for (int i = 1; i < split.length; i++) {
            expression = expression.get(split[i]);
        }

        return expression;
    }

}