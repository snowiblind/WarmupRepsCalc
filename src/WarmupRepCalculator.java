import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
5x5 warmup reps calculator for the big three compound movements.
Calculations are based on Dr. Mike Israetel of Renaissance Periodization's video titled "How to Warm Up for Muscle Growth Training | Hypertrophy Made Simple #3" (youtube.com/watch?v=HDq-68SlPgQ).
Don't be afraid to add in more warmup sets, if needed.

Need to add (in terms of priority):
1. Fix decimal points: plates needed for workout have .0's, and warmup reps drop the .5 when rounding to the nearest 2.5 place.
2. Add (JavaFX) UI.
3. Remember previous workouts.
*/

public class WarmupRepCalculator {
    public static void main(String[] args) throws IllegalWeightException {
        Scanner scanner = new Scanner(System.in);

        int weight = 0;
        String userExercise, unit = "";
        Object exerciseObject = null;
        boolean inputValid = false;

        while (!inputValid) {
            System.out.println("Select: Squat, Bench, Deadlift (CASE SENSITIVE)");
            userExercise = scanner.nextLine();
            try {
                // Dynamically instantiates the selected class by user input
                Class<?> exerciseClass = Class.forName(userExercise);
                Constructor<?> exerciseConstructor = exerciseClass.getConstructor();
                exerciseObject = exerciseConstructor.newInstance();
                inputValid = true;
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException | NoClassDefFoundError e) {
                System.out.println("Invalid exercise selected.");
            }
        }

        while (!unit.equals("kg") && !unit.equals("lb")) {
            System.out.println("Select your measurement system (kg or lb):");
            unit = scanner.next();
            if (!unit.equals("kg") && !unit.equals("lb")) {
                System.out.println("Invalid measurement system selected.");
            }
        }
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter working weight: ");
            try {
                weight = scanner.nextInt();
                if (weight < 0) {
                    throw new IllegalWeightException("Weight cannot be negative.");
                }
                validInput = true;
            } catch (InputMismatchException | IllegalWeightException e) {
                System.out.println("Weight has to be entered as an integer.");
            }
        }

        scanner.close();
        
        // Use reflection to call methods on objects
        try {
            Method setWeightMethod = exerciseObject.getClass().getMethod("setWorkingWeight", int.class, String.class);
            setWeightMethod.invoke(exerciseObject, weight, unit);

            Method multiplierMethod = exerciseObject.getClass().getMethod("multiplier");
            multiplierMethod.invoke(exerciseObject);

            Method printSetsMethod = exerciseObject.getClass().getMethod("printSets");
            printSetsMethod.invoke(exerciseObject);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error setting working weight: " + e.getMessage());
        }
    }
}