package factory;

import factory.plants.Plant;

public interface IAnimal {
    void eat(Animal otherAnimal);
    void eat(Plant plant);

    Double setweight(String name);

    void huntingProbabilities();

    String getTipo();

    void loseWeight();
}
