package around;

import factory.Animal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class Isla {
    private static Isla instanciaUnica;
    private List<Animal>[][] matriz;
    public Map<Animal, Future<?>> futures = new HashMap<>();
    private int filas;
    private int columnas;

    private Isla(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.matriz = new ArrayList[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = new ArrayList<>();
            }
        }
    }
    public static synchronized  Isla getInstancia(int filas, int columnas) {
        if (instanciaUnica == null) {
            // Si la instancia no ha sido creada, se crea
            instanciaUnica = new Isla(filas, columnas);
        }
        // Devolver la instancia existente
        return instanciaUnica;
    }
    public synchronized void moverAnimal(Animal animal, int nuevaFila, int nuevaColumna) {
        if (nuevaFila < 0 || nuevaFila >= filas || nuevaColumna < 0 || nuevaColumna >= columnas) {
            return;  // Movimiento fuera de los límites
        }

        // Remover el animal de su posición actual
        matriz[animal.getFila()][animal.getColumna()].remove(animal);

        // Mover el animal a la nueva posición
        animal.setPosicion(nuevaFila, nuevaColumna);
        matriz[nuevaFila][nuevaColumna].add(animal);
    }
    public synchronized void killAnimal(Animal animal){
        matriz[animal.getFila()][animal.getColumna()].remove(animal);
    }
    public synchronized List<Animal> getAnimalPosition(int fila, int columna) {
        return new ArrayList<>(matriz[fila][columna]);
    }

    public int getFilas() {
        return filas;
    }
    public int getColumnas() {
        return columnas;
    }
    public void mostrarResumen() {
        Map<String, Map<String, Integer>> resumen = new HashMap<>(); // Mapa que guarda <Nombre de animal, <Género, Cantidad>>

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                for (Animal animal : matriz[i][j]) {
                    String especie = animal.getName(); // Nombre específico del animal
                    String genero = animal.getGender(); // "macho" o "hembra"

                    // Si la especie no existe en el mapa, la agrega
                    resumen.putIfAbsent(especie, new HashMap<>());
                    // Si el género no existe en el mapa interno de la especie, inicializa el contador

                    Map<String, Integer> generos = resumen.get(especie);
                    generos.put(genero, generos.getOrDefault(genero, 0) + 1);
                    //resumen.get(especie).put(genero, resumen.get(especie).getOrDefault(genero, 0) + 1);
                }
            }
        }

        // Mostrar el resumen
        for (Map.Entry<String, Map<String, Integer>> entry : resumen.entrySet()) {
            String especie = entry.getKey();
            Map<String, Integer> generos = entry.getValue();
            System.out.println("Especie: " + especie+setUnicode(especie));
            for (Map.Entry<String, Integer> generoEntry : generos.entrySet()) {
                System.out.println("  " + generoEntry.getKey() + ": " + generoEntry.getValue());
            }
        }
    }
    public String setUnicode(String name){
        String unicode = switch (name) {
            case "lobo" -> "\uD83D\uDC3A";
            case "boa" -> "\uD83D\uDC0D";
            case "zorro" -> "\uD83E\uDD8A";
            case "oso" -> "\uD83D\uDC3B";
            case "águila" -> "\uD83E\uDD85";
            case "caballo" -> "\uD83D\uDC34";
            case "jabalí" -> "\uD83D\uDC17";
            case "ciervo" -> "\uD83E\uDD8C";
            case "conejo" -> "\uD83D\uDC30";
            case "ratón" -> "\uD83D\uDC2D";
            case "cabra" -> "\uD83D\uDC10";
            case "oveja" -> "\uD83D\uDC11";
            case "búfalo" -> "\uD83D\uDC03";
            case "pato","planta" -> "\uD83E\uDD86";
            case "oruga" -> "\uD83D\uDC1B";
            default -> " "; // Valor por defecto si no hay coincidencia
        };
        return unicode;
    }

}
