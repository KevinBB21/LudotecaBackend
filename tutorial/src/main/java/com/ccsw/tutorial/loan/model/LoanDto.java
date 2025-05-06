package com.ccsw.tutorial.loan.model;



import java.time.LocalDate;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;

public class LoanDto {
    
    private Long id;
    private LocalDate fechainic;
    private LocalDate fechafin;
    private ClientDto client;
    private GameDto game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechainic() {
        return fechainic;
    }

    public void setFechainic(LocalDate fechainic) {
        this.fechainic = fechainic;
    }

    public LocalDate getFechafin() {
        return fechafin;
    }

    public void setFechafin(LocalDate fechafin) {
        this.fechafin = fechafin;
    }

    public ClientDto getClient() {
        return client;
    }
  
    public void setClient(ClientDto client) {
        this.client = client;
    }
   
    public GameDto getGame() {
        return game;
    }
   
    public void setGame(GameDto game) {
        this.game = game;

    }

    

}
