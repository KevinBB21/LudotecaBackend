package loan;

import com.ccsw.tutorial.client.model.ClientDto; 
import com.ccsw.tutorial.game.model.GameDto; 
import com.ccsw.tutorial.loan.model.LoanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class loanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    public static final Long EXISTS_LOAN_ID = 1L;
    public static final Long NOT_EXISTS_LOAN_ID = 0L;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<LoanDto>> responseType = new ParameterizedTypeReference<List<LoanDto>>() {};

    @Test
    public void shouldNotAllowEndDateBeforeStartDate() {
        LoanDto loanDto = new LoanDto();
        loanDto.setFechainic(LocalDate.of(2025, 5, 10));
        loanDto.setFechafin(LocalDate.of(2025, 5, 5)); // Fecha fin anterior a la fecha inicio
        loanDto.setGame(new GameDto(1L));
        loanDto.setClient(new ClientDto(1L));

        ResponseEntity<String> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(loanDto, new HttpHeaders()),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("The end date cannot be earlier than the start date.");
    }

    @Test
    public void shouldNotAllowLoanPeriodExceeding14Days() {
        LoanDto loanDto = new LoanDto();
        loanDto.setFechainic(LocalDate.of(2025, 5, 1));
        loanDto.setFechafin(LocalDate.of(2025, 5, 20)); // Periodo mayor a 14 días
        loanDto.setGame(new GameDto(1L));
        loanDto.setClient(new ClientDto(1L));

        ResponseEntity<String> response = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(loanDto, new HttpHeaders()),
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("The loan period cannot exceed 14 days.");
    }

    @Test
    public void shouldNotAllowGameToBeLoanedToMultipleClientsOnSameDay() {
        LoanDto firstLoan = new LoanDto();
        firstLoan.setFechainic(LocalDate.of(2025, 5, 1));
        firstLoan.setFechafin(LocalDate.of(2025, 5, 5));
        firstLoan.setGame(new GameDto(1L));
        firstLoan.setClient(new ClientDto(1L));

        ResponseEntity<Void> firstResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(firstLoan, new HttpHeaders()),
                Void.class
        );

        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        LoanDto secondLoan = new LoanDto();
        secondLoan.setFechainic(LocalDate.of(2025, 5, 3));
        secondLoan.setFechafin(LocalDate.of(2025, 5, 7));
        secondLoan.setGame(new GameDto(1L)); // Mismo juego
        secondLoan.setClient(new ClientDto(2L));

        ResponseEntity<String> secondResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(secondLoan, new HttpHeaders()),
                String.class
        );

        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(secondResponse.getBody()).isEqualTo("The game is already loaned to another client during the selected dates.");
    }

    @Test
    public void shouldNotAllowClientToHaveMoreThanTwoLoansOnSameDay() {
        LoanDto firstLoan = new LoanDto();
        firstLoan.setFechainic(LocalDate.of(2025, 5, 1));
        firstLoan.setFechafin(LocalDate.of(2025, 5, 5));
        firstLoan.setGame(new GameDto(1L));
        firstLoan.setClient(new ClientDto(1L));

        ResponseEntity<Void> firstResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(firstLoan, new HttpHeaders()),
                Void.class
        );

        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        LoanDto secondLoan = new LoanDto();
        secondLoan.setFechainic(LocalDate.of(2025, 5, 2));
        secondLoan.setFechafin(LocalDate.of(2025, 5, 6));
        secondLoan.setGame(new GameDto(2L));
        secondLoan.setClient(new ClientDto(1L)); // Mismo cliente

        ResponseEntity<Void> secondResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(secondLoan, new HttpHeaders()),
                Void.class
        );

        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        LoanDto thirdLoan = new LoanDto();
        thirdLoan.setFechainic(LocalDate.of(2025, 5, 3));
        thirdLoan.setFechafin(LocalDate.of(2025, 5, 7));
        thirdLoan.setGame(new GameDto(3L));
        thirdLoan.setClient(new ClientDto(1L)); // Mismo cliente

        ResponseEntity<String> thirdResponse = restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(thirdLoan, new HttpHeaders()),
                String.class
        );

        assertThat(thirdResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(thirdResponse.getBody()).isEqualTo("The client cannot have more than 2 loans during the selected dates.");
    }
}