package rsp2.attempt2;

public enum Section {

	ELECTRONIC("Electronics", Type.ELECTRONIC), 
	CLOTHING("Clothing", Type.CLOTHING),
	FOOD("Groceries", Type.FOOD), 
	TOY("Toys", Type.TOY), 
	HOME("Home Decor", Type.HOME), 
	TOOL("Tools", Type.TOOL), 
	ACCESSORY("Accessories", Type.ACCESSORY), 
	GARDEN("Garden", Type.GARDEN),
	BEAUTY("Beauty", Type.BEAUTY);
	
	private String name;
	private Type type;
	
	Section(String name, Type type){
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}
}
