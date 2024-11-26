package factory;
import around.Isla;
import factory.plants.Plant;

import java.util.concurrent.ExecutorService;

public class AnimalCreator extends Factory {
    @Override
    public Animal createAnimal(String type, String name, String gender, Isla isla, ExecutorService executor, Factory animalFactory) {
        switch (type.toLowerCase()) {
            case "carnivore":
                return new Carnivore(name,gender,isla,executor,animalFactory);
            case "herbivore":
                return new Herbivore(name,gender,isla,executor,animalFactory);
            default:
                throw new IllegalArgumentException("Unknown animal type");
        }
    }
    @Override
    public Plant createPlant(String type, Isla isla, ExecutorService executor, Factory plantFactory) {
        return null; // No aplica para esta fábrica
    }
}
