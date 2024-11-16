package factory;


import around.Isla;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Carnivore extends Animal {
    public Carnivore(String name,String gender, Isla isla, ExecutorService executor,AnimalFactory animalFactory) {
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
                    System.out.println(this.name +isla.setUnicode(this.name)+ " ha devorado a " + otherAnimal.name+isla.setUnicode(otherAnimal.name));
                    this.weight=this.weight+ otherAnimal.weight;
                    future.cancel(true); // Interrumpe el hilo
                    isla.killAnimal(otherAnimal);

                }
            }else {
                System.out.println(this.name+isla.setUnicode(this.name)+ " trato de devorar a " + otherAnimal.name+isla.setUnicode(otherAnimal.name)+", pero "+otherAnimal.name+" ha escapado");
            }
        }
    }
    public String getTipo() {
        return "carnivore";
    }
}
