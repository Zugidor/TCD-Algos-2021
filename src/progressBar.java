import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class progressBar {
	/**
     * Returns the number of lines in a file, with the path to the file.
     * @param: Path to the file we want to count the number of lines in.
     * @return: The number of lines in the file.
     */
    protected static long numberOfLinesInAFile(String pathToFile) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
        long counter = 0;
        while (reader.readLine() != null)
        {
            counter++;
        }
        return counter;
    }

    /**
     * Prints the progress bar to the user to let them visualize the progress being made with particular requests.
     * @param: The current value we are at, and the total value we must reach to be 100% completed.
     * @return: void.
     */
    static void updateProgressBar(int currentValue, long totalValue)
    {
        //The progress bar percentage
        double progressPercentage = (double)currentValue/(int)totalValue;
        //The progress bar width in units of chars
        final int width = 65;
        //We start the progress bar's text
        System.out.print("\r" + (int)(progressPercentage*100) + "% ╠");
        //Now print the middle of the progress bar depending on the progress made
        int i = 0;
        while (i <= (int)(progressPercentage * width))
        {
            System.out.print("█");
            i++;
        }
        while (i < width)
        {
            System.out.print(" ");
            i++;
        }
        //Finally, print out the end of the progress bar and show the progress in terms of the current line we are at.
        System.out.print("╣ " + currentValue + "/" + totalValue);
    }
}
