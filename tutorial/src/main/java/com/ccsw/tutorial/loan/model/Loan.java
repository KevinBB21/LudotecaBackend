package com.ccsw.tutorial.loan.model;

import java.util.Date;

import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.client.model.Client;


import jakarta.persistence.*;


/**
 * @author ccsw
 *
 */
@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "fechainic", nullable = false)
    private Date fechainic;

    @Column(name = "fechafin", nullable = false)
    private Date fechafin;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechainic() {
        return this.fechainic;
    }

    public void setFechainic(Date fechainic) {
        this.fechainic = fechainic;
    }

    public Date getFechafin() {
        return this.fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}



