package rsp2.attempt2;

public enum Item {

    SMARTPHONE("Smartphone", 0.3f, Type.ELECTRONIC),
    JEANS("Jeans", 1f, Type.CLOTHING),
    TV("Television", 25f, Type.ELECTRONIC),
    LEGO("Lego Set", 0.75f, Type.TOY),
    PIZZA("Frozen Pizza", 2.5f, Type.FOOD),
    RUG("Rug", 6f, Type.HOME),
    LAPTOP("Laptop", 3f, Type.ELECTRONIC),
    TSHIRT("T-Shirt", 0.5f, Type.CLOTHING),
    COFFEE_MAKER("Coffee Maker", 4f, Type.ELECTRONIC),
    DOLL("Doll", 0.4f, Type.TOY),
    APPLE("Apple", 0.2f, Type.FOOD),
    TOWEL("Bath Towel", 1.3f, Type.HOME),
    BLENDER("Blender", 3.5f, Type.ELECTRONIC),
    SOCKS("Socks", 0.1f, Type.CLOTHING),
    CHAIR("Chair", 15f, Type.HOME),
    VIDEO_GAME("Video Game", 0.1f, Type.ELECTRONIC),
    CONSOLE("Video Game Console", 5f, Type.ELECTRONIC),
    WATCH("Watch", 0.3f, Type.ACCESSORY),
    SANDWICH("Sandwich", 0.5f, Type.FOOD),
    CURTAINS("Curtains", 2f, Type.HOME),
    SPEAKER("Bluetooth Speaker", 1.2f, Type.ELECTRONIC),
    SUNGLASSES("Sunglasses", 0.2f, Type.ACCESSORY),
    TOASTER("Toaster", 2.5f, Type.ELECTRONIC),
    DRILL("Power Drill", 4.5f, Type.TOOL),
    SCREWDRIVER_SET("Screwdriver Set", 1.5f, Type.TOOL),
    TABLET("Tablet", 1f, Type.ELECTRONIC),
    HAMMER("Hammer", 2f, Type.TOOL),
    HAIR_DRYER("Hair Dryer", 1f, Type.ELECTRONIC),
    PLUSH_TOY("Plush Toy", 0.5f, Type.TOY),
    CEREAL_BOX("Cereal Box", 1f, Type.FOOD),
    BACKPACK("Backpack", 1.5f, Type.ACCESSORY),
    COAT("Coat", 2f, Type.CLOTHING),
    DESK_LAMP("Desk Lamp", 4f, Type.HOME),
    HEADPHONES("Headphones", 0.5f, Type.ELECTRONIC),
    OVEN_MITT("Oven Mitt", 0.3f, Type.HOME),
    MILK_CARTON("Milk Carton", 2f, Type.FOOD),
    PILLOW("Pillow", 1.5f, Type.HOME),
    FLASHLIGHT("Flashlight", 0.7f, Type.ELECTRONIC),
    GARDEN_HOSE("Garden Hose", 5f, Type.GARDEN),
    LAWN_MOWER("Lawn Mower", 50f, Type.GARDEN),
    FLOWER_POT("Flower Pot", 3f, Type.GARDEN),
    GARDEN_SHOVEL("Garden Shovel", 2f, Type.GARDEN),
    WATERING_CAN("Watering Can", 1.5f, Type.GARDEN),
    PLANT_SEEDS("Plant Seeds", 0.1f, Type.GARDEN),
    FERTILIZER("Fertilizer Bag", 40f, Type.GARDEN),
    GARDEN_GLOVES("Garden Gloves", 0.2f, Type.GARDEN),
    PRUNING_SHEARS("Pruning Shears", 0.5f, Type.GARDEN),
    GARDEN_RAKE("Garden Rake", 3f, Type.GARDEN),
    BIRD_FEEDER("Bird Feeder", 1.5f, Type.GARDEN),
    OUTDOOR_LANTERN("Outdoor Lantern", 2f, Type.GARDEN),
    HERB_PLANTER("Herb Planter", 1f, Type.GARDEN),
    COMPOST_BIN("Compost Bin", 20f, Type.GARDEN),
    PESTICIDE_SPRAYER("Pesticide Sprayer", 2f, Type.GARDEN),
    EYE_SHADOW("Eye Shadow", 0.05f, Type.BEAUTY),
    NAIL_POLISH("Nail Polish", 0.04f, Type.BEAUTY),
    FACE_MOISTURIZER("Face Moisturizer", 0.2f, Type.BEAUTY),
    SUNSCREEN("Sunscreen", 0.15f, Type.BEAUTY),
    PERFUME("Perfume", 0.25f, Type.BEAUTY),
    SHAMPOO("Shampoo", 0.5f, Type.BEAUTY),
    CONDITIONER("Conditioner", 0.5f, Type.BEAUTY),
    HAIR_SPRAY("Hair Spray", 0.4f, Type.BEAUTY),
    MAKEUP_REMOVER("Makeup Remover", 0.3f, Type.BEAUTY);

	
	private final String name;
	private final float weight;
	private final Type type;
	
	Item(String name, float weight, Type type){
		this.name = name;
		this.weight = weight;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public float getWeight() {
		return weight;
	}
	public Type getType() {
		return type;
	}
}
