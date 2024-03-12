package src.entities.sim;

public class Profession 
{
    public static String[] names =
    {
        "Barista",
        "Chef",
        "Clown",
        "Dentist",
        "Doctor",
        "Model",
        "Police",
        "Programmer",
        "Security"
    };

    public static int[] salaries =
    {
        20, // barista
        30, // chef
        15, // clown
        40, // dentist
        50, // doctor
        45, // model
        35, // police
        45, // programmer
        15 // security
    };

    private String name;
    private int salary;

    private static int count = names.length;
    private int randomizer = (int) (Math.random() * 100) % count;
    
    // for creating a new sim
    public Profession()
    {
        name = names[randomizer];
        salary = salaries[randomizer];
    }

    // for changing the profession of a sim
    public Profession(int index)
    {
        name = names[index];
        salary = salaries[index];
    }

    public String getName()
    {
        return name;
    }

    public int getSalary()
    {
        return salary;
    }
}
