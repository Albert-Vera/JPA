package org.example;

import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    ControlDeErrores control = new ControlDeErrores();

    public static void main( String[] args ) {

        EntityManagerFactory emf= Persistence.createEntityManagerFactory("damPersistence");
        EntityManager manager = emf.createEntityManager();
        Scanner sc = new Scanner(System.in);
        App app = new App();

        while (true) {
            int salir = app.menu(sc, manager);
            app.actualizarNumeroAlumnos(manager);

            if (salir == 4) break;
        }
        manager.close();

    }

    int menu(Scanner sc, EntityManager manager){

        System.out.println("\n**********************   Data base Institut  **************************************\n");
        System.out.println("\t\t\t\t\t 1. Insertar Datos\n" +
                "\t\t\t\t\t 2. Mostrar Institutos\n" +
                "\t\t\t\t\t 3. Buscar un alumno\n" +
                "\t\t\t\t\t 4. Salir");
        System.out.println("\n*************************************************************************************");

        int opcion = leerOpcionMenu(sc, 5);

        switch (opcion) {
            case 1:
                actualizarNumeroAlumnos(manager);
                actualizarAlumnosInsti(manager);
                menuInsertarDatos(sc, manager);
                break;
            case 2:
                mostrarInstitutos(manager);
                mostrarClases(manager);
                mostrarAlumnos(manager);
                break;
            case 3:
                //mostrarDatos();
                break;
            case 4:
                break;
            default:
                break;
        }
        return opcion;
    }

    int leerOpcionMenu(Scanner sc, int numeroDeOpciones){
        boolean repetir= true;
        String texto = "";
        int numero = 0;

        while (repetir) {
            try {
                System.out.print(  "\t\t\t\n\nElige una opción: ");
                texto = sc.nextLine();
                numero = Integer.parseInt(texto);

                if (numero > 0 && numero < numeroDeOpciones) {
                    repetir = false;
                }else System.out.println("Introduce un valor válido");
            } catch (NumberFormatException e) {
                System.out.println("Ese valor no es válido");
            }
        }
        return numero;
    }

    void menuInsertarDatos(Scanner sc, EntityManager manager){
        while (true) {
            System.out.println("\n**********************   Data base Institut  **************************************\n");
            System.out.println("\t\t\t\t\t 1. Insertar Alumno\n" +
                    "\t\t\t\t\t 2. Insertar Clase\n" +
                    "\t\t\t\t\t 3. Insertar Instituto\n" +
                    "\t\t\t\t\t 4. Volver a menu ");
            System.out.println("\n*************************************************************************************");

            int opcion = leerOpcionMenu(sc, 5);
            mostrarInstitutos(manager);
            mostrarClases(manager);
            mostrarAlumnos(manager);

            switch (opcion) {
                case 1:

                    insertarAlumno(sc, manager);
                    break;
                case 2:

                    insertarClasse(sc, manager);
                    break;
                case 3:
                    insertarInstituto(sc, manager);
                    return;
                case 4:
                    return;
                default:
                    return;
            }
        }
    }

    void insertarAlumno(Scanner sc, EntityManager manager) {
        boolean existe = true;
        int idclaseAlum = 0;
        int dni = 0;
        String nombreAlumno = "";
        System.out.println("\n**********************   INSERTAR ALUMNO  **************************************\n");

        // while para verificar si existe el id de la clase
        while (existe){
            System.out.print("\t\t 1. Id Clase: ");
            idclaseAlum = sc.nextInt(); sc.nextLine();
            existe = control.verificaridClaseAlumno(manager, idclaseAlum);
        }
        existe = true;
        System.out.print("\t\t 1. Nombre del alumno: ");
        nombreAlumno = sc.nextLine();

        // While para verificar si el DNI ya existe
        while(existe) {

            System.out.print("\t\t 1. DNI: ");
            dni = sc.nextInt();
            existe = control.verificarDni(manager, dni);
        }
                System.out.println("\n*************************************************************************************");
                Alumno alumno = new Alumno();
                alumno.setClasseId(idclaseAlum);
                alumno.setNombre(nombreAlumno);
                alumno.setDNI(dni);

                manager.getTransaction().begin();
                manager.persist(alumno);
                existe = false;
            manager.getTransaction().commit();




    }

    void insertarClasse(Scanner sc, EntityManager manager){
        boolean existe = true;
        int idclase = 0;
        int idInstClase = 0;
        System.out.println("\n**********************   INSERTAR CLASE  **************************************\n");

        while(existe) {
            System.out.print("\t\t 1. Id Clase: ");
            idclase = sc.nextInt();
            sc.nextLine();
            existe = control.verificarIdClase(manager, idclase);
        }
        existe = true;
        System.out.print("\t\t 1. Nombre de la clase: ");
        String nombreClase = sc.nextLine();
        System.out.print("\t\t 1. Rama: ");
        String rama = sc.nextLine();

        while(existe) {
            System.out.print("\t\t 1. Id instituto: ");
            idInstClase = sc.nextInt();
            existe = control.verificarIdInstituto(manager, idInstClase);
        }
        System.out.println("\n*************************************************************************************");

        Clase clase = new Clase();
        clase.setId(idclase);
        clase.setNombre(nombreClase);
        clase.setRama(rama);
        clase.setNAlumnos(0);
        clase.setIdInstituto(idInstClase);
        manager.getTransaction().begin();
        manager.persist(clase);
        manager.getTransaction().commit();
    }

    void insertarInstituto(Scanner sc, EntityManager manager){
        boolean existe = true;
        int idInst = 0;
        String nombreInst = "";
        System.out.println("\n**********************   INSERTAR INSTITUTO  **************************************\n");

        while(existe) {
            System.out.print("\t\t 1. Id instituo: ");
            idInst = sc.nextInt();sc.nextLine();
            existe = control.verificarInsertInsti(manager, idInst);
        }
        existe = true;

        while(existe) {
            System.out.print("\t\t 1. Nombre instituto: ");
            nombreInst = sc.nextLine();
            existe = control.verificarNombreInst(manager, nombreInst);
        }
        //TODO eliminar aixo
        System.out.print("\t\t 1. Número de alumnos: ");
        int numeroAlumnos = sc.nextInt();
        System.out.println("\n*************************************************************************************");

        Instituto puig = new Instituto();
        puig.setId(idInst);
        puig.setNombre(nombreInst);
        puig.setNumAlumnos(numeroAlumnos);

        manager.getTransaction().begin();
        manager.persist(puig);
        manager.getTransaction().commit();
    }

    void mostrarInstitutos(EntityManager manager){



        TypedQuery<Instituto> query =
                manager.createQuery("SELECT c FROM Instituto c ORDER BY id", Instituto.class);
        List<Instituto> results = query.getResultList();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t*******************************   INSTITUTOS **************************************");

        for (Instituto a: results){
            System.out.println("\t\t\t\t\t\t\t\t\t\t\tid: " + a.getId() + "\tinstituto: " + a.getNombre() + "\tNúmero de alumnos: " + a.getNumAlumnos());
        }
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t***********************************************************************************");

    }

    void mostrarClases(EntityManager manager){
        TypedQuery<Clase> query =
                manager.createQuery("SELECT c FROM Clase c ORDER BY id", Clase.class);
        List<Clase> results = query.getResultList();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t*******************************   CLASES ******************************************");

        for (Clase a: results){
            System.out.println("\t\t\t\t\t\t\t\t\t\t\tid: " + a.getId() + "\tinstituto id: " + a.getIdInstituto() + "\tNombre: " + a.getNombre()
                    + "\tNúmero Alumnos: " + a.getNAlumnos() + "\tRama: " + a.getRama());
        }
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t***********************************************************************************");
    }

    void mostrarAlumnos(EntityManager manager){
        TypedQuery<Alumno> query =
                manager.createQuery("SELECT a FROM Alumno a ORDER BY clase_id", Alumno.class);
        List<Alumno> results = query.getResultList();
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t*********************************   ALUMNOS ***************************************");

        for (Alumno a: results){
            System.out.println("\t\t\t\t\t\t\t\t\t\t\tClase_id: " + a.getClasseId() + "\tNombre: " + a.getNombre() + "\tDNI: " + a.getDNI());
        }
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t***********************************************************************************");
    }
    void actualizarNumeroAlumnos(EntityManager manager){



        TypedQuery<Integer> queryClaseId =
                (TypedQuery<Integer>) manager.createQuery("SELECT id FROM Clase");
        List<Integer> resultsClases = queryClaseId.getResultList();

        for (Integer clasesId : resultsClases) {
            Query<Long> query =
                    (Query<Long>) manager.createQuery("SELECT  COUNT(*) FROM Alumno WHERE clase_id = ?1 ");
            query.setParameter(1, clasesId);
            //query.setParameter(2, institutoId);

            Long results = query.getSingleResult();
            Clase clase = manager.find(Clase.class, clasesId);

            clase.setNAlumnos(Math.toIntExact(results));

            manager.getTransaction().begin();
            manager.persist(clase);
            manager.getTransaction().commit();
        }
        //}


    }
    void actualizarAlumnosInsti(EntityManager manager){
        TypedQuery<Integer> queryInstiId =
                (TypedQuery<Integer>) manager.createQuery("SELECT i.id FROM Instituto i");
        List<Integer> resultsInsti = queryInstiId.getResultList();

//        for (Integer idInst: resultsInsti){
//            Instituto instituto = manager.find(Instituto.class, idInst);
//
//            Query<Long> queryniu =
//                    (Query<Long>) manager.createQuery("SELECT SUM(n_alumnos) FROM Clase c WHERE instituto_id = ?1");
//            queryniu.setParameter(1,idInst);
//
//            Long resulti = queryniu.getSingleResult();
//            instituto.setNumAlumnos(Math.toIntExact(resulti));
//            manager.getTransaction().begin();
//            manager.persist(instituto);
//            manager.getTransaction().commit();
//        }
    }
}

/**
 * Intento de mostrar a cada instituto SUS clases
 */
//  if (a.getNumAlumnos() > 0){
//          int cosas =  a.getId();
//          String consulta = "SELECT  c.id, c.instituto_id, c.nombre, c.n_alumnos, c.rama FROM Clase AS c  WHERE c.instituto_id = ?1";
//          TypedQuery<Clase> query2 =
//        manager.createQuery( consulta, Clase.class);
//        query2.setParameter(1, cosas);
//
//        List<Clase> results2 = query2.getResultList();
//        }