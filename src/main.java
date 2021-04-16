import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws IOException {
        //Print out title message.
        System.out.println("**************************************************************************************");
        System.out.println("                            \uD835\uDD4D\uD835\uDD52\uD835\uDD5F\uD835\uDD54\uD835\uDD60\uD835\uDD66\uD835\uDD67\uD835\uDD56\uD835\uDD63 \uD835\uDD39\uD835\uDD66\uD835\uDD64 \uD835\uDD44\uD835\uDD52\uD835\uDD5F\uD835\uDD52\uD835\uDD58\uD835\uDD56\uD835\uDD5E\uD835\uDD56\uD835\uDD5F\uD835\uDD65 \uD835\uDD4A\uD835\uDD6A\uD835\uDD64\uD835\uDD65\uD835\uDD56\uD835\uDD5E");
        System.out.println("**************************************************************************************");
        System.out.println("             This system provides 4 unique user queries at your disposal. \n            Simply enter any query number from the table of queries below.");
        System.out.println(
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
                        "+-------+----------------------------------------------------------------------------+");
        Scanner scanner = new Scanner(System.in);
        boolean runApp = true;
        while(runApp){
            System.out.print("\nQuery: ");
            int requestedQuery = scanner.nextInt();
            if(requestedQuery == 1){
                System.out.println("Sorry, this feature is still being developed.");
            }
            else if(requestedQuery == 2){
                System.out.println("Sorry, this feature is still being developed.");
            }
            else if(requestedQuery == 3){
                boolean runUserQuery3 = true;
                Map<String, List<stopTime>> stopTimes = stopTime.generateHashMapOfStopTimes("files/stop_times.txt");
                while(runUserQuery3) {
                    System.out.print("Input an arrival time in the format 'hh:mm:ss': ");
                    String userArrivalTimeInput = scanner.next();
                    stopTime.findListOfTripsWithGivenArrivalTime(userArrivalTimeInput, stopTimes);
                    boolean innerQuery = true;
                    while (innerQuery) {
                        System.out.print("Do you want to search for another arrival time? [Y/N]: ");
                        String userReply = scanner.next();
                        if (userReply.equals("N") || userReply.equals("n")) {
                            innerQuery = false;
                            runUserQuery3 = false;
                        }
                        else if (userReply.equals("Y") || userReply.equals("y")){
                            innerQuery = false;
                        }
                        else {
                            System.out.println("Please provide a valid answer.");
                        }
                    }
                }
            }
            else if(requestedQuery == 4){
                System.out.println("Thank you for using the Vancouver Bus Management System.");
                runApp = false;
            }
            else{
                System.out.println("Please enter a valid query number.");
            }
        }
    }
}
