import java.io.IOException;
import java.util.*;

public class mainApplication
{
	/*-------------CONSTANT STRING VALUES FOR THE FRONT INTERFACE------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
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

    /*-------------VANCOUVER BUS MANAGEMENT SYSTEM APPLICATION---------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    public static void main(String[] args) throws IOException
    {
        //First, we print out the the main title with a description which is shown upon startup of the application.
        System.out.println(String.join("", Collections.nCopies(86,"*")));
        System.out.println(firstTitle);
        System.out.println(secondTitle);
        System.out.println(String.join("", Collections.nCopies(86,"*")));
        System.out.println(String.join("", Collections.nCopies(13, " "))
                + "This system provides 4 unique user queries at your disposal.");
        System.out.println(String.join("", Collections.nCopies(12, " "))
                + "Simply enter any query number from the table of queries below.");

        //Now, we can take user input from the user with a scanner.
        Scanner scanner = new Scanner(System.in);
        boolean runApp = true;
        //After query 1 is requested once, we store the BusStopMap object to speed up future requests for it.
        BusStopMap stopMap = null;
        boolean query1RunPrev = false;
        //After query 3 is requested once, we store the Map with stopTime objects to speed up future requests for it.
        Map<String, List<stopTime>> stopTimes = null;
        boolean query3RunPrev = false;

        //Main application runtime loop.
        while(runApp) 
        {
            //Print out the query table at the start, and also after every time query 1-3 is completed.
            System.out.println(queryTable);
            System.out.print("\nEnter your query: ");
            String userInput = scanner.next();
            
            //We can now react to the user query response.
            switch (userInput)
            {
               /**
                * @feature: Part 1 - Find the shortest paths between 2 bus stops entered by the user.
                * @return: The list of stops en route as well as the associated �cost�.
                */
                case "1":
                    boolean query1Running = true;
                    //We only want to generate a BusStopMap object once.
                    if(!query1RunPrev)
                    {
                    	//We initialize our BusStopMap object only once, as this query is requested for the first time.
                        stopMap = new BusStopMap("input/stops.txt", "input/stop_times.txt", "input/transfers.txt");
                        query1RunPrev = true;
                    }
                    while(query1Running)
                    {   //Receive the two necessary inputs
                        System.out.print("Please enter the ID of the first stop: ");
                        if(scanner.hasNextInt())
                        {
                            int fromID = scanner.nextInt();
                            System.out.print("Please enter the ID of the second stop: ");
                            if(scanner.hasNextInt())
                            {
                                int toID = scanner.nextInt();
                                try
                                {
                                    //Calculate shortest paths and cost
                                    stopMap.makePaths(fromID);
                                    Double cost = stopMap.getCost(toID);
                                    if(cost != null)
                                    {
                                        stopMap.getStops(toID, cost);
                                    }
                                    else
                                    {   //No path found
                                        System.out.println("No route exists between these two stops");
                                    }
                                }
                                catch(IllegalArgumentException e)
                                {   //Error handling
                                    System.out.println("Invalid Input: One or both of the given IDs do not match any stop");
                                }
                            }
                            else
                            {   //Error handling
                                System.out.println("Invalid Input: ID must be a number");
                                scanner.next();
                            }
                        }
                        else
                        {   //Error handling
                            System.out.println("Invalid Input: ID must be a number");
                            scanner.next();
                        }
                        boolean quitAnswerGiven = false;
                        while(!quitAnswerGiven)
                        {   //Ask the user whether to repeat the query or not
                            System.out.print("Would you like to search for different stops? [Y/N]: ");
                            String reply = scanner.next();
                            if(reply.equalsIgnoreCase("N"))
                            {
                                quitAnswerGiven = true;
                                query1Running = false;
                            }
                            else if(reply.equalsIgnoreCase("Y"))
                            {
                                quitAnswerGiven = true;
                            }
                            else
                            {
                                //Error handling
                                System.out.println("Invalid Input: Please enter \"Y\" if yes or \"N\" if no");
                            }
                        }
                    }
                break;
                
