package factory;
import around.Isla;

import java.util.concurrent.ExecutorService;

public class AnimalCreator extends AnimalFactory {
    @Override
    public Animal createAnimal(String type, String name, String gender,Isla isla, ExecutorService executor,AnimalFactory animalFactory) {
        switch (type.toLowerCase()) {
            case "carnivore":
                return new Carnivore(name,gender,isla,executor,animalFactory);
            case "herbivore":
                return new Herbivore(name,gender,isla,executor,animalFactory);
            default:
                throw new IllegalArgumentException("Unknown animal type");
        }
    }
}
