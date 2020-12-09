package ru.klavogonki.kgparser;

/**
 * List of cars with ids and Russian names.
 */
public enum Car {
    // common/public autos
    /**
     * Notably, car 21 is also the F1 car.
     * Looks like car 21 is now a tuning option for car 15.
     * @see <a href="http://klavogonki.ru/img/cars/15.png">Image for car 15</a>
     * @see <a href="http://klavogonki.ru/img/cars/21.png">Image for car 21</a>
     */
    F1(15, "Болид F1"), // Notably, car 21 is also the F1 car,
    FERRARI_FX_CONCEPT(22, "Ferrari FX Concept"),
    ZAZ_965(1, "ЗАЗ 965"),
    AIRBOAT(18, "Аэроглиссер"),
    AUDI_TT(13, "Audi TT"),
    SS_X(42, "SS-X"),
    VOLKSWAGEN_PASSAT_B3(10, "Volkswagen Passat B3"),
    VAZ_2104(2, "ВАЗ 2104"),
    DAEWOO_MATIZ(7, "Daewoo Matiz"),
    FIAT_STRADA(5, "Fiat Strada"),
    UAZ_3151_HUNTER(38, "УАЗ 3151 Hunter"),
    FORD_FOCUS_MK2(8, "Ford Focus Mk2"),
    PEUGEOT_BOXER(4, "Peugeot Boxer"),
    MITSUBISHI_LANCER_X(11, "Mitsubishi Lancer X"),
    BATMOBILE(17, "Бэтмобиль"),
    SMART_FORTWO(23, "Smart Fortwo"),
    NISSAN_ROUND_BOX(37, "Nissan Round Box"),
    BELARUS_922(3, "Беларус 922"),
    RENAULT_MEGANE(29, "Renault Megane"),
    CATERPILLAR_247B(24, "Caterpillar 247B"),
    DODGE_VIPER(27, "Dodge Viper"),
    TOYOTA_LAND_CRUISER(9, "Toyota Land Cruiser"),
    BMW_X6(12, "BMW X6"),
    SS_1(25, "SS-1"),
    FERRARI_ZOBIN(36, "Ferrari Zobin"),
    LAMBORGHINI_MURCIELAGO(16, "Lamborghini Murcielago"),
    BUGATTI_GALIBIER(33, "Bugatti Galibier"),
    VOLKSWAGEN_BEETLE(26, "Volkswagen Beetle"),
    BUGATTI_VEYRON(14, "Bugatti Veyron"),
    DUESENBERG_SPEEDSTER(19, "Duesenberg Speedster"),
    FERRARI_250_TESTA_ROSSA(35, "Ferrari 250 Testa Rossa"),
    UFO(20, "НЛО"),
    TRAM(31, "Трамвай", null, 1005), // todo: fill personal owner if we know it
    CHEVROLET_EN_V(40, "Chevrolet EN-V"),
    HUMMER_H3(6, "Hummer H3"),
    CHEVROLET_ORLANDO(34, "Chevrolet Orlando"),
    SUBARU_TOURER(30, "Subaru Tourer"),
    CITROEN_HYPNOS(28, "Citroen Hypnos"),
    PORSCHE_CAYENNE_S(43, "Porsche Cayenne S", null, 1009), // todo: fill personal owner if we know it
    HONDA_CBR_1000RR(44, "Honda CBR 1000RR", null, 1004), // todo: fill personal owner if we know it
    INSECATOR(32, "Инсекатор", null, 1007), // todo: fill personal owner if we know it
    LM_RALLY_FIGHTER(41, "LM Rally Fighter"),
    DANNY_CHANG_CONCEPT(39, "Danny Chang Concept"),
    CARAVEL(45, "Каравелла", null, 1019), // todo: fill personal owner if we know it

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

    public static final int FIRST_PERSONAL_CAR_ID = 1000;

    public final int id;
    public final String name;
    public final Integer ownerId; // for personal autos
    public final Integer personalId; // for personal autos that have been made public
}
