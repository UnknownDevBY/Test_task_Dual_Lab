import java.util.Date;

public class Bus implements Comparable<Bus> {

    private String company;
    private Date departureTime;
    private Date arrivalTime;

    public Bus(String company, Date departureTime, Date arrivalTime) {
        this.company = company;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public boolean isEfficient(Bus bus) {
        return  (departureTime.getTime() == bus.departureTime.getTime() && arrivalTime.getTime() < bus.arrivalTime.getTime())  ||
                (departureTime.getTime() > bus.departureTime.getTime() && arrivalTime.getTime() == bus.arrivalTime.getTime()) ||
                (departureTime.getTime() > bus.departureTime.getTime() && arrivalTime.getTime() < bus.arrivalTime.getTime());
    }

    public boolean isLongerThanHour() {
        return (arrivalTime.getTime() - departureTime.getTime()) > 3600000;
    }

    @Override
    public int compareTo(Bus o) {
        return departureTime.compareTo(o.departureTime);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", company, ScheduleConverter.DATE_FORMAT.format(departureTime),
                ScheduleConverter.DATE_FORMAT.format(arrivalTime));
    }

    public String getCompany() {
        return company;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }
}
