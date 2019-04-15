import java.awt.Rectangle;
import java.awt.Shape;

public class Car extends PhysicsObject{
	//the dimensions of the car are, in order, NW corner, NE corner, SE corner, SW corner
	
	private Rectangle shape = new Rectangle();
	
	public Car(int x, int y) {
		//speedVar and speedCap are overrides
		this.mass = 100;
		this.speedVar = 2;
		this.speedCap = speedVar * 5;
		this.boost = 0;
		this.boostMax = 10;
		//higher friction is better
		this.friction = 5;		
		this.setWidth(35);
		this.setHeight(18);
		this.setX(x);
		this.setY(y);
		this.mass = 10;
		setShape(new Rectangle(this.northWestCorner[0],this.northWestCorner[1],this.getWidth(),this.getHeight()));
	}
	public void update(Stage stage) {
		super.update(stage);
		updateCorners();
		setShape(new Rectangle(this.northWestCorner[0],this.northWestCorner[1],this.getWidth(),this.getHeight()));
	}
	@Override
	public void accelerate(double magnitude, double direction) {
		this.velocity[0] += (Math.cos(direction) * magnitude);
		this.velocity[1] += (Math.sin(direction) * magnitude);
	}
	
	private void updateCorners() {
		//NW
		this.corners[0][0] = (int)this.getX() - (this.getWidth() /2);
		this.corners[0][1] = (int)this.getY() - (this.getHeight() /2);
		//NE
		this.corners[1][0] = (int)this.getX() + (this.getWidth() /2);
		this.corners[1][1] = (int)this.getY() - (this.getHeight() /2);
		//SE
		this.corners[2][0] = (int)this.getX() + (this.getWidth() /2);
		this.corners[2][1] = (int)this.getY() + (this.getHeight() /2);
		//SW
		this.corners[3][0] = (int)this.getX() - (this.getWidth() /2);
		this.corners[3][1] = (int)this.getY() + (this.getHeight() /2);
	}
	public Rectangle getShape() {
		return shape;
	}
	public void setShape(Rectangle shape) {
		this.shape = shape;
	}
	int [] northWestCorner = corners [0];
}
