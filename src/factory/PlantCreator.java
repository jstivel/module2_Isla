package factory;

import around.Isla;
import factory.plants.Herbs;
import factory.plants.Plant;
import factory.plants.Schubbery;
import factory.plants.Fruit;

import java.util.concurrent.ExecutorService;

public class PlantCreator extends Factory{
    @Override
    public Animal createAnimal(String type, String name, String gender, Isla isla, ExecutorService executor, Factory animalFactory) {
        return null;
    }

    @Override
    public Plant createPlant(String type, Isla isla, ExecutorService executor, Factory plantFactory) {
        switch (type.toLowerCase()) {
            case "tree":
                return new Fruit(isla,executor,plantFactory);
            case "herbs":
                return new Herbs(isla,executor,plantFactory);
            case "schubbery":
                return new Schubbery(isla,executor,plantFactory);
            default:
                throw new IllegalArgumentException("Unknown animal type");
        }
    }
}
