package aplication.model;

public enum OXEnum {
	O("0"),
	X("X"),
	NONE("");
	
	private String str;
	private OXEnum(String str){
		this.str = str;
	}
	@Override
	public String toString() {
		return this.str;
	}
	
	public static OXEnum fromString(String value) {
		switch (value != null ? value.trim().toLowerCase() : "") {
		case "": return NONE;
        case "0": return O; 
		case "x": return X;
		default:
			throw new IllegalArgumentException(
					String.format("Niepoprawna wartość %s",value));
			
		}
	}
}
