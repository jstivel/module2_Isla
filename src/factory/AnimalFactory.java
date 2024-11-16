package factory;


import around.Isla;

import java.util.concurrent.ExecutorService;

public abstract class AnimalFactory {
    public abstract Animal createAnimal(String type, String name, String gender, Isla isla, ExecutorService executor,AnimalFactory animalFactory);
}
