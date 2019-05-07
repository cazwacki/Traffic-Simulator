import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

//DECIDED SCALE: 3X

public class Map extends JPanel {
    
    static ArrayList<Car> cars = new ArrayList<Car>();
    static ArrayList<Rectangle2D.Double> carBoxes = new ArrayList<Rectangle2D.Double>();
    static ArrayList<Rectangle2D.Double> carHurts = new ArrayList<Rectangle2D.Double>();
    static ArrayList<Rectangle2D.Double> carLaneHurts = new ArrayList<Rectangle2D.Double>();
    static int tracker = 0;
    static int[] flushed = {0, 0, 0, 0, 0, 0};
    final static int RED = 0;
    final static int YELLOW = 1;
    final static int GREEN = 2;
    final static int TURNING_RIGHT = 0;
    final static int GOING_STRAIGHT_RIGHT_LANE = 1;
    final static int GOING_STRAIGHT_LEFT_LANE = 2;
    //final static int TURNING_LEFT = 3;
    final static int TURNING_LEFT = 2;
    int[] currentLightLeft = new int[4];
    int[] lightStatus = {RED, RED, GREEN, GREEN, RED, RED, GREEN, GREEN};
    Rectangle2D.Double[] lights = new Rectangle2D.Double[8];
    Shape[] shapes = new Shape[2];
    boolean isAtIntersection;
    boolean congruence;
    boolean focusOnLower = false;
    int TOTAL_TIME = 1200;
    int TIME_LEFT = 1200;
    Timer timer = new Timer(50, new ActionListener() {
  
    	public boolean isCarColliding(Car car) {
            boolean shouldMove = true;
            int numberOfCars = cars.size();
            AffineTransform carAT = new AffineTransform();
            carAT.rotate(Math.toRadians(car.rotation), car.xPos + 7, car.yPos + 4);
            Area carLaneHurtBox = new Area(carAT.createTransformedShape(carLaneHurts.get(cars.indexOf(car))));
            Area carHurtbox = new Area(carAT.createTransformedShape(carHurts.get(cars.indexOf(car))));
            for(int i = 0; i < lights.length; i++) {
            	AffineTransform lightCorr = new AffineTransform();
            	Area lightArea = new Area(lightCorr.createTransformedShape(lights[i]));
            	lightArea.intersect(carHurtbox);
            	if(!lightArea.isEmpty() && !(lightStatus[i] == GREEN) && !checkForIntersection(car)) {
            		shouldMove = false;
            	}
            }
            for(int j = 0; j < numberOfCars; ++j) {
        		Car car2 = cars.get(j);
            	if (car != car2) {
                    //we come from different places
            		AffineTransform carAT2 = new AffineTransform();
                    carAT2.rotate(Math.toRadians(-car2.rotation), car2.xPos + 7, car2.yPos + 4);
                    Area car2Hitbox = new Area(carAT2.createTransformedShape(carBoxes.get(j)));
                    car2Hitbox.intersect(carHurtbox);
                    if(!car2Hitbox.isEmpty()/* || !car2Hurtbox.isEmpty()*/) {
                    	shouldMove = false;
                    }
                    Area car2HitBox = new Area(carAT2.createTransformedShape(carBoxes.get(j)));
                    car2HitBox.intersect(carLaneHurtBox);
                    if(!car2HitBox.isEmpty()) {
                    	shouldMove = false;
                    }
                }
            }
            return shouldMove;
    	}
    	
    	public void handleValidCarAddition() {
    		SpawnPoint spawn = SpawnPoint.randomSpawnPoint();
            SpawnPoint spawn2;
            SpawnPoint spawn3;
            do {
            	spawn2 = SpawnPoint.randomSpawnPoint();
            } while(spawn == spawn2);
            do {
            	spawn3 = SpawnPoint.randomSpawnPoint();
            } while((spawn == spawn3) || (spawn2 == spawn3));
        	tracker = 0;
            int[] spawncounts = {0, 0, 0, 0, 0, 0};
        	for(Car car : cars) {
        		if(car.spawnPoint == SpawnPoint.NORTH) {
        			spawncounts[0] = spawncounts[0] + 1;
        		} else if(car.spawnPoint == SpawnPoint.EAST_TOP) {
        			spawncounts[1] = spawncounts[1] + 1;
        		} else if(car.spawnPoint == SpawnPoint.SOUTH) {
        			spawncounts[2] = spawncounts[2] + 1;
        		} else if(car.spawnPoint == SpawnPoint.WEST_TOP) {
        			spawncounts[3] = spawncounts[3] + 1;
        		} else if(car.spawnPoint == SpawnPoint.EAST_BOTTOM) {
        			spawncounts[4] = spawncounts[4] + 1;
        		} else if(car.spawnPoint == SpawnPoint.WEST_BOTTOM) {
        			spawncounts[5] = spawncounts[5] + 1;
        		} 
        	}
        	boolean shouldAdd1 = true;
        	boolean shouldAdd2 = true;
        	boolean shouldAdd3 = true;
        	for(int i = 0; i < spawncounts.length; i++) {
        		if(spawn.getSpawnpoint() == i && spawncounts[i] > 4) {
        			shouldAdd1 = false;
        		}
        		if(spawn2.getSpawnpoint() == i && spawncounts[i] > 4) {
        			shouldAdd2 = false;
        		}
        		if(spawn3.getSpawnpoint() == i && spawncounts[i] > 4) {
        			shouldAdd3 = false;
        		}
        	}
        	if(shouldAdd1) {
        		cars.add(new Car(spawn, (int)(Math.random()*3), getWidth(), getHeight()));
                addBox(cars);	
        	}
        	if(shouldAdd2) {
        		cars.add(new Car(spawn2, (int)(Math.random()*3), getWidth(), getHeight()));
                addBox(cars);	
        	}
        	if(shouldAdd3) {
        		cars.add(new Car(spawn3, (int)(Math.random()*3), getWidth(), getHeight()));
                addBox(cars);	
        	}
    	}
    	
        @Override
        public void actionPerformed(ActionEvent arg0) {
            if(TIME_LEFT != 0) {
            	tracker += timer.getDelay();
                if(tracker == 1000) {
                	TIME_LEFT--;
                	handleValidCarAddition();
                	for(int i = 0; i < currentLightLeft.length; i++) {
                    	currentLightLeft[i]--;
                    	if(currentLightLeft[i] == 0) {
                    		switch(lightStatus[i*2]) {
                    		case RED:
                    			lightStatus[i*2] = GREEN;
                    			lightStatus[i*2+1] = GREEN;
                    			currentLightLeft[i] = Runner.control.ryg[i*3+2];
                    			break;
                    		case YELLOW:
                    			lightStatus[i*2] = RED;
                    			lightStatus[i*2+1] = RED;
                    			currentLightLeft[i] = Runner.control.ryg[i*3];
                    			break;
                    		case GREEN:
                    			lightStatus[i*2] = YELLOW;
                    			lightStatus[i*2+1] = YELLOW;
                    			currentLightLeft[i] = Runner.control.ryg[i*3+1];
                    			break;
                    		}
                    	}
                    }
                }
                int numberOfCars = cars.size();
                for(int i = 0; i < numberOfCars; ++i) {
                	Car car = cars.get(i);
                	boolean shouldMove = isCarColliding(car);
						for(Car car1 : cars) {
							if(!(car1 == car) && car.yPos == car1.yPos && car.wasAtIntersection == true && Math.abs(car1.xPos - car.xPos) < 5) {
								cars.remove(cars.indexOf(car1));
								carHurts.remove(cars.indexOf(car1));
								carBoxes.remove(cars.indexOf(car1));
								carLaneHurts.remove(cars.indexOf(car1));
							}
						}
                    if(shouldMove) {
                    	car.setAtIntersection(checkForIntersection(car));
                    	if(car.wasAtIntersection && !car.getAtIntersection()) {
                    		if(car.xPos > getWidth()/2) {
                    			if(car.xPos < getWidth()/2 + 40) {
                    				do {
                    					car.turnChoice = (int)(Math.random()*3);
                    				} while(car.turnChoice == TURNING_RIGHT);
                    			} else {
                    				do {
                    					car.turnChoice = (int)(Math.random()*3);
                    				} while(car.turnChoice == TURNING_LEFT);
                    			}
                    		} else if(car.xPos < getWidth()/2) {
                    			if(car.xPos > getWidth()/2 - 40) {
                    				do {
                    					car.turnChoice = (int)(Math.random()*3);
                    				} while(car.turnChoice == TURNING_RIGHT);
                    			} else {
                    				do {
                    					car.turnChoice = (int)(Math.random()*3);
                    				} while(car.turnChoice == TURNING_LEFT);
                    			}
                    		}
                    		car.changeTurn((int)(Math.random()*3));
                    		if(car.dx == 3.5 && car.dy == 0) {
                    			if(car.yPos < getWidth()/2) {
                    				car.spawnPoint = SpawnPoint.WEST_TOP;	
                    				car.intersectionDistance = 40;
                    			} else {
                    				car.spawnPoint = SpawnPoint.WEST_BOTTOM;
                    				car.intersectionDistance = 40;
                    			}
                    		} else if(car.dx == -3.5 && car.dy == 0) { 
                    			if(car.yPos < getWidth()/2) {
                    				car.spawnPoint = SpawnPoint.EAST_TOP;
                    				car.intersectionDistance = 40;
                    			} else {
                    				car.spawnPoint = SpawnPoint.EAST_BOTTOM;
                    				car.intersectionDistance = 40;
                    			}
                    		} else if(car.dx == 0 && car.dy == 3.5) {
                    			car.spawnPoint = SpawnPoint.NORTH;
                    			car.intersectionDistance = 40;
                    		} else {
                    			car.spawnPoint = SpawnPoint.SOUTH;
                    			car.intersectionDistance = 40;
                    		}
                    	}
                    	if(car.getAtIntersection()) {
    	                	carLaneHurts.set(i, new Rectangle2D.Double());
    	                	System.out.println("Removed lane hurt box");
    	                	switch(car.spawnPoint) {
    	                	case EAST_TOP:
    	                	case EAST_BOTTOM:
    	                		if(car.turnChoice == TURNING_RIGHT) {
    	                            //turning right
    	                            if (car.dx != 0 || car.dy != -3.5 || car.rotation != 90) {
    	                                car.dx += .4375;
    	                                car.dy += -.4375;
    	                                car.rotation += 11.25;
    	                            } else {
    	                            	car.setAtIntersection(false);
    	                            	car.wasAtIntersection = true;
    	                            }
    	                        } else if(car.turnChoice == TURNING_LEFT) {
    	                            //turning left
    	                        	if (car.intersectionDistance > 0) {
    	                            	car.intersectionDistance-= 3.5;
    	                            } else if(car.dx != 0 || car.dy != 3.5 || car.rotation != -90) {
    	                                car.dx += .4375;
    	                                car.dy += .4375;
    	                                car.rotation -= 11.25;
    	                            } else {
    	                            	car.setAtIntersection(false);
    	                            	car.wasAtIntersection = true;
    	                            }
    	                        }
    	                		break;
    	                	case WEST_TOP:
    	                	case WEST_BOTTOM:
    	                		if(car.turnChoice == TURNING_LEFT) {
    	                            //turning left
    	                			if (car.intersectionDistance > 0) {
    	                            	car.intersectionDistance-= 3.5;
    	                            } else if(car.dx != 0 || car.dy != -3.5 || car.rotation != 90) {
    	                                car.dx += -.4375;
    	                                car.dy += -.4375;
    	                                car.rotation -= 11.25;
    	                            } else {
    	                            	car.setAtIntersection(false);
    	                            	car.wasAtIntersection = true;
    	                            }
    	                        } else if(car.turnChoice == TURNING_RIGHT) {
    	                            //turning right
    	                            if(car.dx != 0 || car.dy != 3.5 || car.rotation != 270) {
    	                                car.dx += -.4375;
    	                                car.dy += .4375;
    	                                car.rotation += 11.25;
    	                            } else {
    	                            	car.setAtIntersection(false);
    	                            	car.wasAtIntersection = true;
    	                            }
    	                        }
    	                		break;
    	                	case NORTH:
    	                		if(car.turnChoice == TURNING_LEFT) {
    	                            //turning left
    	                			if (car.intersectionDistance > 0) {
    	                            	car.intersectionDistance-= 3.5;
    	                            } else if(car.dx != 3.5 || car.dy != 0 || car.rotation != -180) {
    	                                car.dx += .4375;
    	                                car.dy += -.4375;
    	                                car.rotation += -11.25;
    	                            } else {
    	                            	car.setAtIntersection(false);
    	                            	car.wasAtIntersection = true;
    	                            }
    	                        } else if(car.turnChoice == TURNING_RIGHT) {
    	                            //turning right
    	                            if(car.dx != -3.5 || car.dy != 0 || car.rotation != 0) {
    	                                car.dx += -.4375;
    	                                car.dy += -.4375;
    	                                car.rotation += 11.25;
    	                            } else {
    	                            	car.setAtIntersection(false);
    	                            	car.wasAtIntersection = true;
    	                            }
    	                        }
    	                		break;
    	                	case SOUTH:
    	                		if(car.turnChoice == TURNING_LEFT) {
    	                			//turning left
    	                			if (car.intersectionDistance > 0) {
    	                            	car.intersectionDistance-= 3.5;
    	                            } else if(car.dx != -3.5 || car.dy != 0 || car.rotation != 0) {
    	                                car.dx += -.4375;
    	                                car.dy += .4375;
    	                                car.rotation += -11.25;
    	                            } else {
    	                            	car.setAtIntersection(false);
    	                            	car.wasAtIntersection = true;
    	                            }
    	                        } else if(car.turnChoice == TURNING_RIGHT) {
    	                        	//turning right
    	                            if(car.dx != 3.5 || car.dy != 0 || car.rotation != 180) {
    	                                car.dx += .4375;
    	                                car.dy += .4375;
    	                                car.rotation += 11.25;
    	                            } else {
    	                            	car.setAtIntersection(false);
    	                            	car.wasAtIntersection = true;
    	                            }
    	                        }
    	                		break;
    	                	}
    	                } else {
    	                	carLaneHurts.get(i).setRect(car.xPos - 30, car.yPos - 3, 35, 10);
    	                }
                        car.move();
                        updatehitBox(car);
                    }
                }
                //REMOVING UNNECESSARY ONES
                ArrayList<Integer> deletables = new ArrayList<Integer>();
                for(int i = cars.size() - 1; i >= 0; i--) {
                    if(cars.get(i).xPos < 0 || cars.get(i).xPos > 700 || cars.get(i).yPos < 0 || cars.get(i).yPos > 700) {
                        if(cars.get(i).yPos > 700) {
                        	flushed[SpawnPoint.SOUTH.getSpawnpoint()]++;
                        } else if(cars.get(i).yPos < 0) {
                        	flushed[SpawnPoint.NORTH.getSpawnpoint()]++;
                        } else if(cars.get(i).xPos < 0) {
                        	if(cars.get(i).yPos < getWidth()/2) {
                        		flushed[SpawnPoint.EAST_TOP.getSpawnpoint()]++;
                        	} else {
                        		flushed[SpawnPoint.EAST_BOTTOM.getSpawnpoint()]++;
                        	}
                        } else if(cars.get(i).xPos > 700) {
                        	if(cars.get(i).yPos < getWidth()/2) {
                        		flushed[SpawnPoint.WEST_TOP.getSpawnpoint()]++;
                        	} else {
                        		flushed[SpawnPoint.WEST_BOTTOM.getSpawnpoint()]++;
                        	}
                        }
                    	cars.remove(i);
                        carBoxes.remove(i);
                        carHurts.remove(i);
                        carLaneHurts.remove(i);
                    }
                }
                Runner.control.setFlushed(flushed[0] + flushed[1] + flushed[2] + flushed[3] + flushed[4] + flushed[5]);
            } else {
            	//dialog
            	JOptionPane.showMessageDialog(new JFrame(), "North: " + flushed[SpawnPoint.NORTH.getSpawnpoint()]
            			+ "[] South: " + flushed[SpawnPoint.SOUTH.getSpawnpoint()]
            			+ "[] East Top: " + flushed[SpawnPoint.EAST_TOP.getSpawnpoint()]
            			+ "[] East Bottom: " + flushed[SpawnPoint.EAST_BOTTOM.getSpawnpoint()]
            			+ "[] West Top: " + flushed[SpawnPoint.WEST_TOP.getSpawnpoint()]
            			+ "[] West Bottom: " + flushed[SpawnPoint.WEST_BOTTOM.getSpawnpoint()],
            			"Final Result", JOptionPane.PLAIN_MESSAGE);
            	System.exit(0);
            }
        }

        private boolean checkForIntersection(Car car) {
            Rectangle2D correspondingCarBox = carBoxes.get(cars.indexOf(car));
            if(shapes[0].intersects(correspondingCarBox) || shapes[1].intersects(correspondingCarBox)) {
                return true;
            }
            return false;
        }

        private void updatehitBox(Car car) {
            int index = cars.indexOf(car);
            carBoxes.get(index).setRect(car.xPos, car.yPos, 14, 9);
            carHurts.get(index).setRect(car.xPos - 10, car.yPos - 6, 13, 8);
        }

        private void addBox(ArrayList<Car> cars) {
        	carBoxes.add(new Rectangle2D.Double(0, 0, 0, 0));
        	carHurts.add(new Rectangle2D.Double(0, 0, 0, 0));
        	carLaneHurts.add(new Rectangle2D.Double(0, 0, 0, 0));
        }
        
    });

