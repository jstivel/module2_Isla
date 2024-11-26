package factory;


import around.Isla;
import factory.plants.Plant;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Carnivore extends Animal {
    public Carnivore(String name, String gender, Isla isla, ExecutorService executor, Factory animalFactory) {
        super(name,gender, isla,executor,animalFactory);
    }
    @Override
    public void eat(Animal otherAnimal) {
        Future<?> future = isla.futures.get(otherAnimal);

        if (this.huntingProbabilities.containsKey(otherAnimal.getName())) {
            double probabilidad = this.huntingProbabilities.get(otherAnimal.getName());
            double aleatorio = this.random.nextDouble(); // Genera un número entre 0.0 y 1.0

            // Si el número aleatorio es menor que la probabilidad, la caza es exitosa
            if (aleatorio < probabilidad) {
                if (future != null) {
                    //System.out.println(this.name +isla.setUnicode(this.name)+ " ha devorado a " + otherAnimal.name+isla.setUnicode(otherAnimal.name));
                    logger.info(this.name +isla.setUnicode(this.name)+ " ha devorado a " + otherAnimal.name+isla.setUnicode(otherAnimal.name));
                    this.weight=this.weight+ otherAnimal.weight;
                    future.cancel(true); // Interrumpe el hilo
                    isla.killAnimal(otherAnimal);

                }
            } else {
                //System.out.println(this.name + isla.setUnicode(this.name) + " trato de devorar a " + otherAnimal.name + isla.setUnicode(otherAnimal.name) + ", pero " + otherAnimal.name + " ha escapado");
                logger.info(this.name + isla.setUnicode(this.name) + " trato de devorar a " + otherAnimal.name + isla.setUnicode(otherAnimal.name) + ", pero " + otherAnimal.name + " ha escapado");
            }
        }
    }

    @Override
    public void eat(Plant plant) {

    }

    @Override
    public String getTipo() {
        return "carnivore";

    }
    @Override
    public void loseWeight(){
        double weight = switch (name) {
            case "lobo" -> 8;
            case "boa" -> 3;
            case "zorro" -> 2;
            case "oso" -> 80;
            case "águila" -> 1;
            default -> 0.0;
        };
        this.weight= this.weight-weight;
    }
    @Override
    public Double setweight(String name) {
        double weight = switch (name) {
            case "lobo" -> 50;
            case "boa" -> 15;
            case "zorro" -> 8;
            case "oso" -> 500;
            case "águila" -> 6;
            default -> 0.0; // Valor por defecto si no hay coincidencia
        };
        return weight;
    }
    @Override
    public void huntingProbabilities() {
        if (name.equals("lobo")) {
            huntingProbabilities.put("caballo", 0.10); // 10% de probabilidad
            huntingProbabilities.put("ciervo", 0.15);  // 15% de probabilidad
            huntingProbabilities.put("conejo", 0.60);  // 30% de probabilidad
            huntingProbabilities.put("ratón", 0.80);
            huntingProbabilities.put("cabra", 0.60);
            huntingProbabilities.put("oveja", 0.70);
            huntingProbabilities.put("jabalí", 0.15);
            huntingProbabilities.put("búfalo", 0.10);
            huntingProbabilities.put("pato", 0.40);
        } else if (name.equals("boa")) {
            huntingProbabilities.put("zorro", 0.15);
            huntingProbabilities.put("conejo", 0.20);
            huntingProbabilities.put("ratón", 0.40);
            huntingProbabilities.put("pato", 0.10);
        } else if (name.equals("zorro")) {
            huntingProbabilities.put("conejo", 0.70);
            huntingProbabilities.put("ratón", 0.90);
            huntingProbabilities.put("pato", 0.60);
            huntingProbabilities.put("oruga", 0.40);
        } else if (name.equals("oso")) {
            huntingProbabilities.put("boa", 0.80);
            huntingProbabilities.put("caballo", 0.40);
            huntingProbabilities.put("ciervo", 0.80);
            huntingProbabilities.put("conejo", 0.80);
            huntingProbabilities.put("ratón", 0.90);
            huntingProbabilities.put("cabra", 0.70);
            huntingProbabilities.put("oveja", 0.70);
            huntingProbabilities.put("jabalí", 0.50);
            huntingProbabilities.put("búfalo", 0.20);
            huntingProbabilities.put("pato", 0.10);
        } else if (name.equals("águila")) {
            huntingProbabilities.put("zorro", 0.10);
            huntingProbabilities.put("conejo", 0.90);
            huntingProbabilities.put("ratón", 0.90);
            huntingProbabilities.put("pato", 0.80);
        }
    }
}
