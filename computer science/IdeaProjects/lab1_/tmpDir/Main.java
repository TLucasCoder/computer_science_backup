package tmpDir.tmpDir;

public class Main {

	public static void main(String[] args) {
		Car car = new Car(new Engine(FuelType.Diesel));
		System.out.println(car);
	}
}