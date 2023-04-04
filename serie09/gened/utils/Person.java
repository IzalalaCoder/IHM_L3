package gened.utils;

import util.Contract;


public class Person {

    // CONSTANTES DE CLASSE
    
    private static final Gender[] GENDERS = Gender.values();

    // ATTRIBUTS

    private Gender gender;
    private String name;

    // CONSTRUCTEURS

    public Person() {
        this(genderAlea());
    }
    
    public Person(Gender g) {
        this(validatePreAndGetGender(g), g.name());
    }

    public Person(Gender g, String n) {
        Contract.checkCondition(n != null && g != null);

        gender = g;
        name = n;
    }

    // REQUETES

    public Gender getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    // COMMANDES

    public void setGender(Gender g) {
        Contract.checkCondition(g != null);

        gender = g;
    }

    public void setName(String n) {
        Contract.checkCondition(n != null);

        name = n;
    }
    
    // OUTILS
    
    private static Gender genderAlea() {
        int n = (int) (Math.random() * 2);
        return GENDERS[n];
    }

    private static Gender validatePreAndGetGender(Gender g) {
        Contract.checkCondition(g != null);
        
        return g;
    }
}
