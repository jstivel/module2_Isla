package factory.plants;

import around.Isla;
import factory.Factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Fruit extends Plant{
    public Fruit(Isla isla, ExecutorService executor, Factory plantFactory) {
        super(isla,  executor,  plantFactory);
    }

    @Override
    public void reproduce() {

        if (!executor.isShutdown()) {
            if (getPlants()<(isla.getColumnas()* isla.getFilas())/2) {
                executor.submit(() -> {
                    Plant plant =plantFactory.createPlant(this.getType(), this.isla,this.executor,this.plantFactory);
                    Future<?> future = executor.submit(plant);
                    isla.futurePlants.put(plant, future);
                });
            }
        }
    }

    @Override
    public String getType() {
        return "fruit";
    }

    @Override
    public void setWeight() {
        this.weight=20;
    }
}
