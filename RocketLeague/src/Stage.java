import java.awt.Rectangle;

public class Stage {
	public int [][] corners = new int [2][2];
	int height;
	int width;
	Rectangle shape;
	public Rectangle getStageShape() {
		return shape;
	}
	public void setStageShape(Rectangle stageShape) {
		this.shape = stageShape;
	}
	public Stage (int [] NW, int [] SE) {
		//NW
		this.corners[0][0] = NW[0];
		this.corners[0][1] = NW[1];
		//SE
		this.corners[1][0] = SE[0];
		this.corners[1][1] = SE[1];

		height = this.corners[0][1] - this.corners[1][1];
		width = this.corners[0][0] - this.corners[1][0];
		shape = new Rectangle(this.corners[0][0], this.corners[0][1], -this.width, -this.height);
	}
	public Rectangle getShape() {
		return shape;
	}
	public void setShape(Rectangle shape) {
		this.shape = shape;
	}

}
