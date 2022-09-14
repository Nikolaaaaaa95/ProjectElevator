public class Test {
    public static void main(String[] args) throws InterruptedException {
        Building building = new Building();
        building.createMansOnFloors();
        Elevator elevator = new Elevator(building);

        elevator.start();

    }
}
