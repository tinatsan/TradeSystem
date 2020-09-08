package meetings;

import java.util.HashMap;

/**
 * Constructs a class that stores/manages the availability of Users. This will only be created when a ThreeWayRequest has been instantiated,
 * with all values in the HashMap set to "False". The User has to edit the days they available to exchange items at a Meeting.
 */
public class AvailabilityChart {

    private HashMap<Integer, Boolean> chart;


    /**
     * Constructs an instance of AvailabilityChart. A HashMap will be initialized, with the key as an int representing the day after the request was
     * instantiated (1-7), and the value as a boolean
     * representing whether they are available or not (True: They are available. False: They are not available). All values are initially false.
     */
    public AvailabilityChart() {
        chart = new HashMap<>();


        //initializing the chart so that the person is completely unavailable
        for (int i = 1; i < 8; i++)
            chart.put((Integer) i, false);

    }

    /**
     * Alters the values of the chart. This is called when a User wants to choose a day in which the are free to meet up (set the corresponding day to true).
     *
     * @param day       an Integer representing the day in question.
     * @param available a boolean for whether they are free to meet up or not.
     */
    public void editChart(Integer day, boolean available) {
        chart.replace(day, available);
    }

    /**
     * Gets the current version of a User's chart.
     *
     * @return HashMap of chart (key: Integer, value: Boolean)
     */
    public HashMap<Integer, Boolean> getChart() {
        return chart;
    }


}
