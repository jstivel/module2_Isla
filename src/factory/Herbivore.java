package factory;
import around.Isla;
import factory.plants.Plant;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public class Herbivore extends Animal {

    public Herbivore(String name, String gender, Isla isla, ExecutorService executor, Factory animalFactory) {
        super(name,gender,isla,executor,animalFactory);

    }

    @Override
    public void eat(Animal otherAnimal) {

        Future<?> future = isla.futures.get(otherAnimal);

        if (this.huntingProbabilities.containsKey(otherAnimal.getName())) {
            double probabilidad = huntingProbabilities.get(otherAnimal.getName());
            double aleatorio = random.nextDouble(); // Genera un número entre 0.0 y 1.0

            // Si el número aleatorio es menor que la probabilidad, la caza es exitosa
            if (aleatorio < probabilidad) {
                if (future != null) {
                    System.out.println(this.name+isla.setUnicode(this.name) + " ha devorado a " + otherAnimal.name+isla.setUnicode(otherAnimal.name));
                    this.weight=this.weight+ otherAnimal.weight;
                    future.cancel(true); // Interrumpe el hilo
                    isla.killAnimal(otherAnimal);
                }
            }else {
                System.out.println(this.name+isla.setUnicode(this.name) + " trato de devorar a " + otherAnimal.name+isla.setUnicode(otherAnimal.name)+", pero "+otherAnimal.name+" ha escapado");
            }
        }
    }

    @Override
    public void eat(Plant plant) {
        Future<?> futurePlants = isla.futurePlants.get(plant);
        //System.out.println(this.name+isla.setUnicode(this.name)+" esta comiendo "+plant.getType());
        logger.info(this.name+isla.setUnicode(this.name)+" esta comiendo "+plant.getType());
        futurePlants.cancel(true); // Interrumpe el hilo
        isla.killPlant(plant);
        this.weight= weight+plant.getWeight();
    }
    @Override
    public String getTipo() {
        return "herbivore";
    }
    @Override
    public void loseWeight(){
        double weight = switch (name) {
            case "caballo" -> 60;
            case "ciervo" -> 50;
            case "conejo" -> 0.45;
            case "ratón" -> 0.01;
            case "cabra" -> 10;
            case "oveja" -> 15;
            case "jabalí" -> 50;
            case "búfalo" -> 100;
            case "pato" -> 0.15;
            case "oruga" -> 0;
            default -> 0.0; // Valor por defecto si no hay coincidencia
        };
        this.weight= this.weight-weight;
    }
    public Double setweight(String name) {
        double weight = switch (name) {
            case "caballo","jabalí" -> 400;
            case "ciervo" -> 300;
            case "conejo" -> 2;
            case "ratón" -> 0.05;
            case "cabra" -> 60;
            case "oveja" -> 70;
            case "búfalo" -> 700;
            case "pato"-> 1;
            case "oruga" -> 0.01;
            default -> 0.0; // Valor por defecto si no hay coincidencia
        };
        return weight;
    }
    public void huntingProbabilities() {
        if (name.equals("ratón")) {
            huntingProbabilities.put("oruga", 0.90);
            huntingProbabilities.put("plantas", 1.0);
        } else if (name.equals("jabalí")) {
            huntingProbabilities.put("ratón", 0.50);
            huntingProbabilities.put("oruga", 0.90);
            huntingProbabilities.put("plantas", 1.0);
        } else if (name.equals("pato")) {
            huntingProbabilities.put("oruga", 0.90);
            huntingProbabilities.put("plantas", 1.0);
        } else {
            huntingProbabilities.put("plantas", 1.0);
        }
    }
}
