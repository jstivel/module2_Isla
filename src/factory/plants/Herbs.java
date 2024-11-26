package factory.plants;

import around.Isla;
import factory.Factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Herbs extends Plant{
    public Herbs(Isla isla, ExecutorService executor, Factory plantFactory) {
        super(isla,  executor,  plantFactory);
    }

    @Override
    public void reproduce() {
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
    public String getType() {
        return "herbs";
    }

    @Override
    public void setWeight() {
        this.weight=1;
    }
}