               /**
                * @feature: Part 2 - Let the user search for a bus stop by full name or by the first few characters in the name using a ternary search tree (TST).
                * @return: The full stop information for each stop matching the search criteria.
                */
                case "2":
                    boolean runUserQuery2 = true;
                    while (runUserQuery2)
                    {   //Receive user input
                        System.out.print("Please Enter the name of the bus stop you would like to search for: ");
                        String searchQuery = scanner.next();
                        searchQuery += scanner.nextLine();
                        //stopName() constructor does all the calculation and output
                        new stopName("input/stops.txt", searchQuery);
                        boolean exitQuery = true;
                        while (exitQuery)
                        {   //Ask user whether they wish to repeat this query
                            System.out.print("Do you want to search for another Bus Stop? [Y/N]: ");
                            String userReply = scanner.next();
                            if (userReply.equalsIgnoreCase("N"))
                            {
                                exitQuery = false;
                                runUserQuery2 = false;
                            }
                            else if (userReply.equalsIgnoreCase("Y"))
                            {
                                exitQuery = false;
                            }
                            else
                            {   //Error handling
                                System.out.println("Invalid Input: Please enter \"Y\" if yes or \"N\" if no");
                            }
                        }
                    }
                    break;
               /**
                * @feature: Part 3 - Let the user search for all trips with a given arrival time.
                * @return: Full details of all trips matching the criteria sorted by Trip ID.
                */
                case "3":
                    boolean runUserQuery3 = true;
                    //We only want to generate our Map once.
                    if (!query3RunPrev)
                    {
                        //We generate our Map with our dedicated function to generate a hash map of stopTime objects.
                        stopTimes = stopTime.generateHashMapOfStopTimes("input/stop_times.txt");
                        query3RunPrev = true;
                    }
                    while (runUserQuery3)
                    {
                        System.out.print("Input an arrival time in the format 'hh:mm:ss': ");
                        String userArrivalTimeInput = scanner.next();
                        userArrivalTimeInput = userArrivalTimeInput.trim();
                        //We check if the time given by the user is valid with our dedicated function to verify this.
                        if (validTimeFormat(userArrivalTimeInput))
                        {
                            //We have a dedicated function which prints out all the trips with the given arrival time.
                            stopTime.findListOfTripsWithGivenArrivalTime(userArrivalTimeInput, stopTimes);
                            boolean innerQuery = true;
                            while (innerQuery)
                            {
                                //We then ask the user if they would like to search for another time.
                                //By now, the Map stopTimes has already been generated, hence the query would be quick.
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
                                {   //Error handling
                                    System.out.println("Invalid Input: Please enter \"Y\" if yes or \"N\" if no");
                                }
                            }
                        }
                        else
                        {
                            //Error handling to cover edge cases where the user provides an invalid time format.
                            System.out.println("Please enter a valid time.");
                        }
                    }
                break;
                
               /**
                * @feature: Exit the application.
                * @return: Final farewell message before the application exits.
                */
                case "4":
                    System.out.println("\nThank you for using the Vancouver Bus Management System.");
                    runApp = false;
                break;
                
                default:
                    //If any other input is given, that means the user has entered an invalid response.
                    System.out.println("Please enter a valid query number.\n");
                break;
            }
        }
        //We are finished taking user input, so we can close the scanner.
        scanner.close();
    }

    /*-------------MAIN APPLICATION HELPER METHODS---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**
     * Verifies if a given string is in a valid time format for part 3 of the project.
     * @param: A string representing the input we want to verify.
     * @return: A boolean representing whether the input is in a valid time format or not.
     */
    static private boolean validTimeFormat(String s)
    {
        //The input must be exactly 8 characters long
        if (s == null || s.length() != 8)
        {
            return false;
        }
        //Validate time format
        if (s.charAt(2) == ':' && s.charAt(5) == ':')
        {
            String hh = s.substring(0, 2);
            String mm = s.substring(3, 5);
            String ss = s.substring(6, 8);
            int hours, minutes, seconds;
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
            //If an exception occurs when parsing the the strings as integers, the input is not in a valid time format.
            catch (NumberFormatException nfe)
            {
                return false;
            }
        }
        return false;
    }
}
