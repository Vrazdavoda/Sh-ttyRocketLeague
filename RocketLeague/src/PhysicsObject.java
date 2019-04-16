import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.sound.sampled.Line;

public abstract class PhysicsObject {
	//these will be overwritten in the car and ball class
	public int width;
	public boolean boostOn = false;
	public int boost = 0;
	public int boostMax;
	public float boostMeter = boostMax;
	public int height;
	int speedVar;
	int speedCap;
	float friction;
	final float boostDuration = 5;
	int mass;
	float momentum;
	public int [][] corners = new int [4][2];
	public boolean isMoving = false;
	private boolean boostEmpty = false;
	public boolean uAcc = false;
	public boolean dAcc = false;
	public boolean lAcc = false;
	public boolean rAcc = false;
	//these positions are for center of object
	private float xPosition;
	private float yPosition;
	//these is vector stuff;
	//velocity is an array because it streamlines sections of the code
	public float [] velocity = {0,0};
	public float xVel;
	public float yVel;
	public double Magnitude;
	public double theta = 0;
	public double antiTheta;
	double lastTheta = theta;
	
	public float getX() {
		return this.xPosition;
	}
	public int getWidth() {
		return this.width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return this.height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void setX(float x) {
		this.xPosition = x;
	}
	public float getY() {
		return this.yPosition;
	}
	public void setY(float y) {
		this.yPosition = y;
	}
	public void friction() {
		this.accelerate(this.Magnitude /friction, this.antiTheta);
		for(int i=0;i<velocity.length;++i) {
			if(Math.abs(this.velocity[i]) <0.001) {
				this.velocity[i] = 0;
			}
		}
	}
	public void determineVector() {
		
		this.xVel = this.velocity[0];
		this.yVel = this.velocity[1];
		double combine = Math.pow(xVel, 2) + Math.pow(yVel, 2);
		this.Magnitude = Math.sqrt(combine);
		this.theta = Math.atan2(yVel,xVel);
		this.lastTheta = theta;
		if(Double.isNaN(theta)) {
			this.theta = lastTheta;
		} else if (this.xVel == 0 && this.yVel == 0){
			this.theta = lastTheta;
		}
		this.antiTheta = (theta + Math.PI);
	}
	public void update(Stage stage) {
		this.updateMomentum(stage);
		this.outOfBounds(stage);
		this.determineVector();
		this.updateBoost();
		this.friction();
	}
	public void updateBoost() {
		if(this.boostOn && boostMeter > 0 && !boostEmpty) {
			this.boost = 5;
			this.accelerate(boost,this.theta);
			this.boostMeter -= 1/boostDuration;
		}else {
			this.boost = 0;
			if(boostMeter<boostMax) {
				this.boostMeter += 0.1;
				boostEmpty = true;
			}else if(boostMeter >= boostMax) {
				boostEmpty = false;
			}
		}
		
	}
	public void accelerate(double magnitude, double direction) {
		this.velocity[0] += Math.cos(direction) * magnitude;
		this.velocity[1] += Math.sin(direction) * magnitude;
	}
	public void updateMomentum(Stage stage) {
		if(lAcc && this.velocity[0] >= (-speedCap) && !(outOfBounds(stage))) {
			this.velocity[0] -= speedVar;
		}
		if(rAcc && this.velocity[0] <= speedCap && !(outOfBounds(stage))) {
			this.velocity[0] += speedVar;
		}
		if(uAcc && this.velocity[1] >= (-speedCap) && !(outOfBounds(stage))) {
			this.velocity[1] -= speedVar;
		}
		if(dAcc && this.velocity[1] <= speedCap && !(outOfBounds(stage))){
			this.velocity[1] += speedVar;
		} 
		this.setPos((this.getX() + (int)velocity[0]), (this.getY() + (int)velocity[1]));
		isMoving = (this.Magnitude != 0);
	}
	
	public void setPos(float f, float g) {
		this.setX(f);
		this.setY(g);
	}
	
	public boolean outOfBounds(Stage stage) {
		float bounce = (float) -0.75;
		if(this.getX() - (this.getWidth()/2) < stage.corners[0][0]) {
			this.velocity[0] *= bounce;
			this.setPos(stage.corners[0][0] + (this.getWidth()/2), this.getY());
			return true;
		}
		if(this.getX() + (this.getWidth()/2) > stage.corners[1][0]) {
			this.velocity[0] *= bounce;
			this.setPos(stage.corners[1][0] - (this.getWidth()/2), this.getY());
			return true;
		}
		if(this.getY() - (this.getHeight()/2) < stage.corners[0][1]) {
			this.velocity[1] *= bounce;
			this.setPos(this.getX(), stage.corners[0][1] + (this.getHeight()/2));
			return true;
		}
		if(this.getY() + (this.getHeight()/2)> stage.corners[1][1]) {
			this.velocity[1] *= bounce;
			this.setPos(this.getX(),stage.corners[1][1] - (this.getHeight()/2));
			return true;
		}
		return false;
	}
	public boolean canMove(Shape primary,Shape[] shapeList) {
		for(int i=0;i<shapeList.length;i++) {
			if(primary != shapeList[i]) {
				if(primary.contains((Point2D) shapeList[i]) || (primary.intersects((Rectangle2D) shapeList[i]))) {
					return false;
				}
			}
		}
		return true;
	}
}
