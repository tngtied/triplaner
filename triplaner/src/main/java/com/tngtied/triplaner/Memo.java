package com.tngtied.triplaner;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Memo {
    public String message;
    @Id
    @GeneratedValue
    public int id;

}
