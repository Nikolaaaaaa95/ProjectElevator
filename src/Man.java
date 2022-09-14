import java.util.Objects;
import java.util.Random;

public class Man {

    private int id; // уникальный id человека
    private int point; // этаж на который нужно добраться человеку - цель
    private int floor; // этаж на котором находится человек

    public Man (int floor, int id) {
        this.floor = floor;
        this.id = id;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getPoint() {
        return point;
    }

    public void generatePoint(int limit) {  // выбор цели, этажа на который нужно добраться. Limit - предел этажности здания
        Random random = new Random();
        do {
            this.point = random.nextInt(limit) + 1;
        }
        while (point == floor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Man man = (Man) o;
        return id == man.id && point == man.point && floor == man.floor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, point, floor);
    }

    @Override
    public String toString() {
        return "Man{" +
                "id=" + id +
                ", point=" + point +
                ", floor=" + floor +
                '}';
    }
}
