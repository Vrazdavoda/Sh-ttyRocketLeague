import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import javafx.scene.shape.Line;

public class Client extends Core implements KeyListener{
	public boolean touching = false;
	Car a = new Car(0,0);
	Car b = new Car(0,0);
	Car [] cars = {a,b};
	Ball ball = new Ball(0,0,0);
	Random rand = new Random();
	Stage stage;
	//Shape [] shapeList = {a.getShape(),b.getShape(),ball.getShape(),stage.getShape()};
	int p1Score = 0;
	int p2Score = 0;
	public static long totalTime;
	String mess ="";
	int speedVar = 3;
	int [] middle = {0,0};
	int boostNum=3;
	public static void main(String [] args) {
		new Client().run();
	}
	//initializes the game 
	//sets up ball, cars, and stage
	public void init() {
		super.init();
		Window w = s.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
		middle[0] = s.getWidth()/2;
		middle[1] = s.getHeight()/2;
		ball = new Ball(middle[0], middle[1],60);
		cars[0] = new Car(middle[0] - 300,middle[1]);
		cars[1] = new Car(middle[0] + 300,middle[1]);
		int [] stageCornerNW = {(middle[0] - ((w.getWidth() / 2) - w.getWidth() / 10) ),(middle[1] - ((w.getHeight()/2) - w.getHeight() / 10))};
		int [] stageCornerSE = {(middle[0] + ((w.getWidth() / 2) - w.getWidth() / 10) ),(middle[1] + ((w.getHeight()/2) - w.getHeight() / 10))};
		stage = new Stage(stageCornerNW,stageCornerSE);
	}

