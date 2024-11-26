package factory;

import around.Isla;
import factory.plants.Plant;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public abstract class Animal implements Runnable, IAnimal {
    // Crear una instancia de Random
    Random random = new Random();
    public static final Logger logger = Logger.getLogger(Animal.class.getName());

    static {
        try {
            // Configuración del logger (archivo app.log)
            FileHandler fileHandler = new FileHandler("animal.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("No se pudo configurar el logger: " + e.getMessage());
        }
    }
    protected String name;
    protected String gender;
    protected int fila;
    protected int columna;
    protected Isla isla;
    protected double weight;
    protected Map<String, Double> huntingProbabilities = new HashMap<>();
    protected ExecutorService executor;
    protected Factory animalFactory;


    public Animal(String name, String gender, Isla isla, ExecutorService executor, Factory animalFactory) {

        this.name = name;
        this.gender = gender;
        this.isla=isla;
        this.executor = executor;
        this.animalFactory=animalFactory;
        this.fila = random.nextInt(isla.getFilas());
        this.columna = random.nextInt(isla.getColumnas());
        isla.moverAnimal(this, fila, columna);
        this.weight = setweight(name);
        this.huntingProbabilities();

    }
    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setPosicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public String getName() {
        return name;
    }
    public String getGender() {
        return gender;
    }


    public void move() {

        int nuevaFila = switch (name) {
            case "lobo","águila","cabra","oveja","búfalo" -> fila + random.nextInt(7) - 3;
            case "boa","ratón","oruga" -> fila + random.nextInt(3) - 1;
            case "zorro","oso","conejo","jabalí" -> fila + random.nextInt(5) - 2;
            case "caballo","ciervo","pato" -> fila + random.nextInt(9) - 4;
            default -> fila+0; // Valor por defecto si no hay coincidencia
        };
        int nuevaColumna = switch (name) {
            case "lobo","águila","cabra","oveja","búfalo" ->{
                int desplazamiento=0;
                if (nuevaFila-fila==0) {
                    desplazamiento = random.nextInt(7) - 3;
                }else if (nuevaFila-fila==1){
                    desplazamiento = random.nextInt(5) - 2;
                }else if (nuevaFila-fila==2){
                    desplazamiento = random.nextInt(3) - 1;
                }
                yield columna + desplazamiento;
            }
            case "boa","ratón","oruga" -> {
                int desplazamiento=0;
                if (nuevaFila-fila==0) {
                    desplazamiento = random.nextInt(3) - 1;
                }
                yield columna + desplazamiento;
            }
            case "zorro","oso","conejo","jabalí" -> {
                int desplazamiento=0;
                if (nuevaFila-fila==0) {
                    desplazamiento = random.nextInt(5) - 2;
                }else if (nuevaFila-fila==1){
                    desplazamiento = random.nextInt(3) - 1;
                }
                yield columna + desplazamiento;
            }
            case "caballo","ciervo","pato" ->{
                int desplazamiento=0;
                if (nuevaFila-fila==0) {
                    desplazamiento = random.nextInt(9) - 4;
                }else if (nuevaFila-fila==1){
                    desplazamiento = random.nextInt(7) - 3;
                }else if (nuevaFila-fila==2){
                    desplazamiento = random.nextInt(5) - 2;
                }else if (nuevaFila-fila==3){
                    desplazamiento = random.nextInt(3) - 1;
                }
                yield columna + desplazamiento;
            }
            default -> columna+0; // Valor por defecto si no hay coincidencia
        };
        loseWeight();

        isla.moverAnimal(this, nuevaFila, nuevaColumna);
    }
    private void getOther() {
        List<Animal> animalesEnPosicion = isla.getAnimalPosition(fila, columna);
        if (this.weight < (setweight(this.name))) {
            if (this instanceof Herbivore) {
                List<Plant> plants = isla.getPlantPosition(fila, columna);
                for (Plant plant : plants) {
                    eat(plant);
                }
            }
        }
        for (Animal otherAnimal : animalesEnPosicion) {
            if (otherAnimal.getName() != this.name ) {
                interact(otherAnimal);
            }
            if (otherAnimal.getName()==this.name && otherAnimal.getGender() !=this.gender){
                reproduce();
            }
        }
    }
    public void interact(Animal otherAnimal) {

        if (otherAnimal.getName()==this.name && otherAnimal.getGender()!=this.gender) {
            reproduce();
            //System.out.println(name+isla.setUnicode(name) + " se reproduce con " + otherAnimal.name);
            logger.info(name+isla.setUnicode(name) + " se reproduce con " + otherAnimal.name);
        }else if(this.weight<(setweight(this.name))){
            eat(otherAnimal);
        }
    }
    public void reproduce() {
        // Lógica de reproducción
        int numero = random.nextInt(2) + 1; // Genera un número aleatorio entre 1 y 2
        String genero = (numero == 1) ? "macho" : "hembra";
        //System.out.println("Se ha creado un nuevo "+name+isla.setUnicode(name)+":"+genero);
        logger.info("Se ha creado un nuevo "+name+isla.setUnicode(name)+":"+genero);

        if (!executor.isShutdown()) {
            executor.submit(() -> {
               Animal animal =animalFactory.createAnimal(this.getTipo(), this.name, genero, this.isla,this.executor,this.animalFactory);
                Future<?> future = executor.submit(animal);
                isla.futures.put(animal, future);
            });}

    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            if (this.weight<(setweight(this.name)/2)){
                Thread.currentThread().interrupt();
                //System.out.println(this.name + isla.setUnicode(this.name) + " murio de hambre,el peso del " + this.name + " " + this.gender + " es: " + this.weight + " kg");
                logger.info(this.name + isla.setUnicode(this.name) + " murio de hambre,el peso del " + this.name + " " + this.gender + " es: " + this.weight + " kg");
                isla.killAnimal(this);
                Thread.currentThread().interrupt();
            }
            else {
                move();
                getOther();
            }

            try {
                Thread.sleep(1000); // Pausar por 1 segundo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

}