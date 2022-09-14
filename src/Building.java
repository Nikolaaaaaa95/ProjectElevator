import java.util.*;

public class Building {

    private final int countFloor; // количество этажей
    private Map<Integer, List<Man>> map; //информация о количестве людей на этаже,
    // где Integer - этаж и List - список людей на этом этаже

    public Building() {
        Random random = new Random();
        this.countFloor = random.nextInt(16) + 5; // создание количество этажей здания 5-20
    }

    public void createMansOnFloors() { // создаем людей на этажах
        Random random = new Random();
        map = new HashMap<>();

        for (int i = 1, id = 1; i <= countFloor; i++) {
            List<Man> manList = new ArrayList<>();
            int countMan = random.nextInt(11); // случайно количество людей на этаже 0-10
            for (int j = 0; j < countMan; j++) {
                Man man = new Man(i, id++);          // создаем человека на этаже, передаем этаж и id
                man.generatePoint(countFloor);         // назначаем ему цель поездки
                manList.add(man);
            }
            map.put(i, manList);
        }
    }

    public List<Man> getListManOnFloor(int floor) {  // метод получения списка людей на этаже
        return map.get(floor);
    }

    public void removeListMensWithFloor(int floor, List<Man> list) { // метод удаления людей с этажа, которые сели на лифт
        List<Man> listManOnFloor = map.get(floor);
        map.remove(floor);

        listManOnFloor.removeAll(list);

        for (Man man : listManOnFloor)                      // людям, которые вышли на этаже назначем новую цель
            if (man.getPoint() == floor) {
                man.setFloor(floor);
                man.generatePoint(countFloor);
            }

        map.put(floor, listManOnFloor);
    }

    public void setManOnFloor(int floor, List<Man> list) { // метод добавления людей на этаж, которые вышли из лифта
        List<Man> listOnFloor = map.get(floor);
        map.remove(floor);

        if (!listOnFloor.isEmpty())
            listOnFloor.addAll(list);
        else listOnFloor = new ArrayList<>(list);

        map.put(floor, listOnFloor);
    }

    public int getCountFloor() { // получение количества этажей
        return countFloor;
    }

    public Map<Integer, List<Man>> getMap() { // получаем список этажей и людей на них
        return map;
    }
}
