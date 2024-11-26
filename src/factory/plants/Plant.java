package factory.plants;

import around.Isla;
import factory.Factory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public abstract class Plant implements Runnable, IPlant {
    Random random = new Random();

    protected Isla isla;
    protected double weight;
    protected int fila;
    protected int columna;
    protected ExecutorService executor;
    protected Factory plantFactory;
    public Plant(Isla isla, ExecutorService executor, Factory plantFactory){

        this.isla=isla;
        this.executor = executor;
        this.plantFactory=plantFactory;
        this.fila = random.nextInt(isla.getFilas());
        this.columna = random.nextInt(isla.getColumnas());
        isla.insertPlant(this, fila, columna);
        setWeight();
    }
    protected int getPlants() {
        List<Plant> plantPosition = isla.getPlantPosition(fila, columna);
        int iFruit =0, iHerbs=0, iSchubbery=0;
        for (Plant plants : plantPosition) {
            if (plants instanceof Fruit){
                iFruit++;
            } else if (plants instanceof Herbs) {
                iHerbs++;
            } else if (plants instanceof Schubbery) {
                iSchubbery++;
            }
        }
        int count = switch (this.getType()) {
            case "fruit" -> iFruit;
            case "herbs" -> iHerbs;
            case "schubbery" -> iSchubbery;
            default -> 0; // Valor por defecto si no hay coincidencia
        };
        return count;
    }

    public double getWeight() {
        return weight;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
    public void loseWeight(){

    };
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            reproduce();
        }
    }
}
