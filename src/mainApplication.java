import java.io.IOException;
import java.util.*;

public class mainApplication
{

    public static final String firstTitle =
    		"          _   _                                             ______           \r\n" +
    		"         | | | |                                            | ___ \\          \r\n" +
    		"         | | | | __ _ _ __   ___ ___  _   ___   _____ _ __  | |_/ /_   _ ___ \r\n" +
    		"         | | | |/ _` | '_ \\ / __/ _ \\| | | \\ \\ / / _ \\ '__| | ___ \\ | | / __|\r\n" +
    		"         \\ \\_/ / (_| | | | | (_| (_) | |_| |\\ V /  __/ |    | |_/ / |_| \\__ \\\r\n" +
    		"          \\___/ \\__,_|_| |_|\\___\\___/ \\__,_| \\_/ \\___|_|    \\____/ \\__,_|___/";

    public static final String secondTitle = 
    		"	        ___  ___                                                  _   \r\n" +
    		"	        |  \\/  |                                                 | |  \r\n" +
    		"	        | .  . | __ _ _ __   __ _  __ _  ___ _ __ ___   ___ _ __ | |_ \r\n" +
    		"	        | |\\/| |/ _` | '_ \\ / _` |/ _` |/ _ \\ '_ ` _ \\ / _ \\ '_ \\| __|\r\n" +
    		"	        | |  | | (_| | | | | (_| | (_| |  __/ | | | | |  __/ | | | |_ \r\n" +
    		"	        \\_|  |_/\\__,_|_| |_|\\__,_|\\__, |\\___|_| |_| |_|\\___|_| |_|\\__|\r\n" +
    		"	                                   __/ |                              \r\n" +
    		"	                                  |___/                               " +
    		"	                                                              \r\n";
    
