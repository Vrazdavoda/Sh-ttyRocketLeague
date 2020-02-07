//this section sourced from TheNewBoston from YouTube.com
import java.awt.*;
import javax.swing.*;

@SuppressWarnings("unused")
public abstract class Core {
	private static DisplayMode modes[] = {
		new DisplayMode(1920,1080,32,0),
		new DisplayMode(1366,768,32,0),
		new DisplayMode(800,600,32,0),	
		new DisplayMode(800,600,24,0),
		new DisplayMode(800,600,16,0),
		new DisplayMode(640,480,32,0),
		new DisplayMode(640,480,24,0),
		new DisplayMode(640,480,16,0),
	};
	public boolean running;
	protected ScreenManager s = new ScreenManager();
	
	//stop method
	public void stop() {
		running = false;
	}
	//call init and gameloop
	public void run() {
		try {
			init();
			gameLoop();
		} finally {
			s.restoreScreen();
		}
	}
	public void init() {
		s = new ScreenManager();
		DisplayMode dm = s.findFirstCompatibleMode(modes);
		s.setFullScreen(dm);
		Window w = s.getFullScreenWindow();
		w.setFont(new Font("Arial", Font.PLAIN,20));
		w.setBackground(Color.WHITE);
		w.setForeground(Color.BLACK);
		running = true;
	}
	//this is the gameLoop
	public void gameLoop() {
		long startTime = System.currentTimeMillis();
		long totalTime = startTime;
		while(running) {
			long timePassed = System.currentTimeMillis()-totalTime;
			totalTime += timePassed;
			update(timePassed);
			Graphics2D g = s.getGraphics();
			draw(g);
			g.dispose();
			s.update();
			try {
				Thread.sleep(1000/60);
			} catch (Exception ex) {}
		}
	}
	//update animation
	public void update (long timePassed) {	
	}
	//draws to screen
	public abstract void draw(Graphics2D g);
	
}
