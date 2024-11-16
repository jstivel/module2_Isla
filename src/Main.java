import around.Isla;
import factory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    //lista de animales
    private static final String[] carnivoros = {"lobo", "boa", "zorro", "oso", "águila"};
    private static final String[] herbivoros = {"caballo", "ciervo", "conejo", "ratón", "cabra", "oveja", "jabalí", "búfalo", "pato", "oruga"};
    private static final int columns = 8, rows = 8;
    private static final int [] position = {columns,rows};

    public static void main(String[] args) {


        // Crear un objeto Scanner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in);

        //crear isla
        Isla isla = Isla.getInstancia(rows, columns);

        ///////////crear animales////////////
        // Creamos un ExecutorService para manejar los hilos
        ExecutorService executor = Executors.newCachedThreadPool();
        //creamos la factory de animales
        AnimalFactory animalFactory = new AnimalCreator();

        // Crear hilos para carnivoros
        for (String carnivore : carnivoros) {
            Animal machoCarnivore = animalFactory.createAnimal("carnivore", carnivore, "macho", isla,executor,animalFactory);
            Animal hembraCarnivore = animalFactory.createAnimal("carnivore", carnivore, "hembra", isla,executor,animalFactory);

            //executor.execute(machoCarnivore);
            //executor.execute(hembraCarnivore);
            Future<?> futureMacho = executor.submit(machoCarnivore);
            Future<?> futureHembra = executor.submit(hembraCarnivore);

            isla.futures.put(machoCarnivore, futureMacho);
            isla.futures.put(hembraCarnivore, futureHembra);

        }

        // Crear hilos para herbivoros
        for (String herbivore : herbivoros) {
            Animal machoHerbivore = animalFactory.createAnimal("herbivore", herbivore, "macho", isla,executor,animalFactory);
            Animal hembraHerbivore = animalFactory.createAnimal("herbivore", herbivore, "hembra", isla,executor,animalFactory);

            ///executor.execute(machoHerbivore);
            //executor.execute(hembraHerbivore);
            Future<?> futureMacho = executor.submit(machoHerbivore);
            Future<?> futureHembra = executor.submit(hembraHerbivore);

            isla.futures.put(machoHerbivore, futureMacho);
            isla.futures.put(hembraHerbivore, futureHembra);
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