    public static final String queryTable =
            "+-------+----------------------------------------------------------------------------+\n" +
            "| Query |                                  Action                                    |\n" +
            "+-------+----------------------------------------------------------------------------+\n" +
            "|   1   | Receive a list of stops between 2 bus stops alongside the associated cost. |\n" +
            "+-------+----------------------------------------------------------------------------+\n" +
            "|   2   | Search for a bus stop by it's full name or by the first few characters.    |\n" +
            "+-------+----------------------------------------------------------------------------+\n" +
            "|   3   | Search for all trips with a given arrival time sorted by Trip ID.          |\n" +
            "+-------+----------------------------------------------------------------------------+\n" +
            "|   4   | Exit the program.                                                          |\n" +
            "+-------+----------------------------------------------------------------------------+";
    public static void main(String[] args) throws IOException
    {
        // Print out the title with the title text and a banner.
        System.out.println(String.join("", Collections.nCopies(86,"*")));
        System.out.println(firstTitle);
        System.out.println(secondTitle);
        System.out.println(String.join("", Collections.nCopies(86,"*")));
        System.out.println(String.join("", Collections.nCopies(13, " "))
                + "This system provides 4 unique user queries at your disposal.");
        System.out.println(String.join("", Collections.nCopies(12, " "))
                + "Simply enter any query number from the table of queries below.");
        
        Scanner scanner = new Scanner(System.in);
        boolean runApp = true;
        boolean query1RunPrev = false;
        boolean query3RunPrev = false;
        FloydWarshallMap stopMap = null;
        Map<String, List<stopTime>> stopTimes = null;

        while(runApp)
        {
            System.out.println(queryTable);
            System.out.print("\nEnter your query: ");
            String userInput = scanner.next();
            
            switch (userInput)
            {
                case "1":
                    System.out.println("Sorry, this feature is still being developed.\n");
                    /*
                    boolean query1Running = true;
                    if(!query1RunPrev)
                    {
                        stopMap = new FloydWarshallMap("input/stops.txt", "input/stop_times.txt", "input/transfers.txt");
                        query1RunPrev = true;
                    }
                    while(query1Running)
                    {

                        System.out.print("\nInput the first stop ID: ");
                        String fromIDString = scanner.next();
                        System.out.print("\nInput the second stop ID: ");
                        String toIDString = scanner.next();
                        try
                        {
                            int fromID = Integer.parseInt(fromIDString);
                            int toID = Integer.parseInt(toIDString);
                            double cost = stopMap.getCost(fromID, toID);
                            ArrayList<String> stops = stopMap.getStops(fromID, toID);
                            if(cost != -1 && stops != null)
                            {
                                System.out.println("With a cost of " + cost + ", the shortest route between these two stops is:");
                                for(int i = 0; i < stops.size(); i++)
                                    System.out.println(stops.get(i));
                                // TODO This includes the starting stop and destination, may need to be removed
                            }
                            else
                            {
                                System.out.println("Stop IDs do not exist or no route exists between these two stops.");
                            }
                        }
                        catch (Exception e)
                        {
                            System.out.println("Invalid ID input.");
                        }
                        boolean providedAnswer = false;
                        while(!providedAnswer)
                        {
                            System.out.print("\nWould you like to search for another route? [Y/N]: ");
                            String input = scanner.next();
                            if(input.equalsIgnoreCase("N"))
                            {
                                providedAnswer = true;
                                query1Running = false;
                            }
                            else if(input.equalsIgnoreCase("Y")) providedAnswer = true;
                            else System.out.println("Please provide a valid answer by typing \"Y\" or \"N\".");
                        }
                    }
                     */
                    break;
                case "2":
                     new stopName("input/stops.txt");
                    //System.out.println("Sorry, this feature is still being developed.\n");

                    break;

                case "3":
                    boolean runUserQuery3 = true;
                    if (!query3RunPrev)
                    {
                        stopTimes = stopTime.generateHashMapOfStopTimes("input/stop_times.txt");
                        query3RunPrev = true;
                    }
                    while (runUserQuery3)
                    {
                        System.out.print("Input an arrival time in the format 'hh:mm:ss': ");
                        String userArrivalTimeInput = scanner.next();
                        userArrivalTimeInput = userArrivalTimeInput.trim();
                        if (validTimeFormat(userArrivalTimeInput))
                        {
                            stopTime.findListOfTripsWithGivenArrivalTime(userArrivalTimeInput, stopTimes);
                            boolean innerQuery = true;
                            while (innerQuery)
                            {
                                System.out.print("Do you want to search for another arrival time? [Y/N]: ");
                                String userReply = scanner.next();
                                if (userReply.equalsIgnoreCase("N"))
                                {
                                    innerQuery = false;
                                    runUserQuery3 = false;
                                }
                                else if (userReply.equalsIgnoreCase("Y"))
                                {
                                    innerQuery = false;
                                }
                                else
                                {
                                    System.out.println("Please provide a valid answer.");
                                }
                            }
                        }
                        else
                        {
                            System.out.println("Please enter a valid time.");
                        }
                    }
                    break;
                case "4":
                    System.out.println("\nThank you for using the Vancouver Bus Management System.");
                    runApp = false;
                    break;
                default:
                    System.out.println("Please enter a valid query number.\n");
                    break;
            }
        }
        scanner.close();
    }
    
    static private boolean validTimeFormat(String s)
    {
        if (s == null || s.length() != 8)
        {
            return false;
        }
        if (s.charAt(2) == ':' && s.charAt(5) == ':')
        {
            String hh = s.substring(0, 2);
            String mm = s.substring(3, 5);
            String ss = s.substring(6, 8);
            int hours = -1, minutes = -1, seconds = -1;
            try
            {
                hours = Integer.parseInt(hh);
                minutes = Integer.parseInt(mm);
                seconds = Integer.parseInt(ss);
                if (hours > -1 && hours < 24 &&
                    minutes > -1 && minutes < 60 &&
                    seconds > -1 && seconds < 60)
                {
                    return true;
                }
            }
            catch (NumberFormatException nfe)
            {
                return false;
            }
        }
        return false;
    }
}
