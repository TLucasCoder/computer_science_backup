package tmpDir.tmpDir;

public class Car {
	
	private Engine engine;

	/**
	 * @param engine
	 */
	public Car(Engine engine) {
		this.engine = engine;
	}

	@Override
	public String toString() {
		return "A car with a " + this.engine.getFuel() + " engine.";
	}
}