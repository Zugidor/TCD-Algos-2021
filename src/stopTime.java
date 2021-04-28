import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class stopTime
{
    //Variables which we need in order to initialize a stopTime object.
    protected String arrival_time;
    protected String stop_headsign;
    protected String departure_time;
    protected String shape_dist_travelled;
    protected int trip_id;
    protected int stop_id;
    protected int pickup_type;
    protected int stop_sequence;
    protected int drop_off_type;

    //The constructor for a stopTime object, with parameters in the original order as mentioned in the file.
    protected stopTime(int trip_id, String arrival_time, String departure_time, int stop_id, int stop_sequence, String stop_headsign, int pickup_type, int drop_off_type, String shape_dist_travelled)
    {
        this.trip_id = trip_id;
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.stop_id = stop_id;
        this.stop_sequence = stop_sequence;
        this.stop_headsign = stop_headsign;
        this.pickup_type = pickup_type;
        this.drop_off_type = drop_off_type;
        this.shape_dist_travelled = shape_dist_travelled;
    }

    /*-------------HELPER SORTING ALGORITHMS---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    
    //This is the threshold for switching to insertion sort from merge-sort.
    static final int THRESHOLD = 10;

    /**
     * Sorts an array of doubles using a recursive implementation of Merge Sort (Top-Down merge sort).
     * @param: An unsorted array of stopTime objects.
     * @return: stopTime array sorted in ascending order in relation to each stopTime object's Trip ID.
     */
    protected static stopTime[] mergeSort(stopTime[] a)
    {
        //if the input is null or has a length less than or equal to 1, there is no point sorting anything! (already sorted)
        if(a == null || a.length <= 1)
        {
            return a;
        }

        //Otherwise, let's start sorting!
        stopTime[] auxiliary = new stopTime[a.length];
        //Kick off recursion by passing in 0 and array length-1 as indices (i.e. the full original array)
        mergeSort(a, auxiliary, 0, a.length - 1);
        return a;
    }

    /**
     * Private helper method for mergeSort(sortTime[] a)
     * It automatically uses insertionSort(a, low, high) depending on a cutoff threshold.
     * @param: An array of stopTime objects, an auxiliary array of stopTime objects, a low value, and a high value.
     * @return: void.
     */
    private static void mergeSort(stopTime[] a, stopTime[] aux, int low, int high)
    {
        if (high - low <= THRESHOLD)
        {
            insertionSort(a, low, high);
            return;
        }
        int mid = low + (high - low) / 2;
        mergeSort(a, aux, low, mid);
        mergeSort(a, aux, mid + 1, high);
        merge(a, aux, low, mid, high);
    }

    /**
     * Private helper method for mergeSort(stopTime[] a, stopTime[] aux, int low, int high) to merge elements.
     * @param: An array of stopTime objects, an auxiliary array of stopTime objects, a low value, and a high value.
     * @return: void.
     */
    private static void merge(stopTime[] a, stopTime[] aux, int lo, int mid, int hi)
    {
        //Copy Array into auxiliary array.
        if (hi + 1 - lo >= 0)
        {
            System.arraycopy(a, lo, aux, lo, hi + 1 - lo);
        }

        //Merge elements back into original, but sort them!
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++)
        {
            if (i > mid)
            {
                a[k] = aux[j++];
            }
            else if (j > hi)
            {
                a[k] = aux[i++];
            }
            //Below we compare based on Trip ID.
            else if (a[j].trip_id < aux[i].trip_id)
            {
                a[k] = aux[j++];
            }
            else a[k] = aux[i++];
        }
    }

    /**
     * Private helper method for mergeSort(stopTime[] a, stopTime[] aux, int low, int high) which is used when the input array is less than the cut-off threshold.
     * @param: An array of stopTime objects, a low value, and a high value.
     * @return: void.
     */
    private static void insertionSort(stopTime[] a, int low, int high)
    {
        stopTime key;
        int k;
        for(int i = low+1; i <= high; i++)
        {
            key = a[i];
            k = i - 1;
            //Below we compare based on Trip ID.
            while((k > low-1) && a[k].trip_id > key.trip_id)
            {
                a[k+1] = a[k];
                k--;
            }
            a[k+1] = key;
        }
    }

    /*-------------METHODS FOR PART 3 OF THE ASSIGNMENT SPECIFICATION: SEARCHING FOR TRIPS WITH A GIVEN ARRIVAL TIME---------------------------------------------------------------------------------------------------------------------*/
    /**
     * Generates a Hash-Map of the stop times, containing a list of stopTime objects each with a key equivalent to their arrival time.
     * As the lines are read, the input is handled appropriately by removing all invalid times and making sure all times follow the format of "hh:mm:ss".
     * Every line is turned into a stopTime object. Once the Hash-Map has been generated, the user can request as many queries as they wish very quickly.
     * @param: The path to the file which we read stop times from to generate the Hash-Map.
     * @return: A Hash-Map containing a list of stopTime objects, each with a key equivalent to their arrival time.
     */
    protected static Map<String, List<stopTime>> generateHashMapOfStopTimes (String pathToStopTimesFile) throws IOException
    {
        //We need to declare a few variables which we will use for the progress bar that will be shown to the user.
        int counterForProgressBar = 0;
        int numberOfValidEntries = 0;

        //We need to make a HashMap to store all the stop times, which we will eventually return.
        Map<String, List<stopTime>> stopTimes = new HashMap<>();
        //We also need a BufferedReader to read each line from the file "stop_times.txt", and convert each line into a stopTime object.
        BufferedReader reader = new BufferedReader(new FileReader(pathToStopTimesFile));
        //The first line in the file simply contains the order of the data, which we can ignore.
        reader.readLine();
        //We can initialize this variable to hold the current line we are on, initially being null.
        String currentLine;

        //We have a dedicated function to get the number of lines in the stop times file.
        //We do -1 since the first line is only the titles for each data separated by commas.
        long totalNumberOfLinesInFile = progressBar.numberOfLinesInAFile(pathToStopTimesFile)-1;
        System.out.println("Reading in " + totalNumberOfLinesInFile + " entries from stop_files.txt");
        //Now we iterate through every line in the file, creating objects for each line, and storing them in the HashMap.
        while ((currentLine = reader.readLine()) != null)
        {
            //We can split the current line into an array of strings, each element created by the separation of them with a comma.
            String[] tokens = currentLine.split(",");
            //The first number in the line is the trip ID, which would the first element in the array.
            int trip_id = Integer.parseInt(tokens[0]);
            //The second element in the line is the arrival time, which would be the second element in the array.
            //If there is any white space in the string, it means the time is in the format " h:mm:ss", and we can change this to "hh:mm:ss" for convenience.
            String arrival_time = tokens[1].replaceAll("\\s+", "0");
            //The third element in the line is the departure time, which would be the third element in the array.
            //Once again, if there is any white space in the string, it means the time is in the format " h:mm:ss", and we can change this to "hh:mm:ss" for convenience.
            String departure_time = tokens[2].replaceAll("\\s+", "0");

            //According to the assignment specification, we should remove all invalid times.
            try
            {
                //We can try to parse the strings as LocalTime objects
                LocalTime.parse(arrival_time, DateTimeFormatter.ofPattern("H:mm:ss"));
                LocalTime.parse(departure_time, DateTimeFormatter.ofPattern("H:mm:ss"));
            }
            catch (Exception e) //If any exceptions arise from doing so, that means the times are invalid.
            {
                //We simply skip this iteration of the loop (i.e. this line), and don't include it in the HashMap.
                counterForProgressBar++;
                continue;
            }

            //The fourth element in the line is the stop ID, which would be the fourth element in the array.
            int stop_id = Integer.parseInt(tokens[3]);
            //The fifth element in the line is the stop sequence, which would be the fifth element in the array.
            int stop_Sequence = Integer.parseInt(tokens[4]);
            //The sixth element in the line is the stop head-sign, which would be the sixth element in the array.
            //However, if this element is null, we want t
            String stop_headsign = (tokens[5].equals("")) ? "null" : tokens[5];
            //The seventh element in the line is the pickup type, which would be the seventh element in the array.
            int pickup_type = Integer.parseInt(tokens[6]);
            //The eighth element in the line is the drop off type, which would be the eighth element in the array.
            int drop_off_type = Integer.parseInt(tokens[7]);

            //Now, we have to deal with the ninth and final element in the line, which is the Shape distance travelled.
            //Since we separated the line by splitting them into elements separated by the comma, we need to take into account the chance that this value is empty.
            //If this value is empty, then the length of the array would only be 8. Otherwise, the array's length would be 9, and we can successfully read the value from the array.
            String shape_dist_travelled = (tokens.length == 8) ? null : tokens[8];

            //Get a list of all the stopTime objects in the HashMap with the new arrival time from the latest line.
            //If none exist previously, then make a new array of them that can be used by future lines.
            List<stopTime> newList = stopTimes.getOrDefault(arrival_time, new ArrayList<>());

            //Now we can create the stopTime object since we have all the necessary parameters and we have completed the error-checking for the time values.
            stopTime stopTimeToAdd = new stopTime(trip_id,arrival_time,departure_time,stop_id,stop_Sequence,stop_headsign,pickup_type,drop_off_type,shape_dist_travelled);

            //Now we can add this new stopTime object to the list, of all the stopTime objects with the same arrival time.
            newList.add(stopTimeToAdd);

            //Now we can add the list of stopTime objects back into the HashMap, with the key as the arrival_time.
            //This allows us to carry out requests for individual arrival times later on, and get stopTime objects that match the query.
            stopTimes.put(arrival_time, newList);

            //Update variables for the progress bar
            counterForProgressBar++;
            numberOfValidEntries++;

            //We only want to update the progress bar for every 1% of progress, so the function remains speedy.
            //If we updated the progress bar after every line, the I/0 required would slow down the function.
            if (counterForProgressBar % ((int)totalNumberOfLinesInFile/100) == 0)
            {
                    progressBar.updateProgressBar(counterForProgressBar, totalNumberOfLinesInFile);
            }
        }

        //Once we have successfully read all lines in the file, we have to update the progress bar to represent this.
        progressBar.updateProgressBar((int)totalNumberOfLinesInFile, totalNumberOfLinesInFile);

        //Now we can print some general statistics about the data we have just analyzed.
        System.out.println("\nEntries from stop_files.txt have been successfully analyzed.");
        System.out.println("There were " + (counterForProgressBar) + " entries with " + (numberOfValidEntries) + " valid entries and " +  (totalNumberOfLinesInFile-numberOfValidEntries) + " invalid entries which have been discarded.");

        //At this point, we have successfully read all the lines in the file, and we can close the BufferedReader.
        reader.close();
        //We have successfully generated a HashMap containing lists of stopTime objects, each list with a key of their arrival time representing the stopTimes in that list.
        return stopTimes;
    }

    /**
     * Retrieves all the stopTime objects which match the arrival time requested by the user in a list.
     * Then, the list of sortTime objects are sorted using an improved version of merge-sort.
     * Finally, the full details of the trips that match the arrival time are printed to the user.
     * @param: The arrival time that the user has requested, the generated Hash-Map containing all the stopTime objects.
     * @return: void.
     */
    protected static void findListOfTripsWithGivenArrivalTime (String inputArrivalTime, Map<String, List<stopTime>> Map) {
        //We simply check to see if the map contains any stopTimes which match the arrival time we need.
        List<stopTime> stopTimesWithMatchingArrivalTime = Map.get(inputArrivalTime);

        //If the list is empty, then no matching trips exist.
        if (stopTimesWithMatchingArrivalTime == null)
        {
            System.out.println("Sorry, there are no trips with the arrival time of " + inputArrivalTime);
            return;
        }

        //Otherwise, we have to start sorting the list of sortTime objects by their Trip ID.
        //We can convert the List to an array for simplicity.
        stopTime[] toSort =  stopTimesWithMatchingArrivalTime.toArray(new stopTime[0]);
        //Now we can sort the array of stopTime objects by their Trip ID with our improved merge-sort function.
        stopTime[] sorted = mergeSort(toSort);
        //Convert the sorted array of sortTime objects back to a list.
        stopTimesWithMatchingArrivalTime = Arrays.asList(sorted);

        int previousTripID = stopTimesWithMatchingArrivalTime.get(0).trip_id;
        System.out.println("\n" + String.join("", Collections.nCopies(5," ")) + "Here are all the trips with an arrival time of " + inputArrivalTime + " sorted by Trip ID." + String.join("", Collections.nCopies(6," ")) + "\n");
        System.out.println(String.join("", Collections.nCopies(35,"*")) + " SEARCH-RESULTS " + String.join("", Collections.nCopies(35,"*")));
        for (int i = 0; i < stopTimesWithMatchingArrivalTime.size(); i++)
        {
            if(i != 0)
            {
                System.out.println(String.join("", Collections.nCopies(86,"*")));
            }
            System.out.println(String.join("", Collections.nCopies(35," ")) + "Matching trip #" + (i+1));
            System.out.println("[+] Trip ID: " + stopTimesWithMatchingArrivalTime.get(i).trip_id);
            if (previousTripID > stopTimesWithMatchingArrivalTime.get(i).trip_id)
            {
                System.out.println("An error has occurred with the sorting of Trip ID's.");
            }
            previousTripID = stopTimesWithMatchingArrivalTime.get(i).trip_id;
            System.out.println("[+] Arrival Time: " + stopTimesWithMatchingArrivalTime.get(i).arrival_time);
            System.out.println("[+] Departure Time: " + stopTimesWithMatchingArrivalTime.get(i).departure_time);
            System.out.println("[+] Stop ID: " + stopTimesWithMatchingArrivalTime.get(i).stop_id);
            System.out.println("[+] Stop Sequence: " + stopTimesWithMatchingArrivalTime.get(i).stop_sequence);
            System.out.println("[+] Stop Headsign: " + stopTimesWithMatchingArrivalTime.get(i).stop_headsign);
            System.out.println("[+] Pickup Type: " + stopTimesWithMatchingArrivalTime.get(i).pickup_type);
            System.out.println("[+] Drop Off Type: " + stopTimesWithMatchingArrivalTime.get(i).drop_off_type);
            System.out.println("[+] Shape Distance Travelled: " + stopTimesWithMatchingArrivalTime.get(i).shape_dist_travelled);
        }
        System.out.println(String.join("", Collections.nCopies(86,"*")));
    }
}
