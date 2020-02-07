import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Ball extends PhysicsObject {
	public Ellipse2D shape = new Ellipse2D.Double();;
	public Ball(int x, int y, int r) {
		this.mass = 100;
		this.setX(x);
		this.setY(y);
		this.setHeight(r);
		this.setWidth(r);
		this.friction  = (float) 50;
		this.mass = 5;
	}
	public Shape getShape() {
		return shape;
	}
	public void setShape(Ellipse2D ballShape) {
		this.shape = ballShape;
	}
	public int getRadius() {
		return width;
	}
}
