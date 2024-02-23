package com.example.maledettatreest;

public class Tratta {
    String tname;
    Stazione s1, s2;

    public Tratta(Stazione s1, Stazione s2) {
        this.s1 = s1;
        this.s2 = s2;
        this.tname= s1.sname + "-" + s2.sname;
    }
}
