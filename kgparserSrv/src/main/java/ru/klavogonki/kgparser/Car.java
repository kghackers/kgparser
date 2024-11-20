package ru.klavogonki.kgparser;

import java.util.Arrays;
import java.util.List;

/**
 * List of cars with ids and Russian names.
 */
public enum Car {
    // common/public autos
    ZAZ_965(1, "ЗАЗ 965"),
    VAZ_2104(2, "ВАЗ 2104"),
    BELARUS_922(3, "Беларус 922"),
    PEUGEOT_BOXER(4, "Peugeot Boxer"),
    FIAT_STRADA(5, "Fiat Strada"),
    HUMMER_H3(6, "Hummer H3"),
    DAEWOO_MATIZ(7, "Daewoo Matiz"),
    FORD_FOCUS_MK2(8, "Ford Focus Mk2"),
    TOYOTA_LAND_CRUISER(9, "Toyota Land Cruiser"),

    VOLKSWAGEN_PASSAT_B3(10, "Volkswagen Passat B3"),
    MITSUBISHI_LANCER_X(11, "Mitsubishi Lancer X"),
    BMW_X6(12, "BMW X6"),
    AUDI_TT(13, "Audi TT"),
    BUGATTI_VEYRON(14, "Bugatti Veyron"),
    /**
     * Notably, car 21 is also the F1 car.
     * Looks like car 21 is now a tuning option for car 15.
     * @see <a href="http://klavogonki.ru/img/cars/15.png">Image for car 15</a>
     * @see <a href="http://klavogonki.ru/img/cars/21.png">Image for car 21</a>
     */
    F1(15, "Болид F1"), // Notably, car 21 is also the F1 car,
    LAMBORGHINI_MURCIELAGO(16, "Lamborghini Murcielago"),
    BATMOBILE(17, "Бэтмобиль"),
    AIRBOAT(18, "Аэроглиссер"),
    DUESENBERG_SPEEDSTER(19, "Duesenberg Speedster"),

    UFO(20, "НЛО"),
    // car 21 became a tuning for car 15
    FERRARI_FX_CONCEPT(22, "Ferrari FX Concept"),
    SMART_FORTWO(23, "Smart Fortwo"),
    CATERPILLAR_247B(24, "Caterpillar 247B"),
    SS_1(25, "SS-1"),
    VOLKSWAGEN_BEETLE(26, "Volkswagen Beetle"),
    DODGE_VIPER(27, "Dodge Viper"),
    CITROEN_HYPNOS(28, "Citroen Hypnos"),
    RENAULT_MEGANE(29, "Renault Megane"),

    SUBARU_TOURER(30, "Subaru Tourer"),
    TRAM(31, "Трамвай", 275949, 1005), // http://klavogonki.ru/u/#/275949/car/ - iWheelBuy is also Bombo on the other account
    INSECATOR(32, "Инсекатор", 155280, 1007), // http://klavogonki.ru/u/#/155280/car/ - original owner
    BUGATTI_GALIBIER(33, "Bugatti Galibier"),
    CHEVROLET_ORLANDO(34, "Chevrolet Orlando"),
    FERRARI_250_TESTA_ROSSA(35, "Ferrari 250 Testa Rossa"),
    FERRARI_ZOBIN(36, "Ferrari Zobin"),
    NISSAN_ROUND_BOX(37, "Nissan Round Box"),
    UAZ_3151_HUNTER(38, "УАЗ 3151 Hunter"),
    DANNY_CHANG_CONCEPT(39, "Danny Chang Concept"),

    CHEVROLET_EN_V(40, "Chevrolet EN-V"),
    LM_RALLY_FIGHTER(41, "LM Rally Fighter"),
    SS_X(42, "SS-X"),
    PORSCHE_CAYENNE_S(43, "Porsche Cayenne S", 157594, 1009), // http://klavogonki.ru/u/#/157594/car/ - original owner
    HONDA_CBR_1000RR(44, "Honda CBR 1000RR", 64292, 1004), // http://klavogonki.ru/u/#/64292/car/ - original owner
    CARAVEL(45, "Каравелла", 922, 1019), // http://klavogonki.ru/u/#/922/car/ - original owner