    public Map() {
        super();
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                for (Shape s : shapes) {
                    if (s.contains(me.getPoint())) {//check if mouse is clicked within shape
                        //adjust controller focus
                        if(me.getY() > 350) {
                            focusOnLower = true;
                        } else {
                            focusOnLower = false;
                        }
                        Runner.toggleFocus();
                        
                    }
                }
            }
        });
        currentLightLeft[0] = Runner.control.ryg[2];
        currentLightLeft[1] = Runner.control.ryg[5];
        currentLightLeft[2] = Runner.control.ryg[8];
        currentLightLeft[3] = Runner.control.ryg[11];
        setPreferredSize(new Dimension(700, 700));
        setBackground(new Color(0, 100, 0));
    }
    
    void updateLights(Graphics g, int width, int height) {
//        System.out.println("Updated Lights");
        g.fillRect(width/2, 2*height/3 + 40, 40, 5);
        g.fillRect(width/2-40, 2*height/3 - 45, 40, 5);
        
    }
    
    void layRoads(Graphics g, int width, int height) {
//        System.out.println("Updated Roads");
        g.setColor(Color.BLACK);
        g.fillRect(width/2 - 40, 0, 80, height);
        g.fillRect(0, 2 * height/3 - 40, width, 80);
        g.fillRect(0, height/3 - 40, width, 80);
//        System.out.println("Left Roads");
    }
    
    void laySeparators(Graphics g, int width, int height) {
//        System.out.println("Updated Separators");
        g.setColor(new Color(180, 180, 0));
        for(int i = 1; i <= 2; i++) {
            g.fillRect(0, i * height/3, width/2 - 40, 2);
            g.fillRect(width/2 + 40, i * height/3, width/2 - 40, 2);    
        }
        g.fillRect(width/2 - 2, 0, 2, height/3 - 40); 
        g.fillRect(width/2 - 2, 2 * height/3 + 40, 2, height/3 - 40); 
        g.fillRect(width/2 - 2, height/3 + 40, 2, height/3 - 80); 
//        System.out.println("Left Separators");
    }
    
    void laySublanes(Graphics g, int width, int height) {
//        System.out.println("Updated Sublanes");
        g.setColor(Color.GRAY);
        for(int i = 0; i < height; i += 20) {
            while((i > height/3 - 40 && i < height/3 + 40) || (i > 2 * height/3 - 40 && i < 2 * height/3 + 40)) {
                i += 20;
            }
            g.fillRect(width/2 - 1 + 20, i, 2, 10);
            g.fillRect(width/2 - 1 - 20, i, 2, 10);
        }
        for(int a = 1; a <= 2; a++) {
            for(int i = 0; i < width; i += 20) {
                while(i > width/2 - 40 && i < width/2 + 40) {
                    i += 30;
                }
                g.fillRect(i, a * height/3 - 1 + 20, 10, 2);
                g.fillRect(i, a * height/3 - 1 - 20, 10, 2);
            }    
        }
//        System.out.println("Left Sublanes");
    }
    
    public void initiateLights() {
    	lights[0] = new Rectangle2D.Double(getWidth()/2 - 40, getHeight()/3 - 42, 40, 2); //TOP TOP
    	lights[1] = new Rectangle2D.Double(getWidth()/2, getHeight()/3 + 42, 40, 2); //TOP BOTTOM
    	lights[2] = new Rectangle2D.Double(getWidth()/2 + 42, getHeight()/3 - 40, 2, 40); //TOP RIGHT
    	lights[3] = new Rectangle2D.Double(getWidth()/2 - 42, getHeight()/3, 2, 40); //TOP LEFT
    	lights[4] = new Rectangle2D.Double(getWidth()/2 - 40, 2 * getHeight()/3 - 42, 40, 2); //BOTTOM TOP
    	lights[5] = new Rectangle2D.Double(getWidth()/2, 2 * getHeight()/3 + 40, 42, 2); //BOTTOM BOTTOM
    	lights[6] = new Rectangle2D.Double(getWidth()/2 + 42, 2 * getHeight()/3 - 40, 2, 40); //BOTTOM RIGHT
    	lights[7] = new Rectangle2D.Double(getWidth()/2 - 42, 2 * getHeight()/3, 2, 40); //BOTTOM LEFT
    }
    
    public void layLightStatus(Graphics2D g2d) {
    	for(int i = 0; i < lights.length; i++) {
    		if(lightStatus[i] == RED) {
    			g2d.setColor(Color.RED);
    		} else if(lightStatus[i] == YELLOW) {
    			g2d.setColor(Color.YELLOW);
    		} else {
    			g2d.setColor(Color.GREEN);
    		}
    		g2d.fill(lights[i]);
    	}
    }
    
    public void layCars(Graphics g, Graphics2D g2d) {
        for(int i = 0; i < cars.size(); i++) {
        	g2d.rotate(Math.toRadians(cars.get(i).rotation), cars.get(i).xPos + 7, cars.get(i).yPos + 4);
        	g.setColor(Color.PINK);
            g2d.fill(carBoxes.get(i));
            g.setColor(Color.RED);
            //g2d.draw(carHurts.get(i));
            g.setColor(Color.WHITE);
            //g2d.draw(carLaneHurts.get(i));
            g2d.rotate(-Math.toRadians(cars.get(i).rotation), cars.get(i).xPos + 7, cars.get(i).yPos + 4);
        }
    }
    
    public void paintComponent(Graphics g) {
        int width = getWidth();             // width of window in pixels
        int height = getHeight();           // height of window in pixels
        initiateLights();
        congruence = Runner.isRunning;
        shapes[0] = new Rectangle2D.Double(width/2 - 40, 2 * height/3 - 40, 80, 80);
        shapes[1] = new Rectangle2D.Double(width/2 - 40, height/3 - 40, 80, 80);
        super.paintComponent(g);            // call superclass to make panel display correctly
        Graphics2D g2d = (Graphics2D) g;
        
        layRoads(g, width, height);
        laySublanes(g, width, height);
        laySeparators(g, width, height);
        layCars(g, g2d);
        layLightStatus(g2d);
        
        g2d.setColor(Color.GREEN.darker());
        //g2d.draw(shapes[0]);
        //g2d.draw(shapes[1]);
        repaint();
    }
    
    public void run() {
        timer.start();
    }
}
