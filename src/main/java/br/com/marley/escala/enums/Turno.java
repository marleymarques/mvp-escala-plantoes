package br.com.marley.escala.enums;

public enum Turno {

    MANHA(6),
    TARDE(6),
    NOITE(12);

    private final int horas;

    Turno(int horas) {
        this.horas = horas;
    }

    public int getHoras() {
        return horas;
    }
}