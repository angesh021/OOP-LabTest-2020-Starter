package ie.tudublin;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;
import java.util.ArrayList;

public class Gantt extends PApplet {
	ArrayList<Task> tasks = new ArrayList<Task>();

	public void settings() {
		size(800, 800);
	}

	void loadTasks() {
		Table table = loadTable("tasks.csv", "header");
		for (TableRow row : table.rows()) {
			Task t = new Task(row);
			tasks.add(t);
		}
	}

	private float leftBorder;
	private float border;

	public void printTasks() {
		for (Task t : tasks) {
			println(t);
		}

	}

	public void displayTask()
	{
		stroke(255);
        fill(255);
        textAlign(CENTER, CENTER);

        for(int i = 1 ; i <= 30; i ++)
        {
            float x = map(i, 1, 30, leftBorder, width - border);
			line(x, border,x, height - border);
			text(i, x, border / 2);
        }

		for(int i = 0 ; i < tasks.size(); i ++)
        {
            float y = map(i, 0, tasks.size(), border + 50, height - border - 50);
            Task t = tasks.get(i);
            float x1 = map(t.getStart(), 1, 30, leftBorder, width - border);
            float x2 = map(t.getEnd(), 1, 30, leftBorder, width - border);
            int c = (int) map(i, 0, tasks.size(), 0, 255);
            noStroke();
            fill(c, 255, 255);
            rect(x1, y - 20, x2 - x1, 40);
            fill(255);
            textAlign(LEFT, CENTER);
            text(t.getTask(), 20, y);
        }
	}

	int whichTask = -1;
	boolean isEnd = false;

	public void mousePressed() 
	{
		for(int i = 0 ; i < tasks.size() ; i ++)
		{
            float y = map(i, 0, tasks.size(), border + 50, height - border - 50);
            float y1 = y - 20;
            float y2 = y + 20;
			float x1 = map(tasks.get(i).getStart(), 1, 30, leftBorder, width - border);
			float x2 = map(tasks.get(i).getEnd(), 1, 30, leftBorder, width - border);
			
			if (mouseX >= x1 && mouseX <= x1 + 20 && mouseY >= y1 && mouseY <= y2)
			{
				whichTask = i;
				isEnd = false;
				return;
			}

			if (mouseX <= x2 && mouseX >= x2 - 20 && mouseY >= y1 && mouseY <= y2)
			{
				whichTask = i;
				isEnd = true;
				return;
			}
		}		
		// default value for whichTask
		whichTask = -1;	
	}

	public void mouseDragged() 
	{
		if (whichTask != -1)
		{
			int month = (int)map(mouseX, leftBorder, width - border, 1, 30);
			
			if (month >= 1 && month <= 30)
			{
				Task task = tasks.get(whichTask); 
				if (isEnd)
				{
					if (month - task.getStart() > 0)
					{
						task.setEnd(month);
					}
				}
				else
				{
					if (task.getEnd() - month > 0 )
					{
						task.setStart(month);
					}
				}
			}
		}
	}

	public void setup() {
		colorMode(HSB);
		loadTasks();
		printTasks();
		leftBorder = width * 0.2f;
		border = width * 0.05f;
	}

	public void draw() {
		background(0);
		displayTask();
	}
}
