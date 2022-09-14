import java.util.*;
import java.util.concurrent.TimeUnit;

public class Elevator {

    private List<Man> manListOnElevator; // список людей в лифте
    private Building building; // здание в котором находится лифт
    private int pointFloor; // максимальная цель куда едет лифт вверх или вниз
    private int floor = 1; // этаж на котором находится лифт
    private int nextFloor; // следующий этаж, на который едет лифт
    private char orientation; // направление движения лифта 'U' - вверх,  'D' - вниз
    private int stepNumber = 1; // номер шага в программе

    public Elevator (Building building) {
        this.building = building;
        manListOnElevator = new ArrayList<>();
    }

    public void addManToListElevator() {    // добавляем людей в лифт на этаже
        List<Man> manList = building.getListManOnFloor(this.floor);     // получаем список людей с этажа
        if ((manListOnElevator.isEmpty() || manListOnElevator.size() < 5) && manList != null) {  // проверяем есть место в лифте
            List<Man> newManOnElevator = manList.stream().filter(e ->
            {
                if (orientation == 'U')                        // в зависимости от направления лифта берем людей по пути вверх или вниз
                    return e.getPoint()>floor;
                else return e.getPoint()<floor;
            }).limit(5-manListOnElevator.size()).toList();     // добавляем новое количество людей до 5 человек
            manListOnElevator.addAll(newManOnElevator);                 // добавляем в лифт людей, которым нужно ехать
            building.removeListMensWithFloor(floor, newManOnElevator);   // удаляем с этажа людей, которых забрали
        }
    }

    public void exitManWithElevator() {            // метод выхода людей с лифта
        if (!manListOnElevator.isEmpty()) {
            List<Man> listExit = manListOnElevator.stream().filter(e -> e.getPoint() == floor).toList();  // находим людей которым нужно на этот этаж
            if (!listExit.isEmpty()) {
                building.setManOnFloor(floor, listExit); // добавляем людей на этаж, которые вышли из лифта
                manListOnElevator.removeAll(listExit); // удаляем людей из лифта
            }
        }
    }

    public void changeOrientation() {    // смена направления движения лифта ('U' - вверх или 'D' - вниз)
        if (orientation == 'U')
            orientation = 'D';
        else orientation = 'U';
    }

    public int getNextFloor() {      // запрос на новый следующий этаж для лифта
        int result = 0;
        if (orientation == 'U')
            for (int i = floor + 1; i <= building.getCountFloor(); i++) {
                if (!building.getMap().get(i).isEmpty() && building.getMap().get(i) != null) {   // если он движется вверх проверяем есть ли этажами выше люди
                    result = i;
                    break;
                }
            }
        else {
            for (int i = floor - 1; i > 0; i--)   // если вниз то определяем есть ли люди ниже
                if (!building.getMap().get(i).isEmpty() && building.getMap().get(i) != null) {
                    result = i;
                    break;
                }
        }
        return result;      // отправляем этаж
    }

    public void start() throws InterruptedException {
        int nextMin = 0; // следующий минимальный этаж, на который нужно людям в лифте
        int nextMax = 0; // следующий максимальный этаж, на который нужно людям в лифте

        if (floor == building.getCountFloor() || floor == 1) { // определяем или меняем направление лифта - вверх или низ
            changeOrientation();
        }

        showPositionElevator(); // показываем в консоле позицию людей в здании до посадки

        exitManWithElevator();  // люди выходят из лифта

        if (getNextFloor() == 0 && manListOnElevator.isEmpty())
            changeOrientation();

        addManToListElevator(); // люди заходят в лифт



        showPositionElevator(); // показываем в консоле позицию людей в здании после посадки

        if (!manListOnElevator.isEmpty()) {     // если в лифте есть люди - находим максимальный и следующий этаж куда нужно ехать
            nextMin = manListOnElevator.stream().min(Comparator.comparing(Man::getPoint)).get().getPoint();
            nextMax = manListOnElevator.stream().max(Comparator.comparing(Man::getPoint)).get().getPoint();
        }
        if (orientation == 'U') {
            if (nextMax != 0) {
                pointFloor = nextMax;  // максимальный этаж, куда нужно ехать лифту, исходя из запросов людей в лифте
                nextFloor = nextMin; // следующий этаж куда, куда нужно ехать лифту, исходя из запросов людей в лифте
            }
            for (int i = floor+1; i < building.getCountFloor(); i++) {
                if (!building.getMap().get(i).isEmpty() && building.getMap().get(i) != null)     // проверяем есть ли этажом выше люди
                    if (i < nextFloor && manListOnElevator.size() < 5) { // проверяем - меньше ли этот этаж назнчаного
                        nextFloor = i;
                        break;
                    }
            }

        }
        else {
            if (nextMax != 0) {
                pointFloor = nextMin;
                nextFloor = nextMax;
            }
            for (int i = floor-1; i > 1; i--) {
                if (!building.getMap().get(i).isEmpty() && building.getMap().get(i) != null) {
                    if (i > nextFloor && manListOnElevator.size() < 5) {
                        nextFloor = i;
                        break;
                    }
                }
            }

        }
        if (manListOnElevator.isEmpty()) {     // если лифт пустой запрашиваем следующий этаж у метода
            this.floor = getNextFloor();
        }
        else this.floor = nextFloor;         // если нет, назначем исходя предыдущих расчетов

        TimeUnit.SECONDS.sleep(1); // скорость вывода шагов
        start();
    }

    public void showPositionElevator() {       // метод вывода результата в консоль
        StringBuilder result = new StringBuilder();
        result.append("*** Step ").append(stepNumber++).append(" ***").append("\n");

        for (int i = building.getCountFloor(); i > 0; i--) {
            result.append(i).append(" ").append("|");
            if (i == floor) {
                if (orientation == 'U')
                    result.append(" ^ ");
                else result.append(" v ");

                if (manListOnElevator.isEmpty())
                    result.append("               ");
                else for (Man man : manListOnElevator)
                    result.append(man.getPoint()).append("  ");
            }
            else result.append("                             ");

            result.append("|").append(" ");

            List<Man> listManOnFloor = building.getListManOnFloor(i);
            if (!listManOnFloor.isEmpty())
                for (Man man : listManOnFloor)
                    result.append(man.getPoint()).append(", ");

            result.append("\n");
        }

        System.out.printf(String.valueOf(result));
    }
}
