package com.ccsw.tutorial.loan.model;

import java.sql.Date;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;

public class LoanDto {
    
    private Long id;
    private Date fechainic;
    private Date fechafin;
    private ClientDto client;
    private GameDto game;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechainic() {
        return fechainic;
    }

    public void setFechainic(Date fechainic) {
        this.fechainic = fechainic;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
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
