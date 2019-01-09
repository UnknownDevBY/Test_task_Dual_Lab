import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScheduleConverter {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
    private static Set<Bus> poshBuses;
    private static Set<Bus> grottyBuses;
    private static List<String> output;

    public static void convert(String src, String dest) throws IOException {
        poshBuses = new TreeSet<>();
        grottyBuses = new TreeSet<>();
        output = new ArrayList<>();
        Path from = Paths.get(src);
        Path to = Paths.get(dest);
        Files.readAllLines(from).forEach(ScheduleConverter::processLine);
        leftEfficient(grottyBuses);
        leftEfficient(poshBuses);
        writeToFile(to);
    }

    private static void leftEfficient(Set<Bus> buses) {
        List<Bus> list = new LinkedList<>();
        for(Bus i: buses) {
            for(Bus j: poshBuses) {
                if(i != j && (i.getDepartureTime().getTime() == j.getDepartureTime().getTime() || i.getArrivalTime().getTime() == j.getArrivalTime().getTime()) && !i.isEfficient(j) )
                    list.add(i);
            }
            for(Bus j: grottyBuses) {
                if(i != j && (i.getDepartureTime().getTime() == j.getDepartureTime().getTime() || i.getArrivalTime().getTime() == j.getArrivalTime().getTime()) && !i.isEfficient(j) )
                    list.add(i);
            }
        }
        list.forEach(buses::remove);
    }

    private static void writeToFile(Path to) throws IOException {
        poshBuses.forEach(ScheduleConverter::addToOutput);
        output.add("");
        grottyBuses.forEach(ScheduleConverter::addToOutput);
        if(!Files.exists(to))
            Files.createFile(to);
        Files.write(to, output);
    }

    private static void addToOutput(Bus bus) {
        output.add(bus.toString());
    }

    private static void processLine(String line) {
        int firstSpace = line.indexOf(" ");
        int lastSpace = line.lastIndexOf(" ");
        String company = line.substring(0, firstSpace);
        Date departureTime = null;
        Date arrivalTime = null;
        try {
            departureTime = DATE_FORMAT.parse(line.substring(++firstSpace, lastSpace));
            arrivalTime = DATE_FORMAT.parse(line.substring(lastSpace));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Bus bus = new Bus(company, departureTime, arrivalTime);
        addBus(bus);
    }

    private static void addBus(Bus bus) {
        if(bus.isLongerThanHour())
            return;
        if(bus.getCompany().equals("Posh"))
            poshBuses.add(bus);
        else grottyBuses.add(bus);
    }
}