    // personal autos, looks like their ids start with 1000
    // see https://klavogonki.ru/forum/general/1722/page1/
    // see https://klavogonki.ru/forum/general/15652/page2/#post46
    // !some private cars have become a common cars
    BMW_M6(1000, "BMW M6", 30297), // http://klavogonki.ru/u/#/210230/car/
    HENNESSEY_VENOM_GT1(1001, "Hennessey Venom GT1", 30297), // http://klavogonki.ru/u/#/30297/car/
    DE_LOREAN_TIME_MACHINE(1002, "DeLorean Time Machine", 151752), // http://klavogonki.ru/u/#/151752/car/
    SUBMARINE(1003, "Submarine", 100600), // http://klavogonki.ru/u/#/100600/car/
    // car 1004 made public
    // car 1005 made public
    MOTORCITY_EUROPE_MC1(1006, "Motorcity Europe MC1", 1596), // http://klavogonki.ru/u/#/1596/car/
    // car 1007 made public
    MERCEDES_BENZ_SL_600(1008, "Mercedes-Benz SL 600", 157594), // http://klavogonki.ru/u/#/157594/car/
    // car 1009 made public
    PAGANI_ZONDA_R(1010, "Pagani Zonda R", 235269), // http://klavogonki.ru/u/#/235269/car/
    BATPOD(1011, "Бэтпод", 195256), // http://klavogonki.ru/u/#/195256/car/
    ZIS_155(1012, "ЗИС-155", 306936), // http://klavogonki.ru/u/#/306936/car/
    /**
     * 2 exclusive owners: Fenex &amp; Аромат
     * @see <a href="http://klavogonki.ru/u/#/82885/car/">Fenex' cars</a>
     * @see <a href="http://klavogonki.ru/u/#/101646/car/">Аромат's cars</a>
     */
    VOLKSWAGEN_CORRADO(1013, "Volkswagen Corrado", 101646), // http://klavogonki.ru/u/#/101646/car/
    RED_EAGLE(1015, "Red Eagle", 24119), // http://klavogonki.ru/u/#/24119/car/ // ! From Carmageddon!
    IHOUE(1016, "ΙΧΘΥΣ", 73879), // http://klavogonki.ru/u/#/73879/car/
    PARASAILING(1017, "Parasailing", 291992), // http://klavogonki.ru/u/#/291992/car/
    DRAGON(1018, "Дракон", 211962), // http://klavogonki.ru/u/#/211962/car/
    // car 1019 made public
    N_BAR(1020, "N-Bar", 20106), // http://klavogonki.ru/u/#/20106/car/
    DU_49(1021, "ДУ-49", 173903), // http://klavogonki.ru/u/#/173903/car/
    DUCATI_848_2010(1022, "Ducati 848 2010", 61254), // http://klavogonki.ru/u/#/61254/car/
    TUMBLER(1023, "Tumbler", 195256), // http://klavogonki.ru/u/#/195256/car/
    FERRARI_F2(1024, "Ferrari F12", 320247), // http://klavogonki.ru/u/#/320247/car/
    ;

    Car(final int id, final String name) {
        this.id = id;
        this.name = name;
        this.ownerId = null;
        this.personalId = null;
    }

    Car(final int id, final String name, final int ownerId) { // for personal cars that have not been made public
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.personalId = null;
    }

    Car(final int id, final String name, final Integer ownerId, final Integer personalId) { // for personal cars that have not been made public
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.personalId = personalId;
    }

    public boolean isPersonalOnly() {
        return (ownerId != null) && (personalId == null);
    }

    public boolean wasPersonalButMadePublic() {
        return (personalId != null);
    }

    public boolean isPublic() {
        return (id < FIRST_PERSONAL_CAR_ID);
    }

    public static boolean isPersonalId(int carId) {
        return carId >= FIRST_PERSONAL_CAR_ID;
    }

    public static Car getById(int carId) {
        List<Car> carsWithId = Arrays
            .stream(Car.values())
            .filter(car -> car.id == carId)
            .toList();

        if (carsWithId.size() == 1) {
            return carsWithId.get(0);
        }

        // original owners might have cars with personalId ids!
        List<Car> carsWithPersonalId = Arrays
            .stream(Car.values())
            .filter(car -> (car.personalId != null) && (car.personalId == carId))
            .toList();

        if (carsWithPersonalId.size() == 1) {
            return carsWithPersonalId.get(0);
        }

        throw new IllegalArgumentException(String.format("Found %d cars with id or personalId = %d.", carsWithId.size(), carId));
    }

    public static final int FIRST_PERSONAL_CAR_ID = 1000;

    public final int id;
    public final String name;
    public final Integer ownerId; // for personal autos
    public final Integer personalId; // for personal autos that have been made public
}
