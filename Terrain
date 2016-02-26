//this code was not mine
//it produces the terrain where the disasters occur
// by Chuck Anderson, November 2015, for CS161
// with lots of help from http://math.hws.edu/graphicsbook/c3/s6.html

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.gl2.*;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Terrain extends GLJPanel implements GLEventListener, MouseListener, MouseMotionListener {

    // ========== Instance Variables ==========

    protected double[][] elevation;
    protected double[][][] normal;
    protected int nRows;
    protected int nColumns;
    protected int elevationDisplayList;
    protected int flowDisplayList;
    
    protected FlowInterface useThisFlow;
    protected boolean flowUpdated = false;
    protected int startRow;
    protected int startColumn;
    
    protected JFrame frame;
    protected GLJPanel drawable;
    protected JMenu menuFlows;

    protected int frameNumber = 0;  // The current frame number for an animation.

    protected int origx = 0;
    protected int origy = 0;
    protected boolean rotate = false;
    protected boolean zoom = false;
    protected double cameraPosition = 0;
    protected double anglex = -20;
    protected double angley = 150;
    protected double shiftx = 0;

    // ========== Main method ==========

    public static void main(String[] args) {
	if (args.length == 0) {
	    System.out.println("ERROR: Provide filename of elevation data as command line argment.");
	    System.exit(1);
	}
	String filename = args[0];
	Terrain terrain = new Terrain(filename);
    }

    // ========== Terrain Constructor ==========

    public Terrain(String filename) {
        GLCapabilities caps = new GLCapabilities(null);
        drawable = new GLJPanel(caps);
        drawable.setPreferredSize( new Dimension(800,600) );
        drawable.addGLEventListener(this);
        setLayout(new BorderLayout());
        add(drawable,BorderLayout.CENTER);
        drawable.addMouseListener(this);
        drawable.addMouseMotionListener(this);
	try {
	    loadElevationData(filename);
	} catch (Exception e) {
	    System.out.println("ERROR: Cannot read elevation data file " + filename);
	    System.exit(1);
	}
	useThisFlow = null;

	frame = new JFrame("Oh No, Fort Collins!  (Hold shift key down to rotate.)");
	frame.setContentPane(this);
        frame.setJMenuBar(this.createMenuBar()); // "this" not necessary here
	frame.pack();
	frame.setLocation(50,50);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);

        Timer timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                frameNumber++;
		if (useThisFlow != null) {
		    useThisFlow.update(elevation);
		    flowUpdated = true;
		    display();//repaint();
		}
            }
        });
        timer.setInitialDelay(2000);
        timer.start();
    }

    public int getNRows() {
	return nRows;
    }

    public int getNColumns() {
	return nColumns;
    }

    // ========== Elevation data methods ==========

    public void loadElevationData(String filename) throws IOException {
	Scanner scanner = new Scanner(new File(filename));
	nRows = scanner.nextInt();
	nColumns = scanner.nextInt();
	elevation = new double[nRows][nColumns];
	normal = new double[nRows][nColumns][3];
	String line = scanner.nextLine();
	for (int r = 0; r < nRows; r++)
	    for (int c = 0; c < nColumns; c++)
		elevation[r][c] = scanner.nextDouble();
	// Calculate normal vectors
	for (int row = 0; row < nRows-1; row++)
	    for (int col = 0; col < nColumns-1; col++) {
		double v1x = 1;
		double v1y = elevation[row+1][col+1] - elevation[row][col];
		double v1z = 1;
		double v2x = 1;
		double v2y = elevation[row][col+1] - elevation[row+1][col];
		double v2z = -1;
		normal[row][col][0] = -(v1z * v2y - v1y * v2z);
		normal[row][col][1] = -(v1x * v2z - v1z * v2x);
		normal[row][col][2] = -(v1y * v2x - v1x * v2y);
	    }
    }

    public void createElevationDisplayList() {
	GL2 gl = drawable.getGL().getGL2();
	elevationDisplayList = gl.glGenLists(2);
	gl.glNewList(elevationDisplayList, GL2.GL_COMPILE);
	for (int row = 0; row < nRows-1; row++) {
	    gl.glBegin(GL2.GL_QUAD_STRIP);
	    for (int col = 0; col < nColumns; col++) {
		gl.glNormal3d(normal[row][col][0],normal[row][col][1],normal[row][col][2]);
		gl.glVertex3d(row,elevation[row][col],col);
		gl.glVertex3d(row+1,elevation[row+1][col],col);
	    }
	    gl.glEnd();
	}
	gl.glEndList();
    }

    public void createFlowDisplayList() {
	GL2 gl = drawable.getGL().getGL2();
	flowDisplayList = elevationDisplayList + 1;
	gl.glNewList(flowDisplayList, GL2.GL_COMPILE);
	double[][] amount = useThisFlow.getAmount();
	gl.glBegin(GL2.GL_QUADS);
	for (int r = 0; r < nRows-1; r++)
	    for (int c = 0; c < nColumns-1; c++)
		if (amount[r][c] > 0) {
		    gl.glNormal3d(0.0,1.0,0.0);
		    gl.glVertex3d(r, amount[r][c]+elevation[r][c], c);
		    gl.glVertex3d(r+1, amount[r+1][c]+elevation[r+1][c], c);
		    gl.glVertex3d(r+1, amount[r+1][c+1]+elevation[r+1][c+1], c+1);
		    gl.glVertex3d(r, amount[r][c+1]+elevation[r][c+1], c+1);
		}
	gl.glEnd();
	gl.glEndList();
    }
    
    // ========== GLEventListener methods ==========

    public void init( GLAutoDrawable drawable ) {
	GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.3F, 0.3F, 0.3F, 1.0F);
        gl.glEnable(GL2.GL_DEPTH_TEST);
	gl.glCullFace(GL2.GL_BACK);
	gl.glEnable(GL2.GL_CULL_FACE);
	float[] light_position = {-1000.0f, 3000.0f, 1000.0f, 0.0f};
	float[] sun_light = {1.0f, 1.0f, 0.8f, 1.0f};
	gl.glShadeModel(GL2.GL_SMOOTH); //FLAT); //SMOOTH); 

	gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_POSITION,
		     light_position,0);
	gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_DIFFUSE,
		     sun_light,0);
        gl.glEnable(GL2.GL_LIGHTING);   // Enable lighting.
        gl.glEnable(GL2.GL_LIGHT0);     // Turn on a light.  By default, shines from direction of viewer.
        gl.glEnable(GL2.GL_NORMALIZE);  // OpenGL will make all normal vectors into unit normals
        gl.glEnable(GL2.GL_COLOR_MATERIAL);  // Material ambient and diffuse colors can be set by glColor*
	gl.glMaterialf(GL.GL_FRONT, GLLightingFunc.GL_SHININESS, 64.0f);

	createElevationDisplayList();
    }

    public void display( GLAutoDrawable drawable ) {
	GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.5f,0.5f,0.5f,0.0f);
        gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );

	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadIdentity();
	gl.glOrtho(-400+shiftx, 400+shiftx, -200, 400, -2000, 2000);

	gl.glMatrixMode(GL2.GL_MODELVIEW);
	gl.glLoadIdentity();
	gl.glPushMatrix();
	gl.glLoadIdentity();                // Load unity on current matrix
	gl.glRotated(anglex, 1,0,0);
	gl.glRotated(angley, 0,1,0);
	double scale = 1 - 5*(cameraPosition / 1000.0);
	gl.glScaled(scale * 1, scale * 0.1, scale * 1);
	gl.glTranslated(-nRows/2.0, -1500., -nColumns/2.0); //means
  
	gl.glColor3f(0.7f,0.6f,0.6f);
	gl.glCallList(elevationDisplayList);

	// Draw the flow
	if (useThisFlow != null) {
	    if (flowUpdated)
		createFlowDisplayList();
	    float[] color = new float[3];
	    useThisFlow.getColor(color);
	    //gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
	    gl.glClear( GL.GL_DEPTH_BUFFER_BIT );
	    gl.glColor3f(color[0],color[1],color[2]);
	    gl.glCallList(flowDisplayList);
	    //System.out.println("Drawing flow");
	    flowUpdated = false;
	}
	
	gl.glFlush();
	gl.glPopMatrix ();
	gl.glFinish();
	drawable.swapBuffers(); 
    }

    public void reshape( GLAutoDrawable drawable, int x, int y, 
			 int width, int height) {
	// handle resize event
    }

    public void dispose( GLAutoDrawable drawable) {
    	// when closed
    }

    // ========== Menu methods and class ==========

    public JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();
        menuFlows = new JMenu("Flows");
        menubar.add(menuFlows);
        JMenuItem quitMenuItem = new JMenuItem("Quit");
	quitMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    System.out.println("Quitting");
		    frame.dispose();
		    System.exit(0);
		}
	    });
	menuFlows.add(quitMenuItem);
        return menubar;
    }
    
    public void registerFlow(final FlowInterface flow) {
	JMenuItem item = new JMenuItem(flow.getName());
	item.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    System.out.println("Now using " + flow.getName());
		    useThisFlow = flow;
		    useThisFlow.init();
		    useThisFlow.startAt(startRow,startColumn);
		}
	    });
	menuFlows.insert(item,0);
    }

     public void startFlowAt(int row, int column) {
    	startRow = row;
     	startColumn = column;
     }
	
    
    // ========== Mouse event methods ==========

    protected boolean dragging;  // is a drag operation in progress?
    
    public void mousePressed(MouseEvent evt) {
        if (dragging)
            return;  // don't start a new drag while one is already in progress
	origy = evt.getY();
	origx = evt.getX();
	boolean shifted = (evt.getModifiers() & InputEvent.SHIFT_MASK) > 0;
	rotate = shifted;
	zoom = ! shifted;
    }

    public void mouseReleased(MouseEvent evt) {
	zoom = false;
	rotate = false;
    }

    public void mouseDragged(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();
	if (rotate) {
	    anglex =  (int)(anglex - 0.4 * (y - origy)) % 360;
	    angley =  (int)(angley - 0.4 * (x - origx)) % 360;
	    origx = x;
	    origy = y;
	    repaint();
	} else if (zoom) {
	    cameraPosition += y - origy;
	    shiftx += x - origx;
	    origx = x;
	    origy = y;
	    GL2 gl = drawable.getGL().getGL2();
	    repaint();
	}
    }

    // Other methods required for MouseListener, MouseMotionListener.
    public void mouseMoved(MouseEvent evt) { }
    public void mouseClicked(MouseEvent evt) { }
    public void mouseEntered(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }

}

