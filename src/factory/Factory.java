package factory;


import around.Isla;
import factory.plants.Plant;

import java.util.concurrent.ExecutorService;

public abstract class Factory {
    public abstract Animal createAnimal(String type, String name, String gender, Isla isla, ExecutorService executor, Factory animalFactory);
    public abstract Plant createPlant(String type, Isla isla, ExecutorService executor, Factory plantFactory);

}
