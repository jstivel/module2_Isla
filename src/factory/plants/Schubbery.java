package factory.plants;

import around.Isla;
import factory.Animal;
import factory.Factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Schubbery extends Plant{
    public Schubbery(Isla isla, ExecutorService executor, Factory plantFactory) {
        super(isla,  executor,  plantFactory);
    }

    @Override
    public void reproduce() {
        // Lógica de reproducción
        if (getPlants()<(isla.getColumnas()* isla.getFilas())/2) {
            if (!executor.isShutdown()) {
                executor.submit(() -> {
                    Plant plant =plantFactory.createPlant(this.getType(), this.isla,this.executor,this.plantFactory);
                    Future<?> future = executor.submit(plant);
                    isla.futurePlants.put(plant, future);
                });}
        }
    }
    @Override
    public void setWeight(){
        this.weight=5;
    }

    @Override
    public String getType() {
        return "schubbery";
    }
}