	//this is the loop that runs the game
	public void gameLoop() {
		long startTime = System.currentTimeMillis();
		totalTime = startTime;
		while(running) {
			long timePassed = System.currentTimeMillis()-totalTime;
			totalTime += timePassed;
			update(timePassed);
			Graphics2D g = s.getGraphics();
			updates();
			draw(g);
			collisionTester();
			try {
				Thread.sleep(1000/60);
			} catch (Exception ex) {}
		}
		
	}
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
			case KeyEvent.VK_ESCAPE:
				stop();
				break;
			case KeyEvent.VK_R:
				hardReset();
				break;
			case KeyEvent.VK_A:
				cars[0].lAcc = true;
				break;
			case KeyEvent.VK_D:
				cars[0].rAcc = true;
				break;
			case KeyEvent.VK_W:
				cars[0].uAcc = true;
				break;
			case KeyEvent.VK_S:
				cars[0].dAcc = true;
				break;
			case KeyEvent.VK_SHIFT:
				cars[0].boostOn = true;
				break;
			//Player 2 controls
			case KeyEvent.VK_LEFT:
				cars[1].lAcc = true;
				break;
			case KeyEvent.VK_RIGHT:
				cars[1].rAcc = true;
				break;
			case KeyEvent.VK_UP:
				cars[1].uAcc = true;
				break;
			case KeyEvent.VK_DOWN:
				cars[1].dAcc = true;
				break;
			case KeyEvent.VK_ENTER:
				cars[1].boostOn = true;
				break;
			default:
				break;
		}
		mess = "Pressed :" + KeyEvent.getKeyText(keyCode);
		e.consume();
	}
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
			case KeyEvent.VK_ESCAPE:
				stop();
				break;
			case KeyEvent.VK_R:
				hardReset();
				break;
			case KeyEvent.VK_A:
				cars[0].lAcc = false;
				break;
			case KeyEvent.VK_D:
				cars[0].rAcc = false;
				break;
			case KeyEvent.VK_W:
				cars[0].uAcc = false;
				break;
			case KeyEvent.VK_S:
				cars[0].dAcc = false;
				break;
			case KeyEvent.VK_SHIFT:
				cars[0].boostOn = false;
				break;
			//Player 2 controls
			case KeyEvent.VK_LEFT:
				cars[1].lAcc = false;
				break;
			case KeyEvent.VK_RIGHT:
				cars[1].rAcc = false;
				break;
			case KeyEvent.VK_UP:
				cars[1].uAcc = false;
				break;
			case KeyEvent.VK_DOWN:
				cars[1].dAcc = false;
				break;
			case KeyEvent.VK_ENTER:
				cars[1].boostOn = false;
				break;
			default:
				break;
		}
		e.consume();
	}
	public void keyTyped(KeyEvent e) {
		e.consume();
	}
	//this is the umbrella method for drawing everything
	public void draw(Graphics2D g) {
		Window w = s.getFullScreenWindow();
		g.setColor(w.getBackground());
		g.fillRect(0, 0, s.getWidth(), s.getHeight());
		drawStage(g);
		g.setColor(w.getForeground());
		drawInfo(g,w);
		drawBoosts(g,w,cars);
		drawBall(g,ball);
		drawCars(g);
		drawLines(g);
	}
	//draws some stats
	public void drawInfo(Graphics2D g,Window w) {
		g.drawString("Player1 Score: " + p1Score,(w.getWidth()/2) + 300, (w.getHeight() / 10) - 20);
		g.drawString("Player2 Score: " + p2Score,(w.getWidth()/2) - 300,(w.getHeight() / 10) - 20);
		g.drawString(mess,w.getWidth()/2,100);
	}
	//draws the stage, stage outlines, and goals
	public void drawStage(Graphics2D g) {
		stageShape = new Rectangle(stage.corners[0][0], stage.corners[0][1], -stage.width, -stage.height);
		rGoal = new Rectangle(middle[0] + (stage.width /2),middle[1] - 100, 50,200);
		lGoal = new Rectangle(middle[0] - (stage.width /2) -50,middle[1] - 100, 50,200);
		g.setColor(Color.gray);
		g.fill(stageShape);
		g.setColor(Color.BLACK);
		g.draw(stageShape);
		g.drawLine(middle[0] + (stage.width /2),middle[1] - 100, middle[0] - (stage.width /2) -50,middle[1] - 100);
		g.drawLine(middle[0] + (stage.width /2),middle[1] + 100, middle[0] - (stage.width /2) -50,middle[1] + 100);
		g.drawOval(middle[0]-200, middle[1]-200, 400, 400);
		g.setColor(Color.GREEN);
		g.fill(lGoal);
		g.fill(rGoal);
		g.setColor(Color.BLACK);
		g.draw(lGoal);
		g.draw(rGoal);
	}
	//draws all the cars
	
	public void drawCars(Graphics2D g) {
		for(int i=0;i<cars.length;++i) {
			Graphics2D g2d1 = g; 
			g2d1.rotate(cars[i].theta, cars[i].getX(), cars[i].getY());
			g2d1.draw(cars[i].getShape());
			g2d1.rotate((2 * Math.PI) - cars[i].theta, cars[i].getX(),cars[i].getY());
		}
			
	}
	//draws the magnitude lines for cars
	public void drawLines(Graphics2D g) {
		Graphics2D carLineG = g;
		for(int i=0;i<cars.length;++i) {
			Rectangle carLine = new Rectangle((int)cars[i].getX(),(int)cars[i].getY(),(int)cars[i].Magnitude * 2,1);
			carLineG.rotate(cars[i].theta, cars[i].getX(), cars[i].getY());
			if(i == 0) {carLineG.setColor(Color.red);}
			else {carLineG.setColor(Color.blue);}
			carLineG.fill(carLine);
			carLineG.rotate((2 * Math.PI) - cars[i].theta, cars[i].getX(),cars[i].getY());
		}
		for(int i=0;i<cars.length;++i) {
			Rectangle carLine = new Rectangle((int)cars[i].getX(),(int)cars[i].getY(),50,1);
			carLineG.setColor(Color.MAGENTA);
			double angle = getCollisionAngle(cars[i],ball);
			carLineG.rotate(angle,cars[i].getX(),cars[i].getY());
			carLineG.fill(carLine);
			carLineG.rotate((2 * Math.PI) -angle,cars[i].getX(),cars[i].getY());
		}
	}
	//
	public void drawBoosts(Graphics2D g, Window w, Car[] cars) {
		int width = w.getWidth() / 3;
		int height = w.getHeight() / 11;
		g.drawRect(0,w.getHeight()-height,width -1,height);
		g.drawRect(w.getWidth() - width,w.getHeight()-height,width,height);
		Color boostColor = new Color(72,230,27);
		g.setColor(boostColor);
		g.fillRect(0, w.getHeight()-height,(int)( width * (cars[0].boostMeter / cars[0].boostMax)), height);
		g.fillRect(w.getWidth() - (int)(width* (cars[1].boostMeter / cars[1].boostMax)),w.getHeight()-height,(int)( width * (cars[1].boostMeter / cars[1].boostMax)) ,height);
	}
	//draws the ball
	public void drawBall(Graphics2D g, Ball ball) {
		ball.setShape(new Ellipse2D.Double(ball.getX() -(ball.getRadius()/2) , ball.getY() -(ball.getRadius()/2), ball.getRadius(), ball.getRadius()));
		g.setColor(Color.WHITE);
		g.fill(ball.getShape());
		g.setColor(Color.BLACK);
		g.draw(ball.getShape());
	}
	//collision tester
	public void collisionTester(){
		for (int i = 0 ; i < cars.length ; ++i ) {
			if(ball.getShape().intersects(cars[i].getShape())) {
				float newVelX1 = (ball.velocity[0] * (ball.mass - cars[i].mass) 
						+ (2 * cars[i].mass * cars[i].velocity[0])) / (ball.mass + cars[i].mass);
				float newVelY1 = (ball.velocity[1] * (ball.mass - cars[i].mass) 
						+ (2 * cars[i].mass * cars[i].velocity[1])) / (ball.mass + cars[i].mass);
				float newVelX2 = (cars[i].velocity[0] * (cars[i].mass - ball.mass) 
						+ (2 * ball.mass * ball.velocity[0])) / (ball.mass + cars[i].mass);
				float newVelY2 = (cars[i].velocity[1] * (cars[i].mass - ball.mass) 
						+ (2 * ball.mass * ball.velocity[1])) / (ball.mass + cars[i].mass);
				float carToBallAngle = getCollisionAngle(cars[i],ball);
				float ballToCarAngle = getCollisionAngle(ball,cars[i]);
				ball.setX((ball.getX() + newVelX1));
				ball.setY((ball.getY() + newVelY1));
				cars[i].setX((cars[i].getX() + newVelX2) - cars[i].velocity[0]);
				cars[i].setY((cars[i].getY() + newVelY2) - cars[i].velocity[1]);
				
				ball.accelerate(cars[i].Magnitude, carToBallAngle);
				cars[i].accelerate(ball.Magnitude, (Math.PI * 2) - carToBallAngle );
			}
		}
		goalTest();
		touching = false;
		
	}   
	//resets the game w/o scores reset
	public void reset() {
		for(int i=0;i<cars.length;++i) {
			cars[i].velocity[0] = 0;
			cars[i].velocity[1] = 0;
			cars[i].theta = 0;
			cars[i].setPos(middle[0] - 300 + (i * 600),(middle[1] - 100) + rand.nextInt(200));
			cars[i].boostOn = false;
			cars[i].boostMeter = 10;
		}
		ball.velocity[0] = 0;
		ball.velocity[1] = 0;
		ball.setPos(middle[0],middle[1]);
	}
	//resets the game 
	public void hardReset() {
		p1Score = 0;
		p2Score = 0;
		reset();
	}
	//tests to see if the ball is in a goals
	public void goalTest() {
		try {
			if(lGoal.contains(ball.getX(),ball.getY())) {
				p2Score ++;
				mess = "GOAL";
				try {
					Thread.sleep(1000);
				} catch (Exception e) {}
				reset();
			}else if (rGoal.contains(ball.getX(),ball.getY())) {
				p1Score ++;
				try {
					Thread.sleep(1000);
				} catch (Exception e) {}
				reset();
			}
		} catch (Exception e) {}
	}
	//this is all the updates for physics objects
	public void updates() {
		for(int i=0;i<cars.length;++i) {
			cars[i].update(stage);
		}
		ball.update(stage);
		s.update();
		
	}
	
	public static float getCollisionAngle(PhysicsObject one, PhysicsObject two) {
		float angle = (float)(Math.atan2((int)two.getY() - (int)one.getY(), (int)two.getX() - (int)one.getX()));
		//dong
		return angle;
	}
	
	
	//shapes for objects
	public Rectangle stageShape;
	public Rectangle rGoal;
	public Rectangle lGoal;
	
}
