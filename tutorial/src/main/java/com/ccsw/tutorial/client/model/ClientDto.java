package com.ccsw.tutorial.client.model;

public class ClientDto {
    
    private Long id;

    private String name;

    public ClientDto() {
    }

    public ClientDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }   

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
