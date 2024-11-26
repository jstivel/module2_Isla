import around.Isla;
import factory.*;
import factory.plants.Plant;

import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    //lista de animales

    private static final String[] carnivoros = {"lobo", "boa", "zorro", "oso", "águila"};
    private static final String[] herbivoros = {"caballo", "ciervo", "conejo", "ratón", "cabra", "oveja", "jabalí", "búfalo", "pato", "oruga"};
    //private static final String[] plants ={"herbs","schubbery","tree"};
    private static final int columns = 8, rows = 8;

    public static void main(String[] args) {


        // Crear un objeto Scanner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in);

        //crear isla
        Isla isla = Isla.getInstancia(rows, columns);

        ///////////crear animales////////////
        // Creamos un ExecutorService para manejar los hilos
        ExecutorService executor = Executors.newCachedThreadPool();
        //creamos la factory de animales
        Factory animalFactory = new AnimalCreator();
        Factory plantFactory = new PlantCreator();

        // Crear hilos para carnivoros
        for (String carnivore : carnivoros) {
            for (int i =0;i<3;i++) {
                Animal machoCarnivore = animalFactory.createAnimal("carnivore", carnivore, "macho", isla,executor,animalFactory);
                Animal hembraCarnivore = animalFactory.createAnimal("carnivore", carnivore, "hembra", isla,executor,animalFactory);

                //executor.execute(machoCarnivore);
                //executor.execute(hembraCarnivore);
                Future<?> futureMacho = executor.submit(machoCarnivore);
                Future<?> futureHembra = executor.submit(hembraCarnivore);

                isla.futures.put(machoCarnivore, futureMacho);
                isla.futures.put(hembraCarnivore, futureHembra);
            }

        }

        // Crear hilos para herbivoros
        for (String herbivore : herbivoros) {
            if (herbivore == "ratón" || herbivore == "conejo") {
                for (int i =0;i<5;i++) {
                    Animal machoHerbivore = animalFactory.createAnimal("herbivore", herbivore, "macho", isla, executor, animalFactory);
                    Animal hembraHerbivore = animalFactory.createAnimal("herbivore", herbivore, "hembra", isla, executor, animalFactory);
                    Future<?> futureMacho = executor.submit(machoHerbivore);
                    Future<?> futureHembra = executor.submit(hembraHerbivore);

                    isla.futures.put(machoHerbivore, futureMacho);
                    isla.futures.put(hembraHerbivore, futureHembra);
                }

            } else {
                Animal machoHerbivore = animalFactory.createAnimal("herbivore", herbivore, "macho", isla, executor, animalFactory);
                Animal hembraHerbivore = animalFactory.createAnimal("herbivore", herbivore, "hembra", isla, executor, animalFactory);
                Future<?> futureMacho = executor.submit(machoHerbivore);
                Future<?> futureHembra = executor.submit(hembraHerbivore);

                isla.futures.put(machoHerbivore, futureMacho);
                isla.futures.put(hembraHerbivore, futureHembra);
            }
        }
        // Crear hilos para plantas
        for (int i=0;rows*columns>i;i++){
            Plant tree = plantFactory.createPlant("tree",isla,executor,plantFactory);
            Future<?> futureTree = executor.submit(tree);
            isla.futurePlants.put(tree, futureTree);
        }
        for (int i=0;(rows*columns)*4>i;i++){
            Plant herbs = plantFactory.createPlant("herbs",isla,executor,plantFactory);
            Future<?> futureHerbs = executor.submit(herbs);
            isla.futurePlants.put(herbs, futureHerbs);
        }
        for (int i=0;(rows*columns)*2>i;i++){
            Plant schubbery = plantFactory.createPlant("schubbery",isla,executor,plantFactory);
            Future<?> futureSchubbery = executor.submit(schubbery);
            isla.futurePlants.put(schubbery, futureSchubbery);
        }




        // Esperar un tiempo específico antes de detener los hilos
        try {
            Thread.sleep(10000); // Deja que los hilos trabajen por 5 segundos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Detener todos los hilos
        System.out.println("Deteniendo todos los hilos...");
        executor.shutdownNow(); // Interrumpe todos los hilos
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        isla.mostrarResumen();

    }
}