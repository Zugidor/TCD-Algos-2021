import java.io.IOException;
import java.util.*;

public class mainApplication
{
    //We have constant string values which will be shown as the program starts up.
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
    //This string will act as a table showing all the possible queries to the user.
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
        //Print out the the main title with a description which is shown upon startup of the application.
        System.out.println(String.join("", Collections.nCopies(86,"*")));
        System.out.println(firstTitle);
        System.out.println(secondTitle);
        System.out.println(String.join("", Collections.nCopies(86,"*")));
        System.out.println(String.join("", Collections.nCopies(13, " "))
                + "This system provides 4 unique user queries at your disposal.");
        System.out.println(String.join("", Collections.nCopies(12, " "))
                + "Simply enter any query number from the table of queries below.");

        //Now we can take user input from the user with a scanner.
        Scanner scanner = new Scanner(System.in);
        boolean runApp = true;
        boolean query3RunPrev = false;
        boolean query1RunPrev = false;
        //After query 3 is requested once, we store the Map with stopTime objects to speed up future requests for it.
        Map<String, List<stopTime>> stopTimes = null;
        BusStopMap stopMap = null;

        //Main application runtime loop
        while(runApp)
        {
            //Print out the query table at the start, and also after every time query 1-3 is completed.
            System.out.println(queryTable);
            System.out.print("\nEnter your query: ");
            String userInput = scanner.next();
            //User query response
            switch (userInput)
            {
                case "1":
                    System.out.println("Sorry, this feature is still being developed.\n");
                    boolean query1Running = true;
                    if(!query1RunPrev)
                    {
                        stopMap = new BusStopMap("input/stops.txt", "input/stop_times.txt", "input/transfers.txt");
                        query1RunPrev = true;
                    }
                    while(query1Running)
                    {
                        System.out.print("\nPlease enter the ID of the first stop: ");
                        int fromID = scanner.nextInt();
                        System.out.print("\nPlease enter the ID of the second stop: ");
                        int toID = scanner.nextInt();
                        stopMap.makePaths(fromID);
                        System.out.println("With a cost of " + stopMap.getCost(toID) + ", the shortest route is:");
                        for(String name : stopMap.getStops(toID))
                        {
                            System.out.println(name);
                        }
                        boolean quitAnswerGiven = false;
                        while(!quitAnswerGiven)
                        {
                            System.out.print("\nWould you like to search for different stops? [Y/N]: ");
                            String reply = scanner.next();
                            if(reply.equalsIgnoreCase("N"))
                            {
                                quitAnswerGiven = true;
                                query1Running = false;
                            }
                            else if(reply.equalsIgnoreCase("Y")) quitAnswerGiven = true;
                            else System.out.println("Invalid answer. Please type Y or N");
                        }
                    }
                break;
                case "2":
                    new stopName("input/stops.txt");
                    System.out.println("Note: This feature is currently unfinished.\n");
                break;
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
                                {
                                    //Error handling to cover edge cases where the user provides invalid input to [Y/N].
                                    System.out.println("Please provide a valid answer.");
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
                case "4":
                    //Final farewell message before the application finishes.
                    System.out.println("\nThank you for using the Vancouver Bus Management System.");
                    runApp = false;
                break;
                default:
                    //If any other input is given, that means the user has inputted an invalid response.
                    System.out.println("Please enter a valid query number.\n");
                break;
            }
        }
        scanner.close();
    }

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
