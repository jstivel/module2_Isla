package factory;

import around.Isla;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class Animal implements Runnable {
    // Crear una instancia de Random
    Random random = new Random();
    protected String name;
    protected String gender;
    protected int fila;
    protected int columna;
    protected Isla isla;
    protected double weight;
    protected Map<String, Double> huntingProbabilities = new HashMap<>();
    protected ExecutorService executor;
    protected AnimalFactory animalFactory;


    public Animal(String name, String gender, Isla isla, ExecutorService executor,AnimalFactory animalFactory) {
        this.name = name;
        this.gender = gender;
        this.isla=isla;
        this.executor = executor;
        this.animalFactory=animalFactory;
        this.fila = random.nextInt(isla.getFilas());
        this.columna = random.nextInt(isla.getColumnas());
        isla.moverAnimal(this, fila, columna);
        this.weight = setweight(name);
        huntingProbabilities();

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
    public Double setweight(String name) {
        double weight = switch (name) {
            case "lobo" -> 50;
            case "boa" -> 15;
            case "zorro" -> 8;
            case "oso" -> 500;
            case "águila" -> 6;
            case "caballo","jabalí" -> 400;
            case "ciervo" -> 300;
            case "conejo" -> 2;
            case "ratón" -> 0.05;
            case "cabra" -> 60;
            case "oveja" -> 70;
            case "búfalo" -> 700;
            case "pato","planta" -> 1;
            case "oruga" -> 0.01;
            default -> 0.0; // Valor por defecto si no hay coincidencia
        };
        return weight;
    }
    private void huntingProbabilities() {
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
        } else if (name.equals("ratón")) {
            huntingProbabilities.put("oruga", 0.90);
        } else if (name.equals("jabalí")) {
            huntingProbabilities.put("ratón", 0.50);
            huntingProbabilities.put("oruga", 0.90);
        } else if (name.equals("pato")) {
            huntingProbabilities.put("ratón", 0.50);
            huntingProbabilities.put("oruga", 0.90);
        } else {
            huntingProbabilities.put("plantas", 1.0);
        }
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
        if (this instanceof Carnivore) {
            loseWeight();
        }
        isla.moverAnimal(this, nuevaFila, nuevaColumna);
    }

    public abstract String getTipo();
    public abstract void eat(Animal otherAnimal);

    private void getOther() {
        List<Animal> animalesEnPosicion = isla.getAnimalPosition(fila, columna);
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
            System.out.println(name+isla.setUnicode(name) + " se reproduce con " + otherAnimal.name);
        }else {
            eat(otherAnimal);
        }
    }
    public void reproduce() {
        // Lógica de reproducción
        int numero = random.nextInt(2) + 1; // Genera un número aleatorio entre 1 y 2
        String genero = (numero == 1) ? "macho" : "hembra";
        System.out.println("Se ha creado un nuevo "+name+isla.setUnicode(name)+":"+genero);

        if (!executor.isShutdown()) {
            executor.submit(() -> {
                // lógica de interacción o creación de nuevos animales
                //executor.execute(animalFactory.createAnimal(this.getTipo(), this.name, genero, this.isla,this.executor,this.animalFactory));
                Animal animal =animalFactory.createAnimal(this.getTipo(), this.name, genero, this.isla,this.executor,this.animalFactory);
                Future<?> future = executor.submit(animal);
                isla.futures.put(animal, future);
            });}

    }
    public void loseWeight(){
        double weight = switch (name) {
            case "lobo" -> 8;
            case "boa" -> 3;
            case "zorro" -> 2;
            case "oso" -> 80;
            case "águila" -> 1;
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
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            if (this.weight<(setweight(this.name)/2)){
                Thread.currentThread().interrupt();
                System.out.println(this.name + isla.setUnicode(this.name) + " murio de hambre,el peso del " + this.name + " " + this.gender + " es: " + this.weight + " kg");
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